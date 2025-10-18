package mobile.automation.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import mobile.automation.config.IOSCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Driver Manager for Appium Driver lifecycle
 * Uses ThreadLocal for parallel execution support
 */
public class DriverManager {

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";

    /**
     * Initialize and return iOS driver
     */
    public static AppiumDriver initializeIOSDriver() {
        try {
            // Get iOS capabilities
            XCUITestOptions options = IOSCapabilities.getIOSOptions();

            // Print capabilities for debugging
            System.out.println("Initializing iOS Driver...");
            IOSCapabilities.printCapabilities(options);

            // Create iOS Driver
            URL serverUrl = new URL(APPIUM_SERVER_URL);
            IOSDriver iosDriver = new IOSDriver(serverUrl, options);

            // Store in ThreadLocal
            driver.set(iosDriver);

            System.out.println("iOS Driver initialized successfully!");
            return iosDriver;

        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid Appium server URL: " + APPIUM_SERVER_URL, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize iOS driver", e);
        }
    }

    /**
     * Get the current driver instance
     */
    public static AppiumDriver getDriver() {
        AppiumDriver currentDriver = driver.get();
        if (currentDriver == null) {
            throw new IllegalStateException("Driver not initialized. Call initializeIOSDriver() first.");
        }
        return currentDriver;
    }

    /**
     * Quit and remove driver
     */
    public static void quitDriver() {
        AppiumDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                System.out.println("Quitting driver...");
                currentDriver.quit();
                System.out.println("Driver quit successfully!");
            } catch (Exception e) {
                System.err.println("Error while quitting driver: " + e.getMessage());
            } finally {
                driver.remove();
            }
        }
    }

    /**
     * Check if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }

    /**
     * Get Appium server URL
     */
    public static String getAppiumServerUrl() {
        String serverUrl = System.getProperty("appium.server.url");
        return serverUrl != null ? serverUrl : APPIUM_SERVER_URL;
    }

    /**
     * Initialize driver based on platform
     */
    public static AppiumDriver initializeDriver(String platform) {
        if (platform == null || platform.isEmpty()) {
            platform = "ios";  // Default to iOS
        }

        switch (platform.toLowerCase()) {
            case "ios":
                return initializeIOSDriver();
            case "android":
                throw new UnsupportedOperationException("Android driver not yet implemented");
            default:
                throw new IllegalArgumentException("Unsupported platform: " + platform);
        }
    }

    /**
     * Restart the app (without reinstalling)
     */
    public static void restartApp() {
        AppiumDriver currentDriver = getDriver();
        if (currentDriver instanceof IOSDriver) {
            IOSDriver iosDriver = (IOSDriver) currentDriver;
            iosDriver.terminateApp(getBundleId());
            iosDriver.activateApp(getBundleId());
        }
    }

    /**
     * Close the app
     */
    public static void closeApp() {
        AppiumDriver currentDriver = getDriver();
        if (currentDriver instanceof IOSDriver) {
            IOSDriver iosDriver = (IOSDriver) currentDriver;
            iosDriver.terminateApp(getBundleId());
        }
    }

    /**
     * Launch the app
     */
    public static void launchApp() {
        AppiumDriver currentDriver = getDriver();
        if (currentDriver instanceof IOSDriver) {
            IOSDriver iosDriver = (IOSDriver) currentDriver;
            iosDriver.activateApp(getBundleId());
        }
    }

    /**
     * Get bundle ID from system property or default
     */
    private static String getBundleId() {
        String bundleId = System.getProperty("ios.bundleId");
        return bundleId != null ? bundleId : "com.mekan.wikipedia";
    }

    /**
     * Reset app (reinstall)
     */
    public static void resetApp() {
        AppiumDriver currentDriver = getDriver();
        if (currentDriver instanceof IOSDriver) {
            IOSDriver iosDriver = (IOSDriver) currentDriver;
            String bundleId = getBundleId();
            iosDriver.terminateApp(bundleId);
            iosDriver.removeApp(bundleId);
            // App will be reinstalled on next activation
        }
    }

    /**
     * Get current platform
     */
    public static String getPlatform() {
        AppiumDriver currentDriver = getDriver();
        if (currentDriver instanceof IOSDriver) {
            return "ios";
        }
        return "unknown";
    }
}
