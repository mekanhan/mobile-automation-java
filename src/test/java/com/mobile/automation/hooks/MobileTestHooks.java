package com.mobile.automation.hooks;

import com.mobile.automation.config.MobileConfig;
import com.mobile.automation.core.driver.DriverManager;
import com.mobile.automation.platforms.android.AndroidCapabilities;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobileTestHooks {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileTestHooks.class);
    private MobileConfig config;
    
    @Before
    public void setUp(Scenario scenario) {
        LOGGER.info("Starting scenario: {}", scenario.getName());
        
        config = MobileConfig.getInstance();
        
        try {
            // Initialize driver if not already initialized
            if (!DriverManager.isDriverInitialized()) {
                initializeDriver();
            }
            
            LOGGER.info("Scenario setup completed for: {}", scenario.getName());
        } catch (Exception e) {
            LOGGER.error("Failed to setup scenario: {}", scenario.getName(), e);
            throw new RuntimeException("Scenario setup failed", e);
        }
    }
    
    @After
    public void tearDown(Scenario scenario) {
        try {
            // Take screenshot if scenario failed
            if (scenario.isFailed() && config.isScreenshotOnFailure()) {
                takeScreenshot(scenario);
            }
            
            LOGGER.info("Scenario completed: {} - Status: {}", 
                       scenario.getName(), 
                       scenario.getStatus());
            
        } catch (Exception e) {
            LOGGER.error("Error in scenario teardown: {}", scenario.getName(), e);
        }
    }
    
    private void initializeDriver() {
        // Get platform from system property or default to Android
        String platformName = System.getProperty("platform", "android");
        DriverManager.Platform platform = DriverManager.Platform.valueOf(platformName.toUpperCase());
        
        // Set Appium server URL from configuration
        DriverManager.setAppiumServerUrl(config.getAppiumServerUrl());
        
        // Create capabilities based on platform
        DesiredCapabilities capabilities;
        switch (platform) {
            case ANDROID:
                capabilities = createAndroidCapabilities();
                break;
            case IOS:
                capabilities = createIOSCapabilities();
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platformName);
        }
        
        // Initialize driver
        DriverManager.initializeDriver(platform, capabilities);
        
        LOGGER.info("Driver initialized for platform: {}", platformName);
    }
    
    private DesiredCapabilities createAndroidCapabilities() {
        // Check if specific app package/activity is configured
        String appPackage = config.getAppPackage();
        String appActivity = config.getAppActivity();
        String appPath = config.getAppPath();
        
        DesiredCapabilities capabilities;
        
        if (appPath != null && !appPath.isEmpty()) {
            // Use APK path if specified
            capabilities = AndroidCapabilities.getApkCapabilities(appPath);
        } else if (appPackage != null && appActivity != null) {
            // Use package and activity
            capabilities = AndroidCapabilities.getCustomAppCapabilities(appPackage, appActivity);
        } else {
            // Default to calculator app
            capabilities = AndroidCapabilities.getCalculatorCapabilities();
        }
        
        // Apply additional configuration
        applyConfigurationToCapabilities(capabilities);
        
        return capabilities;
    }
    
    private DesiredCapabilities createIOSCapabilities() {
        // iOS capabilities - placeholder implementation
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("automationName", "XCUITest");
        
        // Apply additional configuration
        applyConfigurationToCapabilities(capabilities);
        
        LOGGER.info("Created iOS capabilities - placeholder implementation");
        return capabilities;
    }
    
    private void applyConfigurationToCapabilities(DesiredCapabilities capabilities) {
        // Apply configuration values to capabilities
        capabilities.setCapability("noReset", config.isNoReset());
        capabilities.setCapability("fullReset", config.isFullReset());
        capabilities.setCapability("autoGrantPermissions", config.isAutoGrantPermissions());
        capabilities.setCapability("newCommandTimeout", config.getNewCommandTimeout());
        
        // Add device-specific capabilities if specified
        String deviceName = config.getDeviceName();
        if (deviceName != null && !deviceName.isEmpty()) {
            capabilities.setCapability("deviceName", deviceName);
        }
        
        String platformVersion = config.getPlatformVersion();
        if (platformVersion != null && !platformVersion.isEmpty()) {
            capabilities.setCapability("platformVersion", platformVersion);
        }
        
        LOGGER.debug("Applied configuration to capabilities");
    }
    
    private void takeScreenshot(Scenario scenario) {
        try {
            if (DriverManager.isDriverInitialized()) {
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot");
                LOGGER.info("Screenshot attached to scenario: {}", scenario.getName());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to take screenshot for scenario: {}", scenario.getName(), e);
        }
    }
    
    // Hook for driver cleanup at the end of test suite
    @After("@quit-driver")
    public void quitDriver() {
        if (DriverManager.isDriverInitialized()) {
            DriverManager.quitDriver();
            LOGGER.info("Driver quit after scenario");
        }
    }
}