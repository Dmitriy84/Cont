package com.continuum.cucumber.be;

import com.continuum.cucumber.run.BEHooks;
import com.continuum.cucumber.shell.Shell;
import com.continuum.cucumber.utils.CommonUtils;
import com.continuum.cucumber.utils.enumerations.HttpMethod;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.jodah.failsafe.function.CheckedRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.core.Option.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BaseBeSteps extends AbstractBEStep {
    private Map<String, String> convertDataTableToMap(DataTable parameters) {
        Map<String, String> paramsMap = new HashMap<>(parameters.asMap(String.class, String.class));
        paramsMap.remove("key", "value");
        return paramsMap;
    }

    /**
     * Set base path for request. Like http://google.com
     *
     * @param endPoint an absolute URL
     * @author Mykola Mizin
     */
    @Given("endpoint is {string}")
    public void intiRequestSpecWithUrl(String endPoint) {
        requestSpec = requestSpec.basePath(endPoint);
    }

    /**
     * Get TOKEN from Continuum and add it as IPlanetDirectoryPro header to request
     *
     * @author Mykola Mizin
     */
    @Given("login to Continuum")
    public void loginToContinuum() {
        AuthUtil.setup();
        requestSpec = given().header("IPlanetDirectoryPro", AuthUtil.getToken())
                .baseUri(Shell.Application.getEnvironment().getBaseUrlBE()).log().all().config(config);
    }

    /**
     * Add parameters to the endpoint call
     *
     * @param parameters - parameters for sending with request
     * @author Mykola Mizin
     */
    @Given("with parameters")
    public void intiRequestSpecWithUrl(DataTable parameters) {
        requestSpec = requestSpec.pathParams(convertDataTableToMap(parameters));
    }

    /**
     * Add headers to the endpoint call
     *
     * @param headers - headers in format key | value
     * @author Mykola Mizin
     */
    @Given("headers are")
    public void addHeadersToRequestSpec(DataTable headers) {
        requestSpec.headers(convertDataTableToMap(headers));
    }

    /**
     * Add body to the endpoint call
     *
     * @param body - body in JSON format to send with request
     * @author Mykola Mizin
     */
    @Given("request JSON body is")
    public void addBodyToRequestSpec(String body) {
        requestSpec.body(body);
    }

    /**
     * Send request and save received response
     *
     * @param type HTTP method (PUT, GET, POST) to send request {@link com.continuum.cucumber.utils.enumerations.HttpMethod}
     * @author Mykola Mizin
     */
    @Given("{HttpMethod} request was send")
    public void sendRequest(HttpMethod type) {
        Supplier<Response> request;
        switch (type) {
            case POST:
                request = () -> requestSpec.post();
                break;
            case GET:
                request = () -> requestSpec.get();
                break;
            case PUT:
                request = () -> requestSpec.put();
                break;
            default:
                throw new NoSuchElementException("Unknown requestSpec type <" + type + ">");
        }
        context.write("response", request.get());
        context.write("request", request);
        BEHooks.setContext(context);
    }

    /**
     * Check response status code from endpoint
     *
     * @param code - expected response status code
     * @author Mykola Mizin
     */
    @Then("response status code is {int}")
    public void validateStatusCode(int code) {
        context.read("response", Response.class).then().statusCode(code);
    }

    /**
     * Compare JSON with multiple configure options such as:
     * - ignores order in array;
     * - ignores value and compare only type
     *
     * @param structure - expected JSON in the response
     * @author Mykola Mizin
     */
    @Then("response JSON body without values should be")
    public void validateWholeResponseStructure(String structure) {
        assertThatJson(context.read("response", Response.class).
                getBody().asString()).when(IGNORING_VALUES, IGNORING_ARRAY_ORDER)
                .isEqualTo(structure);
    }

    /**
     * The same result as for @see com.continuum.cucumber.be.BaseBeSteps#validateResponseValue, but retry couple times for expected result.
     * Sleep 500 ms between attempts
     *
     * @param times     Number of attempts
     * @param structure JSON body in response
     * @author Dmytro Malohlovets
     */
    @Then("retry '{int}' times for response JSON body should have value")
    @SuppressWarnings("unchecked")
    public void retryValidateResponseValue(int times, String structure) {
        CheckedRunnable run = () -> {
            val response = (Response) context.read("request", Supplier.class).get();
            assertThatJson(response.getBody().asString())
                    .when(IGNORING_EXTRA_FIELDS, IGNORING_ARRAY_ORDER, IGNORING_EXTRA_ARRAY_ITEMS)
                    .isEqualTo(structure);
        };
        CommonUtils.retryOnException(run, times, 500, AssertionError.class);
    }

    /**
     * Compare JSON with multiple configure options such as:
     * - ignores order in array;
     * - ignores unexpected array items
     * - ignores extra fields in the compared value
     * <p>
     * Important: we do not ignore array values
     *
     * @param structure - expected JSON in the response
     * @author Mykola Mizin
     */
    @Then("response JSON body should have value")
    public void validateResponseValue(String structure) {
        assertThatJson(context.read("response", Response.class).getBody().asString())
                .when(IGNORING_EXTRA_FIELDS, IGNORING_ARRAY_ORDER, IGNORING_EXTRA_ARRAY_ITEMS)
                .isEqualTo(structure);
    }

    /**
     * Check response contains text. It could be many occurrences
     *
     * @param values - expected text
     * @author Mykola Mizin
     */
    @Then("response body string contains values {strings}")
    public void validateResponseAsStringContaining(List<String> values) {
        String response = context.read("response", Response.class).getBody().asString();
        values.forEach(v -> assertThat(response).as("Response body string doesn't contain expected value")
                .contains(v));
    }

    /**
     * Logout user from Continuum
     *
     * @author Mykola Mizin
     */
    @Then("logout from Continuum")
    public void logOutFromContinuum() {
        AuthUtil.logout();
    }
}