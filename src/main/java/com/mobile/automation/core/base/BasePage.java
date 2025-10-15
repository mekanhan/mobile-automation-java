package com.mobile.automation.core.base;

import com.mobile.automation.config.MobileConfig;
import com.mobile.automation.core.driver.DriverManager;
import com.mobile.automation.core.wait.WaitHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected AppiumDriver driver;
    protected MobileConfig config;
    
    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.config = MobileConfig.getInstance();
        initializePageElements();
    }
    
    private void initializePageElements() {
        PageFactory.initElements(
            new AppiumFieldDecorator(driver, Duration.ofSeconds(config.getImplicitWaitTimeout())), 
            this
        );
        logger.debug("Initialized page elements for: {}", this.getClass().getSimpleName());
    }
    
    // Common element interaction methods
    protected void click(WebElement element) {
        // For PageFactory elements, wait for the element itself to be clickable
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(config.getExplicitWaitTimeout()))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(element));
        element.click();
        logger.debug("Clicked element: {}", element);
    }
    
    protected void click(By locator) {
        WaitHelper.waitForElementToBeClickable(locator).click();
        logger.debug("Clicked element by locator: {}", locator);
    }
    
    protected void sendKeys(WebElement element, String text) {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(config.getExplicitWaitTimeout()))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(element));
        element.clear();
        element.sendKeys(text);
        logger.debug("Sent keys '{}' to element: {}", text, element);
    }
    
    protected void sendKeys(By locator, String text) {
        WebElement element = WaitHelper.waitForElementToBeClickable(locator);
        element.clear();
        element.sendKeys(text);
        logger.debug("Sent keys '{}' to element by locator: {}", text, locator);
    }
    
    protected String getText(WebElement element) {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(config.getExplicitWaitTimeout()))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(element));
        String text = element.getText();
        logger.debug("Got text '{}' from element: {}", text, element);
        return text;
    }
    
    protected String getText(By locator) {
        String text = WaitHelper.waitForElementToBeVisible(locator).getText();
        logger.debug("Got text '{}' from element by locator: {}", text, locator);
        return text;
    }
    
    protected boolean isElementDisplayed(WebElement element) {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(2))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf(element));
            boolean displayed = element.isDisplayed();
            logger.debug("Element displayed status: {} for element: {}", displayed, element);
            return displayed;
        } catch (Exception e) {
            logger.debug("Element not displayed: {}", element);
            return false;
        }
    }
    
    protected boolean isElementDisplayed(By locator) {
        try {
            boolean displayed = WaitHelper.waitForElementToBeVisible(locator, 2).isDisplayed();
            logger.debug("Element displayed status: {} for locator: {}", displayed, locator);
            return displayed;
        } catch (Exception e) {
            logger.debug("Element not displayed for locator: {}", locator);
            return false;
        }
    }
    
    protected boolean isElementEnabled(WebElement element) {
        boolean enabled = element.isEnabled();
        logger.debug("Element enabled status: {} for element: {}", enabled, element);
        return enabled;
    }
    
    protected List<WebElement> getElements(By locator) {
        List<WebElement> elements = WaitHelper.waitForElementsPresence(locator);
        logger.debug("Found {} elements for locator: {}", elements.size(), locator);
        return elements;
    }
    
    protected void waitForElementToDisappear(By locator) {
        WaitHelper.waitForElementToDisappear(locator);
        logger.debug("Element disappeared for locator: {}", locator);
    }
    
    protected void waitForTextInElement(WebElement element, String expectedText) {
        WaitHelper.waitForTextToBePresentInElement(element, expectedText);
        logger.debug("Text '{}' appeared in element: {}", expectedText, element);
    }
    
    // Utility methods
    protected By getLocator(WebElement element) {
        // For PageFactory elements, we'll use a different approach
        // Since we can't easily extract locator from PageFactory elements,
        // we'll work directly with the elements for most operations
        return null; // Placeholder - not used in current implementation
    }
    
    protected void scrollToElement(WebElement element) {
        // Platform-specific scrolling implementation
        logger.debug("Scrolling to element: {}", element);
        // Implementation depends on platform (Android/iOS)
    }
    
    protected void hideKeyboard() {
        try {
            if (driver instanceof io.appium.java_client.HidesKeyboard) {
                ((io.appium.java_client.HidesKeyboard) driver).hideKeyboard();
                logger.debug("Keyboard hidden");
            }
        } catch (Exception e) {
            logger.debug("No keyboard to hide or hide keyboard failed: {}", e.getMessage());
        }
    }
    
    // Abstract methods that subclasses must implement
    public abstract boolean isPageDisplayed();
    
    // Page-specific verification method
    public void verifyPageIsDisplayed() {
        if (!isPageDisplayed()) {
            throw new IllegalStateException(this.getClass().getSimpleName() + " is not displayed");
        }
        logger.info("{} is displayed", this.getClass().getSimpleName());
    }
}