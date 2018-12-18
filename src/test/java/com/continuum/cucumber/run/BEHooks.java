package com.continuum.cucumber.run;

import com.continuum.cucumber.be.AuthUtil;
import com.continuum.cucumber.be.JsonUtils;
import com.continuum.cucumber.configuration.TestContext;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;
import io.restassured.response.Response;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assume;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
public class BEHooks {
    @Setter
    private static TestContext context;

    @Before("skip")
    public void skip(Scenario sc) {
        log.warn("SKIP SCENARIO: " + sc.getName());
        Assume.assumeTrue(false);
    }

    @Before("@BE")
    public void setup() {
        AuthUtil.setup();
    }

    @After("@BE")
    public void teardown() {
        AuthUtil.logout();
    }

    @AfterStep("@BE")
    public void attachResponseBody(Scenario scenario) {
        attachBody(scenario, context);
        context = null;
    }

    private void attachBody(Scenario scenario, TestContext context) {
        if (context != null && context.isValuePresent("response")) {
            Response response = context.read("response", Response.class);
            String rawBody = response.getBody().asString();
            String body = JsonUtils.isJSONValid(rawBody) ? JsonUtils.toPrettyFormat(rawBody) : rawBody;
            String result = "Response:\n" +
                    "Response time: " + response.timeIn(TimeUnit.SECONDS) + " seconds\n" +
                    "Status code: " + response.getStatusCode() + "\n" +
                    "Status line: " + response.getStatusLine() + "\n" +
                    "Response body: " + body + "\n";
            scenario.embed(result.getBytes(StandardCharsets.UTF_8), "text/plain");
        }
    }
}