package com.continuum.cucumber.utils.testrail;

import com.continuum.cucumber.shell.Shell;
import com.continuum.cucumber.utils.CommonUtils;
import com.continuum.cucumber.utils.cucumberResults.CucumberResultsParser;
import com.continuum.cucumber.utils.cucumberResults.CucumberScenario;
import com.continuum.cucumber.utils.enumerations.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import net.jodah.failsafe.function.CheckedRunnable;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.continuum.cucumber.utils.CommonArtifactUtils.getCucumberJsonResultsFile;
import static com.continuum.cucumber.utils.cucumberResults.CucumberResultUtilities.*;

@Slf4j
public class TestRailIntegrator {
    private static TestRailAPIClient client = new TestRailAPIClient();
    private static CucumberResultsParser cucumberResultsParser = new CucumberResultsParser(getCucumberJsonResultsFile());

    public static void updateTestRailResults() {
        if (!Shell.Application.getTestRail().isUpdate())
            return;
        String duration;
        String errorMsg;
        int resultTR;
        List<CucumberScenario> result = cucumberResultsParser.getAllScenarios();
        String testRun = getTestRailRunId();
        if (!result.isEmpty()) {
            for (CucumberScenario sc : result) {
                resultTR = getTestRailStatus(sc.getStatus());
                duration = getDurationMinutesString(sc.getDuration());
                errorMsg = sc.getErrorMessage();
                for (String tcID : sc.getTcIds()) {
                    addResultTestRail(testRun, tcID, resultTR, duration, errorMsg);
                }
            }
        } else
            log.error("*** No Scenario results were found in cucumber.json file ***");
    }


    @SuppressWarnings("unchecked")
    private static void addResultTestRail(String testRunID, String testCaseID, int status, String executionTime, String errorMsg) {
        Map<String, Comparable> data = new HashMap<>();
        data.put("status_id", status);
        data.put("elapsed", executionTime);
        data.put("comment", errorMsg);
        log.info(String.format("*** Trying to update result for TC <%s> in TR <%s> in  Test Rail  with status <%s>, elapsed <%s>, comment <%s>***", testCaseID,
                testRunID, status, executionTime, errorMsg));
        CheckedRunnable run = () -> {
            client.sendRequest("add_result_for_case/" + testRunID + "/" + testCaseID, HttpMethod.POST, data);
            log.info(String.format("*** Result for TC <%s> in TR <%s> updated in  Test Rail  Successfully ***", testCaseID,
                    testRunID));
        };
        try {
            CommonUtils.retryOnException(run, 2, 500, TestRailAPIException.class, IOException.class);
        } catch (Throwable e) {
            log.info(String.format("*** Failed to update TC <%s> in testrun <%s> after 2 retries with exception: <%s>", testCaseID, testRunID, e));
        }

    }


}