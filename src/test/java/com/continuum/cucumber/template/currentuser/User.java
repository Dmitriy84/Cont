package com.continuum.cucumber.template.currentuser;

import lombok.Data;

@Data
public class User {
    private String login;
    private String firstName;
    private String email;
    private String house;
    private String activated;
}