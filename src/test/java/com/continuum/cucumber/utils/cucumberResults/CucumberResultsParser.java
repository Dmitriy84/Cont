package com.continuum.cucumber.utils.cucumberResults;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class CucumberResultsParser {

    private JsonArray jsonObject;

    public CucumberResultsParser(String path) {
        jsonObject = getRoot(Paths.get(path).toFile());
    }

    private JsonArray getRoot(File file) {
        return readJSONFile(file);
    }

    private JsonArray readJSONFile(File file) {
        try {
            JsonParser p = new JsonParser();
            JsonReader doc = new JsonReader(new FileReader(file.getPath()));
            return p.parse(doc).getAsJsonArray();
        } catch (FileNotFoundException e) {
            throw new NoSuchElementException("File was not found! Exception: " + e.getMessage());
        }
    }

    private CucumberScenarioElement getScenarioElement(JsonObject jsonElement) {
        CucumberScenarioElement elementObj = new CucumberScenarioElement();
        if (jsonElement.has("name"))
            elementObj.setName(jsonElement.get("name").getAsString());
        JsonObject result = jsonElement.getAsJsonObject("result");
        elementObj.setStatus(result.get("status").getAsString());
        if (!elementObj.getStatus().equalsIgnoreCase("skipped"))
            elementObj.setDuration(result.get("duration").getAsLong());
        if (result.has("error_message")) {
            elementObj.setErrorMessage(result.get("error_message").getAsString());
        }
        return elementObj;
    }

    private CucumberScenario getScenario(JsonObject scenario, CucumberScenario... background) {
        CucumberScenario scenario1 = new CucumberScenario();
        scenario1.setName(scenario.get("name").getAsString());
        if (scenario.get("type").getAsString().equals("scenario"))
            scenario1.setTcIds();
        if (background.length > 0) {
            scenario1.setBackground(background[0]);
            scenario1.getBackground().getScenarioElements().forEach(scenario1::setElement);
        }
        setElementsToScenario(scenario, scenario1, ScenarioElement.before, ScenarioElement.steps, ScenarioElement.after);
        scenario1.setStatus();
        scenario1.setErrorMessage();
        scenario1.setDuration();
        return scenario1;
    }


    private void setElementsToScenario(JsonObject scenario, CucumberScenario cucumberScenario, ScenarioElement... elementNames) {
        Gson mGson = new Gson();
        Arrays.stream(elementNames)
                .map(ScenarioElement::name) //map ScenarioElement to it's name
                .filter(scenario::has) //filter for only JsonElements with key name
                .map(scenario::getAsJsonArray) //get all JsonArrays after filtering
                .map(arr -> mGson.fromJson(arr, JsonObject[].class)) //map each JsonArray to List<JsonObject>
                .flatMap(Arrays::stream) //flatten Steam<List<JsonObject>> to Stream<JsonObject>
                .map(this::getScenarioElement) //map each JsonObject to CucumberScenarioElement object
                .forEach(cucumberScenario::setElement); //set each CucumberScenarioElement to cucumberScenario object
    }


    public List<CucumberScenario> getAllScenarios() {
        JsonArray features = jsonObject.getAsJsonArray();
        List<CucumberScenario> result = new ArrayList<>();
        for (JsonElement f : features) {
            JsonArray elements = f.getAsJsonObject().getAsJsonArray("elements");
            CucumberScenario background = null;
            for (JsonElement e : elements) {
                if (e.getAsJsonObject().get("type").getAsString().equals("scenario")) {
                    CucumberScenario scenario;
                    if (background != null) {
                        scenario = getScenario(e.getAsJsonObject(), background);
                        background = null;
                    } else
                        scenario = getScenario(e.getAsJsonObject());
                    result.add(scenario);
                } else
                    background = getScenario(e.getAsJsonObject());
            }
        }
        Set<List<String>> uniqueTCListIds = result.stream().map(CucumberScenario::getTcIds).collect(Collectors.toSet());
        uniqueTCListIds.stream()
                .map(sc -> result.stream().filter(s -> s.getTcIds().equals(sc)).collect(Collectors.toList()))
                .filter(scenarioOutlineRuns -> scenarioOutlineRuns.size() > 1)
                .forEach(scenarioOutlineRuns -> {
                    result.add(getMergedScenarioOutlineRun(scenarioOutlineRuns));
                    result.removeAll(scenarioOutlineRuns);
                });
        return result;
    }

    private CucumberScenario getMergedScenarioOutlineRun(List<CucumberScenario> scenarios) {
        CucumberScenario newScenario = new CucumberScenario();
        newScenario.setDuration(scenarios.stream().mapToLong(CucumberScenario::getDuration).sum());
        newScenario.setErrorMessage(scenarios.stream()
                .filter(s -> !s.getErrorMessage().isEmpty()).map(CucumberScenario::getErrorMessage).reduce((r, m) -> r + "\n" + m)
                .orElse(""));
        newScenario.setScenarioElements(scenarios.get(0).getScenarioElements());
        newScenario.setName(scenarios.get(0).getName());
        newScenario.setStatus(scenarios.stream().filter(s -> s.getStatus().equals("failed")).map(CucumberScenario::getStatus).findFirst().orElse("passed"));
        newScenario.setTcIds(scenarios.get(0).getTcIds());
        return newScenario;
    }
}
