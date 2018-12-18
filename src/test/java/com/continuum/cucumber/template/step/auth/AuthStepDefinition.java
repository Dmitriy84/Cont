package com.continuum.cucumber.template.step.auth;

import com.continuum.cucumber.template.step.AbstractBaseStepDefinition;
import cucumber.api.java.en.Given;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AuthStepDefinition extends AbstractBaseStepDefinition {
    @Autowired
    private AuthStepWorld stepWorld;

    @Given("^I'm authenticated with user (.*) and password (.*) on the url (.*)$")
    public void authenticateUser(String username, String password, String authUrl) throws Exception {
        CloseableHttpResponse response = this.doAuthenticateUser(username, password, authUrl);
        stepWorld.setResponse(response);
        JwtToken token = this.retrieveResourceFromResponse(response, JwtToken.class);
        stepWorld.setJwtToken(token);
    }

    private CloseableHttpResponse doAuthenticateUser(String username, String password, String authUrl)
            throws IOException {
        log.info("Authenticate user, calling url in POST mode: {}", authUrl);

        HttpPost httpPost = new HttpPost(authUrl);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        httpPost.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse reponse = HttpClientBuilder.create().build().execute(httpPost);
        return reponse;
    }
}