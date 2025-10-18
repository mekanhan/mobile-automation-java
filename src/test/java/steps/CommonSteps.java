package steps;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mobile.automation.pages.base.BasePage;
import org.testng.Assert;

/**
 * Common Step Definitions
 * Contains atomic, reusable steps that work across all pages
 */
public class CommonSteps {

    private AppiumDriver driver;
    private BasePage currentPage;
    private TestContext testContext;

    /**
     * Constructor for dependency injection
     */
    public CommonSteps(TestContext testContext) {
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.currentPage = testContext.getCurrentPage();
    }

    // ==================== ACTION STEPS ====================

    /**
     * Click/Tap on element
     */
    @When("I click on {string}")
    @When("I tap {string}")
    @When("I tap on {string}")
    public void iTapOn(String elementName) {
        currentPage.click(elementName);
    }

    /**
     * Enter text into field
     */
    @When("I enter {string} into {string}")
    @When("I type {string} into {string}")
    public void iEnterTextInto(String text, String fieldName) {
        currentPage.enterText(fieldName, text);
    }

    /**
     * Clear and enter text
     */
    @When("I clear and enter {string} into {string}")
    public void iClearAndEnter(String text, String fieldName) {
        currentPage.enterText(fieldName, text);
    }

    /**
     * Wait for element to appear
     */
    @When("I wait for {string} to appear")
    @When("I wait for {string} to be visible")
    public void iWaitForElement(String elementName) {
        currentPage.waitForElement(elementName);
    }

    /**
     * Wait for element to disappear
     */
    @When("I wait for {string} to disappear")
    public void iWaitForElementToDisappear(String elementName) {
        currentPage.waitForElementToDisappear(elementName);
    }

    // ==================== SWIPE ACTIONS ====================

    /**
     * Swipe up
     */
    @When("I swipe up")
    public void iSwipeUp() {
        currentPage.swipeUp();
    }

    /**
     * Swipe down
     */
    @When("I swipe down")
    public void iSwipeDown() {
        currentPage.swipeDown();
    }

    /**
     * Swipe left
     */
    @When("I swipe left")
    public void iSwipeLeft() {
        currentPage.swipeLeft();
    }

    /**
     * Swipe right
     */
    @When("I swipe right")
    public void iSwipeRight() {
        currentPage.swipeRight();
    }

    /**
     * Scroll to element
     */
    @When("I scroll to {string}")
    public void iScrollTo(String elementName) {
        currentPage.scrollToElement(elementName);
    }

    // ==================== ASSERTION STEPS ====================

    /**
     * Verify element is visible
     */
    @Then("I should see {string}")
    @Then("{string} should be visible")
    @Then("{string} is visible")
    public void iShouldSee(String elementName) {
        Assert.assertTrue(currentPage.isElementVisible(elementName),
                "Element '" + elementName + "' is not visible");
    }

    /**
     * Verify element is not visible
     */
    @Then("I should not see {string}")
    @Then("{string} should not be visible")
    public void iShouldNotSee(String elementName) {
        Assert.assertFalse(currentPage.isElementVisible(elementName),
                "Element '" + elementName + "' is visible but should not be");
    }

    /**
     * Verify element is enabled
     */
    @Then("{string} should be enabled")
    public void elementShouldBeEnabled(String elementName) {
        Assert.assertTrue(currentPage.isElementEnabled(elementName),
                "Element '" + elementName + "' is not enabled");
    }

    /**
     * Verify element contains text
     */
    @Then("{string} should contain text {string}")
    @Then("{string} contains {string}")
    public void elementShouldContainText(String elementName, String expectedText) {
        Assert.assertTrue(currentPage.elementContainsText(elementName, expectedText),
                "Element '" + elementName + "' does not contain text: " + expectedText);
    }

    /**
     * Verify element text equals
     */
    @Then("{string} should have text {string}")
    @Then("{string} text is {string}")
    public void elementShouldHaveText(String elementName, String expectedText) {
        String actualText = currentPage.getText(elementName);
        Assert.assertEquals(actualText, expectedText,
                "Element '" + elementName + "' text mismatch");
    }

    /**
     * Verify page contains text
     */
    @Then("the page should contain {string}")
    public void pageShouldContainText(String text) {
        String pageSource = driver.getPageSource();
        Assert.assertTrue(pageSource.contains(text),
                "Page does not contain text: " + text);
    }

    // ==================== UTILITY STEPS ====================

    /**
     * Hide keyboard
     */
    @When("I hide the keyboard")
    public void iHideKeyboard() {
        currentPage.hideKeyboard();
    }

    /**
     * Take screenshot
     */
    @When("I take a screenshot")
    public void iTakeScreenshot() {
        // Screenshot logic would go here
        // This is typically handled by hooks
    }

    /**
     * Wait for seconds (use sparingly)
     */
    @When("I wait for {int} seconds")
    public void iWaitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
