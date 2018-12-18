package com.continuum.cucumber.run;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.continuum.cucumber.shell.Shell;
import com.continuum.cucumber.ui.enumerations.DriverNaming;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.AfterStep;
import cucumber.api.java.Before;
import io.github.bonigarcia.wdm.DriverManagerType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;

import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class UiHooks {

    private byte[] getBrowserConsoleLogs() {
        String s = "";
        if (DriverManagerType.CHROME.toString().equalsIgnoreCase(Configuration.browser)) {
            LogEntries logs = getWebDriver().manage().logs().get("browser");
            s = logs.getAll().stream().map(LogEntry::toString).reduce((r, l) -> r + "\n" + l).orElse("");
        }
        return s.getBytes(StandardCharsets.UTF_8);
    }

    @Before("@UI")
    public void setup() {
        DriverNaming driverNaming = DriverNaming.valueOf(Shell.Application.getSelenium().getBrowser());
        Configuration.browser = driverNaming.getSelenideName();
        WebDriverManager.getInstance(DriverManagerType.valueOf(driverNaming.getWdmName())).setup();
        if (Shell.Application.getSelenium().isRemote())
            Configuration.remote = Shell.Application.getSelenium().getHubUrl();
    }

    @AfterStep("@UI")
    public void clearConsoleLogs() {
        if (DriverManagerType.CHROME.toString().equalsIgnoreCase(Configuration.browser))
            getWebDriver().manage().logs().get("browser");
    }

    @After("@UI")
    public void attachArtifacts(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) getWebDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
            byte[] source = getBrowserConsoleLogs();
            scenario.embed(source, "text/plain");
            byte[] url = getWebDriver().getCurrentUrl().getBytes(StandardCharsets.UTF_8);
            scenario.embed(url, "text/plain");
        }
        WebDriverRunner.closeWebDriver();
        Selenide.close();
    }

}