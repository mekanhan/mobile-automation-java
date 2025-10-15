package com.mobile.automation.steps.calculator;

import com.mobile.automation.core.base.BaseSteps;
import com.mobile.automation.pages.calculator.CalculatorPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class CalculatorSteps extends BaseSteps {
    
    private CalculatorPage calculatorPage;
    
    public CalculatorSteps() {
        super();
        this.calculatorPage = new CalculatorPage();
    }
    
    @Given("the calculator app is open")
    public void theCalculatorAppIsOpen() {
        logStep("Verifying calculator app is open");
        calculatorPage.verifyPageIsDisplayed();
    }
    
    @Given("I launch the Android Calculator app")
    public void iLaunchTheAndroidCalculatorApp() {
        logStep("Launching Android Calculator app - handled by hooks");
        // This is now handled by the mobile test hooks
        calculatorPage.verifyPageIsDisplayed();
    }
    
    @Given("the calculator is ready for input")
    public void theCalculatorIsReadyForInput() {
        logStep("Verifying calculator is ready for input");
        calculatorPage.verifyPageIsDisplayed();
    }
    
    @Given("the calculator is cleared")
    public void theCalculatorIsCleared() {
        logStep("Clearing calculator");
        calculatorPage.clickClear();
    }
    
    @When("I enter the number {string}")
    public void iEnterTheNumber(String number) {
        logStep("Entering number: {}", number);
        calculatorPage.enterNumber(number);
    }
    
    @When("I click the {string} button")
    public void iClickTheButton(String button) {
        logStep("Clicking button: {}", button);
        
        switch (button.toLowerCase()) {
            case "add":
            case "+":
                calculatorPage.clickAdd();
                break;
            case "subtract":
            case "-":
                calculatorPage.clickSubtract();
                break;
            case "multiply":
            case "×":
            case "*":
                calculatorPage.clickMultiply();
                break;
            case "divide":
            case "÷":
            case "/":
                calculatorPage.clickDivide();
                break;
            case "equals":
            case "=":
                calculatorPage.clickEquals();
                break;
            case "clear":
            case "clr":
                calculatorPage.clickClear();
                break;
            case "delete":
            case "del":
                calculatorPage.clickDelete();
                break;
            default:
                throw new IllegalArgumentException("Unknown button: " + button);
        }
    }
    
    @When("I perform addition of {string} and {string}")
    public void iPerformAdditionOfAnd(String firstNumber, String secondNumber) {
        logStep("Performing addition: {} + {}", firstNumber, secondNumber);
        calculatorPage.performAddition(firstNumber, secondNumber);
    }
    
    @When("I perform subtraction of {string} and {string}")
    public void iPerformSubtractionOfAnd(String firstNumber, String secondNumber) {
        logStep("Performing subtraction: {} - {}", firstNumber, secondNumber);
        calculatorPage.performSubtraction(firstNumber, secondNumber);
    }
    
    @When("I perform multiplication of {string} and {string}")
    public void iPerformMultiplicationOfAnd(String firstNumber, String secondNumber) {
        logStep("Performing multiplication: {} × {}", firstNumber, secondNumber);
        calculatorPage.performMultiplication(firstNumber, secondNumber);
    }
    
    @When("I perform division of {string} and {string}")
    public void iPerformDivisionOfAnd(String firstNumber, String secondNumber) {
        logStep("Performing division: {} ÷ {}", firstNumber, secondNumber);
        calculatorPage.performDivision(firstNumber, secondNumber);
    }
    
    @Then("the result should be {string}")
    public void theResultShouldBe(String expectedResult) {
        logStep("Verifying result should be: {}", expectedResult);
        
        boolean resultMatches = calculatorPage.verifyResult(expectedResult);
        String actualResult = calculatorPage.getResult();
        
        Assert.assertTrue(resultMatches, 
            String.format("Expected result: %s, but got: %s", expectedResult, actualResult));
        
        logger.info("Result verification passed - Expected: {}, Actual: {}", expectedResult, actualResult);
    }
    
    @Then("the result should contain {string}")
    public void theResultShouldContain(String partialResult) {
        logStep("Verifying result contains: {}", partialResult);
        
        String actualResult = calculatorPage.getResult();
        boolean contains = actualResult.contains(partialResult);
        
        Assert.assertTrue(contains, 
            String.format("Expected result to contain: %s, but got: %s", partialResult, actualResult));
        
        logger.info("Result contains verification passed - Expected contains: {}, Actual: {}", 
                   partialResult, actualResult);
    }
    
    @Then("the calculator display should be clear")
    public void theCalculatorDisplayShouldBeClear() {
        logStep("Verifying calculator display is clear");
        
        String result = calculatorPage.getResult();
        boolean isClear = result.isEmpty() || "0".equals(result);
        
        Assert.assertTrue(isClear, 
            String.format("Expected calculator to be clear, but display shows: %s", result));
        
        logger.info("Calculator display clear verification passed");
    }
    
    @Then("I should be able to see the calculator interface")
    public void iShouldBeAbleToSeeTheCalculatorInterface() {
        logStep("Verifying calculator interface is visible");
        
        boolean isDisplayed = calculatorPage.isPageDisplayed();
        Assert.assertTrue(isDisplayed, "Calculator interface should be visible");
        
        logger.info("Calculator interface visibility verification passed");
    }
    
    // Additional step definitions for complex scenarios
    @When("I perform the calculation {string}")
    public void iPerformTheCalculation(String calculation) {
        logStep("Performing calculation: {}", calculation);
        
        // Parse and execute the calculation
        // This is a simplified implementation - could be enhanced for complex expressions
        calculatorPage.clickClear();
        
        // Example: "5+3" -> enter 5, click +, enter 3, click =
        String[] parts = calculation.split("(?=[+\\-×÷])|(?<=[+\\-×÷])");
        
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            
            if (part.matches("\\d+")) {
                // It's a number
                calculatorPage.enterNumber(part);
            } else {
                // It's an operation
                iClickTheButton(part);
            }
        }
        
        // Click equals at the end
        calculatorPage.clickEquals();
    }
    
    // Legacy step definitions for backward compatibility
    @When("I perform addition of {int} plus {int}")
    public void iPerformAdditionOfPlus(int num1, int num2) {
        logStep("Performing addition: {} + {}", num1, num2);
        calculatorPage.performAddition(String.valueOf(num1), String.valueOf(num2));
    }
    
    @When("I enter number {int}")
    public void iEnterNumber(int number) {
        logStep("Entering number: {}", number);
        calculatorPage.enterNumber(String.valueOf(number));
    }
    
    @When("I click digit {int}")
    public void iClickDigit(int digit) {
        logStep("Clicking digit: {}", digit);
        calculatorPage.clickNumber(digit);
    }
    
    @When("I click add button")
    public void iClickAddButton() {
        logStep("Clicking add button");
        calculatorPage.clickAdd();
    }
    
    @When("I click subtract button") 
    public void iClickSubtractButton() {
        logStep("Clicking subtract button");
        calculatorPage.clickSubtract();
    }
    
    @When("I click equals button")
    public void iClickEqualsButton() {
        logStep("Clicking equals button");
        calculatorPage.clickEquals();
    }
    
    @When("I click clear button")
    public void iClickClearButton() {
        logStep("Clicking clear button");
        calculatorPage.clickClear();
    }
    
    @Then("the result should be {int}")
    public void theResultShouldBe(int expectedResult) {
        logStep("Verifying result should be: {}", expectedResult);
        
        boolean resultMatches = calculatorPage.verifyResult(String.valueOf(expectedResult));
        String actualResult = calculatorPage.getResult();
        
        Assert.assertTrue(resultMatches, 
            String.format("Expected result: %d, but got: %s", expectedResult, actualResult));
        
        logger.info("Result verification passed - Expected: {}, Actual: {}", expectedResult, actualResult);
    }
    
    @Then("the calculator display shows {string}")
    public void theCalculatorDisplayShows(String expectedDisplay) {
        logStep("Verifying display shows: {}", expectedDisplay);
        
        String actualResult = calculatorPage.getResult();
        boolean contains = actualResult.contains(expectedDisplay) || actualResult.equals(expectedDisplay);
        
        Assert.assertTrue(contains, 
            String.format("Expected display to show '%s', but got: '%s'", expectedDisplay, actualResult));
        
        logger.info("Display verification passed - Expected contains: {}, Actual: {}", 
                   expectedDisplay, actualResult);
    }
}