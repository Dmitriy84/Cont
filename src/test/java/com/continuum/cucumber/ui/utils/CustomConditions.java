package com.continuum.cucumber.ui.utils;

import com.codeborne.selenide.Condition;
import lombok.experimental.UtilityClass;
import org.openqa.selenium.WebElement;

@UtilityClass
public class CustomConditions {
    public static Condition attributePartlyMatch(final String attributeName, final String attributeValue) {
        return new Condition("attributePartlyMatch") {
            public boolean apply(WebElement element) {
                return element.getAttribute(attributeName) != null && element.getAttribute(attributeName).contains(attributeValue);
            }

            public String toString() {
                return this.name + " " + attributeName + "contains condition " + attributeValue;
            }
        };
    }

    public static Condition buttonEnabled() {
        return Condition.attribute("role", "button");
    }

    public static Condition cssValuePartlyMatch(final String cssValueName, final String cssValue) {
        return new Condition("cssValuePartlyMatch") {
            public boolean apply(WebElement element) {
                return element.getCssValue(cssValueName) != null && element.getCssValue(cssValueName).contains(cssValue);
            }

            public String toString() {
                return this.name + " " + cssValueName + "contains condition " + cssValue;
            }
        };
    }

}