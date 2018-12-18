package com.continuum.cucumber.run;

import com.continuum.cucumber.shell.Shell;
import com.continuum.cucumber.utils.cucumberResults.CucumberScenario;
import cucumber.api.Scenario;
import cucumber.api.java.Before;

import java.nio.charset.StandardCharsets;

public class BaseHooks {

    private final static String tcLink = "<a href='%s%s'>Link to TestRail test case #%s</a>";

    private void embedTCIdsInReport(Scenario scenario) {
        String embedText = CucumberScenario.getTcIds(scenario.getName())
                .stream()
                .map(id -> String.format(tcLink, Shell.Application.getTestRailCaseUrl(), id, id)).reduce((r, id) -> r + "\n" + id).orElse("");
        if (!embedText.isEmpty())
            scenario.embed(embedText.getBytes(StandardCharsets.UTF_8), "text/html");
    }

    @Before
    public void addUrlToTestRail(Scenario scenario) {
        embedTCIdsInReport(scenario);
    }
}
