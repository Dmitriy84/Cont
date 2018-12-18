package com.continuum.cucumber.ui.pages;

import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.ui.pages.blocks.ConfigureSolutionsPopup;
import lombok.Getter;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.codeborne.selenide.Selenide.$;

@Component
@Getter
public class SecurityTabPage extends BasePage<SecurityTabPage> {
    @Autowired
    private ConfigureSolutionsPopup configureSolutionsPopup;

    public SecurityTabPage() {
        setUrl("qadashb/QuickAccess/NewDesktops/scorecard");
    }


    public void selectAction(String name) {
        getAction(name).click();
    }

    public SelenideElement getAction(String name){
        return $(By.xpath(String.format("//*[contains(text(),'%s')]", name)));
    }
}