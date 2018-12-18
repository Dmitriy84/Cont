package com.continuum.cucumber.utils;

import cucumber.runtime.RuntimeOptions;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;

import java.io.File;
import java.util.List;
import java.util.NoSuchElementException;

@UtilityClass
@Slf4j
public class CommonArtifactUtils {

    public static String getCucumberJsonResultsFile() {
        val opt = new RuntimeOptions(System.getProperty("cucumber.options"));
        var json = System.getProperty("user.dir") + File.separator;
        try {
            val f = opt.getClass().getDeclaredField("pluginFormatterNames");
            f.setAccessible(true);
            val pref = "json:";
            json += ((List<String>) f.get(opt)).stream().filter(p -> p.startsWith(pref)).findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No option with prefix <" + pref + "> in cucumber.options")).replace(pref, "");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Something has changed cucumber.options property", e);
        }
        return json;
    }
}
