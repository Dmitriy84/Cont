package com.continuum.cucumber.ui.steps.wizard_steps;

import com.codeborne.selenide.Condition;
import com.continuum.cucumber.ui.enumerations.StepsEnums;
import com.continuum.cucumber.ui.enumerations.Timeouts;
import com.continuum.cucumber.ui.pages.SecurityTabPage;
import com.continuum.cucumber.ui.steps.AbstractUIStepsDefinition;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.continuum.cucumber.ui.enumerations.Timeouts.EXTRA_SMALL;
import static com.continuum.cucumber.ui.enumerations.Timeouts.MEDIUM;
import static com.continuum.cucumber.utils.CommonUtils.getRandomInt;
import static org.assertj.core.api.Assertions.assertThat;

public class WizardFirstStepSteps extends AbstractUIStepsDefinition {
    @Autowired
    private SecurityTabPage securityTabPage;

    /**
     * Wait for 'Activate and configure solutions' wizard to appear/disappear on the Security tab
     *
     * @param visibility Parameter indicating expected visibility of wizard (VISIBLE/INVISIBLE)
     *                   {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Visibility}
     * @author Anna Ostrovskaya
     */
    @Then("\"Activate and configure solutions\" popup is {Visibility}")
    public void activateAndConfigureSolutionsAppears(StepsEnums.Visibility visibility) {
        securityTabPage.getConfigureSolutionsPopup().waitFor(visibility.getCondition(), EXTRA_SMALL.milliseconds);
    }

    /**
     * On 'Select client's site' step of the wizard select client's site by name from the dropdown
     *
     * @param siteName Name of the client's site that should be selected (e.g. 'Test site 1')
     * @author Anna Ostrovskaya
     */
    @When("select client's site {string} on the \"Select client site\" step")
    public void selectClientSite(String siteName) {
        var selector = securityTabPage.getConfigureSolutionsPopup().getSelectClientSiteBlock().getSelectClientSiteElement();
        selector.expandSitesDropdown().searchForAvailableSite(siteName);
        selector.getSites().get(0).click();
        selector.getFilterInput().waitUntil(Condition.disappears,
                EXTRA_SMALL.milliseconds);
        selector.getSelectedSite().shouldHave(Condition.text(siteName));
    }

    /**
     * On 'Select client's site' step of the wizard select client's site from known sites quantity
     *
     * @param bound Quantity of available sites (should be positive value)
     * @author Anna Ostrovskaya
     */
    @When("select random client's site from {int} sites on the \"Select client site\" step")
    public void selectRandomClientSite(int bound) {
        context.write("siteName",
                securityTabPage.getConfigureSolutionsPopup().getSelectClientSiteBlock().getSelectClientSiteElement().expandSitesDropdown().selectSiteByIndex(getRandomInt(bound)));
    }

    /**
     * On wizard popup verify that 'Select client's site' step is opened
     *
     * @author Anna Ostrovskaya
     */
    @Then("\"Select client site\" step of wizard is opened")
    public void onFirstWizardStep() {
        securityTabPage.getConfigureSolutionsPopup().getSelectClientSiteBlock().waitFor(Condition.appears, MEDIUM.milliseconds);
    }

    /**
     * On 'Select client's site' step of the wizard verify that correct note is shown
     *
     * @param note Expected note's text (e.g. 'Some text here')
     * @author Anna Ostrovskaya
     */
    @Then("note on the \"Select client site\" step should be {string}")
    public void checkNoteOnSelectClientSiteStep(String note) {
        securityTabPage.getConfigureSolutionsPopup().getSelectClientSiteBlock().getNoteTextBlockOnSelectClientSiteStep()
                .shouldHave(Condition.exactText(note));
    }

    /**
     * Click 'Close' button on the wizard
     *
     * @author Anna Ostrovskaya
     */
    @When("close \"Activate and configure solutions\" popup is via close button")
    public void closeActivateAndConfigureSolutionsViaClose() {
        securityTabPage.getConfigureSolutionsPopup().getCloseButton().should(Condition.visible).click();
    }


    /**
     * On 'Select client's site' step of the wizard search for client's sites and verify whether the returned sites' list matches expected values
     *
     * @param searchQuery    Search query for site selectors (e.g. 'Test')
     * @param expectedResult List of client's sites names which are expected to be found with provided search query
     *                       (e.g. "Test site 1, Test site 2, ..., Test site 3")
     *                       If no results are expected then expectedResult should contain only one elements with text "no results"
     * @author Anna Ostrovskaya
     */
    @When("search for client's site(s) {string} with expected result {strings}")
    public void searchForClientsSites(String searchQuery, List<String> expectedResult) {
        var selector = securityTabPage.getConfigureSolutionsPopup().getSelectClientSiteBlock().getSelectClientSiteElement();
        selector.expandSitesDropdown().searchForAvailableSite(searchQuery);
        if (expectedResult.contains("no results"))
            assertThat(selector.getNoResults().waitUntil(Condition.appear, Timeouts.EXTRA_SMALL.milliseconds).getText())
                    .as("Incorrect message is show when search for client's sites returned no results").isEqualTo("Sorry, no results found.");
        else
            assertThat(selector.getAvailableSiteNames()).as("Incorrect client's sites are displayed in search results!").containsOnlyElementsOf(expectedResult);
    }

    /**
     * On 'Select client's site' step of the wizard clear search input field of the 'Select client's site' element
     *
     * @author Anna Ostrovskaya
     */
    @When("clear search field on the Select Client Site element")
    public void clearSearchForClientSite() {
        securityTabPage.getConfigureSolutionsPopup().getSelectClientSiteBlock().getSelectClientSiteElement().clearSearch();

    }

    /**
     * On 'Select client's site' step of the wizard remember values of the currently displayed in dropdown client's sites names
     * Sites' names are saved in the variable with name 'sitesNames'
     *
     * @author Anna Ostrovskaya
     */
    @Then("remember currently displayed sites' names")
    public void getSitesNamesBeforeSearch() {
        context.write("sitesNames",
                securityTabPage.getConfigureSolutionsPopup().getSelectClientSiteBlock().getSelectClientSiteElement().expandSitesDropdown()
                        .getAvailableSiteNames().toArray(new String[0]));
    }

    /**
     * On 'Select client's site' step of the wizard verify that currently displayed client's sites are the same that were remembered during step
     * Then ("remember currently displayed sites' names")
     *
     * @author Anna Ostrovskaya
     */
    @Then("compare current sites' list with previous one")
    public void allClientSitesShown() {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getSelectClientSiteBlock().getSelectClientSiteElement().expandSitesDropdown().getAvailableSiteNames())
                .as("Incorrect list of client's sites are shown!")
                .contains(context.read("sitesNames", String[].class));
    }

}
