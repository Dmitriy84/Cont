package com.continuum.cucumber.run;

import com.continuum.cucumber.SpringBootCucumberApplication;
import com.continuum.cucumber.utils.GenerateReport;
import com.continuum.cucumber.utils.HtmlEmailSender;
import com.continuum.cucumber.utils.testrail.TestRailIntegrator;
import cucumber.api.CucumberOptions;
import org.junit.AfterClass;
import org.junit.runner.RunWith;

@RunWith(ContinuumCucumberRunner.class)
@CucumberOptions(glue = SpringBootCucumberApplication.PACKAGES)
public class TestRunner {
    @AfterClass
    public static void generateArtifacts() {
        GenerateReport.generateReport();
        HtmlEmailSender.sendReport();
        TestRailIntegrator.updateTestRailResults();
    }
}