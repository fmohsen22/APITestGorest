package client;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestClient {

    private final int connectionTimeout = 60000;
    private final int socketTimeout = 60000;
    private String accessToken = "";

    private RequestSpecification requestSpecification;

    static {
        RestAssured.defaultParser = Parser.JSON;
        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(new ObjectMapperConfig());
    }

    public RestClient(String AccessToken)
    {
        accessToken = AccessToken;
        RequestSpecBuilder requestSpecBuilder = getDefaultRequestSpecBuilder();
        requestSpecification = RestAssured.given()
                .spec(requestSpecBuilder.build())
                .redirects()
                .follow(true)
                .request();
    }

    public <T> T get(String endpoint, Class<T> returnType) {
        return (T) sendRequestAndGetResponse(endpoint, HttpRequestType.GET);
    }

    public <T> T post(String endpoint, Class<T> returnType) {
        return (T) sendRequestAndGetResponse(endpoint, HttpRequestType.POST);
    }

    public <T> T delete(String endpoint, Class<T> returnType) {
        return (T) sendRequestAndGetResponse(endpoint, HttpRequestType.DELETE);
    }

    public <T> T put(String endpoint, Class<T> returnType) {
        return (T) sendRequestAndGetResponse(endpoint, HttpRequestType.PUT);
    }

    public <T> T patch(String endpoint, Class<T> returnType) {
        return (T) sendRequestAndGetResponse(endpoint, HttpRequestType.PATCH);
    }

    private Response sendRequestAndGetResponse(String endpoint, HttpRequestType requestType) {
        switch (requestType) {
            case POST:
                return requestSpecification.auth().oauth2(accessToken).when().post(endpoint).andReturn();
            case DELETE:
                return requestSpecification.auth().oauth2(accessToken).when().delete(endpoint).andReturn();
            case PUT:
                return requestSpecification.auth().oauth2(accessToken).when().put(endpoint).andReturn();
            case PATCH:
                return requestSpecification.auth().oauth2(accessToken).when().patch(endpoint).andReturn();
            case GET:
            default:
                return requestSpecification.auth().oauth2(accessToken).when().get(endpoint).andReturn();
        }
    }

    private RequestSpecBuilder getDefaultRequestSpecBuilder() {
        return new RequestSpecBuilder()
                .setConfig(RestAssuredConfig.config().httpClient(
                        HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", connectionTimeout)
                                .setParam("http.socket.timeout", socketTimeout)))
                .setContentType(ContentType.JSON);
    }

}
