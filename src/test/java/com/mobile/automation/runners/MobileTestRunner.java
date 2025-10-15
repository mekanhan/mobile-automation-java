package com.mobile.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {
        "com.mobile.automation.steps", 
        "com.mobile.automation.hooks"
    },
    tags = "not @skip",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/mobile/cucumber.html",
        "json:target/cucumber-reports/mobile/cucumber.json",
        "junit:target/cucumber-reports/mobile/cucumber.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    publish = true
)
public class MobileTestRunner extends AbstractTestNGCucumberTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileTestRunner.class);
    
    @BeforeClass
    @Parameters({"platform", "environment"})
    public void setUpClass(String platform, String environment) {
        LOGGER.info("Setting up test suite for platform: {}, environment: {}", platform, environment);
        
        // Set system properties for configuration
        if (platform != null) {
            System.setProperty("platform", platform);
        }
        if (environment != null) {
            System.setProperty("environment", environment);
        }
        
        LOGGER.info("Test suite setup completed");
    }
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}