package com.continuum.cucumber.utils;

import com.continuum.cucumber.shell.Shell;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;

import java.io.File;
import java.util.Collections;

import static com.continuum.cucumber.utils.CommonArtifactUtils.getCucumberJsonResultsFile;

@Slf4j
public class GenerateReport {
    public static void generateReport() {
        val configuration = new Configuration(
                new File(Shell.Application.getReportOutputDirectory()),
                Shell.Application.getName() + " Automation Report");
        configuration.setBuildNumber("version 1");
        new ReportBuilder(Collections.singletonList(getCucumberJsonResultsFile()), configuration).generateReports();
    }
}