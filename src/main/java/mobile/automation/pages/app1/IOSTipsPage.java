package mobile.automation.pages.app1;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import mobile.automation.pages.base.BasePage;
import org.openqa.selenium.WebElement;

/**
 * iOS Wikipedia Tips/Onboarding Page Object
 * This page appears on first launch when the app is freshly installed
 * Shows 4 pages of introduction content with Skip/Next buttons
 */
public class IOSTipsPage extends BasePage {

    /**
     * Constructor
     */
    public IOSTipsPage(AppiumDriver driver) {
        super(driver);
    }

    /**
     * Check if currently on Tips/Onboarding page
     * Uses multiple indicators to ensure we're on the tips page
     *
     * @return true if tips page is visible
     */
    public boolean isOnTipsPage() {
        try {
            // Check for Skip button (present on all 4 pages)
            boolean skipVisible = isElementDisplayedByAccessibilityId("Skip");

            // Also check for page indicator to be more certain
            WebElement pageIndicator = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypePageIndicator"));
            boolean indicatorVisible = isElementDisplayed(pageIndicator);

            return skipVisible && indicatorVisible;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if Skip button is visible
     * Useful for conditional logic
     *
     * @return true if Skip button is visible
     */
    public boolean isSkipButtonVisible() {
        return isElementDisplayedByAccessibilityId("Skip");
    }

    /**
     * Skip the tips/onboarding flow
     * Clicks the Skip button to bypass all 4 pages
     */
    public void skipTips() {
        System.out.println("Skipping Wikipedia tips page...");
        tapByAccessibilityId("Skip");
        System.out.println("Tips page skipped successfully");
    }

    /**
     * Skip tips page if it's present
     * Safe to call even when tips page is not showing
     * This is the recommended method for automatic handling
     *
     * @return true if tips were skipped, false if tips page wasn't present
     */
    public boolean skipIfPresent() {
        try {
            if (isOnTipsPage()) {
                System.out.println("Tips page detected on first launch");
                skipTips();

                // Wait a moment for the transition
                Thread.sleep(1000);

                return true;
            } else {
                System.out.println("No tips page detected (app previously launched)");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Tips page handling: " + e.getMessage());
            return false;
        }
    }

    /**
     * Go through all tips pages by clicking Next
     * Alternative to skipping - goes through the full onboarding flow
     * Clicks Next 3 times to reach the last page
     */
    public void goThroughAllPages() {
        System.out.println("Going through all tips pages...");

        try {
            // There are 4 pages total (page 1 of 4)
            // Click Next 3 times to reach page 4
            for (int i = 1; i <= 3; i++) {
                if (isElementDisplayedByAccessibilityId("Next")) {
                    System.out.println("Moving to page " + (i + 1));
                    tapByAccessibilityId("Next");
                    Thread.sleep(500); // Small delay for animation
                }
            }

            System.out.println("Completed all tips pages");
        } catch (Exception e) {
            System.err.println("Error navigating tips pages: " + e.getMessage());
        }
    }

    /**
     * Get current page number from indicator
     *
     * @return current page number (e.g., "1" from "page 1 of 4")
     */
    public String getCurrentPage() {
        try {
            WebElement pageIndicator = driver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypePageIndicator"));
            String indicatorText = getText(pageIndicator);
            // Parse "page 1 of 4" to get "1"
            if (indicatorText != null && indicatorText.contains("page")) {
                String[] parts = indicatorText.split(" ");
                if (parts.length >= 2) {
                    return parts[1];
                }
            }
            return "unknown";
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * Check if on first page of tips
     *
     * @return true if on page 1
     */
    public boolean isFirstPage() {
        return "1".equals(getCurrentPage());
    }

    /**
     * Check if Learn More button is visible
     *
     * @return true if Learn More button is visible
     */
    public boolean isLearnMoreVisible() {
        return isElementDisplayedByAccessibilityId("Learn more about Wikipedia");
    }
}
