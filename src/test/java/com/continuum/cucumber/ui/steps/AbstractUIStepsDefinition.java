package com.continuum.cucumber.ui.steps;

import com.continuum.cucumber.configuration.TestContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractUIStepsDefinition {
    @Autowired
    protected TestContext context;
}
