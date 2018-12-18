package com.continuum.cucumber.ui.enumerations;

import com.codeborne.selenide.Condition;
import com.continuum.cucumber.configuration.TypeRegistryConfigurerAnnotation;
import lombok.Getter;
import org.openqa.selenium.Keys;

import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.Condition.not;

public class StepsEnums {
    public static final List<Class<? extends Enum>> externalEnums = Arrays.asList(Keys.class);

    @TypeRegistryConfigurerAnnotation
    public enum Status {
        ENABLED(Condition.and("enabled", Condition.appears, not(Condition.attribute("disabled"))), "Active"),
        DISABLED(Condition.and("disabled",
                Condition.appears, Condition.attribute("disabled")), "Inactive");
        @Getter
        private Condition condition;
        @Getter
        private String statusName;

        Status(Condition val, String status) {
            condition = val;
            statusName = status;
        }
    }

    @TypeRegistryConfigurerAnnotation
    public enum Visibility {
        VISIBLE(Condition.appear), INVISIBLE(not(Condition.appears));
        @Getter
        private Condition condition;

        Visibility(Condition val) {
            condition = val;
        }
    }

    @TypeRegistryConfigurerAnnotation
    public enum Boolean {
        TRUE(true), FALSE(false);
        @Getter
        private boolean value;

        Boolean(boolean val) {
            value = val;
        }
    }


    @TypeRegistryConfigurerAnnotation
    public enum Color {
        DARK_BLUE("rgb(17, 115, 189)"), LIGHT_BLUE("rgb(113, 171, 216)"), WHITE("rgb(255, 255, 255)"), RED("rgb(239, 83, 80)");
        @Getter
        private String value;

        Color(String val) {
            value = val;
        }
    }

    @TypeRegistryConfigurerAnnotation
    public enum Order {
        ASCENDING, DESCENDING

    }

}