package com.mobile.automation.platforms.android;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AndroidCapabilities {
    private static final Logger LOGGER = LoggerFactory.getLogger(AndroidCapabilities.class);
    
    public static DesiredCapabilities getCalculatorCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", "Android Device");
        capabilities.setCapability("appPackage", "com.google.android.calculator");
        capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("fullReset", false);
        capabilities.setCapability("newCommandTimeout", 300);
        capabilities.setCapability("autoGrantPermissions", true);
        
        LOGGER.info("Created Android Calculator capabilities: {}", capabilities);
        return capabilities;
    }
    
    public static DesiredCapabilities getCustomAppCapabilities(String appPackage, String appActivity) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", "Android Device");
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("noReset", true);
        capabilities.setCapability("fullReset", false);
        capabilities.setCapability("newCommandTimeout", 300);
        capabilities.setCapability("autoGrantPermissions", true);
        
        LOGGER.info("Created Android capabilities for {}:{}", appPackage, appActivity);
        return capabilities;
    }
    
    public static DesiredCapabilities getApkCapabilities(String apkPath) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", "Android Device");
        capabilities.setCapability("app", apkPath);
        capabilities.setCapability("noReset", false);
        capabilities.setCapability("fullReset", true);
        capabilities.setCapability("newCommandTimeout", 300);
        capabilities.setCapability("autoGrantPermissions", true);
        
        LOGGER.info("Created Android APK capabilities for: {}", apkPath);
        return capabilities;
    }
    
    public static DesiredCapabilities getDeviceSpecificCapabilities(String deviceName, String platformVersion) {
        DesiredCapabilities capabilities = getCalculatorCapabilities();
        
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", platformVersion);
        
        LOGGER.info("Created device-specific capabilities for {} (Android {})", deviceName, platformVersion);
        return capabilities;
    }
    
    public static DesiredCapabilities getBrowserCapabilities(String browserName) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("deviceName", "Android Device");
        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("newCommandTimeout", 300);
        
        LOGGER.info("Created Android browser capabilities for: {}", browserName);
        return capabilities;
    }
}