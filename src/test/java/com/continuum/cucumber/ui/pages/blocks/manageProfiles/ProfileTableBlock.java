package com.continuum.cucumber.ui.pages.blocks.manageProfiles;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.continuum.cucumber.ui.pages.blocks.BaseBlock;
import lombok.Getter;
import lombok.var;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Getter
@Component
public class ProfileTableBlock extends BaseBlock {
    By rowLocator = By.xpath("//*[@role='gridcell']//*[@data-component='CategoryCellComponent']/../../..");
    private ElementsCollection rows;
    private ElementsCollection profileNames = $$(By.cssSelector("[data-component='ProfileHeaderComponent']"));
    private SelenideElement ascSortingButton = $(By.cssSelector("span[ref='eSortAsc']"));
    private SelenideElement descSortingButton = $(By.cssSelector("span[ref='eSortDesc']"));

    public ProfileTableBlock() {
        super(By.cssSelector("[data-component='ProfileTable']"));
    }

    public boolean isEmpty() {
        return this.$(By.xpath("//*[contains(@text, 'Profiles are not defined')]")).isDisplayed();
    }

    public String getRiskLevelForProfileByName(String profileName, String categoryName) {
        var profileElem = profileNames.stream().filter(p -> p.getText().contains(profileName.toUpperCase())).findFirst()
                .orElseThrow(() -> new NoSuchElementException("No profile with name " + profileName));
        var profileId = profileNames.indexOf(profileElem);
        return getRiskCategoryElement(categoryName).$(By.xpath(String.format("./*[@role='gridcell' and @col-id='label'][%d]", profileId + 1))).getText();
    }

    public ElementsCollection getRowsWithOrder() {
        List<WebElement> result = WebDriverRunner.getWebDriver().findElements(rowLocator);
        if (result.size() > 0 && result.get(0).getAttribute("index") != null)
            result.sort(Comparator.comparingInt(c -> Integer.parseInt(c.getAttribute("index"))));
        rows = new ElementsCollection(result);
        return rows;
    }


    public SelenideElement getRiskCategoryElement(String categoryName) {
        return getRows().stream().filter(r -> getRiskCategoryName(r).equals(categoryName)).findFirst()
                .orElseThrow(() -> new NoSuchElementException("No category with name " + categoryName));
    }


    public String getRiskCategoryName(SelenideElement row) {
        return row.$(By.xpath("./*[@role='gridcell' and @col-id='name']")).getText();
    }
}
