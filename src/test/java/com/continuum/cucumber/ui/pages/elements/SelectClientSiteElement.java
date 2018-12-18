package com.continuum.cucumber.ui.pages.elements;

import com.codeborne.selenide.*;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import lombok.Getter;
import lombok.var;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.continuum.cucumber.ui.enumerations.Timeouts.EXTRA_SMALL;
import static com.continuum.cucumber.ui.enumerations.Timeouts.SMALL;
import static com.continuum.cucumber.ui.utils.CustomWaits.*;


@Component
@Getter
public class SelectClientSiteElement extends BaseBlock {
    private SelenideElement expandButton = $(By.cssSelector("button i"));
    private SelenideElement selectedSite = $(By.cssSelector("button span"));
    private SelenideElement filterInput = $(By.cssSelector("input"));
    private ElementsCollection sites = $$(By.cssSelector("*[data-component='SitesComponent'] div[role='listitem']"));
    private SelenideElement noResults = $(By.cssSelector("*[data-component='SitesComponent']"));
    private ElementsCollection siteNames = $$(By.cssSelector("*[data-component='SitesComponent'] div[role='listitem'] label"));
    private SelenideElement clearSearch = $(By.cssSelector("div[data-component='SearchSitesComponent'] i"));

    public SelectClientSiteElement() {
        super(By.cssSelector("div[data-component='SitesSelectorComponent']"));
    }

    public String selectSiteByIndex(int index) {
        var sitesQty = sites.size();
        var allLoaded = false;
        while (sitesQty <= index && !allLoaded) {
            sites.get(sites.size() - 1).scrollIntoView(true);
            waitForJSExecuted();
            allLoaded = !isElementsQtyChanged(sites, sitesQty);
            sitesQty = sites.size();
        }
        if (sitesQty <= index)
            throw new NoSuchElementException("No site with index " + index);
        var expectedSite = siteNames.get(index).getText();
        sites.get(index).click();
        selectedSite.shouldHave(Condition.text(expectedSite));
        return expectedSite;
    }

    public SelectClientSiteElement clearSearch() {
        waitForJSExecuted();
        var sitesQty = sites.size();
        clearSearch.click();
        waitForJSExecuted();
        waitForElementsQtyChanged(sites, sitesQty);
        return this;
    }

    public List<String> getAvailableSiteNames() {
        waitForJSExecuted();
        return siteNames.stream().map(SelenideElement::getText).collect(Collectors.toList());
    }


    public void searchForAvailableSite(String search) {
        filterInput.waitUntil(Condition.appear, SMALL.milliseconds);
        waitForJSExecuted();
        new Actions(WebDriverRunner.getWebDriver()).sendKeys(filterInput, search).perform();
        waitForJSExecuted();

    }

    public SelectClientSiteElement expandSitesDropdown() {
        waitForJSExecuted();
        if (!filterInput.isDisplayed()) {
            expandButton.waitUntil(Condition.appear, EXTRA_SMALL.milliseconds).waitUntil(Condition.enabled, EXTRA_SMALL.milliseconds).click();
            sites.shouldHave(CollectionCondition.sizeGreaterThan(0), SMALL.milliseconds);
        }
        return this;
    }

    public SelectClientSiteElement closeSitesDropdown() {
        waitForJSExecuted();
        if (filterInput.isDisplayed()) {
            expandButton.waitUntil(Condition.appear, EXTRA_SMALL.milliseconds).waitUntil(Condition.enabled, EXTRA_SMALL.milliseconds).click();
            filterInput.waitUntil(Condition.disappear, SMALL.milliseconds);
        }
        return this;
    }

}