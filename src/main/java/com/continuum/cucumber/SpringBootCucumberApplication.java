package com.continuum.cucumber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = SpringBootCucumberApplication.PACKAGES)
public class SpringBootCucumberApplication {
    public static final String PACKAGES = "com.continuum.cucumber";

    public static void main(String[] args) {
        SpringApplication.run(SpringBootCucumberApplication.class, args);
    }
}