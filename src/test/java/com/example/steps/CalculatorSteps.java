package com.example.steps;

import com.example.config.CapabilitiesFactory;
import com.example.core.SimpleDriverManager;
import com.example.pages.SimpleCalculatorPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * Step definitions for Calculator app automation
 */
public class CalculatorSteps {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatorSteps.class);
    private SimpleCalculatorPage calculatorPage;
    
    @Given("I launch the Android Calculator app")
    public void iLaunchTheAndroidCalculatorApp() {
        LOGGER.info("Launching Android Calculator app");
        DesiredCapabilities capabilities = CapabilitiesFactory.createCalculatorCapabilitiesForDemo();
        SimpleDriverManager.initializeDriver("android", capabilities);
        calculatorPage = new SimpleCalculatorPage(SimpleDriverManager.getDriver());
    }
    
    @And("the calculator is ready for input")
    public void theCalculatorIsReadyForInput() {
        LOGGER.info("Verifying calculator is ready");
        Assert.assertTrue(calculatorPage.isCalculatorReady(), 
            "Calculator should be ready for input");
    }
    
    @When("I perform addition of {int} plus {int}")
    public void iPerformAdditionOfPlus(int num1, int num2) {
        LOGGER.info("Performing addition: {} + {}", num1, num2);
        calculatorPage.performAddition(num1, num2);
    }
    
    @When("I perform subtraction of {int} minus {int}")
    public void iPerformSubtractionOfMinus(int num1, int num2) {
        LOGGER.info("Performing subtraction: {} - {}", num1, num2);
        calculatorPage.performSubtraction(num1, num2);
    }
    
    @When("I perform {string} of {int} and {int}")
    public void iPerformOperationOfNumbers(String operation, int num1, int num2) {
        LOGGER.info("Performing {} of {} and {}", operation, num1, num2);
        
        switch (operation.toLowerCase()) {
            case "addition":
                calculatorPage.performAddition(num1, num2);
                break;
            case "subtraction":
                calculatorPage.performSubtraction(num1, num2);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }
    }
    
    @When("I enter number {int}")
    public void iEnterNumber(int number) {
        LOGGER.info("Entering number: {}", number);
        calculatorPage.enterNumber(number);
    }
    
    @When("I click digit {int}")
    public void iClickDigit(int digit) {
        LOGGER.info("Clicking digit: {}", digit);
        calculatorPage.clickDigit(digit);
    }
    
    @When("I click add button")
    public void iClickAddButton() {
        LOGGER.info("Clicking add button");
        calculatorPage.clickAdd();
    }
    
    @When("I click subtract button")
    public void iClickSubtractButton() {
        LOGGER.info("Clicking subtract button");
        calculatorPage.clickSubtract();
    }
    
    @When("I click equals button")
    public void iClickEqualsButton() {
        LOGGER.info("Clicking equals button");
        calculatorPage.clickEquals();
    }
    
    @When("I click clear button")
    public void iClickClearButton() {
        LOGGER.info("Clicking clear button");
        calculatorPage.clickClear();
    }
    
    @Then("the result should be {int}")
    public void theResultShouldBe(int expectedResult) {
        LOGGER.info("Verifying result should be: {}", expectedResult);
        
        // Wait a moment for result to display
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String actualResult = calculatorPage.getResult();
        LOGGER.info("Actual result: {}", actualResult);
        
        // Handle different result formats (some calculators show decimal places)
        String expectedStr = String.valueOf(expectedResult);
        Assert.assertTrue(
            actualResult.equals(expectedStr) || actualResult.equals(expectedStr + ".0") || 
            actualResult.contains(expectedStr),
            String.format("Expected result to be %d, but was: %s", expectedResult, actualResult)
        );
    }
    
    @And("the calculator display shows {string}")
    public void theCalculatorDisplayShows(String expectedDisplay) {
        LOGGER.info("Verifying display shows: {}", expectedDisplay);
        
        String actualDisplay = calculatorPage.getResult();
        LOGGER.info("Actual display: {}", actualDisplay);
        
        Assert.assertTrue(
            actualDisplay.contains(expectedDisplay) || actualDisplay.equals(expectedDisplay),
            String.format("Expected display to show '%s', but was: '%s'", expectedDisplay, actualDisplay)
        );
    }
    
}