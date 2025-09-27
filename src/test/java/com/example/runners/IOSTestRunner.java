package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * iOS-specific Test Runner
 * Runs tests with iOS-specific tags and configurations
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.example.steps", "com.example.hooks"},
    tags = "@ios and not @skip",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/ios/cucumber.html",
        "json:target/cucumber-reports/ios/cucumber.json",
        "junit:target/cucumber-reports/ios/cucumber.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    publish = true
)
public class IOSTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}