package com.continuum.cucumber.ui.pages.elements;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.ui.enumerations.StepsEnums;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import org.openqa.selenium.By;

import static com.continuum.cucumber.ui.enumerations.Timeouts.EXTRA_SMALL;

public class SliderElement extends BaseBlock {
    private SelenideElement label = $(By.cssSelector("span:nth-of-type(3)"));

    public SliderElement(By by) {
        super(by);
    }

    public StepsEnums.Status getValue() {
        return label.getText().equalsIgnoreCase("Active") ? StepsEnums.Status.ENABLED : StepsEnums.Status.DISABLED;
    }

    public void setValue(StepsEnums.Status value) {
        if (value != getValue())
            label.click();
        label.waitUntil(Condition.text(value == StepsEnums.Status.ENABLED ? "Active" : "Inactive"), EXTRA_SMALL.milliseconds);
    }
}