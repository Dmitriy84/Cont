package com.continuum.cucumber.ui.pages;

import com.codeborne.selenide.Selenide;
import com.continuum.cucumber.shell.Shell;
import lombok.Getter;
import lombok.Setter;

import static com.continuum.cucumber.ui.utils.CustomWaits.waitForJSExecuted;
import static com.continuum.cucumber.ui.utils.CustomWaits.waitForPageLoad;

public class BasePage<T> {
    @Getter
    @Setter
    protected String url = "";

    @SuppressWarnings("unchecked")
    public T open() {
        Selenide.open(getFullUrl());
        waitForPageLoad();
        waitForJSExecuted();
        return (T) this;
    }

    public String getFullUrl() {
        return Shell.Application.getEnvironment().getBaseUrlUI() + url;
    }
}