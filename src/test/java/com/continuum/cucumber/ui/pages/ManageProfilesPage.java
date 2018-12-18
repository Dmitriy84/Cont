package com.continuum.cucumber.ui.pages;

import com.codeborne.selenide.ElementsCollection;
import com.continuum.cucumber.ui.pages.blocks.manageProfiles.HeaderBlockProfiles;
import com.continuum.cucumber.ui.pages.blocks.manageProfiles.ProfileTableBlock;
import lombok.Getter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.codeborne.selenide.Selenide.$$;

@Component
@Getter
public class ManageProfilesPage extends BasePage<ManageProfilesPage> {

    private ElementsCollection tabs = $$(By.cssSelector("div[data-component='Tabs'] li[data-component='Tab']"));
    @Autowired
    private ProfileTableBlock profileTableBlock;
    @Autowired
    private HeaderBlockProfiles headerBlock;

    public ManageProfilesPage() {
        setUrl("QaDashB/QuickAccess/NewDesktops/scorecard/manage-profile-and-protect-profiles?tab=user-account-profiles");
    }

    public String[] getSecurityRiskCategories() {
        return getProfileTableBlock().getRowsWithOrder().stream()
                .map(r -> getProfileTableBlock().getRiskCategoryName(r)).toArray(String[]::new);
    }

    public Pair[] getSecurityRiskCategoriesWithValues(String profileName) {
        return Arrays.stream(getSecurityRiskCategories()).map(c -> new ImmutablePair<>(c,
                getProfileTableBlock().getRiskLevelForProfileByName(profileName, c))).toArray(Pair[]::new);
    }
}
