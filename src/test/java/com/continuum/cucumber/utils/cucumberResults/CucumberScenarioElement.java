package com.continuum.cucumber.utils.cucumberResults;

import lombok.Data;

@Data
public class CucumberScenarioElement {
    private String name;
    private String status;
    private String errorMessage;
    private long duration;
}
