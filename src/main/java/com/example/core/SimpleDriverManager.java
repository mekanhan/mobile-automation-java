package com.example.core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Simple Driver Manager for handling Appium driver instances
 * Simplified version without complex features for demo purposes
 */
public class SimpleDriverManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDriverManager.class);
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
    private static final Duration IMPLICIT_WAIT = Duration.ofSeconds(10);
    
    private SimpleDriverManager() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Initialize driver based on platform and device configuration
     */
    public static void initializeDriver(String platform, DesiredCapabilities capabilities) {
        try {
            AppiumDriver appiumDriver;
            URL serverUrl = new URL(APPIUM_SERVER_URL);
            
            if ("android".equalsIgnoreCase(platform)) {
                appiumDriver = new AndroidDriver(serverUrl, capabilities);
                LOGGER.info("Android driver initialized successfully");
            } else {
                throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
            
            appiumDriver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT);
            driver.set(appiumDriver);
            
        } catch (MalformedURLException e) {
            LOGGER.error("Invalid Appium server URL: {}", APPIUM_SERVER_URL, e);
            throw new RuntimeException("Failed to initialize driver", e);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize {} driver", platform, e);
            throw new RuntimeException("Driver initialization failed", e);
        }
    }
    
    /**
     * Get current driver instance
     */
    public static AppiumDriver getDriver() {
        AppiumDriver currentDriver = driver.get();
        if (currentDriver == null) {
            throw new IllegalStateException("Driver not initialized. Call initializeDriver() first.");
        }
        return currentDriver;
    }
    
    /**
     * Quit driver and clean up resources
     */
    public static void quitDriver() {
        AppiumDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                currentDriver.quit();
                LOGGER.info("Driver quit successfully");
            } catch (Exception e) {
                LOGGER.warn("Error while quitting driver", e);
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
}