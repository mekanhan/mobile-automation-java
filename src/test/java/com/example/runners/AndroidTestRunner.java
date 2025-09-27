package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Android-specific Test Runner
 * Runs tests with Android-specific tags and configurations
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.example.steps", "com.example.hooks"},
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
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}