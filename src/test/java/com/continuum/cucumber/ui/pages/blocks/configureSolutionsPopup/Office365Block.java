package com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup;

import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import lombok.Getter;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Office365Block extends BaseBlock {
    private SelenideElement infoPanelOffice365 = $(By.cssSelector("div[data-component='PanelWithIconComponent']:nth-of-type(2)"));
    private SelenideElement loginBlock = $(By.cssSelector("div[data-component='PanelWithIconComponent']:nth-of-type(1)"));
    private SelenideElement goToOffice365SiteBtn = $(By.cssSelector("button"));
    private SelenideElement linkToDocCenterOffice365 = infoPanelOffice365.$(By.tagName("a"));

    public Office365Block() {
        super(By.cssSelector("div[data-component='Office365ActivationComponent']"));
    }
}
