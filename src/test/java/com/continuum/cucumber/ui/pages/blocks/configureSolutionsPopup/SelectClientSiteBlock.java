package com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup;

import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import com.continuum.cucumber.ui.pages.elements.SelectClientSiteElement;
import lombok.Getter;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SelectClientSiteBlock extends BaseBlock {
    @Autowired
    private SelectClientSiteElement selectClientSiteElement;
    private SelenideElement noteTextBlockOnSelectClientSiteStep = $(By.cssSelector("div[data-component='PanelWithIconComponent']:nth-of-type(2) span"));

    public SelectClientSiteBlock() {
        super(By.cssSelector("div[data-component='SelectSiteComponent']"));
    }
}
