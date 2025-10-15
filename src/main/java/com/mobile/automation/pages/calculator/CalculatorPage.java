package com.mobile.automation.pages.calculator;

import com.mobile.automation.core.base.BasePage;
import com.mobile.automation.core.wait.WaitHelper;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.WebElement;

public class CalculatorPage extends BasePage {
    
    // Cross-platform locators using @AndroidFindBy and @iOSXCUITFindBy
    @AndroidFindBy(id = "com.google.android.calculator:id/result_final")
    @iOSXCUITFindBy(accessibility = "result")
    private WebElement resultDisplay;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/result_preview")
    @iOSXCUITFindBy(accessibility = "preview")
    private WebElement previewDisplay;
    
    // Number buttons
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_0")
    @iOSXCUITFindBy(accessibility = "0")
    private WebElement button0;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_1")
    @iOSXCUITFindBy(accessibility = "1")
    private WebElement button1;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_2")
    @iOSXCUITFindBy(accessibility = "2")
    private WebElement button2;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_3")
    @iOSXCUITFindBy(accessibility = "3")
    private WebElement button3;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_4")
    @iOSXCUITFindBy(accessibility = "4")
    private WebElement button4;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_5")
    @iOSXCUITFindBy(accessibility = "5")
    private WebElement button5;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_6")
    @iOSXCUITFindBy(accessibility = "6")
    private WebElement button6;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_7")
    @iOSXCUITFindBy(accessibility = "7")
    private WebElement button7;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_8")
    @iOSXCUITFindBy(accessibility = "8")
    private WebElement button8;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/digit_9")
    @iOSXCUITFindBy(accessibility = "9")
    private WebElement button9;
    
    // Operation buttons
    @AndroidFindBy(id = "com.google.android.calculator:id/op_add")
    @iOSXCUITFindBy(accessibility = "+")
    private WebElement addButton;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/op_sub")
    @iOSXCUITFindBy(accessibility = "−")
    private WebElement subtractButton;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/op_mul")
    @iOSXCUITFindBy(accessibility = "×")
    private WebElement multiplyButton;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/op_div")
    @iOSXCUITFindBy(accessibility = "÷")
    private WebElement divideButton;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/eq")
    @iOSXCUITFindBy(accessibility = "=")
    private WebElement equalsButton;
    
    // Clear buttons
    @AndroidFindBy(id = "com.google.android.calculator:id/clr")
    @iOSXCUITFindBy(accessibility = "Clear")
    private WebElement clearButton;
    
    @AndroidFindBy(id = "com.google.android.calculator:id/del")
    @iOSXCUITFindBy(accessibility = "Delete")
    private WebElement deleteButton;
    
    @Override
    public boolean isPageDisplayed() {
        try {
            return isElementDisplayed(resultDisplay);
        } catch (Exception e) {
            logger.warn("Calculator page not displayed: {}", e.getMessage());
            return false;
        }
    }
    
    // Number input methods
    public CalculatorPage clickNumber(int number) {
        logAction("Clicking number: " + number);
        
        WebElement numberButton = getNumberButton(number);
        click(numberButton);
        
        return this;
    }
    
    private WebElement getNumberButton(int number) {
        switch (number) {
            case 0: return button0;
            case 1: return button1;
            case 2: return button2;
            case 3: return button3;
            case 4: return button4;
            case 5: return button5;
            case 6: return button6;
            case 7: return button7;
            case 8: return button8;
            case 9: return button9;
            default: throw new IllegalArgumentException("Invalid number: " + number + ". Must be 0-9.");
        }
    }
    
    public CalculatorPage enterNumber(String number) {
        logAction("Entering number: " + number);
        
        for (char digit : number.toCharArray()) {
            if (Character.isDigit(digit)) {
                clickNumber(Character.getNumericValue(digit));
            } else {
                throw new IllegalArgumentException("Invalid character in number: " + digit);
            }
        }
        
        return this;
    }
    
    // Operation methods
    public CalculatorPage clickAdd() {
        logAction("Clicking add button");
        click(addButton);
        return this;
    }
    
    public CalculatorPage clickSubtract() {
        logAction("Clicking subtract button");
        click(subtractButton);
        return this;
    }
    
    public CalculatorPage clickMultiply() {
        logAction("Clicking multiply button");
        click(multiplyButton);
        return this;
    }
    
    public CalculatorPage clickDivide() {
        logAction("Clicking divide button");
        click(divideButton);
        return this;
    }
    
    public CalculatorPage clickEquals() {
        logAction("Clicking equals button");
        click(equalsButton);
        return this;
    }
    
    // Clear methods
    public CalculatorPage clickClear() {
        logAction("Clicking clear button");
        click(clearButton);
        return this;
    }
    
    public CalculatorPage clickDelete() {
        logAction("Clicking delete button");
        click(deleteButton);
        return this;
    }
    
    // Result methods
    public String getResult() {
        String result = getText(resultDisplay).trim();
        logAction("Getting result: " + result);
        return result;
    }
    
    public String getPreview() {
        String preview = getText(previewDisplay).trim();
        logAction("Getting preview: " + preview);
        return preview;
    }
    
    public void waitForResult(String expectedResult) {
        logAction("Waiting for result: " + expectedResult);
        WaitHelper.waitForCalculatorResult(resultDisplay, expectedResult);
    }
    
    public boolean verifyResult(String expectedResult) {
        try {
            waitForResult(expectedResult);
            String actualResult = getResult();
            boolean matches = actualResult.equals(expectedResult) || 
                             actualResult.equals(expectedResult + ".0") ||
                             actualResult.equals(expectedResult + ".00");
            
            logAction(String.format("Result verification - Expected: %s, Actual: %s, Match: %s", 
                     expectedResult, actualResult, matches));
            
            return matches;
        } catch (Exception e) {
            logger.error("Failed to verify result: {}", expectedResult, e);
            return false;
        }
    }
    
    // High-level calculation methods
    public CalculatorPage performAddition(String firstNumber, String secondNumber) {
        logAction(String.format("Performing addition: %s + %s", firstNumber, secondNumber));
        
        clickClear();
        enterNumber(firstNumber);
        clickAdd();
        enterNumber(secondNumber);
        clickEquals();
        
        return this;
    }
    
    public CalculatorPage performSubtraction(String firstNumber, String secondNumber) {
        logAction(String.format("Performing subtraction: %s - %s", firstNumber, secondNumber));
        
        clickClear();
        enterNumber(firstNumber);
        clickSubtract();
        enterNumber(secondNumber);
        clickEquals();
        
        return this;
    }
    
    public CalculatorPage performMultiplication(String firstNumber, String secondNumber) {
        logAction(String.format("Performing multiplication: %s × %s", firstNumber, secondNumber));
        
        clickClear();
        enterNumber(firstNumber);
        clickMultiply();
        enterNumber(secondNumber);
        clickEquals();
        
        return this;
    }
    
    public CalculatorPage performDivision(String firstNumber, String secondNumber) {
        logAction(String.format("Performing division: %s ÷ %s", firstNumber, secondNumber));
        
        if ("0".equals(secondNumber)) {
            throw new IllegalArgumentException("Division by zero is not allowed");
        }
        
        clickClear();
        enterNumber(firstNumber);
        clickDivide();
        enterNumber(secondNumber);
        clickEquals();
        
        return this;
    }
    
    private void logAction(String action) {
        logger.info("Calculator action: {}", action);
    }
}