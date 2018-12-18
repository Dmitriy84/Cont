package com.continuum.cucumber.ui.steps.wizard_steps;

import com.codeborne.selenide.Condition;
import com.continuum.cucumber.ui.enumerations.StepsEnums;
import com.continuum.cucumber.ui.pages.SecurityTabPage;
import com.continuum.cucumber.ui.steps.AbstractUIStepsDefinition;
import com.continuum.cucumber.ui.utils.CustomConditions;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.continuum.cucumber.ui.utils.CustomWaits.waitForJSExecuted;
import static com.continuum.cucumber.ui.utils.UiUtils.clickJS;

public class PopupFooterSteps extends AbstractUIStepsDefinition {
    @Autowired
    private SecurityTabPage securityTabPage;

    /**
     * In the footer of the wizard click button with given title
     *
     * @param buttonName button title (e.g 'Continue')
     * @author Anna Ostrovskaya
     */
    @When("click button with name {string} in footer")
    public void clickFooterButton(String buttonName) {
        waitForJSExecuted();
        securityTabPage.getConfigureSolutionsPopup().getFooter().getButtonByName(buttonName).should(Condition.visible).should(Condition.enabled).click();
    }

    /**
     * In the footer of the wizard verify that buttons with given titles have expected visibility
     *
     * @param buttonNames list of buttons' names (e.g. "Continue, Cancel, ..., Back")
     * @param visibility  button visibility (VISIBLE/INVISIBLE) {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Visibility}
     * @author Anna Ostrovskaya
     */
    @Then("button(s) {strings} in footer should be {Visibility}")
    public void checkFooterButtonsVisibility(List<String> buttonNames, StepsEnums.Visibility visibility) {
        buttonNames.forEach(btn ->
                securityTabPage.getConfigureSolutionsPopup().getFooter().getButtonByName(btn).shouldBe(visibility.getCondition())
        );
    }

    /**
     * In the footer of the wizard verify that buttons with given titles have expected status
     *
     * @param buttonNames list of buttons' names (e.g. "Continue, Cancel, ..., Back")
     * @param status      buttons' status {@link com.continuum.cucumber.ui.enumerations.StepsEnums.Status}
     * @author Anna Ostrovskaya
     */
    @Then("button(s) {strings} in footer should be {Status}")
    public void checkFooterButtonsStatus(List<String> buttonNames, StepsEnums.Status status) {
        buttonNames.forEach(btn ->
                securityTabPage.getConfigureSolutionsPopup().getFooter().getButtonByName(btn).shouldBe(status.getCondition())
        );
    }

    /**
     * In the footer of the wizard verify that buttons with given titles have expected color
     *
     * @param buttonNames list of buttons' names (e.g. "Continue, Cancel, ..., Back")
     * @param Color       buttons' color (WHITE/LIGHT_BLUE/DARK_BLUE) {@link StepsEnums.Color}
     * @author Anna Ostrovskaya
     */
    @Then("button(s) {strings} in footer should have color {Color}")
    public void checkFooterButtonsColor(List<String> buttonNames, StepsEnums.Color Color) {
        buttonNames.forEach(btn ->
                securityTabPage.getConfigureSolutionsPopup().getFooter().getButtonByName(btn).shouldBe(CustomConditions.cssValuePartlyMatch("background",
                        Color.getValue())));
    }
}
