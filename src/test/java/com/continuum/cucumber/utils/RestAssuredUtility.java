package com.continuum.cucumber.utils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.ConnectionConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.junit.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class RestAssuredUtility {
    public static Response postWithQueryParameters(String authToken, Map queryParameters, String URI) {
        return setAuthToken(authToken).params(queryParameters).when().post(URI);
    }

    public static String generateURIWithPathParameter(String URI, Object param) {
        return URI.replace("{pathParameter}", param.toString());
    }

    public static RequestSpecification setAuthToken(String authToken) {
        return RestAssured.given().log().all().headers("iPlanetDirectoryPro", authToken);
    }

    public static void verifyStatusCode(Response res, int expectedStatusCode) {
        res.getStatusCode();
        res.then().assertThat().statusCode(expectedStatusCode);
    }

    public static RequestSpecification setFormParameters(String formParametersJson) {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBody(formParametersJson);
        builder.setContentType(ContentType.JSON + "; charset=UTF-8");
        RequestSpecification requestSpec = builder.build();
        requestSpec.log();
        return requestSpec;
    }

    public static Response postWithFormParameters(String authToken, String formParametersJson, String URI) {
        return setAuthToken(authToken).spec(setFormParameters(formParametersJson)).when().post(URI);
    }

    public static Response getWithBody(JSONObject formParametersJson, String URI) {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.closeIdleConnectionsAfterEachResponseAfter(5L, TimeUnit.MINUTES);
        log.info(formParametersJson.toJSONString());
        return RestAssured.given().contentType(ContentType.JSON).body(formParametersJson).when().get(URI);
    }

    public static Response postWithNoBody(String URI) {
        return RestAssured.given().log().all().contentType(ContentType.JSON).log().all().when().post(URI);
    }

    public static Response postWithNoBody(String URI, String authToken) {
        return setAuthToken(authToken).contentType(ContentType.JSON).when().post(URI);
    }

    public static Response getWithQueryparameters(String URI, Map queryParameters) {
        return RestAssured.given().log().all().params(queryParameters).when().get(URI);
    }

    public static Response getWithQueryparameters(String URI, String authToken, Map queryParameters) {
        return setAuthToken(authToken).params(queryParameters).when().get(URI);
    }

    public static Response putWithQueryparameters(String authToken, Map queryParameters, String URI) {
        return setAuthToken(authToken).params(queryParameters).when().put(URI);
    }

    public static Response getWithPathParameters(String authToken, String URI) {
        return setAuthToken(authToken).when().get(URI);
    }

    public static Response getWithNoParameters(String URI) {
        return RestAssured.given().log().all().when().get(URI);
    }

    public static Response getWithNoParameters(String URI, String authToken) {
        return setAuthToken(authToken).when().get(URI);
    }

    public static Response putWithFormParameters(String authToken, String formParametersJson, String URI) {
        return setAuthToken(authToken).spec(setFormParameters(formParametersJson)).when().put(URI);
    }

    public static Response putWithPathParameters(String authToken, String URI) {
        return setAuthToken(authToken).when().put(URI);
    }

    public static Response deleteWithPathParameters(String authToken, String URI) {
        return setAuthToken(authToken).when().delete(URI);
    }

    public static void verifyNodevalue(Response res, String node, Object expectedNodeValue) {
        res.then().body(node, Matchers.equalTo(expectedNodeValue));
    }

    public static void verifyNodeExists(Response res, String node) {
        Assert.assertTrue(isNodePresent(res, node));
    }

    private static boolean isNodePresent(Response res, String node) {
        return new JsonPath(res.asString()).getJsonObject(node) != null;
    }

    public static void assertNodePresent(Response res, String node) {
        Assert.assertTrue(isNodePresent(res, node));
    }

    public static void assertNodeNotPresent(String node, Response res) {
        Assert.assertFalse(isNodePresent(res, node));
    }

    public static Object getNodeValue(Response res, String node) {
        return JsonPath.with(res.asString()).get(node);
    }

    public static List getNodeValues(Response res, String node) {
        return JsonPath.with(res.asString()).get(node);
    }

    public static boolean isInList(List li, Object o) {
        return li.contains(o);
    }

    public static RequestSpecification setFormParameters() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setContentType("application/json; charset=UTF-8");
        return builder.build();
    }

    public static Response postWithPathParameters(String authToken, String URI) {
        return setAuthToken(authToken).spec(setFormParameters()).when().post(URI);
    }

    public static Response deleteWithQueryParameters(String authToken, Map queryParameters, String URI) {
        return setAuthToken(authToken).params(queryParameters).when().delete(URI);
    }

    public static void wait(int timeInSec) throws InterruptedException {
        Thread.sleep(10000L);
    }
}