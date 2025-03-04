package org.example;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class BaseApi {

    protected String BASE_URL = "http://localhost:8080";

    protected String convertResponseToString(HttpResponse response) throws IOException {
        InputStream responseStream = response.getEntity().getContent();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }

    protected String convertResToString(Response response) throws IOException {
        InputStream responseStream = response.getBody().asInputStream();
        Scanner scanner = new Scanner(responseStream, "UTF-8");
        String responseString = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return responseString;
    }

    protected String convertHttpResponseToString(HttpResponse httpResponse) throws IOException {
        InputStream inputStream = httpResponse.getEntity().getContent();
        return convertInputStreamToString(inputStream);
    }
    //This in turn makes use of another private method:

    protected String convertInputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        String string = scanner.useDelimiter("\\Z").next();
        scanner.close();
        return string;
    }

    protected HttpResponse getRequestResponse(String path) throws IOException {

        HttpGet request = new HttpGet(BASE_URL + path);

        CloseableHttpClient httpClient = HttpClients.createDefault();;
        HttpResponse httpResponse = httpClient.execute(request);
        verify(getRequestedFor(urlEqualTo(path)));
        return httpResponse;
    }

    protected Response getResponse(String path) {

        RestAssured.baseURI = BASE_URL;

        RequestSpecification httpRequest = RestAssured.given();

        Response response = httpRequest.request(Method.GET, path);

        // HttpGet request = new HttpGet(BASE_URL + path);

        // HttpResponse httpResponse = httpClient.execute(request);
        verify(getRequestedFor(urlEqualTo(path)));
        return response;
    }

    protected HttpResponse postRequestResponse(String path) throws IOException {
        HttpPost request = new HttpPost(BASE_URL + path);
        CloseableHttpClient httpClient = HttpClients.createDefault();;
        HttpResponse httpResponse = httpClient.execute(request);
        verify(postRequestedFor(urlEqualTo(path)));
        return httpResponse;
    }

    protected HttpResponse deleteRequestResponse(String path) throws IOException {
        HttpDelete request = new HttpDelete(BASE_URL + path);

        CloseableHttpClient httpClient = HttpClients.createDefault();;
        HttpResponse httpResponse = httpClient.execute(request);
        verify(deleteRequestedFor(urlEqualTo(path)));
        return httpResponse;
    }
}
