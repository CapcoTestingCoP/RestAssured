package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.example.BaseApi;
import util.ResponseFactory;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
public class MyEmployeeStepDef extends BaseApi {
    Map<String, String> resultMap = new HashMap<>();
    Response response;
    private String responseString;

    @Given("employee data below")
    public void an_employee_data_below(io.cucumber.datatable.DataTable dataTable) {
        resultMap = dataTable.asMap(String.class, String.class);
        Map<String, String> finalMap = new HashMap<>();
        finalMap.putIfAbsent("RequestName", "employee");
        finalMap.putAll(resultMap);
        resultMap = finalMap;
    }
    @When("the employee data is submitted")
    public void i_submit_the_employee_data() throws Exception {
        response = ResponseFactory.processPostResponse("/users", resultMap);
        log.info("Response body message {}", response.prettyPrint());
    }
    @Then("a new employee record is created")
    public void a_new_employee_record_is_created() {
        assertThat(response.getStatusCode(), is(201));
    }

    @Then("the system displays a confirmation message \"Employee record created successfully.")
    public void the_system_displays_a_confirmation_message_employee_record_created_successfully(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> resultMap = dataTable.asMap(String.class, String.class);
        for(Map.Entry<String, String> entry : resultMap.entrySet()){
            responseString = response.getBody().prettyPrint();
            String jsonElement = entry.getValue();
            String valueForAGivenKey = ResponseFactory.readJsonData(responseString, entry.getKey().trim());
            assertThat(response.getStatusCode(), is(201));
            assertThat("The body key does not match", valueForAGivenKey, is(jsonElement));
        }
    }
}
