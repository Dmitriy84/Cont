package com.continuum.cucumber.ui.pages.blocks;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import lombok.var;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
public class HeaderBlock extends BaseBlock {
    private ElementsCollection tabLinks = $$(By.cssSelector("#divMainTopNav li"));

    public HeaderBlock() {
        super("div.head_container");
    }

    public void selectTabByVisibleName(String name) {
        var filtered = tabLinks.filter(Condition.exactText(name));
        filtered.shouldHaveSize(1).get(0).click();
        filtered.get(0).shouldHave(Condition.attribute("class", "active"));
    }
}