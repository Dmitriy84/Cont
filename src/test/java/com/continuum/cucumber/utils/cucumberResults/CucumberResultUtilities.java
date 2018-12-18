package com.continuum.cucumber.utils.cucumberResults;


import com.continuum.cucumber.shell.Shell;
import lombok.experimental.UtilityClass;

import java.util.NoSuchElementException;
import java.util.Optional;

@UtilityClass
public class CucumberResultUtilities {

    public static String getDurationMinutesString(Long result) {
        long durSec = result / 1000000000L / 60L;
        if (durSec < 1L) {
            durSec = 1L;
        }
        return String.valueOf(durSec);
    }

    public static int getTestRailStatus(String status) {
        switch (status) {
            case "passed":
                return 1;
            case "failed":
                return 5;
            case "skipped":
            default:
                return 2;
        }
    }

    public static String getFormattedTestSteps(CucumberScenario scenario) {
        Optional<String> steps =
                scenario.getScenarioElements().stream().filter(o -> o.getName() != null).map(CucumberScenarioElement::getName).reduce((r, s) -> r + s + ". \n");
        return steps.orElse("");
    }

    public static String getTestRailRunId() {
        String testRun = Shell.Application.getTestRail().getTestRun();
        if (testRun.contains("R")) {
            return testRun.split("R")[1];
        }
        throw new NoSuchElementException("No TestRail testrun id is set in application.yml (application.testrail.testRun)");
    }
}
