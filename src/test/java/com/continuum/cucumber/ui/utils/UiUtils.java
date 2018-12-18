package com.continuum.cucumber.ui.utils;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.openqa.selenium.JavascriptExecutor;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

@UtilityClass
public class UiUtils {
    public static void switchTabAndCloseByIndex(int index) {
        switchToTabByIndex(index);
        ((JavascriptExecutor) getWebDriver()).executeScript("window.close()");
        getWebDriver().switchTo().window(new ArrayList<>(getWebDriver().getWindowHandles()).get(0));
    }

    public static void switchToTabByIndex(int index) {
        val tabs = new ArrayList<String>(getWebDriver().getWindowHandles());
        if (tabs.size() > index)
            getWebDriver().switchTo().window(tabs.get(index));
        else
            throw new NoSuchElementException("No tab with index " + index + " available! Total qty of tabs " + tabs.size());
    }

    public static String getDataFromClipboard() throws UnsupportedFlavorException, IOException {
        val clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        val contents = clipboard.getContents(null);
        return (String) contents.getTransferData(DataFlavor.stringFlavor);
    }

    public static void clickJS(SelenideElement element) {
        element.scrollTo();
        ((JavascriptExecutor) WebDriverRunner.getWebDriver()).executeScript("arguments[0].click()",
                element);
    }

}