package mobile.automation.pages.base;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

/**
 * Base Page Object class containing common actions and utilities
 * All page objects should extend this class
 */
public abstract class BasePage {

    protected AppiumDriver driver;
    protected WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 10;

    // ============================================
    // CONSTRUCTOR
    // ============================================
    
    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        waitFor(5); // Wait 5 seconds for page to load
    }

    // ============================================
    // TAP / CLICK ACTIONS
    // ============================================
    
    /**
     * Taps on the given WebElement
     * @param element The element to tap
     */
    protected void tap(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }
    
    /**
     * Taps on an element using its accessibility ID
     * @param accessibilityId The accessibility ID of the element
     */
    protected void tapByAccessibilityId(String accessibilityId) {
        WebElement element = driver.findElement(AppiumBy.accessibilityId(accessibilityId));
        tap(element);
    }

    /**
     * Taps on an element using XPath
     * @param xpath The XPath of the element
     */
    protected void tapByXPath(String xpath) {
        WebElement element = driver.findElement(AppiumBy.xpath(xpath));
        tap(element);
    }

    // ============================================
    // TEXT INPUT ACTIONS
    // ============================================
    
    /**
     * Sends text to the given element
     * @param element The element to send text to
     * @param text The text to send
     */
    protected void sendKeys(WebElement element, String text) {
        waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Sends text to an element using its accessibility ID
     * @param accessibilityId The accessibility ID of the element
     * @param text The text to send
     */
    protected void sendKeysByAccessibilityId(String accessibilityId, String text) {
        WebElement element = driver.findElement(AppiumBy.accessibilityId(accessibilityId));
        sendKeys(element, text);
    }
    
    /**
     * Sets the value of an element (useful for iOS)
     * @param element The element to set value for
     * @param value The value to set
     */
    protected void setValue(WebElement element, String value) {
        waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(value);
    }

    // ============================================
    // GET TEXT / ATTRIBUTE ACTIONS
    // ============================================
    
    /**
     * Gets the text of the given element
     * @param element The element to get text from
     * @return The text of the element
     */
    protected String getText(WebElement element) {
        waitForElementToBeVisible(element);
        return element.getText();
    }
    
    /**
     * Gets the text using accessibility ID
     * @param accessibilityId The accessibility ID of the element
     * @return The text of the element
     */
    protected String getTextByAccessibilityId(String accessibilityId) {
        WebElement element = driver.findElement(AppiumBy.accessibilityId(accessibilityId));
        return getText(element);
    }
    
    /**
     * Gets an attribute value from the element
     * @param element The element to get attribute from
     * @param attribute The attribute name (e.g., "value", "name", "label")
     * @return The attribute value
     */
    protected String getAttribute(WebElement element, String attribute) {
        waitForElementToBeVisible(element);
        return element.getAttribute(attribute);
    }

    // ============================================
    // VISIBILITY / VERIFICATION ACTIONS
    // ============================================
    
    /**
     * Checks if an element is displayed
     * @param element The element to check
     * @return true if the element is displayed, false otherwise
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            waitForElementToBeVisible(element);
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if an element is displayed using accessibility ID
     * @param accessibilityId The accessibility ID of the element
     * @return true if the element is displayed, false otherwise
     */
    protected boolean isElementDisplayedByAccessibilityId(String accessibilityId) {
        try {
            WebElement element = driver.findElement(AppiumBy.accessibilityId(accessibilityId));
            return isElementDisplayed(element);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Checks if an element is enabled
     * @param element The element to check
     * @return true if the element is enabled, false otherwise
     */
    protected boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ============================================
    // WAIT ACTIONS
    // ============================================
    
    /**
     * Waits for an element to be visible
     * @param element The element to wait for
     */
    protected void waitForElementToBeVisible(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Waits for an element to be clickable
     * @param element The element to wait for
     */
    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
    
    /**
     * Waits for a specific duration
     * @param seconds The number of seconds to wait
     */
    protected void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ============================================
    // SCROLL ACTIONS
    // ============================================
    
    /**
     * Scrolls down on the screen
     */
    protected void scrollDown() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8);
        int endY = (int) (size.height * 0.2);

        swipe(startX, startY, startX, endY, 1000);
    }

    /**
     * Scrolls up on the screen
     */
    protected void scrollUp() {
        Dimension size = driver.manage().window().getSize();
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.2);
        int endY = (int) (size.height * 0.8);

        swipe(startX, startY, startX, endY, 1000);
    }

    /**
     * Swipes up on the screen (public alias for scrollUp)
     */
    public void swipeUp() {
        scrollUp();
    }

    /**
     * Swipes down on the screen (public alias for scrollDown)
     */
    public void swipeDown() {
        scrollDown();
    }
    
    /**
     * Scrolls to an element using accessibility ID (iOS specific)
     * @param elementName The friendly name or accessibility ID of the element
     */
    public void scrollToElement(String elementName) {
        // Convert friendly name to accessibility ID if needed
        String accessibilityId = mapFriendlyNameToAccessibilityId(elementName);

        // Check if element is already visible
        if (isElementDisplayedByAccessibilityId(accessibilityId)) {
            System.out.println("Element '" + elementName + "' is already visible, no scroll needed");
            return;
        }

        // Scroll until element is found
        int maxScrolls = 10;
        for (int i = 0; i < maxScrolls; i++) {
            try {
                // Try to find the element
                if (isElementDisplayedByAccessibilityId(accessibilityId)) {
                    System.out.println("Element '" + elementName + "' found after " + i + " scrolls");
                    return;
                }
            } catch (Exception e) {
                // Element not found, continue scrolling
            }

            // Swipe up to scroll down the page
            swipeUp();

            try {
                Thread.sleep(500); // Small delay for scroll animation
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        throw new RuntimeException("Element '" + elementName + "' not found after " + maxScrolls + " scroll attempts");
    }

    /**
     * Map friendly element names to actual accessibility IDs
     * Override this in page objects for page-specific mappings
     */
    protected String mapFriendlyNameToAccessibilityId(String friendlyName) {
        // Common mappings
        switch (friendlyName) {
            case "Today Header":
                return "Today";
            case "Featured Article":
                return "Featured article";
            case "Search Field":
                return "Search Wikipedia";
            case "Tabs Button":
                return "Tabs";
            case "Profile Button":
                return "profile-button";
            default:
                // Return as-is if no mapping found
                return friendlyName;
        }
    }
    
    /**
     * Swipes left on the screen
     */
    public void swipeLeft() {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.8);
        int endX = (int) (size.width * 0.2);
        int y = size.height / 2;

        swipe(startX, y, endX, y, 500);
    }

    /**
     * Swipes right on the screen
     */
    public void swipeRight() {
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * 0.2);
        int endX = (int) (size.width * 0.8);
        int y = size.height / 2;

        swipe(startX, y, endX, y, 500);
    }

    /**
     * Generic swipe method using W3C Actions (Appium 2.x compatible)
     */
    private void swipe(int startX, int startY, int endX, int endY, int duration) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipeSeq = new Sequence(finger, 1);

        swipeSeq.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
        swipeSeq.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipeSeq.addAction(finger.createPointerMove(Duration.ofMillis(duration), PointerInput.Origin.viewport(), endX, endY));
        swipeSeq.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipeSeq));
    }

    // ============================================
    // HIDE KEYBOARD ACTION
    // ============================================
    
    /**
     * Hides the keyboard if it's displayed
     */
    public void hideKeyboard() {
        try {
            driver.executeScript("mobile: hideKeyboard");
        } catch (Exception e) {
            // Keyboard not present or already hidden
        }
    }

    // ============================================
    // GET DRIVER
    // ============================================
    
    /**
     * Gets the driver instance
     * @return The AppiumDriver instance
     */
    public AppiumDriver getDriver() {
        return driver;
    }

    // ============================================
    // LEGACY/COMPATIBILITY METHODS
    // (For backward compatibility with step definitions)
    // ============================================

    /**
     * Click on element by name (legacy method for step definitions)
     * @param elementName The friendly name of the element
     */
    public void click(String elementName) {
        tapByAccessibilityId(elementName);
    }

    /**
     * Enter text into field by name (legacy method for step definitions)
     * @param elementName The friendly name of the element
     * @param text The text to enter
     */
    public void enterText(String elementName, String text) {
        sendKeysByAccessibilityId(elementName, text);
    }

    /**
     * Get text from element by name (legacy method for step definitions)
     * @param elementName The friendly name of the element
     * @return The text of the element
     */
    public String getText(String elementName) {
        return getTextByAccessibilityId(elementName);
    }

    /**
     * Check if element is visible by name (legacy method for step definitions)
     * @param elementName The friendly name of the element
     * @return true if visible, false otherwise
     */
    public boolean isElementVisible(String elementName) {
        return isElementDisplayedByAccessibilityId(elementName);
    }

    /**
     * Check if element is enabled by name (legacy method for step definitions)
     * @param elementName The friendly name of the element
     * @return true if enabled, false otherwise
     */
    public boolean isElementEnabled(String elementName) {
        try {
            waitForElementToBeVisible(driver.findElement(io.appium.java_client.AppiumBy.accessibilityId(elementName)));
            return driver.findElement(io.appium.java_client.AppiumBy.accessibilityId(elementName)).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for element to appear by name (legacy method for step definitions)
     * @param elementName The friendly name of the element
     */
    public void waitForElement(String elementName) {
        waitForElementToBeVisible(driver.findElement(io.appium.java_client.AppiumBy.accessibilityId(elementName)));
    }

    /**
     * Wait for element to disappear by name (legacy method for step definitions)
     * @param elementName The friendly name of the element
     */
    public void waitForElementToDisappear(String elementName) {
        try {
            org.openqa.selenium.support.ui.WebDriverWait wait = new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(30));
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated(
                io.appium.java_client.AppiumBy.accessibilityId(elementName)));
        } catch (Exception e) {
            // Element already invisible
        }
    }

    /**
     * Check if element contains text
     * @param elementName The friendly name of the element
     * @param expectedText The text to check for
     * @return true if contains text, false otherwise
     */
    public boolean elementContainsText(String elementName, String expectedText) {
        try {
            String actualText = getTextByAccessibilityId(elementName);
            return actualText != null && actualText.contains(expectedText);
        } catch (Exception e) {
            return false;
        }
    }
}