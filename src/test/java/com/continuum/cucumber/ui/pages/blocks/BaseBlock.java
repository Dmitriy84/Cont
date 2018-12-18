package com.continuum.cucumber.ui.pages.blocks;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class BaseBlock {
    @Getter
    protected SelenideElement wrappedElement;

    public BaseBlock(String element) {
        wrappedElement = Selenide.$(element);
    }

    public BaseBlock(SelenideElement element) {
        wrappedElement = element;
    }

    public BaseBlock(By element) {
        wrappedElement = Selenide.$(element);
    }

    public BaseBlock waitFor(Condition condition, long timeout) {
        wrappedElement.waitUntil(condition, timeout);
        return this;
    }

    public BaseBlock should(Condition condition) {
        wrappedElement.should(condition);
        return this;
    }

    public ElementsCollection $$(By by) {
        return wrappedElement.$$(by);
    }

    public SelenideElement $(By by) {
        return wrappedElement.$(by);
    }
}