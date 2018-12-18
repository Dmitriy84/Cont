package com.continuum.cucumber.ui.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;
import static com.continuum.cucumber.ui.enumerations.Timeouts.EXTRA_LONG;

@Component
public class LoginPage extends BasePage<LoginPage> {
    private SelenideElement username = $(By.id("idToken1"));
    private SelenideElement password = $(By.id("idToken2"));
    private SelenideElement loginBtn = $(By.id("loginButton_0"));

    public void onPage() {
        username.waitUntil(Condition.appears, EXTRA_LONG.milliseconds);
    }

    public void login(String username, String password) {
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        loginBtn.click();
    }
}