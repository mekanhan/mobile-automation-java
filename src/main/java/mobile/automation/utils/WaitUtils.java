package mobile.automation.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_POLLING = 2;

    /**
     * Wait for element to be visible
     */
    public static WebElement waitForVisibility(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for element to be visible with default timeout
     */
    public static WebElement waitForVisibility(AppiumDriver driver, By locator) {
        return waitForVisibility(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be clickable
     */
    public static WebElement waitForClickability(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait for element to be clickable with default timeout
     */
    public static WebElement waitForClickability(AppiumDriver driver, By locator) {
        return waitForClickability(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be present
     */
    public static WebElement waitForPresence(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to be present with default timeout
     */
    public static WebElement waitForPresence(AppiumDriver driver, By locator) {
        return waitForPresence(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to disappear
     */
    public static boolean waitForInvisibility(AppiumDriver driver, By locator, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Fluent wait for element with custom conditions
     */
    public static WebElement fluentWait(AppiumDriver driver, By locator, int timeoutInSeconds, int pollingInSeconds) {
        FluentWait<AppiumDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .pollingEvery(Duration.ofSeconds(pollingInSeconds))
                .ignoring(Exception.class);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Simple thread sleep (use sparingly)
     */
    public static void hardWait(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Wait for element to contain specific text
     */
    public static boolean waitForTextToBePresent(AppiumDriver driver, By locator, String text, int timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
}
