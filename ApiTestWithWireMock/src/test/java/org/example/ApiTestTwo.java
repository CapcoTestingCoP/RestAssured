package org.example;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Rule;
import org.junit.Test;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class ApiTestTwo extends BaseApi {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule();


    @Test
    public void shouldAnswerWithTrue() throws IOException {
        stubFor(get(urlPathMatching("/baeldung/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"testing-library\": \"WireMock\"")));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/baeldung/wiremock");
        HttpResponse httpResponse = httpClient.execute(request);
        String stringResponse = convertHttpResponseToString(httpResponse);


        //A request is executed and a response is returned, respectively, afterwards:
        verify(getRequestedFor(urlEqualTo("/baeldung/wiremock")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals("\"testing-library\": \"WireMock\"", stringResponse);
    }

    @Test
    public void testWithRequestHeaderMatching() throws IOException {
        stubFor(get(urlPathEqualTo("/baeldung/wiremock"))
                .withHeader("Accept", matching("text/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "text/html")
                        .withBody("!!! Service Unavailable !!!")));
       // Similar to the preceding subsection, we illustrate HTTP interaction using the HttpClient API, with help of the same helper methods:

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/baeldung/wiremock");
        request.addHeader("Accept", "text/html");
        HttpResponse httpResponse = httpClient.execute(request);
        String stringResponse = convertHttpResponseToString(httpResponse);
      //  The following verification and assertions confirm functions of the stub we created before:

        verify(getRequestedFor(urlEqualTo("/baeldung/wiremock")));
        assertEquals(503, httpResponse.getStatusLine().getStatusCode());
        assertEquals("text/html", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals("!!! Service Unavailable !!!", stringResponse);
    }

    @Test
    public void requestWithBodyMatching() throws IOException {
        stubFor(post(urlEqualTo("/baeldung/wiremock"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"testing-library\": \"WireMock\""))
                .withRequestBody(containing("\"creator\": \"Tom Akehurst\""))
                .withRequestBody(containing("\"website\": \"wiremock.org\""))
                .willReturn(aResponse()
                        .withStatus(200)));
       // Now, it is time to create a StringEntity object that will be used as the body of a request:


       // freestar
        InputStream jsonInputStream = new FileInputStream("wiremock_intro.json");
        String jsonString = convertInputStreamToString(jsonInputStream);
        StringEntity entity = new StringEntity(jsonString);
       // The code above uses one of the conversion helper methods define before, convertInputStreamToString.

       // Here is content of the wiremock_intro.json file on the classpath:

       // HTTP requests and responses can be configured and executed as follows:

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8080/baeldung/wiremock");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(entity);
        HttpResponse response = httpClient.execute(request);
        //This is the testing code used to validate the stub:

        verify(postRequestedFor(urlEqualTo("/baeldung/wiremock"))
                .withHeader("Content-Type", equalTo("application/json")));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testMapping(){
        stubFor(get(urlEqualTo("/body-file"))
                .willReturn(aResponse()
                        .withBodyFile("path/to/myfile.xml").withStatus(201)));
    }

    private String buildApiMethodUrl() {
        return String.format("http://localhost:%d/api/message",
                this.wireMockRule.port()
        );
    }
}
