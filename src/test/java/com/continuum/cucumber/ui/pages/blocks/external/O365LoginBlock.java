package com.continuum.cucumber.ui.pages.blocks.external;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.ui.enumerations.Timeouts;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import lombok.Getter;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
@Getter
public class O365LoginBlock extends BaseBlock {

    private SelenideElement login = $(By.cssSelector("input[name='loginfmt']"));
    private SelenideElement nextBtn = $(By.cssSelector("input[type='submit']"));
    private SelenideElement password = $(By.cssSelector("input[name='passwd']"));
    private SelenideElement dontSaveSignIn = $(By.cssSelector("input#idBtn_Back"));
    private SelenideElement passwordError = $(By.cssSelector("div#passwordError"));

    public O365LoginBlock() {
        super(By.cssSelector("div.inner"));
    }

    public void login(String login, String password, boolean success) {
        this.login.waitUntil(Condition.appears, Timeouts.SMALL.milliseconds).sendKeys(login);
        nextBtn.click();
        this.password.waitUntil(Condition.appears, Timeouts.MEDIUM.milliseconds).sendKeys(password);
        nextBtn.click();
        if (success) {
            this.password.waitUntil(Condition.disappear, Timeouts.MEDIUM.milliseconds);
            dontSaveSignIn.click();
            dontSaveSignIn.waitUntil(Condition.disappear, Timeouts.MEDIUM.milliseconds);
        } else
            passwordError.waitUntil(Condition.appears, Timeouts.MEDIUM.milliseconds);
    }
}
