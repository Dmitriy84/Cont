package com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.continuum.cucumber.ui.enumerations.Timeouts;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
public class GetStartedBlock extends BaseBlock {

    private ElementsCollection textNotes = $$(By.cssSelector("span"));
    private SelenideElement downloadAgentButton = $(By.cssSelector("div[data-component='PanelWithIconComponent']:nth-of-type(1) a"));
    private SelenideElement copyAgentKeyButton = $(By.cssSelector("div[data-component='PanelWithIconComponent']:nth-of-type(2) button"));
    private SelenideElement agentKeyField = $(By.xpath("//button/../span"));
    private ElementsCollection installAgentSteps = $$(By.xpath("//span[contains(text(),'Next steps')]/following-sibling::div/div"));

    public GetStartedBlock() {
        super(By.cssSelector("div[data-component='GetStartedComponent']"));
    }

    public String getAgentStepTitleByIndex(int index) {
        if (installAgentSteps.size() > index)
            return installAgentSteps.get(index).$(By.xpath("./span[2]")).getText();
        throw new NoSuchElementException("No agent installation step with index=" + index);
    }

    public String getAgentStepBodyByIndex(int index) {
        if (installAgentSteps.size() > index)
            return installAgentSteps.get(index).$(By.xpath("./span[3]")).getText();
        throw new NoSuchElementException("No agent installation step with index=" + index);
    }

    public String getSiteForWhichSolutionIsConfigured() {
        Matcher matcher =Pattern.compile("security solution(s?) for (.+)").matcher(textNotes.stream().filter(h -> h.getText().contains("You have successfully" +
                " activated")).findFirst().orElseThrow(() -> new NoSuchElementException(String.format(
                "No header with text <%s>", "You have activated"))).getText());
        if(matcher.find())
            return matcher.group(2);
        throw new NoSuchElementException("No client's site name detected!");
    }

    public void waitForAgentKeyFieldFilled() {
        agentKeyField.waitUntil(Condition.not(Condition.empty), Timeouts.EXTRA_LONG.milliseconds * 2);
    }
}
