package com.continuum.cucumber.ui.steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.continuum.cucumber.shell.Shell;
import com.continuum.cucumber.ui.enumerations.StepsEnums;
import com.continuum.cucumber.ui.pages.BaseContinuumPage;
import com.continuum.cucumber.ui.pages.DevicesPage;
import com.continuum.cucumber.ui.pages.LoginPage;
import com.continuum.cucumber.ui.pages.SecurityTabPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.var;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.continuum.cucumber.ui.enumerations.Timeouts.EXTRA_LONG;
import static com.continuum.cucumber.ui.utils.CustomWaits.waitForJSExecuted;
import static com.continuum.cucumber.ui.utils.CustomWaits.waitForNumberOfTabsToBe;
import static com.continuum.cucumber.ui.utils.UiUtils.switchTabAndCloseByIndex;
import static com.continuum.cucumber.ui.utils.UiUtils.switchToTabByIndex;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseUiStepsDefinition extends AbstractUIStepsDefinition {
    @Autowired
    private LoginPage loginPage;
    @Autowired
    private BaseContinuumPage baseContinuumPage;
    @Autowired
    private SecurityTabPage securityTabPage;
    @Autowired
    private DevicesPage devicesPage;

    /**
     * Open page with provided full URL
     *
     * @param url full url in the format 'http://test.com'
     * @author Anna Ostrovskaya
     */
    @When("open page with full url {string}")
    public void openUrl(String url) {
        open(url);
        waitForJSExecuted();
    }

    /**
     * Open page from 'Continuum' portal with provided url
     *
     * @param url part of the url which will be appended to the base 'Continuum' portal url (e.g. 'qadashb/QuickAccess/NewDesktops')
     * @author Anna Ostrovskaya
     */
    @When("open page from \"Continuum\" portal with url {string}")
    public void openUrlFromContinuum(String url) {
        open(Shell.Application.getEnvironment().getBaseUrlUI() + url);
        waitForJSExecuted();
    }


    /**
     * Open 'Continuum' portal login page
     *
     * @author Anna Ostrovskaya
     */
    @When("open \"Continuum\" portal base page")
    public void openContinuumBasePage() {
        openUrl(Shell.Application.getEnvironment().getBaseUrlUI());
        loginPage.onPage();
    }

    /**
     * On 'Continuum' portal open 'Security solution' tab
     *
     * @author Anna Ostrovskaya
     */
    @When("open \"Security Solution\" page")
    public void openSecurityPage() {
        securityTabPage.open();
        waitForJSExecuted();
    }

    /**
     * Login to 'Continuum' portal as user with provided creds
     *
     * @param userName User's email (e.g. 'test@mail.com')
     * @param password User's password (e.g. 'pass')
     * @author Anna Ostrovskaya
     */
    @When("login in as user {string} with password {string}")
    public void loginAsUser(String userName, String password) {
        openContinuumBasePage();
        loginPage.login(userName, password);
        baseContinuumPage.getHeaderBlock().waitFor(Condition.appear, EXTRA_LONG.milliseconds);
    }

    /**
     * Login to 'Continuum' portal with default creds
     *
     * @author Anna Ostrovskaya
     */
    @When("login in as default user")
    public void loginAsDefaultUser() {
        var getUser = Shell.Application.getEnvironment().getUser();
        var getPass = Shell.Application.getEnvironment().getPass();
        loginAsUser(getUser, getPass);
    }

    /**
     * On 'Continuum' portal click tab with given name in the header
     *
     * @param name Tab's name (e.g. 'Security')
     * @author Anna Ostrovskaya
     */
    @When("click tab with name {string}")
    public void clickTabByName(String name) {
        baseContinuumPage.getHeaderBlock().selectTabByVisibleName(name);
    }


    /**
     * Imitate clicking on specified keyboard
     *
     * @param keys Keyboard key {@link Keys}
     * @author Anna Ostrovskaya
     */
    @When("click keyboard key {Keys}")
    public void clickKeys(Keys keys) {
        Selenide.actions().sendKeys(keys).perform();
    }

    /**
     * Verify that current page URL contains expected value
     *
     * @param URL expected URL value (e.g. 'someurl')
     * @author Anna Ostrovskaya
     */
    @Then("current page URL should contain {string}")
    public void checkCurrentPageUrl(String URL) {
        assertThat(getWebDriver().getCurrentUrl()).as("Current page URL doesn't contain expected value").contains(URL);
        switchTabAndCloseByIndex(1);
    }

    /**
     * Wait for quantity of opened tabs/windows to be as expected
     *
     * @param qty expected tabs/windows quantity
     * @author Anna Ostrovskaya
     */
    @Then("wait quantity of opened tabs/windows to be {int}")
    public void waitForNewTabOpened(int qty) {
        waitForNumberOfTabsToBe(qty);
    }

    /**
     * Switch to tab/window with given index
     *
     * @param index index of tab/window
     * @author Anna Ostrovskaya
     */
    @Then("switch to tab/window with index {int}")
    public void switchToTabWithIndex(int index) {
        switchToTabByIndex(index);
    }

    /**
     * Verify that page contains expected text values (verification is made against page source code)
     *
     * @param values list of strings that should be verified
     * @author Anna Ostrovskaya
     */
    @Then("page should contain values {strings}")
    public void checkPageContainsText(List<String> values) {
        var pageSource = WebDriverRunner.source();
        values.forEach(v -> assertThat(pageSource).as("Page doesn't contain expected value '%s'", v).contains(v));
    }

    /**
     * Verify that page contains expected text values that were remembered during previous steps with provided key name
     * (verification is made against page source code)
     *
     * @param name key name with which list of expected values was saved (e.g. 'sitesNames' from step @see com.continuum.cucumber.ui.steps
     *             .WizardFirstStepSteps#getSitesNamesBeforeSearch)
     * @author Anna Ostrovskaya
     */
    @Then("page should contain values saved on previous step with name {string}")
    public void checkPageContainsText(String name) {
        var pageSource = WebDriverRunner.source();
        Arrays.stream(context.read(name, String[].class)).forEach(v -> assertThat(pageSource).as("Page doesn't contain expected value '%s'", v).contains(v));
    }

    /**
     * Click 1st element of GUI containing provided text
     *
     * @param value text in the element
     * @author Anna Ostrovskaya
     */
    @When("click element containing text {string}")
    public void clickElementWithText(String value) {
        $(By.xpath("//*[contains(text(),'" + value + "')]")).click();
        waitForJSExecuted();
    }

    /**
     * Logout from 'Continuum' portal
     *
     * @author Anna Ostrovskaya
     */
    @When("logout from 'Continuum' portal via GUI")
    public void logoutContinuum() {
        open(Shell.Application.getEnvironment().getBaseUrlUI() + "QADashB/ContinuumLogin/ContinuumLogout");
        loginPage.onPage();
    }

    /**
     * On 'Devices' page on 'Continuum' portal select 'Profile and Protect' view from the dropdown, select client's site with provided name and verify that
     * 'Profile and Protect' column is visible/invisible in the grid
     *
     * @param visibility expected visibility of column (VISIBLE/INVISIBLE) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Visibility}
     * @param siteName   client's site name
     * @author Anna Ostrovskaya
     */
    @Then("\"Profile and Protect\" column should be {Visibility} for site {string} on \"Devices\" page")
    public void checkPPColumnVisibility(StepsEnums.Visibility visibility, String siteName) {
        devicesPage.selectPPView().selectSite(siteName).getPpColumn().shouldBe(visibility.getCondition());
    }

    /**
     * Select option with specified name from 'Configure' dropdown on the Security tab
     *
     * @param name Name of the option from 'Configure' dropdown on the Security tab (e.g. 'Activate and configure solutions')
     * @author Anna Ostrovskaya
     */
    @When("select option {string} from \"Configure\" dropdown")
    public void selectOptionByNameFromConfigureDropdown(String name) {
        waitForJSExecuted();
        securityTabPage.selectAction(name);
        waitForJSExecuted();
    }

    /**
     * "Security' tab of "Continuum" portal is opened
     *
     * @author Anna Ostrovskaya
     */
    @Then("\"Security\" tab should be opened")
    public void onSecurityTab() {
        waitForJSExecuted();
        securityTabPage.getAction("Activate and configure solutions").shouldBe(Condition.appear);
    }

}