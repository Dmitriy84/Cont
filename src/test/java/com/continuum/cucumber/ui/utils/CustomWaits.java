package com.continuum.cucumber.ui.utils;

import com.codeborne.selenide.ElementsCollection;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.continuum.cucumber.ui.enumerations.Timeouts.EXTRA_LONG;
import static com.continuum.cucumber.ui.enumerations.Timeouts.SMALL;

@UtilityClass
public class CustomWaits {
    public static ExpectedCondition<Boolean> javascriptDone = d -> {
        try {//window.setTimeout executes only when browser is idle,
            //introduces needed wait time when javascript is running in browser
            return ((Boolean) ((JavascriptExecutor) d).executeAsyncScript(
                    " var callback =arguments[arguments.length - 1]; " +
                            " var count=42; " +
                            " setTimeout( collect, 0);" +
                            " function collect() { " +
                            " if(count-->0) { " +
                            " setTimeout( collect, 0); " +
                            " } " +
                            " else {callback(" +
                            "    true" +
                            " );}" +
                            " } "
            ));
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    };

    public static ExpectedCondition<Boolean> pageLoaded = d -> ((JavascriptExecutor) getWebDriver()).executeScript("return document.readyState")
            .equals("complete");


    public static void waitForNumberOfTabsToBe(int qty) {
        new WebDriverWait(getWebDriver(), SMALL.seconds).
                until((driver) -> driver.getWindowHandles().size() == qty);
    }

    public static void waitForJSExecuted() {
        new WebDriverWait(getWebDriver(), EXTRA_LONG.seconds).until(javascriptDone);
    }

    public static void waitForPageLoad() {
        new WebDriverWait(getWebDriver(), EXTRA_LONG.seconds).until(pageLoaded);
    }

    public static void waitForElementsQtyChanged(ElementsCollection elements, int sizeBefore, int... timeoutMiliseconds) {
        new WebDriverWait(getWebDriver(), timeoutMiliseconds.length > 0 ? timeoutMiliseconds[0] : SMALL.seconds).until((ExpectedCondition<Boolean>) driver -> elements.size() != sizeBefore);
    }

    public static boolean isElementsQtyChanged(ElementsCollection elements, int sizeBefore, int... timeout) {
        try {
            waitForElementsQtyChanged(elements, sizeBefore, timeout);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }


}