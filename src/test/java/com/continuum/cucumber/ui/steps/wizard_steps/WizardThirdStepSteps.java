package com.continuum.cucumber.ui.steps.wizard_steps;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.continuum.cucumber.ui.pages.SecurityTabPage;
import com.continuum.cucumber.ui.steps.AbstractUIStepsDefinition;
import com.continuum.cucumber.ui.utils.UiUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import static com.continuum.cucumber.ui.enumerations.Timeouts.MEDIUM;
import static com.continuum.cucumber.ui.utils.CustomWaits.waitForJSExecuted;
import static org.assertj.core.api.Assertions.assertThat;

public class WizardThirdStepSteps extends AbstractUIStepsDefinition {
    @Autowired
    private SecurityTabPage securityTabPage;

    /**
     * On 'Get Started' step of the wizard wait for 'Agent Installation Key' field to be filled with key's value
     *
     * @author Anna Ostrovskaya
     */
    @When("wait for \"Agent Installation Key\" field to be filled")
    public void waitForAgentKeyFieldFilled() {
        securityTabPage.getConfigureSolutionsPopup().getGetStartedBlock().waitForAgentKeyFieldFilled();
    }

    /**
     * On 'Get Started' step of the wizard click 'Copy' button next to 'Agent Installation Key' and remember value that was copied to the clipboard
     *
     * @throws IOException
     * @throws UnsupportedFlavorException
     * @author Anna Ostrovskaya
     */
    @When("click \"Copy\" button next to \"Agent Installation Key\" field")
    public void copyAgentInstallationKey() throws IOException, UnsupportedFlavorException {
        securityTabPage.getConfigureSolutionsPopup().getGetStartedBlock().getCopyAgentKeyButton().click();
        context.write("agentKeyCopied", UiUtils.getDataFromClipboard());
    }

    /**
     * On 'Get Started' step of the wizard verify that value of 'Agent Installation Key' copied to the clipboard matches value visible in the 'Agent
     * Installation Key' field
     *
     * @author Anna Ostrovskaya
     */
    @Then("copied to clipboard \"Agent Installation key\" matches value in the field")
    public void checkCopiedAgentInstallationKey() {
        assertThat(context.readString("agentKeyCopied")).as("Incorrect 'Agent Installation Key' was copied to clipboard")
                .isEqualTo(securityTabPage.getConfigureSolutionsPopup().getGetStartedBlock().getAgentKeyField().getValue());
    }

    /**
     * Verify that 'Get Started' step of the wizard is opened
     *
     * @author Anna Ostrovskaya
     */
    @Then("\"Get Started\" step of wizard is opened")
    public void onThirdWizardStep() {
        securityTabPage.getConfigureSolutionsPopup().getGetStartedBlock().waitFor(Condition.appears, MEDIUM.milliseconds);
    }


    /**
     * On 'Get Started' step of the wizard verify that displayed client's site name matches given value
     *
     * @param siteName expected client's site name (e.g. 'Test site 1')
     * @author Anna Ostrovskaya
     */
    @Then("site name on \"Get Started\" step should be {string}")
    public void checkSiteNameOnGetStartedStep(String siteName) {
        waitForJSExecuted();
        assertThat(securityTabPage.getConfigureSolutionsPopup().getGetStartedBlock().getSiteForWhichSolutionIsConfigured())
                .as("Incorrect site name is displayed on the 'Get Started' step").isEqualTo(siteName);
    }

    /**
     * On 'Get Started' step of the wizard verify that on clicking 'Download' button agent file is downloaded
     *
     * @throws FileNotFoundException
     * @author Anna Ostrovskaya
     */
    @Then("agent file should be downloaded")
    public void downloadAgent() throws FileNotFoundException {
        var fileName = securityTabPage.getConfigureSolutionsPopup().getGetStartedBlock().getDownloadAgentButton().download(5000).getName();
        assertThat(Paths.get(Configuration.reportsFolder, fileName).toFile()).as("Agent file was not downloaded!").exists().hasExtension("exe");
    }

    /**
     * On 'Get Started' step of the wizard verify that step describing installation of the Agent with given index has expected title
     *
     * @param index number of the Agent installation step (starting from 0)
     * @param title expected Agent installation step title (e.g. 'Step title')
     * @author Anna Ostrovskaya
     */
    @Then("installation step number {int} should have title {string}")
    public void verifyInstallationAgentStepTitle(int index, String title) {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getGetStartedBlock().getAgentStepTitleByIndex(index)).as("Incorrect step title!").isEqualTo(title);
    }

    /**
     * On 'Get Started' step of the wizard verify that step describing installation of the Agent with given index has expected body
     *
     * @param index number of the Agent installation step (starting from 0)
     * @param body  expected Agent installation step body (e.g.:
     *              """
     *              Step body
     *              """)
     * @author Anna Ostrovskaya
     */
    @Then("installation step number {int} should have body")
    public void verifyInstallationAgentStepBody(int index, String body) {
        assertThat(securityTabPage.getConfigureSolutionsPopup().getGetStartedBlock().getAgentStepBodyByIndex(index)).as("Incorrect step body!").isEqualTo(body.replace("\n", " "));
    }
}
