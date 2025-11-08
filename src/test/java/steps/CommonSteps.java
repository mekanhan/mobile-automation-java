package steps;

import io.appium.java_client.AppiumDriver;
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
     * Element name can be: constant (HEADER_TODAY), friendly name (Today Header), or direct locator
     */
    @When("I click on {string}")
    @When("I tap {string}")
    @When("I tap on {string}")
    public void iTapOn(String elementName) {
        currentPage.click(elementName);
    }

    /**
     * Enter text into field
     * Field name can be: constant (SEARCH_FIELD), friendly name (Search Field), or direct locator
     */
    @When("I enter {string} into {string}")
    @When("I type {string} into {string}")
    public void iEnterTextInto(String text, String fieldName) {
        currentPage.enterText(fieldName, text);
    }

    /**
     * Clear and enter text
     * Field name can be: constant (SEARCH_FIELD), friendly name (Search Field), or direct locator
     */
    @When("I clear and enter {string} into {string}")
    public void iClearAndEnter(String text, String fieldName) {
        currentPage.enterText(fieldName, text);
    }

    /**
     * Wait for element to appear
     * Element name can be: constant (TAB_PLACES), friendly name (Places Tab), or direct locator
     */
    @When("I wait for {string} to appear")
    @When("I wait for {string} to be visible")
    public void iWaitForElement(String elementName) {
        currentPage.waitForElement(elementName);
    }

    /**
     * Wait for element to disappear
     * Element name can be: constant (TAB_PLACES), friendly name (Places Tab), or direct locator
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
     * Element name can be: constant (HEADER_TOP_READ), friendly name (Top Read), or direct locator
     */
    @When("I scroll to {string}")
    public void iScrollTo(String elementName) {
        currentPage.scrollToElement(elementName);
    }

    // ==================== ASSERTION STEPS ====================

    /**
     * Verify element is visible
     * Element name can be: constant (HEADER_TODAY), friendly name (Today Header), or direct locator
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
     * Element name can be: constant, friendly name, or direct locator
     */
    @Then("I should not see {string}")
    @Then("{string} should not be visible")
    public void iShouldNotSee(String elementName) {
        Assert.assertFalse(currentPage.isElementVisible(elementName),
                "Element '" + elementName + "' is visible but should not be");
    }

    /**
     * Verify element is enabled
     * Element name can be: constant, friendly name, or direct locator
     */
    @Then("{string} should be enabled")
    public void elementShouldBeEnabled(String elementName) {
        Assert.assertTrue(currentPage.isElementEnabled(elementName),
                "Element '" + elementName + "' is not enabled");
    }

    /**
     * Verify element contains text
     * Element name can be: constant, friendly name, or direct locator
     */
    @Then("{string} should contain text {string}")
    @Then("{string} contains {string}")
    public void elementShouldContainText(String elementName, String expectedText) {
        Assert.assertTrue(currentPage.elementContainsText(elementName, expectedText),
                "Element '" + elementName + "' does not contain text: " + expectedText);
    }

    /**
     * Verify element text equals
     * Element name can be: constant, friendly name, or direct locator
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
     * Take screenshot manually during test execution
     * Screenshots are automatically saved to target/screenshots directory
     * and attached to Allure report if Allure is enabled
     */
    @When("I take a screenshot")
    @When("I capture a screenshot")
    public void iTakeScreenshot() {
        if (driver != null) {
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver)
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);

                String timestamp = String.valueOf(System.currentTimeMillis());
                String fileName = "manual_screenshot_" + timestamp;

                // Save to file system
                saveScreenshotToFile(screenshot, fileName);

                // Try to attach to Allure report if available
                try {
                    io.qameta.allure.Allure.getLifecycle().addAttachment(
                        fileName,
                        "image/png",
                        "png",
                        screenshot
                    );
                    System.out.println("✅ Screenshot attached to Allure report: " + fileName);
                } catch (NoClassDefFoundError e) {
                    System.out.println("✅ Screenshot saved (Allure not available): " + fileName);
                }
            } catch (Exception e) {
                System.err.println("❌ Failed to capture screenshot: " + e.getMessage());
            }
        } else {
            System.err.println("❌ Cannot take screenshot: driver is null");
        }
    }

    /**
     * Save screenshot to file system with compression
     */
    private void saveScreenshotToFile(byte[] screenshot, String name) {
        try {
            java.nio.file.Path screenshotsDir = java.nio.file.Paths.get("target/screenshots");
            if (!java.nio.file.Files.exists(screenshotsDir)) {
                java.nio.file.Files.createDirectories(screenshotsDir);
            }

            // Compress screenshot
            byte[] compressedScreenshot = compressScreenshot(screenshot);

            java.nio.file.Path screenshotPath = screenshotsDir.resolve(name + ".jpg");
            java.nio.file.Files.write(screenshotPath, compressedScreenshot);

            // Calculate size reduction
            long originalSize = screenshot.length;
            long compressedSize = compressedScreenshot.length;
            int reduction = (int) ((1 - (double) compressedSize / originalSize) * 100);

            System.out.println("📁 Screenshot saved to: " + screenshotPath);
            System.out.println("📊 Size: " + formatBytes(compressedSize) + " (reduced by " + reduction + "%)");
        } catch (Exception e) {
            System.err.println("❌ Failed to save screenshot to file: " + e.getMessage());
        }
    }

    /**
     * Compress screenshot to reduce file size
     */
    private byte[] compressScreenshot(byte[] originalScreenshot) {
        try {
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(originalScreenshot);
            java.awt.image.BufferedImage image = javax.imageio.ImageIO.read(bais);

            // Get quality from system property (default: 0.7 = 70%)
            float quality = Float.parseFloat(System.getProperty("screenshot.quality", "0.7"));

            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageWriter writer = javax.imageio.ImageIO.getImageWritersByFormatName("jpg").next();
            javax.imageio.stream.ImageOutputStream ios = javax.imageio.ImageIO.createImageOutputStream(baos);
            writer.setOutput(ios);

            javax.imageio.ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(javax.imageio.ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }

            writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
            writer.dispose();
            ios.close();

            return baos.toByteArray();
        } catch (Exception e) {
            System.err.println("⚠️  Failed to compress screenshot: " + e.getMessage());
            return originalScreenshot;
        }
    }

    /**
     * Format bytes to human-readable size
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
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
