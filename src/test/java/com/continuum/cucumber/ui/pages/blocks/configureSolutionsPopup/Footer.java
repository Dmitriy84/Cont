package com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup;

import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import lombok.Getter;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Footer extends BaseBlock {
    private String buttonLocator = "./button/span[contains(text(),'%s')]/..";

    public Footer() {
        super(By.cssSelector("div[data-component='ActivateAndConfigureFooterComponent']"));
    }

    public SelenideElement getButtonByName(String name) {
        return $(By.xpath(String.format(buttonLocator, name)));
    }
}
