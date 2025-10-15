package com.mobile.automation.core.base;

import com.mobile.automation.config.MobileConfig;
import com.mobile.automation.core.driver.DriverManager;
import com.mobile.automation.platforms.android.AndroidCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseTest {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected MobileConfig config;
    
    @BeforeMethod(alwaysRun = true)
    @Parameters({"platform", "deviceName", "platformVersion"})
    public void setUp(@Optional("android") String platform, 
                     @Optional String deviceName, 
                     @Optional String platformVersion) {
        logger.info("Setting up test for platform: {}", platform);
        
        config = MobileConfig.getInstance();
        
        try {
            initializeDriver(platform, deviceName, platformVersion);
            onTestSetup();
            logger.info("Test setup completed successfully");
        } catch (Exception e) {
            logger.error("Test setup failed", e);
            throw new RuntimeException("Failed to setup test", e);
        }
    }
    
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            onTestTeardown();
            
            if (DriverManager.isDriverInitialized()) {
                logger.info("Quitting driver");
                DriverManager.quitDriver();
            }
            logger.info("Test teardown completed successfully");
        } catch (Exception e) {
            logger.error("Test teardown failed", e);
        }
    }
    
    private void initializeDriver(String platform, String deviceName, String platformVersion) {
        DriverManager.Platform driverPlatform = DriverManager.Platform.valueOf(platform.toUpperCase());
        DesiredCapabilities capabilities;
        
        switch (driverPlatform) {
            case ANDROID:
                capabilities = createAndroidCapabilities(deviceName, platformVersion);
                break;
            case IOS:
                capabilities = createIOSCapabilities(deviceName, platformVersion);
                break;
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
        
        // Set Appium server URL from configuration
        DriverManager.setAppiumServerUrl(config.getAppiumServerUrl());
        
        // Initialize driver
        DriverManager.initializeDriver(driverPlatform, capabilities);
        
        logger.info("Driver initialized for platform: {} with capabilities: {}", platform, capabilities);
    }
    
    private DesiredCapabilities createAndroidCapabilities(String deviceName, String platformVersion) {
        DesiredCapabilities capabilities;
        
        if (deviceName != null && platformVersion != null) {
            capabilities = AndroidCapabilities.getDeviceSpecificCapabilities(deviceName, platformVersion);
        } else {
            // Use app package and activity from configuration if available
            String appPackage = config.getAppPackage();
            String appActivity = config.getAppActivity();
            
            if (appPackage != null && appActivity != null) {
                capabilities = AndroidCapabilities.getCustomAppCapabilities(appPackage, appActivity);
            } else {
                // Default to calculator app
                capabilities = AndroidCapabilities.getCalculatorCapabilities();
            }
        }
        
        // Apply additional configuration
        applyConfigurationToCapabilities(capabilities);
        
        return capabilities;
    }
    
    private DesiredCapabilities createIOSCapabilities(String deviceName, String platformVersion) {
        // iOS capabilities implementation - placeholder for future
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("automationName", "XCUITest");
        
        if (deviceName != null) {
            capabilities.setCapability("deviceName", deviceName);
        }
        if (platformVersion != null) {
            capabilities.setCapability("platformVersion", platformVersion);
        }
        
        applyConfigurationToCapabilities(capabilities);
        
        logger.info("Created iOS capabilities - placeholder implementation");
        return capabilities;
    }
    
    private void applyConfigurationToCapabilities(DesiredCapabilities capabilities) {
        // Apply configuration values to capabilities
        capabilities.setCapability("noReset", config.isNoReset());
        capabilities.setCapability("fullReset", config.isFullReset());
        capabilities.setCapability("autoGrantPermissions", config.isAutoGrantPermissions());
        capabilities.setCapability("newCommandTimeout", config.getNewCommandTimeout());
        
        // Add app path if specified
        String appPath = config.getAppPath();
        if (appPath != null && !appPath.isEmpty()) {
            capabilities.setCapability("app", appPath);
        }
        
        logger.debug("Applied configuration to capabilities");
    }
    
    protected void takeScreenshot(String testName) {
        if (config.isScreenshotOnFailure() && DriverManager.isDriverInitialized()) {
            try {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                String fileName = String.format("%s_%s_%s.png", testName, config.getPlatformName(), timestamp);
                String screenshotPath = config.getScreenshotPath();
                
                // Create screenshot directory if it doesn't exist
                File screenshotDir = new File(screenshotPath);
                if (!screenshotDir.exists()) {
                    screenshotDir.mkdirs();
                }
                
                // Take screenshot using Appium
                String fullPath = screenshotPath + File.separator + fileName;
                File screenshot = DriverManager.getDriver().getScreenshotAs(org.openqa.selenium.OutputType.FILE);
                java.nio.file.Files.copy(screenshot.toPath(), new File(fullPath).toPath());
                
                logger.info("Screenshot saved: {}", fullPath);
            } catch (Exception e) {
                logger.error("Failed to take screenshot", e);
            }
        }
    }
    
    protected String getCurrentPlatform() {
        DriverManager.Platform platform = DriverManager.getCurrentPlatform();
        return platform != null ? platform.name().toLowerCase() : "unknown";
    }
    
    protected boolean isAndroid() {
        return DriverManager.getCurrentPlatform() == DriverManager.Platform.ANDROID;
    }
    
    protected boolean isIOS() {
        return DriverManager.getCurrentPlatform() == DriverManager.Platform.IOS;
    }
    
    // Hook methods that subclasses can override
    protected void onTestSetup() {
        // Override in subclasses for specific setup
    }
    
    protected void onTestTeardown() {
        // Override in subclasses for specific teardown
    }
}