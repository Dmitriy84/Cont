package com.continuum.cucumber.ui.steps.wizard_steps;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.continuum.cucumber.ui.enumerations.StepsEnums;
import com.continuum.cucumber.ui.pages.O365LoginPage;
import com.continuum.cucumber.ui.pages.SecurityTabPage;
import com.continuum.cucumber.ui.pages.blocks.ConfigureSolutionsPopup;
import com.continuum.cucumber.ui.steps.AbstractUIStepsDefinition;
import com.continuum.cucumber.ui.utils.CustomConditions;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.var;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static com.continuum.cucumber.ui.enumerations.Timeouts.LONG;
import static com.continuum.cucumber.ui.enumerations.Timeouts.SMALL;
import static com.continuum.cucumber.ui.utils.CustomWaits.waitForNumberOfTabsToBe;
import static com.continuum.cucumber.ui.utils.CustomWaits.waitForPageLoad;
import static com.continuum.cucumber.ui.utils.UiUtils.switchToTabByIndex;
import static org.assertj.core.api.Assertions.assertThat;

public class WizardSecondStepSteps extends AbstractUIStepsDefinition {
    @Autowired
    private SecurityTabPage securityTabPage;

    @Autowired
    private O365LoginPage o365LoginPage;

    /**
     * On the wizard verify that "Activate solutions" step of wizard is opened
     *
     * @author Anna Ostrovskaya
     */
    @Then("\"Activate solutions\" step of wizard is opened")
    public void onSecondWizardStep() {
        securityTabPage.getConfigureSolutionsPopup().getTabSwitchers().shouldHave(CollectionCondition.sizeGreaterThan(0), LONG.milliseconds);
    }

    /**
     * On 'Activate solutions' step of the wizard for 'Office 365' product login to Office 365 with valid creds and switch back to 'Continuum' portal page
     *
     * @param email    Office 365 user email (e.g. 'email@mail.com')
     * @param password Office 365 user password (e.g. 'pass')
     * @author Anna Ostrovskaya
     */
    @When("login to Office 365 with email {string} and password {string}")
    public void loginToOffice365(String email, String password) {
        o365LoginPage.getO365LoginBlock().login(email, password, true);
        waitForNumberOfTabsToBe(1);
        switchToTabByIndex(0);
    }

    /**
     * On 'Activate solutions' step of the wizard for 'Office 365' product login to Office 365 with invalid creds and switch back to 'Continuum' portal page
     *
     * @param email    Office 365 user email (e.g. 'email@mail.com')
     * @param password Office 365 user password (e.g. 'pass')
     * @author Anna Ostrovskaya
     */
    @When("login to Office 365 with invalid email {string} and password {string}")
    public void loginToOffice365Error(String email, String password) {
        o365LoginPage.getO365LoginBlock().login(email, password, false);
        switchToTabByIndex(0);
    }

    /**
     * On 'Activate solutions' step of the wizard for 'Office 365' product click 'Login to Office 365' button on the second step of wizard
     *
     * @author Anna Ostrovskaya
     */
    @When("click 'Go to Office 365' button")
    public void clickGoToOffice365Btn() {
        waitForPageLoad();
        securityTabPage.getConfigureSolutionsPopup().getOffice365Block().getGoToOffice365SiteBtn().shouldBe(Condition.appear).shouldBe(Condition.enabled).click();
        waitForNumberOfTabsToBe(2);
        switchToTabByIndex(1);
        //Wait for redirection to O365 login page to start
        new Actions(WebDriverRunner.getWebDriver()).pause(2000).perform();
        o365LoginPage.getO365LoginBlock().waitFor(Condition.appears, SMALL.milliseconds);
    }

    /**
     * On 'Activate solutions' step of the wizard change status of currently selected product
     *
     * @param status Status of product which should be set (ENABLED/DISABLED) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Status}
     * @author Anna Ostrovskaya
     */
    @When("change status of product to {Status}")
    public void changeStatusOfProduct(StepsEnums.Status status) {
        securityTabPage.getConfigureSolutionsPopup().getActivateSlider().setValue(status);
    }

    /**
     * On 'Activate solutions' step of the wizard change status of currently selected product to the opposite
     *
     * @author Anna Ostrovskaya
     * @deprecated
     */
    @Deprecated
    @When("change status of product to opposite")
    public void changeStatusOfProductToOpposite() {
        securityTabPage.getConfigureSolutionsPopup().getActivateSlider()
                .setValue(context.read("tabStatus", StepsEnums.Status.class) == StepsEnums.Status.ENABLED ? StepsEnums.Status.DISABLED :
                        StepsEnums.Status.ENABLED);
    }

