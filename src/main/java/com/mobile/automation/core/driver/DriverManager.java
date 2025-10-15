package com.mobile.automation.core.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    
    private static String appiumServerUrl = System.getProperty("appium.url", "http://127.0.0.1:4723");
    
    public static void initializeDriver(Platform platform, DesiredCapabilities capabilities) {
        try {
            URL serverUrl = new URL(appiumServerUrl);
            AppiumDriver appiumDriver;
            
            switch (platform) {
                case ANDROID:
                    LOGGER.info("Initializing Android driver with capabilities: {}", capabilities);
                    appiumDriver = new AndroidDriver(serverUrl, capabilities);
                    break;
                case IOS:
                    LOGGER.info("Initializing iOS driver with capabilities: {}", capabilities);
                    appiumDriver = new IOSDriver(serverUrl, capabilities);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported platform: " + platform);
            }
            
            appiumDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.set(appiumDriver);
            
            LOGGER.info("Successfully initialized {} driver", platform);
            
        } catch (MalformedURLException e) {
            LOGGER.error("Invalid Appium server URL: {}", appiumServerUrl, e);
            throw new RuntimeException("Failed to initialize driver due to invalid URL", e);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize {} driver", platform, e);
            throw new RuntimeException("Driver initialization failed", e);
        }
    }
    
    public static AppiumDriver getDriver() {
        AppiumDriver appiumDriver = driver.get();
        if (appiumDriver == null) {
            throw new RuntimeException("Driver not initialized. Call initializeDriver() first.");
        }
        return appiumDriver;
    }
    
    public static AndroidDriver getAndroidDriver() {
        AppiumDriver appiumDriver = getDriver();
        if (!(appiumDriver instanceof AndroidDriver)) {
            throw new RuntimeException("Current driver is not an AndroidDriver");
        }
        return (AndroidDriver) appiumDriver;
    }
    
    public static IOSDriver getIOSDriver() {
        AppiumDriver appiumDriver = getDriver();
        if (!(appiumDriver instanceof IOSDriver)) {
            throw new RuntimeException("Current driver is not an IOSDriver");
        }
        return (IOSDriver) appiumDriver;
    }
    
    public static boolean isDriverInitialized() {
        return driver.get() != null;
    }
    
    public static Platform getCurrentPlatform() {
        if (!isDriverInitialized()) {
            return null;
        }
        
        AppiumDriver appiumDriver = getDriver();
        if (appiumDriver instanceof AndroidDriver) {
            return Platform.ANDROID;
        } else if (appiumDriver instanceof IOSDriver) {
            return Platform.IOS;
        }
        return Platform.UNKNOWN;
    }
    
    public static void quitDriver() {
        AppiumDriver appiumDriver = driver.get();
        if (appiumDriver != null) {
            try {
                LOGGER.info("Quitting {} driver", getCurrentPlatform());
                appiumDriver.quit();
            } catch (Exception e) {
                LOGGER.error("Error while quitting driver", e);
            } finally {
                driver.remove();
            }
        }
    }
    
    public static void setAppiumServerUrl(String url) {
        appiumServerUrl = url;
        LOGGER.info("Appium server URL set to: {}", url);
    }
    
    public static String getAppiumServerUrl() {
        return appiumServerUrl;
    }
    
    public enum Platform {
        ANDROID, IOS, UNKNOWN
    }
}