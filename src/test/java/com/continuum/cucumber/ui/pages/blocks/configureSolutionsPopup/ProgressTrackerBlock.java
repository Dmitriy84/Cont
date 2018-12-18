package com.continuum.cucumber.ui.pages.blocks.configureSolutionsPopup;

import com.codeborne.selenide.ElementsCollection;
import com.continuum.cucumber.configuration.TypeRegistryConfigurerAnnotation;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import lombok.Getter;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Getter
@Component
public class ProgressTrackerBlock extends BaseBlock {
    public final List<String> expectedBulletsOrder = new ArrayList<String>() {{
        add(StepName.SELECT_SITE.getBulletName());
        add(StepName.ACTIVATE_SOLUTIONS.getBulletName());
        add(StepName.GET_STARTED.getBulletName());
    }};

    private ElementsCollection bullets = $$(By.cssSelector("li"));
    private By notCompletedBullets = By.xpath("//li/i[not(i[@data-component='Icon'])]");

    public ProgressTrackerBlock() {
        super(By.cssSelector("div[data-component='ProgressTracker']"));
    }

    public List<String> getBulletsNames() {
        return bullets.stream().map(t -> t.$(By.cssSelector("span")).getText()).collect(Collectors.toList());
    }

    public String getActiveBulletName() {
        return $$(notCompletedBullets).stream().filter(b -> b.getCssValue("background").contains("rgb(17, 115, 189)"))
                .findFirst().orElseThrow(() -> new NoSuchElementException("No active bullet")).$(By.xpath("./following-sibling::span")).getText();
    }

    @TypeRegistryConfigurerAnnotation
    public enum StepName {
        SELECT_SITE("Select client site"), ACTIVATE_SOLUTIONS("Activate solutions"), GET_STARTED("Get started");

        @Getter
        private String bulletName;

        StepName(String bulletName) {
            this.bulletName = bulletName;
        }
    }
}
