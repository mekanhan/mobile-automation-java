package hooks;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import mobile.automation.driver.DriverManager;
import mobile.automation.pages.app1.IOSTipsPage;
import mobile.automation.server.AppiumServerManager;
import mobile.automation.utils.ScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import steps.TestContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Cucumber Hooks for test lifecycle management
 */
public class Hooks {

    private AppiumDriver driver;
    private TestContext testContext;
    private ScreenRecorder screenRecorder;
    private static int scenarioCounter = 0;

    /**
     * Before All hook - Start Appium server once before all tests
     * This runs once per test suite execution
     */
    @BeforeAll
    public static void startAppiumServer() {
        System.out.println("========================================");
        System.out.println("INITIALIZING TEST SUITE");
        System.out.println("========================================");

        try {
            AppiumServerManager.startServer();
            System.out.println("Appium server initialization completed");
        } catch (Exception e) {
            System.err.println("Failed to start Appium server: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Cannot proceed without Appium server", e);
        }
    }

    /**
     * After All hook - Stop Appium server after all tests complete
     * This runs once after all test scenarios have finished
     */
    @AfterAll
    public static void stopAppiumServer() {
        System.out.println("========================================");
        System.out.println("CLEANING UP TEST SUITE");
        System.out.println("========================================");

        try {
            AppiumServerManager.stopServer();
            System.out.println("Appium server cleanup completed");
        } catch (Exception e) {
            System.err.println("Error stopping Appium server: " + e.getMessage());
        }
    }

    /**
     * Constructor for dependency injection
     * Cucumber will create one instance per scenario
     */
    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    /**
     * Before hook - Initialize driver and page objects
     * Runs before each scenario
     */
    @Before
    public void setUp(Scenario scenario) {
        scenarioCounter++;

        System.out.println("========================================");
        System.out.println("Starting Scenario: " + scenario.getName());
        System.out.println("========================================");

        // Initialize driver
        String platform = System.getProperty("platform", "ios");
        driver = DriverManager.initializeDriver(platform);

        // Start screen recording if enabled
        if (isRecordingEnabled()) {
            startScreenRecording(scenario, platform);
        }

        // Handle first-launch tips page (appears only on fresh install)
        handleTipsPageIfPresent();

        // Set driver in test context (shared with step definitions)
        testContext.setDriver(driver);

        System.out.println("Setup completed successfully!");
    }

    /**
     * Handle Wikipedia tips/onboarding page if it appears
     * This page only shows on first launch after fresh install
     * Automatically skips the tips page to get to the main Explorer screen
     */
    private void handleTipsPageIfPresent() {
        try {
            IOSTipsPage tipsPage = new IOSTipsPage(driver);
            tipsPage.skipIfPresent();
        } catch (Exception e) {
            // Tips page handling failed, but this shouldn't stop the test
            System.out.println("Tips page handling skipped: " + e.getMessage());
        }
    }

    /**
     * After hook - Take screenshot on failure, stop recording, and quit driver
     * Runs after each scenario
     */
    @After
    public void tearDown(Scenario scenario) {
        System.out.println("========================================");
        System.out.println("Finishing Scenario: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        System.out.println("========================================");

        // Stop screen recording and attach video
        if (screenRecorder != null && screenRecorder.isRecording()) {
            stopAndAttachRecording(scenario);
        }

        // Take screenshot on failure
        if (scenario.isFailed() && driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot on Failure");
                System.out.println("Screenshot captured for failed scenario");
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }

        // Quit driver
        try {
            DriverManager.quitDriver();
        } catch (Exception e) {
            System.err.println("Error during driver cleanup: " + e.getMessage());
        }

        System.out.println("Teardown completed!");
    }

    /**
     * Before hook for specific tags (optional)
     * Example: Run only for @resetApp tagged scenarios
     */
    @Before("@resetApp")
    public void resetApp() {
        System.out.println("Resetting app...");
        DriverManager.resetApp();
    }

    /**
     * After hook for specific tags (optional)
     * Example: Always take screenshot for @screenshot tagged scenarios
     */
    @After("@screenshot")
    public void takeScreenshot(Scenario scenario) {
        if (driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Scenario Screenshot");
                System.out.println("Screenshot captured");
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    /**
     * Get driver instance (for use in step definitions if needed)
     */
    public AppiumDriver getDriver() {
        return driver;
    }

    // ==================== SCREEN RECORDING HELPER METHODS ====================

    /**
     * Check if screen recording is enabled
     */
    private boolean isRecordingEnabled() {
        String recordingEnabled = System.getProperty("screen.recording", "false");
        return Boolean.parseBoolean(recordingEnabled);
    }

    /**
     * Start screen recording for current scenario
     */
    private void startScreenRecording(Scenario scenario, String platform) {
        try {
            screenRecorder = new ScreenRecorder();

            // Generate filename: wikipedia_iOS_explorer_1
            String fileName = ScreenRecorder.generateFileNameFromScenario(scenario.getName(), platform);

            if (platform.equalsIgnoreCase("ios")) {
                // Get device UDID from capabilities
                String udid = (String) driver.getCapabilities().getCapability("udid");
                screenRecorder.startIOSRecording(udid, fileName);
            } else if (platform.equalsIgnoreCase("android")) {
                // Get device serial from capabilities
                String deviceName = (String) driver.getCapabilities().getCapability("deviceName");
                screenRecorder.startAndroidRecording(deviceName, fileName);
            }
        } catch (Exception e) {
            System.err.println("Failed to start screen recording: " + e.getMessage());
            screenRecorder = null;
        }
    }

    /**
     * Stop screen recording and attach to report
     */
    private void stopAndAttachRecording(Scenario scenario) {
        try {
            String videoPath = screenRecorder.stopRecording();

            if (videoPath != null) {
                File videoFile = new File(videoPath);
                if (videoFile.exists()) {
                    byte[] videoBytes = Files.readAllBytes(videoFile.toPath());
                    scenario.attach(videoBytes, "video/mp4", "Test Execution Recording");
                    System.out.println("📹 Video attached to report: " + videoPath);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to attach video to report: " + e.getMessage());
        }
    }
}
