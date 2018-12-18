package com.continuum.cucumber.utils.testrail;

import com.continuum.cucumber.shell.Application;
import com.continuum.cucumber.shell.Shell;
import com.continuum.cucumber.utils.enumerations.HttpMethod;
import gherkin.deps.com.google.gson.Gson;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.NoSuchElementException;

@Data
@Log
public class TestRailAPIClient {
    private String user;
    private String password;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String url;

    public TestRailAPIClient() {
        Application.TestRail testRail = Shell.Application.getTestRail();
        String baseUrl = testRail.getUrl();
        if (!baseUrl.endsWith("/"))
            baseUrl += "/";
        url = baseUrl + "index.php?/api/v2/";
        user = testRail.getUser();
        password = testRail.getPassword();
    }

    private String getAuthorization(String user, String password) {
        return Base64.getEncoder().encodeToString((user + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    public HttpResponse sendRequest(String url, HttpMethod method, Object data) throws TestRailAPIException, IOException {
        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            return executeRequest(prepareRequest(url, method, data), client);
        }
    }

    private HttpResponse executeRequest(HttpUriRequest request, CloseableHttpClient client) throws TestRailAPIException {
        StatusLine statusLine;
        try {
           CloseableHttpResponse response = client.execute(request);
            statusLine=response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.OK.value())
                return response;
        }
        catch (Exception ex) {
            throw new TestRailAPIException("TestRail request failed with message <" + ex + "> for the request endpoint: "+request.getURI());
        }
        throw new TestRailAPIException("TestRail API returned HTTP " + statusLine + "(" + statusLine.getReasonPhrase() + ") " +
                "for the request endpoint: "+request.getURI());
    }

    private HttpUriRequest prepareRequest(String url, HttpMethod method, Object data) {
        String token = getAuthorization(this.user, this.password);
        log.info("AUTH TOKEN FOR TR: " + token);
        Header[] headers = {new BasicHeader("Content-Type", "application/json"),
                new BasicHeader("Authorization", "Basic " + token)};
        String fullUrl = this.url + url;
        switch (method) {
            case GET:
                HttpGet get = new HttpGet(fullUrl);
                get.setHeaders(headers);
                return get;
            case POST:
                HttpPost post = new HttpPost(fullUrl);
                post.setHeaders(headers);
                if (data != null) {
                    String body=new Gson().toJson(data);
                    post.setEntity(new StringEntity(body, StandardCharsets.UTF_8));
                }
                return post;
            default:
                throw new NoSuchElementException("No implementation for HTTP method <" + method + ">");
        }
    }

}