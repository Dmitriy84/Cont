package com.continuum.cucumber.run;

import com.continuum.cucumber.shell.YamlPropertyLoaderFactory;
import cucumber.api.junit.Cucumber;
import org.junit.runners.model.InitializationError;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ContinuumCucumberRunner extends Cucumber {
    static {
        try {
            YamlPropertyLoaderFactory.getPropertySource(new EncodedResource(new ClassPathResource("application.yml"), StandardCharsets.UTF_8));
        } catch (IOException e) {
        }
    }

    public ContinuumCucumberRunner(Class clazz) throws InitializationError, IOException {
        super(clazz);
    }
}