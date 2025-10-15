package com.mobile.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MobileConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MobileConfig.class);
    private static volatile MobileConfig instance;
    private Properties properties;
    
    private MobileConfig() {
        loadConfiguration();
    }
    
    public static MobileConfig getInstance() {
        if (instance == null) {
            synchronized (MobileConfig.class) {
                if (instance == null) {
                    instance = new MobileConfig();
                }
            }
        }
        return instance;
    }
    
    private void loadConfiguration() {
        properties = new Properties();
        
        // Load default properties
        loadPropertiesFile("config/mobile.properties");
        
        // Load platform-specific properties
        String platform = System.getProperty("platform", "android");
        loadPropertiesFile("config/" + platform + ".properties");
        
        // Load environment-specific properties
        String environment = System.getProperty("environment", "local");
        loadPropertiesFile("config/environments/" + environment + ".properties");
        
        // Override with system properties
        properties.putAll(System.getProperties());
        
        LOGGER.info("Configuration loaded for platform: {}, environment: {}", platform, environment);
    }
    
    private void loadPropertiesFile(String fileName) {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream != null) {
                Properties tempProps = new Properties();
                tempProps.load(inputStream);
                properties.putAll(tempProps);
                LOGGER.debug("Loaded properties from: {}", fileName);
            } else {
                LOGGER.warn("Properties file not found: {}", fileName);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load properties from: {}", fileName, e);
        }
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                LOGGER.warn("Invalid integer value for {}: {}, using default: {}", key, value, defaultValue);
            }
        }
        return defaultValue;
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
    
    // Mobile-specific configuration methods
    public String getAppiumServerUrl() {
        return getProperty("appium.server.url", "http://127.0.0.1:4723");
    }
    
    public int getImplicitWaitTimeout() {
        return getIntProperty("mobile.wait.implicit", 10);
    }
    
    public int getExplicitWaitTimeout() {
        return getIntProperty("mobile.wait.explicit", 10);
    }
    
    public String getPlatformName() {
        return getProperty("mobile.platform.name", "Android");
    }
    
    public String getDeviceName() {
        return getProperty("mobile.device.name", "Android Device");
    }
    
    public String getPlatformVersion() {
        return getProperty("mobile.platform.version");
    }
    
    public String getAppPackage() {
        return getProperty("mobile.app.package");
    }
    
    public String getAppActivity() {
        return getProperty("mobile.app.activity");
    }
    
    public String getAppPath() {
        return getProperty("mobile.app.path");
    }
    
    public boolean isNoReset() {
        return getBooleanProperty("mobile.app.noReset", true);
    }
    
    public boolean isFullReset() {
        return getBooleanProperty("mobile.app.fullReset", false);
    }
    
    public boolean isAutoGrantPermissions() {
        return getBooleanProperty("mobile.app.autoGrantPermissions", true);
    }
    
    public int getNewCommandTimeout() {
        return getIntProperty("mobile.session.newCommandTimeout", 300);
    }
    
    public String getAutomationName() {
        return getProperty("mobile.automation.name", "UiAutomator2");
    }
    
    // Test execution configuration
    public boolean isParallelExecution() {
        return getBooleanProperty("test.execution.parallel", false);
    }
    
    public int getThreadCount() {
        return getIntProperty("test.execution.threadCount", 1);
    }
    
    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("test.screenshot.onFailure", true);
    }
    
    public String getScreenshotPath() {
        return getProperty("test.screenshot.path", "target/screenshots");
    }
    
    public String getReportsPath() {
        return getProperty("test.reports.path", "target/reports");
    }
    
    // Debug method to print all properties
    public void printConfiguration() {
        LOGGER.info("Current configuration:");
        properties.forEach((key, value) -> {
            if (!key.toString().toLowerCase().contains("password") && 
                !key.toString().toLowerCase().contains("secret")) {
                LOGGER.info("  {} = {}", key, value);
            }
        });
    }
}