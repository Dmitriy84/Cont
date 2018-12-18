package com.continuum.cucumber.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Shell {
    public static Application Application;
    @Autowired
    private Application config;

    @PostConstruct
    public void init() {
        Application = config;
    }
}