package com.mobile.automation.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {
        "com.mobile.automation.steps", 
        "com.mobile.automation.hooks"
    },
    tags = "@android and not @skip",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/android/cucumber.html",
        "json:target/cucumber-reports/android/cucumber.json",
        "junit:target/cucumber-reports/android/cucumber.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    publish = true
)
public class AndroidTestRunner extends AbstractTestNGCucumberTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidTestRunner.class);
    
    @BeforeClass
    public void setUpAndroidTests() {
        LOGGER.info("Setting up Android-specific test configuration");
        
        // Set Android as default platform
        System.setProperty("platform", "android");
        
        // Set default environment if not specified
        if (System.getProperty("environment") == null) {
            System.setProperty("environment", "local");
        }
        
        LOGGER.info("Android test setup completed");
    }
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}