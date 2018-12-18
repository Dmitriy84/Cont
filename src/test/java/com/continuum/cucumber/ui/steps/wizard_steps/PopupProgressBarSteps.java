package com.continuum.cucumber.ui.steps.wizard_steps;

import com.continuum.cucumber.ui.pages.SecurityTabPage;
import com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup.ProgressTrackerBlock;
import com.continuum.cucumber.ui.steps.AbstractUIStepsDefinition;
import cucumber.api.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class PopupProgressBarSteps extends AbstractUIStepsDefinition {
    @Autowired
    private SecurityTabPage securityTabPage;

    /**
     * In the progress bar of the wizard verify that active step matches given value
     *
     * @param stepName expected name of step (SELECT_SITE/ACTIVATE_SOLUTIONS/GET_STARTED)
     *                 {@link com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup.ProgressTrackerBlock.StepName}
     * @author Anna Ostrovskaya
     */
    @Then("active step in Progress Tracker should be {StepName}")
    public void checkActiveBulletName(ProgressTrackerBlock.StepName stepName) {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getProgressTrackerBlock().getActiveBulletName())
                .as("Incorrect active bullet in Progress Tracker block").isEqualTo(stepName.getBulletName());
    }

    /**
     * In the progress bar of the wizard verify that order step matches given value
     *
     * @param steps expected list of steps (e.g. SELECT_SITE, ACTIVATE_SOLUTIONS, GET_STARTED)
     *              {@link com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup.ProgressTrackerBlock.StepName}
     * @author Anna Ostrovskaya
     */
    @Then("order of steps in Progress tracker should be {StepNameList}")
    public void checkBulletsOrder(List<ProgressTrackerBlock.StepName> steps) {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getProgressTrackerBlock().getBulletsNames())
                .as("Incorrect order of bullets in 'Progress Tracker'")
                .isEqualTo(steps.stream().map(ProgressTrackerBlock.StepName::getBulletName).collect(Collectors.toList()));
    }
}
