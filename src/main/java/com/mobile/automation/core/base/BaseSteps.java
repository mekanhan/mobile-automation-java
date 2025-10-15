package com.mobile.automation.core.base;

import com.mobile.automation.config.MobileConfig;
import com.mobile.automation.core.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSteps {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected AppiumDriver driver;
    protected MobileConfig config;
    
    public BaseSteps() {
        this.driver = DriverManager.getDriver();
        this.config = MobileConfig.getInstance();
        logger.debug("Initialized step definitions: {}", this.getClass().getSimpleName());
    }
    
    protected void logStep(String stepDescription) {
        logger.info("Executing step: {}", stepDescription);
    }
    
    protected void logStep(String stepDescription, Object... parameters) {
        logger.info("Executing step: " + stepDescription, parameters);
    }
    
    protected void verifyStep(boolean condition, String errorMessage) {
        if (!condition) {
            logger.error("Step verification failed: {}", errorMessage);
            throw new AssertionError(errorMessage);
        }
        logger.debug("Step verification passed: {}", errorMessage);
    }
    
    protected void waitForStep(int seconds) {
        try {
            logger.debug("Waiting for {} seconds", seconds);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Step wait interrupted", e);
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
    
    protected void takeScreenshot(String stepName) {
        try {
            if (config.isScreenshotOnFailure()) {
                // Implementation for taking screenshots during steps
                logger.info("Taking screenshot for step: {}", stepName);
                // Screenshot implementation would go here
            }
        } catch (Exception e) {
            logger.warn("Failed to take screenshot for step: {}", stepName, e);
        }
    }
}