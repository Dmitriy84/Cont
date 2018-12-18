package com.continuum.cucumber.shell;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("application")
//@PropertySource(value = "classpath:application.yml", factory = YamlPropertyLoaderFactory.class)
public class Application {
    private String reportOutputDirectory;
    private String name;
    private List<String> repoNames = new ArrayList<>();
    private Selenium selenium = new Selenium();
    private Jenkins jenkins = new Jenkins();
    private List<Host> remotehosts = new ArrayList<>();
    private Mail mail = new Mail();
    private TestRail testRail = new TestRail();
    private TestResultsDb testResultsDb = new TestResultsDb();
    private String testRailCaseUrl;
    private Environment environment;

    @Data
    public static class TestResultsDb {
        private String url;
        private String name;
        private String user;
        private String password;
    }

    @Data
    public static class TestRail {
        private boolean update;
        private String testRun;
        private String url;
        private String user;
        private String password;
    }


    @Data
    public static class Mail {
        private String reportUser;
        private String reportPassword;
        private boolean reportMail;
        private String reportReceiver;
    }

    @Data
    public static class Selenium {
        @Value("#{systemProperties['application.selenium.browser'] ?: 'CHROME'}")
        private String browser;
        private boolean remote;
        @Value("#{systemProperties['application.selenium.hubUrl'] ?: 'http://localhost:4444/wd/hub'}")
        private String hubUrl;
    }

    @Data
    public static class Jenkins {
        private String user;
        private String password;
        private boolean enabled;
        private List<String> fruits = new ArrayList<>();
    }

    @Data
    public static class Host {
        private String name;
        private String port;
        private String ip;
        private String user;
        private String password;
    }

    @Data
    public static class Environment {
        private String name;
        private String user;
        private String pass;
        private String baseUrlUI;
        private String baseUrlBE;
        @Value("#{systemProperties['application.selenium.environment.timeoutsMultiplicator'] ?: '1'}")
        private int timeoutsMultiplicator;
        private String authUrl;
    }
}