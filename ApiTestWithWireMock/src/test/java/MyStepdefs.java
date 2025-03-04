import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.BaseApi;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class MyStepdefs extends BaseApi {
    private WireMockServer wireMockServer = new WireMockServer();
    Map<String, String> resultMap = new HashMap<>();
    Response response;
    private String responseString;

    @Before
    public void before_all(){
        wireMockServer.start();
    }

    @After
    public void after_all(){
        wireMockServer.stop();
    }

    @Given("that I have setup {string} data")
    public void that_i_have_setup_employee_data(String requestName, DataTable dataTable) {
        resultMap = dataTable.asMap(String.class, String.class);
        Map<String, String> finalMap = new HashMap<>();
        finalMap.putIfAbsent("RequestName", requestName);
        finalMap.putAll(resultMap);
        resultMap = finalMap;
    }

    @Given("that I have setup employee recruitment data")
    public void that_i_have_setup_test_data() throws Exception {
        response = ResponseFactory.processPostResponse("/users", resultMap);
        log.info("Response body message {}", response.prettyPrint());
        assertThat(response.getStatusCode(), is(201));
    }

    @When("I query the employee recruitment data")
    public void i_query_the_employee_recruitment_data() throws Exception {
        response = ResponseFactory.processGetResponse("/mabel", resultMap);
        log.info("Response body message {}", response.prettyPrint());
    }

    /**
     * Then I should be able receive confirmation payment is sent
     * |message, inner_key| five|
     * @param dataTable
     * @throws Exception
     */
    @Then("I should be able receive confirmation of his employment")
    public void i_should_be_able_receive_confirmation_of_his_employment(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Map<String, String> resultMap = dataTable.asMap(String.class, String.class);
        for(Map.Entry<String, String> entry : resultMap.entrySet()){
            responseString = response.getBody().prettyPrint();
            String jsonElement = entry.getValue();
            String valueForAGivenKey;
            if(entry.getKey().contains(",")){
                String[] jsonKeys = entry.getKey().split(",");
                JsonObject jsonObject = new Gson().fromJson(responseString, JsonElement.class).getAsJsonObject();
                valueForAGivenKey = ResponseFactory.getValueFromJsonObjectByKeyPair(jsonObject, jsonKeys);
            }else {
                valueForAGivenKey = ResponseFactory.readJsonData(responseString, entry.getKey().trim());
            }
            assertThat(response.getStatusCode(), is(202));
            assertThat("The body key does not match", valueForAGivenKey, is(jsonElement));
        }
    }
}
