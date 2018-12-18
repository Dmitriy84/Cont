package com.continuum.cucumber.template.exportdata;

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

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ExportDataStepsDefinitions extends AbstractBaseStepDefinition {

    private HttpResponse lastHttpResponse;
    private ExportedData exportedData;

    @When("^I request data export by calling \"([^\"]*)\"$")
    public void requestDataExport(final String currentUserApiUrl) throws IOException {
        log.info("Calling url in GET mode: {}", currentUserApiUrl);
        HttpUriRequest request = new HttpGet(currentUserApiUrl);
//        requestSpec.addHeader("x-auth-token", stepWorld.getJwtToken().getToken());
        lastHttpResponse = HttpClientBuilder.create().build().execute(request);

        // Extracting exported data if possible
        if (lastHttpResponse.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
            exportedData = this.retrieveResourceFromResponse(lastHttpResponse, ExportedData.class);
        }
    }

    @Then("^the data detail must contains:$")
    public void checkExportedData(DataTable data) {
        Map<String, String> detailAsMap = data.asMap(String.class, String.class);
        assertThat(detailAsMap.get("login")).isEqualTo(exportedData.getLogin());
        assertThat(detailAsMap.get("appName")).isEqualTo(exportedData.getAppName());
        assertThat(detailAsMap.get("data")).isEqualTo(exportedData.getData());
    }
}