    /**
     * On 'Activate solutions' step of the wizard remember current status of selected product
     *
     * @author Anna Ostrovskaya
     */
    @When("get current status of selected product")
    public void getStatusOfTabOnActivateSolutions() {
        context.write("tabStatus", securityTabPage.getConfigureSolutionsPopup().getActivateSlider().getValue());
    }

    /**
     * On 'Activate solutions' step of the wizard  product select given product
     *
     * @param ProductName product name (PROFILE/OFFICE365/DETECT) {@link com.continuum.cucumber.ui.pages.blocks.ConfigureSolutionsPopup.ProductName}
     * @author Anna Ostrovskaya
     */
    @When("select product {ProductName} on \"Activate solution\" step")
    public void selectTabOnActivateSolutionStep(ConfigureSolutionsPopup.ProductName ProductName) {
        securityTabPage.getConfigureSolutionsPopup().selectTabOnActivateSolutionsStep(ProductName);
    }

    /**
     * On 'Activate solutions' step of the wizard verify that selected product is matching the given one
     *
     * @param ProductName given product name (PROFILE/OFFICE365/DETECT) {@link com.continuum.cucumber.ui.pages.blocks.ConfigureSolutionsPopup.ProductName}
     * @author Anna Ostrovskaya
     */
    @Then("selected product on \"Activate Solutions\" step should be {ProductName}")
    public void checkSelectedTabOnActivateSolutions(ConfigureSolutionsPopup.ProductName ProductName) {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getSelectedTabOnActivateSolutionsStep())
                .as("Incorrect tab is active on 'Activate Solutions' step").isEqualTo(ProductName.getProductName());
    }

    /**
     * On 'Activate solutions' step of the wizard verify status of activation slider for currently selected product
     *
     * @param state expected position of activation slider (ENABLED/DISABLED) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Status}
     * @author Anna Ostrovskaya
     */
    @Then("status of currently selected product should be {Status}")
    public void checkActivationSliderValue(StepsEnums.Status state) {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getActivateSlider().getValue())
                .as("Incorrect state of activation slider on 'Activate Solutions' step").isEqualTo(state);
    }

    /**
     * On 'Activate solutions' step of the wizard for 'Office 365' product verify that current status of activation slider matches the one that was remembered
     *
     * @author Anna Ostrovskaya
     * @deprecated
     */
    @Deprecated
    @Then("status of currently selected product should be as it was previously")
    public void checkActivationSliderValue() {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getActivateSlider().getValue())
                .as("Incorrect state of activation slider on 'Activate Solutions' step").isEqualTo(context.read("tabStatus", StepsEnums.Status.class));
    }

    /**
     * On 'Activate solutions' step of the wizard for 'Office 365' product click 'Learn more in doc center' link
     *
     * @author Anna Ostrovskaya
     */
    @When("click 'Learn more in doc center' link")
    public void clickLearnMoreOffice365Link() {
        var link = securityTabPage.getConfigureSolutionsPopup().getOffice365Block().getLinkToDocCenterOffice365();
        context.write("learnMoreLink", link.getAttribute("href"));
        link.shouldBe(Condition.appears).click();
    }

    /**
     * On 'Activate solutions' step of the wizard verify that order of products' matches the given one
     *
     * @param productNames expected order of products (PROFILE/OFFICE365/DETECT)
     *                     {@link com.continuum.cucumber.ui.pages.blocks.ConfigureSolutionsPopup.ProductName}
     * @author Anna Ostrovskaya
     */
    @Then("order of products on \"Activate Solutions\" step should be {ProductNameList}")
    public void checkTabsOrder(List<ConfigureSolutionsPopup.ProductName> productNames) {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getTabSwitchersOrder()).as("Incorrect order of product tabs on 'Activate Solutions' step")
                .isEqualTo(productNames.stream().map(ConfigureSolutionsPopup.ProductName::getProductName).collect(Collectors.toList()));
    }

    /**
     * On 'Activate solutions' step of the wizard verify that product's status matches the given one
     *
     * @param ProductName name of the product (PROFILE/OFFICE365/DETECT)
     *                    {@link com.continuum.cucumber.ui.pages.blocks.ConfigureSolutionsPopup.ProductName}
     * @param status      expected product's status (ENABLED/DISABLED) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Status}
     * @author Anna Ostrovskaya
     */
    @Then("state of product {ProductName} should be {Status}")
    public void checkTabState(ConfigureSolutionsPopup.ProductName ProductName, StepsEnums.Status status) {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getTabStatus(ProductName))
                .as("Incorrect status of tab %s", ProductName.getProductName()).isEqualToIgnoringCase(status.getStatusName());
    }

    /**
     * On 'Activate solutions' step of the wizard for 'Office 365' product verify 'Login to Office 365' button visibility
     *
     * @param visibility expected visibility (VISIBLE/INVISIBLE) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Visibility}
     * @author Anna Ostrovskaya
     */
    @Then("Login to Office 365 button should be {Visibility}")
    public void checkLoginToO365ButtonVisible(StepsEnums.Visibility visibility) {
        securityTabPage.getConfigureSolutionsPopup().getOffice365Block().getGoToOffice365SiteBtn().shouldBe(visibility.getCondition());
    }

    /**
     * On 'Activate solutions' step of the wizard for 'Office 365' product verify 'Login to Office 365' button status
     *
     * @param status expected status (ENABLED/DISABLED) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Status}
     * @author Anna Ostrovskaya
     */
    @Then("Login to Office 365 button should be {Status}")
    public void checkLoginToO365ButtonStatus(StepsEnums.Status status) {
        securityTabPage.getConfigureSolutionsPopup().getOffice365Block().getGoToOffice365SiteBtn().shouldBe(status.getCondition());
    }


    /**
     * On 'Activate solutions' step of the wizard for 'Office 365' product verify message in 'Login to Office 365' block
     *
     * @param message expected message (e.g. 'Some message text')
     * @author Anna Ostrovskaya
     */
    @Then("message in \"Login to Office 365\" block should be {string}")
    public void checkLoginToO365Message(String message) {
        securityTabPage.getConfigureSolutionsPopup().getOffice365Block().getLoginBlock().shouldHave(Condition.text(message));
    }

    /**
     * On 'Activate solutions' step of the wizard verify visibility of general error message with expected text
     *
     * @param message    expected message (e.g. 'Some message text')
     * @param visibility expected visibility of error message (VISIBLE/INVISIBLE) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Visibility}
     * @author Anna Ostrovskaya
     */
    @Then("general error message with text {string} should be {Visibility}")
    public void checkGeneralErrorMessage(String message, StepsEnums.Visibility visibility) {
        var element = securityTabPage.getConfigureSolutionsPopup().getGeneralErrorMessage();
        element.waitUntil(visibility.getCondition(), SMALL.milliseconds);
        if (visibility == StepsEnums.Visibility.VISIBLE)
            element.shouldHave(Condition.exactText(message));
    }

    /**
     * On 'Activate solutions' step of the wizard verify visibility of error message with expected text on product tab
     *
     * @param message    expected message (e.g. 'Some message text')
     * @param visibility expected visibility of error message (VISIBLE/INVISIBLE) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Visibility}
     * @author Anna Ostrovskaya
     */
    @Then("error message on product's tab with text {string} should be {Visibility}")
    public void checkProductTabErrorMessage(String message, StepsEnums.Visibility visibility) {
        var element = securityTabPage.getConfigureSolutionsPopup().getErrorMessageOnProductTab();
        element.waitUntil(visibility.getCondition(), SMALL.milliseconds);
        if (visibility == StepsEnums.Visibility.VISIBLE)
            element.shouldHave(Condition.exactText(message));
    }

    /**
     * On 'Activate solutions' step of the wizard verify that product's tab is highlighted with expected color
     *
     * @param productName product name (PROFILE/OFFICE365/DETECT)
     *                    {@link com.continuum.cucumber.ui.pages.blocks.ConfigureSolutionsPopup.ProductName}
     * @param color       expected highlight color (WHITE/RED/LIGHT_BLUE/DARK_BLUE) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Color}
     * @author Anna Ostrovskaya
     */
    @Then("{ProductName} product's tab should be highlighted in {Color}")
    public void checkProductTabHighlightedWithColor(ConfigureSolutionsPopup.ProductName productName, StepsEnums.Color color) {
        securityTabPage.getConfigureSolutionsPopup().getTabSwitcherByName(productName).shouldBe(CustomConditions.cssValuePartlyMatch("border",
                color.getValue()));
    }

}
