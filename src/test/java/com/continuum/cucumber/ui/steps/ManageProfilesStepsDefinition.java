package com.continuum.cucumber.ui.steps;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.WebDriverRunner;
import com.continuum.cucumber.ui.enumerations.StepsEnums;
import com.continuum.cucumber.ui.enumerations.Timeouts;
import com.continuum.cucumber.ui.pages.ManageProfilesPage;
import com.continuum.cucumber.ui.pages.SecurityTabPage;
import com.continuum.cucumber.ui.pages.blocks.manageProfiles.ProfileTableBlock;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.continuum.cucumber.ui.utils.CustomWaits.waitForJSExecuted;
import static org.assertj.core.api.Assertions.assertThat;

public class ManageProfilesStepsDefinition extends AbstractUIStepsDefinition {

    @Autowired
    private SecurityTabPage securityTabPage;
    @Autowired
    private ManageProfilesPage manageProfilesPage;

    /**
     * Verify that "Manage Profile &amp; Protect profiles" page on "Continuum" portal is opened
     *
     * @author Anna Ostrovskaya
     */
    @Then("\"Manage Profile & Protect profiles\" page is opened")
    public void onManageProfilesPage() {
        ProfileTableBlock tableBlock = manageProfilesPage.getProfileTableBlock();
        tableBlock.waitFor(Condition.appears, Timeouts.SMALL.milliseconds);
        tableBlock.$$(tableBlock.getRowLocator()).shouldHave(CollectionCondition.sizeGreaterThan(0),
                Timeouts.SMALL.milliseconds);
        new Actions(WebDriverRunner.getWebDriver()).pause(500).perform();
    }

    /**
     * Verify that "Manage Profile &amp; Protect profiles" page on "Continuum" portal is opened without available profiles
     *
     * @author Anna Ostrovskaya
     */
    @Then("\"Manage Profile & Protect profiles\" page is opened without available profiles")
    public void onManageProfilesPageWithoutProfiles() {
        manageProfilesPage.getHeaderBlock().waitFor(Condition.appears, Timeouts.SMALL.milliseconds);
        manageProfilesPage.getProfileTableBlock().$$(manageProfilesPage.getProfileTableBlock().getRowLocator()).shouldHave(CollectionCondition.size(0),
                Timeouts.SMALL.milliseconds);
        assertThat(manageProfilesPage.getProfileTableBlock().isEmpty()).as("Profiles table is not empty!").isFalse();
    }

    /**
     * Verify that breadcrumbs in the header of "Manage Profile &amp; Protect profiles" page have expected order of values
     *
     * @param values expected order of breadcrumbs (e.g. "Security, Manage Profiles")
     * @author Anna Ostrovskaya
     */
    @Then("breadcrumbs in the header should be {strings}")
    public void checkBreadcrumbs(List<String> values) {
        assertThat(Arrays.stream(manageProfilesPage.getHeaderBlock().getBreadCrumbs().getText().split("/")).map(String::trim).collect(Collectors.toList()))
                .as("Incorrect breadcrumbs in the header")
                .containsExactly(values.toArray(new String[0]));
    }

    /**
     * Get and remember (as variable with name "securityCategories") list of available security risk categories and their values for provided security profile
     * on "Manage Profile &amp; Protect profiles" page
     *
     * @param profileName name of security profile for which categories names and values should be saved (e.g. "Secirity profile 1")
     * @author Anna Ostrovskaya
     */
    @When("remember security risk categories with values for profile {string}")
    public void getAvailableSecurityRiskCategories(String profileName) {
        context.write("securityCategories", manageProfilesPage.getSecurityRiskCategoriesWithValues(profileName));
    }

    /**
     * Verify that list of available security risk categories and their values matches list saved on the previous steps @see
     * ManageProfilesStepsDefinition#getAvailableSecurityRiskCategories on "Manage Profile &amp; Protect profiles" page
     *
     * @param profileName name of security profile for which categories names and values should be evaluated (e.g. "Secirity profile 1")
     * @author Anna Ostrovskaya
     */
    @Then("security risk categories with values should be the same as remembered values for profile {string}")
    public void checkCategoriesValues(String profileName) {
        assertThat(manageProfilesPage.getSecurityRiskCategoriesWithValues(profileName)).as("Incorrect security risk categories names and values!")
                .containsExactlyInAnyOrder(context.read("securityCategories", Pair[].class));
    }

    /**
     * Verify that list of available security risk categories has expected order on "Manage Profile &amp; Protect profiles" page
     *
     * @param order expected categories order (ASCENDING/DESCENDING) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Order}
     * @author Anna Ostrovskaya
     */
    @Then("security risk categories order should be {Order}")
    public void checkCategoriesOrder(StepsEnums.Order order) {
        String[] categoriesNames = manageProfilesPage.getSecurityRiskCategories();
        switch (order) {
            case ASCENDING:
                Arrays.sort(categoriesNames, Collections.reverseOrder(String.CASE_INSENSITIVE_ORDER));
                break;
            case DESCENDING:
                Arrays.sort(categoriesNames);
                break;
            default:
                throw new NoSuchElementException("No definition of sorting with name " + order.name());
        }
        assertThat(manageProfilesPage.getSecurityRiskCategories()).as("Incorrect security categories order in the header").containsExactly(categoriesNames);
    }

    /**
     * Sort security risk categories with expected order on "Manage Profile &amp; Protect profiles" page
     *
     * @param order expected categories order (ASCENDING/DESCENDING) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Order}
     * @author Anna Ostrovskaya
     */
    @When("sort security risk categories by {Order} order")
    public void sortCategories(StepsEnums.Order order) {
        ProfileTableBlock tableBlock = manageProfilesPage.getProfileTableBlock();
        switch (order) {
            case ASCENDING:
                if (tableBlock.getAscSortingButton().isDisplayed())
                    tableBlock.getAscSortingButton().click();
                break;
            case DESCENDING:
                if (tableBlock.getDescSortingButton().isDisplayed())
                    tableBlock.getDescSortingButton().click();
                break;
            default:
                throw new NoSuchElementException("No definition of sorting with name " + order.name());
        }
        waitForJSExecuted();
        new Actions(WebDriverRunner.getWebDriver()).pause(500).perform();
    }

    /**
     * Click 'Back' button in the header on "Manage Profile &amp; Protect profiles" page
     *
     * @author Anna Ostrovskaya
     */
    @Then("click \"Back\" button on \"Manage Profiles\" page")
    public void clickBackButton() {
        manageProfilesPage.getHeaderBlock().getBackButton().click();
    }
}
