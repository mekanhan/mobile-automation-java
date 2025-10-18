package mobile.automation.config;

import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.time.Duration;

/**
 * iOS Capabilities Configuration
 * Configured for iPhone 16 Pro Simulator with iOS 18.6
 */
public class IOSCapabilities {

    /**
     * Get iOS capabilities using XCUITestOptions (Appium 2.x recommended approach)
     */
    public static XCUITestOptions getIOSOptions() {
        XCUITestOptions options = new XCUITestOptions();

        // Platform Configuration
        options.setPlatformName("iOS");
        options.setPlatformVersion("18.6");
        options.setDeviceName("iPhone 16 Pro");

        // Automation Engine
        options.setAutomationName("XCUITest");

        // App Configuration
        String appPath = System.getProperty("user.dir") +
                        "/src/main/resources/apps/ios/Wikipedia.app";
        options.setApp(new File(appPath).getAbsolutePath());

        // Bundle ID (backup if app path fails)
        options.setBundleId("com.mekan.wikipedia");

        // Launch & Reset Configuration
        options.setNoReset(false);  // Fresh install each test
        options.setFullReset(false); // Don't uninstall after test

        // Timeouts
        options.setNewCommandTimeout(Duration.ofSeconds(300));
        options.setWdaLaunchTimeout(Duration.ofSeconds(60));
        options.setWdaConnectionTimeout(Duration.ofSeconds(60));

        // Alert Handling
        options.setAutoAcceptAlerts(true);
        options.setAutoDismissAlerts(false);

        // Performance Optimizations
        options.setUseNewWDA(false);  // Reuse WebDriverAgent
        options.setCapability("usePrebuiltWDA", true);  // Use prebuilt WDA

        // Derived Data Path (speeds up WDA installation)
        String derivedDataPath = System.getProperty("user.home") +
                                "/Library/Developer/Xcode/DerivedData/WebDriverAgent";
        options.setDerivedDataPath(derivedDataPath);

        // Simulator Specific Settings
        options.setIsHeadless(false);  // Show simulator window
        options.setSimulatorStartupTimeout(Duration.ofSeconds(180));

        // Additional Settings
        options.setShowXcodeLog(false);  // Reduce log verbosity
        options.setClearSystemFiles(true);  // Clean temp files

        return options;
    }

    /**
     * Get iOS capabilities as DesiredCapabilities (legacy approach for compatibility)
     */
    public static DesiredCapabilities getIOSCapabilities() {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Platform Configuration
        capabilities.setCapability("platformName", "iOS");
        capabilities.setCapability("platformVersion", "18.6");
        capabilities.setCapability("deviceName", "iPhone 16 Pro");

        // Automation Engine
        capabilities.setCapability("automationName", "XCUITest");

        // App Configuration
        String appPath = System.getProperty("user.dir") +
                        "/src/main/resources/apps/ios/Wikipedia.app";
        capabilities.setCapability("app", new File(appPath).getAbsolutePath());
        capabilities.setCapability("bundleId", "com.mekan.wikipedia");

        // Launch & Reset Configuration
        capabilities.setCapability("noReset", false);
        capabilities.setCapability("fullReset", false);

        // Timeouts
        capabilities.setCapability("newCommandTimeout", 300);
        capabilities.setCapability("wdaLaunchTimeout", 60000);
        capabilities.setCapability("wdaConnectionTimeout", 60000);

        // Alert Handling
        capabilities.setCapability("autoAcceptAlerts", true);
        capabilities.setCapability("autoDismissAlerts", false);

        // Performance Optimizations
        capabilities.setCapability("useNewWDA", false);
        capabilities.setCapability("usePrebuiltWDA", true);

        // Derived Data Path
        String derivedDataPath = System.getProperty("user.home") +
                                "/Library/Developer/Xcode/DerivedData/WebDriverAgent";
        capabilities.setCapability("derivedDataPath", derivedDataPath);

        // Simulator Settings
        capabilities.setCapability("isHeadless", false);
        capabilities.setCapability("simulatorStartupTimeout", 180000);

        // Additional Settings
        capabilities.setCapability("showXcodeLog", false);
        capabilities.setCapability("clearSystemFiles", true);

        return capabilities;
    }

    /**
     * Get capabilities for Real Device (iPhone 16 Pro)
     * Requires: UDID, Team ID, xcodeOrgId, xcodeSigningId
     */
    public static XCUITestOptions getRealDeviceOptions(String udid, String teamId) {
        XCUITestOptions options = getIOSOptions();

        // Override for real device
        options.setDeviceName("iPhone 16 Pro");  // Can be any name for real device
        options.setUdid(udid);  // Device UDID

        // Code Signing (required for real device)
        options.setCapability("xcodeOrgId", teamId);
        options.setCapability("xcodeSigningId", "iPhone Developer");
        options.setCapability("updatedWDABundleId", "com.mekan.WebDriverAgentRunner");

        // Real device specific
        options.setUseNewWDA(true);  // May need new WDA for real device

        return options;
    }

    /**
     * Get app path from config or default location
     */
    private static String getAppPath() {
        String appPath = System.getProperty("ios.app.path");
        if (appPath == null || appPath.isEmpty()) {
            appPath = System.getProperty("user.dir") +
                     "/src/main/resources/apps/ios/Wikipedia.app";
        }
        return appPath;
    }

    /**
     * Print capabilities for debugging
     */
    public static void printCapabilities(XCUITestOptions options) {
        System.out.println("========== iOS Capabilities ==========");
        System.out.println("Platform: " + (options.getPlatformName() != null ? options.getPlatformName() : "N/A"));
        System.out.println("Version: " + (options.getPlatformVersion().isPresent() ? options.getPlatformVersion().get() : "N/A"));
        System.out.println("Device: " + (options.getDeviceName().isPresent() ? options.getDeviceName().get() : "N/A"));
        System.out.println("App: " + (options.getApp().isPresent() ? options.getApp().get() : "N/A"));
        System.out.println("Bundle ID: " + (options.getBundleId().isPresent() ? options.getBundleId().get() : "N/A"));
        System.out.println("Automation: " + (options.getAutomationName().isPresent() ? options.getAutomationName().get() : "N/A"));
        System.out.println("=====================================");
    }
}
