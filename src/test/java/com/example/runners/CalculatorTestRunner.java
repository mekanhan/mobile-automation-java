package com.example.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calculator Demo Test Runner - Updated for New Architecture
 * Backward compatibility runner that uses the new mobile automation framework
 * Runs Calculator app automation tests for demonstration
 */
@CucumberOptions(
    features = "src/test/resources/features/calculator.feature",
    glue = {
        "com.mobile.automation.steps", 
        "com.mobile.automation.hooks"
    },
    tags = "@calculator and @smoke",
    plugin = {
        "pretty",
        "html:target/cucumber-reports/calculator/cucumber.html",
        "json:target/cucumber-reports/calculator/cucumber.json",
        "junit:target/cucumber-reports/calculator/cucumber.xml",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    publish = true
)
public class CalculatorTestRunner extends AbstractTestNGCucumberTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatorTestRunner.class);
    
    @BeforeClass
    public void setUpCalculatorDemo() {
        LOGGER.info("🚀 Calculator Demo - Using New Mobile Automation Architecture");
        
        // Set Android as platform for the demo
        System.setProperty("platform", "android");
        
        // Set local environment
        System.setProperty("environment", "local");
        
        // Log configuration for demo
        LOGGER.info("Platform: Android");
        LOGGER.info("Environment: Local");
        LOGGER.info("New architecture features:");
        LOGGER.info("  ✓ Cross-platform driver management");
        LOGGER.info("  ✓ Enhanced wait strategies");
        LOGGER.info("  ✓ Configuration management");
        LOGGER.info("  ✓ Improved page object model");
        
        LOGGER.info("Calculator demo setup completed");
    }
    
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}