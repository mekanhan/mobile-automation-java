package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Calculator Demo Test Runner
 * Runs Calculator app automation tests for demonstration
 */
@CucumberOptions(
    features = "src/test/resources/features/calculator.feature",
    glue = {"com.example.steps", "com.example.hooks"},
    tags = "@calculator and not @skip",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/calculator/cucumber.html",
        "json:target/cucumber-reports/calculator/cucumber.json",
        "junit:target/cucumber-reports/calculator/cucumber.xml"
    },
    monochrome = true,
    publish = true
)
public class CalculatorTestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}