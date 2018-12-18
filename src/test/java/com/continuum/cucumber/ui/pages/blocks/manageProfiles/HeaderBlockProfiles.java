package com.continuum.cucumber.ui.pages.blocks.manageProfiles;

import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import lombok.Getter;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
@Getter
public class HeaderBlockProfiles extends BaseBlock {

    private SelenideElement backButton = $(By.cssSelector("[data-component='Icon']"));
    private SelenideElement breadCrumbs = $(By.cssSelector("[data-component='Breadcrumbs'] span:nth-of-type(1)"));
    private SelenideElement manageExclusionBtn = $(By.cssSelector("[data-component='Button']"));

    public HeaderBlockProfiles() {
        super(By.cssSelector("[data-component='HeaderPanelComponent']"));
    }
}
