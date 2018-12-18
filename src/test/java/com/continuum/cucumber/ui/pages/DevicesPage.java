package com.continuum.cucumber.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;
import static com.continuum.cucumber.ui.enumerations.Timeouts.SMALL;
import static com.continuum.cucumber.ui.utils.CustomWaits.waitForJSExecuted;

@Component
@Getter
public class DevicesPage extends BasePage<DevicesPage> {

    private SelenideElement selectView = $(By.cssSelector("[id^='View']"));
    private SelenideElement ppColumn = $(By.xpath("//span[@role='columnheader' and contains(text(), 'Profile & Protect')]"));
    private SelenideElement allSites = $(By.xpath("//*[contains(text(), 'ALL SITES')]"));

    public DevicesPage selectSite(String siteName) {
        allSites.click();
        $(By.xpath("//span[contains(text(), '" + siteName + "')]")).click();
        allSites.waitUntil(Condition.disappear, SMALL.milliseconds);
        waitForJSExecuted();
        return this;
    }

    public DevicesPage selectPPView() {
        selectView.selectOptionContainingText("Profile and Protect");
        waitForJSExecuted();
        return this;
    }

}
