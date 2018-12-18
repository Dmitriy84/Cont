package com.continuum.cucumber.template.step.auth;

import lombok.Data;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Component;

@Component
@Data
public class AuthStepWorld {
    private CloseableHttpResponse response;
    private JwtToken jwtToken = new JwtToken();
}