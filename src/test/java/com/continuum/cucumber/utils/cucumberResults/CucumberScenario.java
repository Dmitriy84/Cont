package com.continuum.cucumber.utils.cucumberResults;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class CucumberScenario {

    private String status = "passed";
    private String errorMessage = "";
    private long duration;
    private List<CucumberScenarioElement> scenarioElements = new ArrayList<>();
    private List<String> tcIds;
    private String name;
    private CucumberScenario background;

    public static List<String> getTcIds(String name) {
        Matcher matcher = Pattern.compile("C(\\d+)").matcher(name);
        List<String> ids = new ArrayList<>();
        while (matcher.find())
            ids.add(matcher.group(1));
        if (!ids.isEmpty())
            return ids;
        throw new NoSuchElementException("No test case ID is provided for scenario '" + name + "'");
    }

    public void setErrorMessage() {
        if (background != null && background.getErrorMessage() != null && !background.getErrorMessage().isEmpty())
            errorMessage = background.getErrorMessage();
        if (!scenarioElements.isEmpty() && errorMessage.isEmpty())
            scenarioElements.stream().filter(entity -> entity.getErrorMessage() != null).findFirst().ifPresent(entity -> errorMessage =
                    entity.getErrorMessage());
    }

    public void setStatus() {
        if (background != null && background.getStatus() != null && background.status.equalsIgnoreCase("failed"))
            status = background.getStatus();
        if (!scenarioElements.isEmpty() && status.equalsIgnoreCase("passed"))
            scenarioElements.stream().filter(entity -> entity.getStatus().equalsIgnoreCase("failed")).findFirst().ifPresent(entity -> status =
                    entity.getStatus());
    }


    public void setElement(CucumberScenarioElement element) {
        scenarioElements.add(element);
    }

    public void setDuration() {
        if (background != null)
            duration += background.getDuration();
        if (!scenarioElements.isEmpty())
            duration += scenarioElements.stream().mapToLong(CucumberScenarioElement::getDuration).sum();
    }


    public void setTcIds() {
        tcIds = getTcIds(name);
    }


}


