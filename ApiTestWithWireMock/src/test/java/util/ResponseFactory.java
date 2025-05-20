package util;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public class ResponseFactory {

    private static RequestSpecification requestSpecification = null;
    private static String BASE_URL = "http://localhost:8080";


    public static Response processGetResponse(String path, Map<String, String> inputMap) throws Exception {
        requestSpecification = processRequestSpecification(inputMap);
        return requestSpecification.get(path);
    }

    public static Response processDeleteResponse(String path, Map<String, String> inputMap) throws Exception {
        requestSpecification = processRequestSpecification(inputMap);
        return requestSpecification.delete(path);
    }

    public static Response processPostResponse(String path, Map<String, String> inputMap) throws Exception {
        requestSpecification = processRequestSpecification(inputMap);
        return requestSpecification.post(path);
    }

    public static Response processPutResponse(String path, Map<String, String> inputMap) throws Exception {
        requestSpecification = processRequestSpecification(inputMap);
        return requestSpecification.put(path);
    }

    public static Response processPatchResponse(String path, Map<String, String> inputMap) throws Exception {
        requestSpecification = processRequestSpecification(inputMap);
        return requestSpecification.patch();
    }

    private static RequestSpecification processRequestSpecification(Map<String, String> inputMap) throws Exception {
        RestAssured.baseURI = BASE_URL;
        requestSpecification = RestAssured.given();
        processheader(inputMap);
        String requestBody = processRequestBodyTemplate(inputMap);
        log.info("Request body process {}", requestBody);
        requestSpecification.body(requestBody);
        return requestSpecification;
    }

    private static String processRequestBodyTemplate(Map<String, String> inputMap) throws Exception {
        if(inputMap.get("RequestName") == null){
            return "{}";
        }
        String fileContent = null;
        String filePath = "src/test/resources/requests/" + inputMap.get("RequestName") + ".json";
        try (FileReader fileReader = new FileReader(filePath)){
            JSONParser jsonParser = new JSONParser();
            fileContent = jsonParser.parse(fileReader).toString();
        } catch (IOException | ParseException e) {
            throw new Exception("Request has invalid Json" + e);
        }
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline(fileContent);
        return template.apply(inputMap);
    }

    public static String getValueFromJsonObjectByKeyPair(JsonObject jsonObject, String[] keys) throws Exception {
        String currentKey = keys[0].trim();
        if(keys.length == 1 && jsonObject.has(currentKey)){
            return jsonObject.get(currentKey).getAsString();
        } else if(!jsonObject.has(currentKey)){
            throw new Exception(currentKey + "Please provide valid json node keys separated by comma");
        }
        JsonObject asJsonObject = null;
        if(jsonObject.isJsonArray()){
            asJsonObject = jsonObject.getAsJsonArray().get(0).getAsJsonObject().getAsJsonObject(currentKey);
        }else {
            JsonElement jsonElement = jsonObject.get(currentKey);
            int nextKeyIndex = 1;
            String[] nextRemaningKeys = Arrays.copyOfRange(keys, nextKeyIndex, keys.length);
            if(jsonElement.isJsonArray()){
                int size = jsonElement.getAsJsonArray().size() - 1;
                for (int count = 0; count < size; count++) {
                    if (jsonElement.getAsJsonArray().get(count).getAsJsonObject().has(nextRemaningKeys[0].trim())) {
                        asJsonObject = jsonElement.getAsJsonArray().get(count).getAsJsonObject();
                        break;
                    }
                }
            }else {
                asJsonObject = jsonElement.getAsJsonObject();
            }
        }

        int nextKeyIndex = 1;
        String[] remaningKeys = Arrays.copyOfRange(keys, nextKeyIndex, keys.length);
        return getValueFromJsonObjectByKeyPair(asJsonObject, remaningKeys);
    }

    /**
     *
     * The another for normal nested object without array is $.accounting.workInfo.subDepart.report.reportManager
     * The another for nested object with array is $.accounting[0].workInfo.subDepart.report.reportManager
     * where accounting[0] is the first object of accounting with arrays of object
     * @param json
     * @param readAnotation
     * @return
     */
    public static String readJsonData(String json, String readAnotation) {
        ReadContext ctx = JsonPath.parse(json);
        return ctx.read("$." + readAnotation).toString();
    }

    /**
     * Please provide the header as part od any steps add the key pair value to map
     * |Content-Type|application/json|
     * @param inputMap
     */
    private static void processheader(Map<String, String> inputMap){
        String contentType = "Content-Type";
        log.info("Processing the header for rest call");
        if(inputMap.containsKey(contentType)){
            String header = inputMap.get(contentType).toLowerCase();
            if(header.contains("application/json")){
                requestSpecification.header(contentType, ContentType.JSON.getContentTypeStrings()[0]);
            }
        }
    }
}
