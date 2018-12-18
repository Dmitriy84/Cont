package com.continuum.cucumber.template.currentuser;

import com.continuum.cucumber.SpringBootCucumberApplication;
import com.continuum.cucumber.template.step.AbstractBaseStepDefinition;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ContextConfiguration(classes = SpringBootCucumberApplication.class)
public class CurrentUserStepsDefinitions extends AbstractBaseStepDefinition {
    private HttpResponse lastHttpResponse;
    private User currentUser;

    @When("^I ask for the current user calling \"([^\"]*)\"$")
    public void requestCurrentUser(final String currentUserApiUrl) throws IOException {
        log.info("Calling url in GET mode: {}", currentUserApiUrl);
        HttpUriRequest request = new HttpGet(currentUserApiUrl);
//        requestSpec.addHeader("x-auth-token", stepWorld.getJwtToken().getToken());
        lastHttpResponse = HttpClientBuilder.create().build().execute(request);

        if (lastHttpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value())
            currentUser = retrieveResourceFromResponse(lastHttpResponse, User.class);
    }

    @Then("^I get (\\w+) response$")
    public void checkLastResponseSatus(String responseSatus) {
        log.info("Checking last response status is {}", responseSatus);
        assertThat(lastHttpResponse.getStatusLine().getStatusCode())
                .isEqualTo(HttpStatus.valueOf(responseSatus).value());
    }

    @Then("^the user detail must contains:$")
    public void checkUserDetail(DataTable userDetail) {
        Map<String, String> detailAsMap = userDetail.asMap(String.class, String.class);
        assertThat(detailAsMap.get("login")).isEqualTo(currentUser.getLogin());
        assertThat(detailAsMap.get("firstName")).isEqualTo(currentUser.getFirstName());
        assertThat(detailAsMap.get("email")).isEqualTo(currentUser.getEmail());
        assertThat(detailAsMap.get("house")).isEqualTo(currentUser.getHouse());
        assertThat(detailAsMap.get("activated")).isEqualTo(currentUser.getActivated());
    }
}