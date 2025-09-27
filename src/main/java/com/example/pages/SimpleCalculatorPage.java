package com.example.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

/**
 * Simple Calculator Page Object for Android Calculator App
 * Uses basic Appium interactions without complex inheritance
 */
public class SimpleCalculatorPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCalculatorPage.class);
    private final AppiumDriver driver;
    private final WebDriverWait wait;
    
    public SimpleCalculatorPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    // Element locators
    private WebElement getDigitButton(int digit) {
        String locator = "com.google.android.calculator:id/digit_" + digit;
        return wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.id(locator)));
    }
    
    private WebElement getAddButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("com.google.android.calculator:id/op_add")));
    }
    
    private WebElement getEqualsButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("com.google.android.calculator:id/eq")));
    }
    
    private WebElement getClearButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("com.google.android.calculator:id/clr")));
    }
    
    private WebElement getSubtractButton() {
        return wait.until(ExpectedConditions.elementToBeClickable(
            AppiumBy.id("com.google.android.calculator:id/op_sub")));
    }
    
    private WebElement getResultDisplay() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(
            AppiumBy.id("com.google.android.calculator:id/result_final")));
    }
    
    // Action methods
    public void clickDigit(int digit) {
        getDigitButton(digit).click();
        LOGGER.info("Clicked digit: {}", digit);
    }
    
    public void clickAdd() {
        getAddButton().click();
        LOGGER.info("Clicked add button");
    }
    
    public void clickSubtract() {
        getSubtractButton().click();
        LOGGER.info("Clicked subtract button");
    }
    
    public void clickEquals() {
        getEqualsButton().click();
        LOGGER.info("Clicked equals button");
    }
    
    public void clickClear() {
        getClearButton().click();
        LOGGER.info("Clicked clear button");
    }
    
    public String getResult() {
        String result = getResultDisplay().getText();
        LOGGER.info("Got result: {}", result);
        return result;
    }
    
    // High-level operations
    public void performAddition(int num1, int num2) {
        LOGGER.info("Performing addition: {} + {}", num1, num2);
        clickClear(); // Ensure clean state
        clickDigit(num1);
        clickAdd();
        clickDigit(num2);
        clickEquals();
    }
    
    public void performSubtraction(int num1, int num2) {
        LOGGER.info("Performing subtraction: {} - {}", num1, num2);
        clickClear(); // Ensure clean state
        clickDigit(num1);
        clickSubtract();
        clickDigit(num2);
        clickEquals();
    }
    
    public void enterNumber(int number) {
        String numberStr = String.valueOf(number);
        LOGGER.info("Entering number: {}", number);
        
        for (char digit : numberStr.toCharArray()) {
            int digitValue = Character.getNumericValue(digit);
            clickDigit(digitValue);
        }
    }
    
    public boolean isCalculatorReady() {
        try {
            getDigitButton(1);
            return true;
        } catch (Exception e) {
            LOGGER.warn("Calculator not ready: {}", e.getMessage());
            return false;
        }
    }
}