package com.continuum.cucumber.be;

import com.continuum.cucumber.configuration.TestContext;
import com.continuum.cucumber.ui.enumerations.Timeouts;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import org.apache.http.params.CoreConnectionPNames;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.config.HeaderConfig.headerConfig;

@Getter
public abstract class AbstractBEStep {
    @Autowired
    protected TestContext context;
    protected RequestSpecification requestSpec;

    protected RestAssuredConfig config = RestAssured.config()
            .httpClient(HttpClientConfig.httpClientConfig()
                    .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, Timeouts.SMALL.milliseconds)
                    .setParam(CoreConnectionPNames.SO_TIMEOUT, Timeouts.SMALL.milliseconds))
            .headerConfig(headerConfig().overwriteHeadersWithName("IPlanetDirectoryPro"));
}