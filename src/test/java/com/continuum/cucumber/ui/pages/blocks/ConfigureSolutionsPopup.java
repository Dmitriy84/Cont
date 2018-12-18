package com.continuum.cucumber.ui.pages.blocks;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.configuration.TypeRegistryConfigurerAnnotation;
import com.continuum.cucumber.ui.enumerations.StepsEnums;
import com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup.*;
import com.continuum.cucumber.ui.pages.elements.SliderElement;
import com.continuum.cucumber.ui.utils.CustomConditions;
import lombok.Getter;
import lombok.var;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.continuum.cucumber.ui.enumerations.Timeouts.SMALL;

@Getter
@Component
public class ConfigureSolutionsPopup extends BaseBlock {
    public static final String selectClientSiteNote = "After you select the site, you'll be able to activate one or more security solutions for it.";
    private SelenideElement generalMessage = $(By.cssSelector("[data-component='InlineNotification']"));
    @Autowired
    private Footer footer;
    @Autowired
    private Office365Block office365Block;
    @Autowired
    private SelectClientSiteBlock selectClientSiteBlock;
    @Autowired
    private GetStartedBlock getStartedBlock;
    @Autowired
    private ProgressTrackerBlock progressTrackerBlock;
    private SelenideElement closeButton = $(By.cssSelector("div[data-component='ModalTitleElement'] i[data-component='Icon']"));
    //Switchers between tabs on 'Activate Solutions' step
    private ElementsCollection tabSwitchers = $$(By.cssSelector("div[data-component='SolutionsTabItemComponent']"));
    //Activate slider on tabs on 'Activate Solutions' step
    private SliderElement activateSlider = new SliderElement(By.cssSelector("div[role='switch']"));
    //Blocks on 'Profile and Protect' tab on 'Activate Solutions' step
    private SelenideElement textBoxOnProfileAndProtectTab = $(By.cssSelector("div[data-component='PanelWithIconComponent']"));
    private SelenideElement generalErrorMessage = $(By.cssSelector("[data-component='InlineNotification']"));
    private SelenideElement errorMessageOnProductTab = activateSlider.$(By.xpath("./../following-sibling::div[1]"));

    public ConfigureSolutionsPopup() {
        super(By.cssSelector("div[aria-label='Activate and configure security solutions']"));
    }

    public SelenideElement getErrorMessageOnProductTab() {
        return $$(By.cssSelector("div[data-component='PanelWithIconComponent']")).find(CustomConditions.cssValuePartlyMatch("border",
                StepsEnums.Color.RED.getValue()));
    }

    public List<String> getTabSwitchersOrder() {
        return tabSwitchers.stream().map(s -> s.$("span").getText()).collect(Collectors.toList());
    }

    public SelenideElement getTabSwitcherByName(ProductName ProductName) {
        return tabSwitchers.stream().filter(t -> t.$(By.cssSelector("span")).getText().equals(ProductName.ProductName)).findFirst().orElseThrow(() -> new NoSuchElementException("No tab with " +
                "ProductName " + ProductName + " is available!"));
    }

    public void selectTabOnActivateSolutionsStep(ProductName ProductName) {
        var tabSwitcher = getTabSwitcherByName(ProductName);
        tabSwitcher.click();
        tabSwitcher.waitUntil(CustomConditions.cssValuePartlyMatch("border", "rgb(119, 186, 237)"), SMALL.milliseconds);
    }

    public String getSelectedTabOnActivateSolutionsStep() {
        return tabSwitchers.stream().filter(t -> t.getCssValue("border").contains("rgb(119, 186, 237)")).findFirst().orElseThrow(() -> new NoSuchElementException("No" +
                " active tab!")).$(By.cssSelector("span:nth-of-type(1)")).getText();
    }

    public String getTabStatus(ProductName ProductName) {
        return getTabSwitcherByName(ProductName).$(By.cssSelector("span:nth-of-type(2)")).getText();
    }

    @TypeRegistryConfigurerAnnotation
    public enum ProductName {
        PROFILE("Profile and Protect Endpoint and User Account"), OFFICE365("Microsoft Office 365"), DETECT("Detect & Respond Endpoint");

        @Getter
        private String ProductName;

        ProductName(String name) {
            this.ProductName = name;
        }
    }
}