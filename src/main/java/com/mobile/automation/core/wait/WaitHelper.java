package com.mobile.automation.core.wait;

import com.mobile.automation.core.driver.DriverManager;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class WaitHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaitHelper.class);
    private static final int DEFAULT_TIMEOUT = 10;
    private static final int DEFAULT_POLL_INTERVAL = 500;
    
    public static WebElement waitForElementToBeClickable(By locator) {
        return waitForElementToBeClickable(locator, DEFAULT_TIMEOUT);
    }
    
    public static WebElement waitForElementToBeClickable(By locator, int timeoutInSeconds) {
        LOGGER.debug("Waiting for element to be clickable: {}", locator);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    public static WebElement waitForElementToBeVisible(By locator) {
        return waitForElementToBeVisible(locator, DEFAULT_TIMEOUT);
    }
    
    public static WebElement waitForElementToBeVisible(By locator, int timeoutInSeconds) {
        LOGGER.debug("Waiting for element to be visible: {}", locator);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    public static WebElement waitForElementPresence(By locator) {
        return waitForElementPresence(locator, DEFAULT_TIMEOUT);
    }
    
    public static WebElement waitForElementPresence(By locator, int timeoutInSeconds) {
        LOGGER.debug("Waiting for element presence: {}", locator);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    
    public static List<WebElement> waitForElementsPresence(By locator) {
        return waitForElementsPresence(locator, DEFAULT_TIMEOUT);
    }
    
    public static List<WebElement> waitForElementsPresence(By locator, int timeoutInSeconds) {
        LOGGER.debug("Waiting for elements presence: {}", locator);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    
    public static boolean waitForElementToDisappear(By locator) {
        return waitForElementToDisappear(locator, DEFAULT_TIMEOUT);
    }
    
    public static boolean waitForElementToDisappear(By locator, int timeoutInSeconds) {
        LOGGER.debug("Waiting for element to disappear: {}", locator);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    
    public static boolean waitForTextToBePresentInElement(WebElement element, String text) {
        return waitForTextToBePresentInElement(element, text, DEFAULT_TIMEOUT);
    }
    
    public static boolean waitForTextToBePresentInElement(WebElement element, String text, int timeoutInSeconds) {
        LOGGER.debug("Waiting for text '{}' to be present in element", text);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }
    
    public static boolean waitForAttributeContains(By locator, String attribute, String value) {
        return waitForAttributeContains(locator, attribute, value, DEFAULT_TIMEOUT);
    }
    
    public static boolean waitForAttributeContains(By locator, String attribute, String value, int timeoutInSeconds) {
        LOGGER.debug("Waiting for attribute '{}' to contain '{}' in element: {}", attribute, value, locator);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.attributeContains(locator, attribute, value));
    }
    
    public static <T> T waitUntil(Function<AppiumDriver, T> condition) {
        return waitUntil(condition, DEFAULT_TIMEOUT);
    }
    
    public static <T> T waitUntil(Function<AppiumDriver, T> condition, int timeoutInSeconds) {
        LOGGER.debug("Waiting for custom condition");
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        return wait.until(driver -> condition.apply((AppiumDriver) driver));
    }
    
    public static void waitForCalculatorResult(WebElement resultElement, String expectedValue) {
        waitForCalculatorResult(resultElement, expectedValue, DEFAULT_TIMEOUT);
    }
    
    public static void waitForCalculatorResult(WebElement resultElement, String expectedValue, int timeoutInSeconds) {
        LOGGER.debug("Waiting for calculator result: {}", expectedValue);
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(timeoutInSeconds));
        wait.until(driver -> {
            String actualText = resultElement.getText().trim();
            return actualText.equals(expectedValue) || 
                   actualText.equals(expectedValue + ".0") ||
                   actualText.equals(expectedValue + ".00");
        });
    }
    
    public static void sleep(int milliseconds) {
        try {
            LOGGER.debug("Sleeping for {} milliseconds", milliseconds);
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.warn("Sleep interrupted", e);
        }
    }
}