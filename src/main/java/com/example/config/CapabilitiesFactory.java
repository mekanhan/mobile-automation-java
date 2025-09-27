package com.example.config;

import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class for creating platform-specific desired capabilities
 */
public class CapabilitiesFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(CapabilitiesFactory.class);
    
    private CapabilitiesFactory() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Create iOS capabilities
     */
    public static DesiredCapabilities createIOSCapabilities(String deviceName, String platformVersion, String bundleId) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Platform capabilities
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        
        // App capabilities
        capabilities.setCapability("bundleId", bundleId);
        capabilities.setCapability("autoAcceptAlerts", true);
        capabilities.setCapability("autoDismissAlerts", false);
        
        // Performance and stability
        capabilities.setCapability("newCommandTimeout", 60);
        capabilities.setCapability("wdaStartupRetries", 3);
        capabilities.setCapability("wdaStartupRetryInterval", 20000);
        capabilities.setCapability("shouldUseSingletonTestManager", false);
        
        // Optional capabilities for real devices
        if (!isSimulator(deviceName)) {
            capabilities.setCapability("xcodeOrgId", "YOUR_TEAM_ID");
            capabilities.setCapability("xcodeSigningId", "iPhone Developer");
            capabilities.setCapability("updatedWDABundleId", "com.example.WebDriverAgentRunner");
        }
        
        LOGGER.info("Created iOS capabilities for device: {}, version: {}", deviceName, platformVersion);
        return capabilities;
    }
    
    /**
     * Create Android capabilities
     */
    public static DesiredCapabilities createAndroidCapabilities(String deviceName, String platformVersion, 
                                                               String appPackage, String appActivity) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Platform capabilities
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        
        // App capabilities
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("autoAcceptAlerts", true);
        
        // Performance and stability
        capabilities.setCapability("newCommandTimeout", 60);
        capabilities.setCapability("uiautomator2ServerInstallTimeout", 60000);
        capabilities.setCapability("uiautomator2ServerLaunchTimeout", 60000);
        capabilities.setCapability("androidInstallTimeout", 90000);
        
        // Optional for real devices
        if (!isEmulator(deviceName)) {
            capabilities.setCapability("udid", "YOUR_DEVICE_UDID");
        }
        
        LOGGER.info("Created Android capabilities for device: {}, version: {}", deviceName, platformVersion);
        return capabilities;
    }
    
    /**
     * Create capabilities from config
     */
    public static DesiredCapabilities createCapabilitiesFromConfig() {
        ConfigManager.TestConfig config = ConfigManager.getConfig();
        String platform = config.platformName().toLowerCase();
        
        switch (platform) {
            case "ios":
                return createIOSCapabilities(
                    config.deviceName(),
                    config.platformVersion(),
                    config.bundleId()
                );
            case "android":
                return createAndroidCapabilities(
                    config.deviceName(),
                    config.platformVersion(),
                    config.appPackage(),
                    config.appActivity()
                );
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
    
    /**
     * Create capabilities for Calculator app demo (Android)
     */
    public static DesiredCapabilities createCalculatorCapabilities(String deviceName, String platformVersion) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        
        // Platform capabilities
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
        
        // Calculator app (Google Calculator - pre-installed on most Android devices)
        capabilities.setCapability("appPackage", "com.google.android.calculator");
        capabilities.setCapability("appActivity", "com.android.calculator2.Calculator");
        
        // Alternative calculator packages for different Android versions
        // capabilities.setCapability("appPackage", "com.android.calculator2");
        // capabilities.setCapability("appActivity", ".Calculator");
        
        // Performance and stability
        capabilities.setCapability("autoGrantPermissions", true);
        capabilities.setCapability("newCommandTimeout", 60);
        capabilities.setCapability("uiautomator2ServerInstallTimeout", 60000);
        capabilities.setCapability("androidInstallTimeout", 90000);
        
        // Reset settings for clean demo
        capabilities.setCapability("noReset", false);
        capabilities.setCapability("fullReset", false);
        
        LOGGER.info("Created Calculator app capabilities for device: {}, version: {}", deviceName, platformVersion);
        return capabilities;
    }
    
    /**
     * Create Calculator capabilities with defaults for demo
     */
    public static DesiredCapabilities createCalculatorCapabilitiesForDemo() {
        String defaultDevice = "Android Device"; // Works with any connected device/emulator
        String defaultVersion = "16"; // Compatible with most modern Android versions
        return createCalculatorCapabilities(defaultDevice, defaultVersion);
    }
    
    /**
     * Create capabilities for specific device configuration
     */
    public static DesiredCapabilities createCapabilities(String platform, String deviceName, 
                                                        String platformVersion, String appIdentifier) {
        switch (platform.toLowerCase()) {
            case "ios":
                return createIOSCapabilities(deviceName, platformVersion, appIdentifier);
            case "android":
                // Special case for Calculator demo
                if ("calculator".equalsIgnoreCase(appIdentifier)) {
                    return createCalculatorCapabilities(deviceName, platformVersion);
                }
                
                String[] appDetails = appIdentifier.split("/");
                String appPackage = appDetails[0];
                String appActivity = appDetails.length > 1 ? appDetails[1] : "";
                return createAndroidCapabilities(deviceName, platformVersion, appPackage, appActivity);
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }
    
    /**
     * Add common capabilities for both platforms
     */
    private static void addCommonCapabilities(DesiredCapabilities capabilities) {
        capabilities.setCapability("noReset", false);
        capabilities.setCapability("fullReset", false);
        capabilities.setCapability("printPageSourceOnFindFailure", true);
    }
    
    /**
     * Check if device is iOS simulator
     */
    private static boolean isSimulator(String deviceName) {
        return deviceName.toLowerCase().contains("simulator") || 
               deviceName.toLowerCase().contains("iphone") ||
               deviceName.toLowerCase().contains("ipad");
    }
    
    /**
     * Check if device is Android emulator
     */
    private static boolean isEmulator(String deviceName) {
        return deviceName.toLowerCase().contains("emulator") ||
               deviceName.toLowerCase().contains("pixel") ||
               deviceName.toLowerCase().contains("nexus");
    }
}