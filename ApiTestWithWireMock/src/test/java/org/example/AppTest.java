package org.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.Gson;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.apache.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit test for simple App.
 */
public class AppTest extends BaseApi {
   private WireMockServer wireMockServer = new WireMockServer();

    @Before
    public void setUp() {
        wireMockServer.start();
    }

    @Test
    public void bestShouldReturnOne() throws IOException {
        //A request is executed and a response is returned, respectively, afterwards:
        HttpResponse httpResponse = getRequestResponse("/mabel");
        String responseString = convertResponseToString(httpResponse);
        assertThat(httpResponse.getStatusLine().getStatusCode(), is(202));
        assertThat(responseString, is("Mable I love you"));
    }

    @Test
    public void bestShouldReturnTwo() throws IOException {

        Response httpResponse = getResponse("/joy");
        String responseString = convertResToString(httpResponse);
        assertThat(httpResponse.getStatusCode(), is(200));
        assertThat("Joy I love you", is(responseString));
    }

    @Test
    public void bestShouldReturnAny() throws IOException {

        HttpResponse httpResponse = getRequestResponse("/*");

        String responseString = convertResponseToString(httpResponse);
        JSONObject json = new Gson().fromJson(responseString, JSONObject.class);

        assertThat(httpResponse.getStatusLine().getStatusCode(), is(404));
        assertThat(httpResponse.getFirstHeader("Content-Type").getValue(), is("application/json"));
        assertThat(json.get("status").toString(), is("Error"));
    }

    @After
    public void shutDown() {
       // wireMockServer.stop();
    }
}
