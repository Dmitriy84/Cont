package com.continuum.cucumber.utils;

import com.continuum.cucumber.shell.Shell;
import com.continuum.cucumber.utils.cucumberResults.CucumberResultsParser;
import com.continuum.cucumber.utils.cucumberResults.CucumberScenario;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.continuum.cucumber.utils.CommonArtifactUtils.getCucumberJsonResultsFile;
import static com.continuum.cucumber.utils.cucumberResults.CucumberResultUtilities.*;

@Slf4j
public class ResultWriter {
    private static CucumberResultsParser cucumberResultsParser = new CucumberResultsParser(getCucumberJsonResultsFile());

    private static String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private static String getStatus(String status) {
        String newStatus = null;
        byte var3 = -1;
        switch (status.hashCode()) {
            case -1911513968:
                if (status.equals("Passed")) {
                    var3 = 6;
                }
                break;
            case -1281977283:
                if (status.equals("failed")) {
                    var3 = 0;
                }
                break;
            case -995381136:
                if (status.equals("passed")) {
                    var3 = 4;
                }
                break;
            case -21437972:
                if (status.equals("blocked")) {
                    var3 = 11;
                }
                break;
            case 2181950:
                if (status.equals("Fail")) {
                    var3 = 3;
                }
                break;
            case 2480177:
                if (status.equals("Pass")) {
                    var3 = 7;
                }
                break;
            case 3135262:
                if (status.equals("fail")) {
                    var3 = 2;
                }
                break;
            case 3433489:
                if (status.equals("pass")) {
                    var3 = 5;
                }
                break;
            case 64279661:
                if (status.equals("Block")) {
                    var3 = 8;
                }
                break;
            case 93832333:
                if (status.equals("block")) {
                    var3 = 10;
                }
                break;
            case 1643215308:
                if (status.equals("Blocked")) {
                    var3 = 9;
                }
                break;
            case 2096857181:
                if (status.equals("Failed")) {
                    var3 = 1;
                }
        }

        switch (var3) {
            case 0:
            case 1:
            case 2:
            case 3:
                newStatus = "Fail";
                break;
            case 4:
            case 5:
            case 6:
            case 7:
                newStatus = "Pass";
                break;
            case 8:
            case 9:
            case 10:
            case 11:
                newStatus = "Blocked";
        }

        return newStatus;
    }

    public static void writeToDB() {
        if (Shell.Application.getJenkins().isEnabled()) {
            String scenarioName;
            final String[] testCase = {"NA"};
            String executionTime;
            String testSteps;
            String status;
            String errorMessage;
            String tableName = "execution_status";
            String applicationName = Shell.Application.getName();
            String[] columnNames = new String[]{"testcase_id", "testsuite_id", "TestCase_Name", "testcase_status", "execution_date", "timeexecution",
                    "prod_name", "description"};
            DBUtilities.connectDB("mysql");
            String testRun = getTestRailRunId();
            for (CucumberScenario o : cucumberResultsParser.getAllScenarios()) {
                scenarioName = o.getName();
                o.getTcIds().stream().reduce((r, s) -> r + ", " + s).ifPresent(res -> testCase[0] = res);
                status = o.getStatus();
                errorMessage = o.getErrorMessage();
                testSteps = getFormattedTestSteps(o);
                executionTime = getDurationMinutesString(o.getDuration());
                String[] columnValues = new String[]{testCase[0], testRun, scenarioName, getStatus(status), getTimestamp(), executionTime, applicationName,
                        errorMessage + "\nSteps to reproduce:\n" + testSteps};
                DBUtilities.insertValues(tableName, columnNames, columnValues);
            }
            DBUtilities.closeConnection();
        } else {
            log.info("You are running the tests from local machine. Database cannot be connected.");
        }
    }
}