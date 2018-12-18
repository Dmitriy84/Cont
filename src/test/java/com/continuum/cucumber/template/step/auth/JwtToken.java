package com.continuum.cucumber.template.step.auth;

import lombok.Data;

@Data
public class JwtToken {
    private String token;
    private long expires;
}