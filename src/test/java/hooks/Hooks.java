package hooks;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import mobile.automation.driver.DriverManager;
import mobile.automation.pages.wikipedia.IOSTipsPage;
import mobile.automation.server.AppiumServerManager;
import mobile.automation.utils.ScreenRecorder;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import steps.TestContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

/**
 * Cucumber Hooks for test lifecycle management
 */
public class Hooks {

    private AppiumDriver driver;
    private TestContext testContext;
    private ScreenRecorder screenRecorder;
    private static int scenarioCounter = 0;

    /**
     * Before All hook - Clean old results and start Appium server
     * This runs once per test suite execution
     */
    @BeforeAll
    public static void startAppiumServer() {
        System.out.println("========================================");
        System.out.println("INITIALIZING TEST SUITE");
        System.out.println("========================================");

        // Clean old test artifacts
        cleanOldAllureResults();
        cleanOldScreenshots();
        cleanOldRecordings();

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
     * After All hook - Stop Appium server and generate report
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

        // Generate Allure report if enabled
        generateAllureReport();
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
        boolean recordingEnabled = isRecordingEnabled();
        System.out.println("🎥 Screen recording enabled: " + recordingEnabled);
        if (recordingEnabled) {
            System.out.println("📹 Starting screen recording for scenario: " + scenario.getName());
            startScreenRecording(scenario, platform);
        } else {
            System.out.println("⏭️  Screen recording disabled (enable with -Dscreen.recording=true or in config.properties)");
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

                // Save to target/screenshots directory (compressed)
                // Note: Not using scenario.attach() to avoid duplicate screenshots in test-output/
                saveFailureScreenshot(screenshot, scenario.getName());

                System.out.println("✅ Screenshot captured for failed scenario");
            } catch (Exception e) {
                System.err.println("❌ Failed to capture screenshot: " + e.getMessage());
                e.printStackTrace();
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
     * Save failure screenshot to file system with compression
     */
    private void saveFailureScreenshot(byte[] screenshot, String scenarioName) {
        String fileName = "FAILURE_" + scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_") + "_" + System.currentTimeMillis();
        saveScreenshotToTargetDirectory(screenshot, fileName);
    }

    /**
     * Generic method to save screenshot to target/screenshots directory with compression
     */
    private void saveScreenshotToTargetDirectory(byte[] screenshot, String fileName) {
        try {
            Path screenshotsDir = Paths.get("target/screenshots");
            if (!Files.exists(screenshotsDir)) {
                Files.createDirectories(screenshotsDir);
            }

            // Ensure proper file extension
            if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg")) {
                fileName += ".png";
            }

            Path screenshotPath = screenshotsDir.resolve(fileName);

            // Compress screenshot to reduce file size
            byte[] compressedScreenshot = compressScreenshot(screenshot);

            Files.write(screenshotPath, compressedScreenshot);

            // Calculate size reduction
            long originalSize = screenshot.length;
            long compressedSize = compressedScreenshot.length;
            int reduction = (int) ((1 - (double) compressedSize / originalSize) * 100);

            System.out.println("📁 Screenshot saved to: " + screenshotPath);
            System.out.println("📊 Size: " + formatBytes(compressedSize) + " (reduced by " + reduction + "%)");
        } catch (Exception e) {
            System.err.println("⚠️  Failed to save screenshot to file: " + e.getMessage());
        }
    }

    /**
     * Compress screenshot to reduce file size
     * Uses JPEG compression with quality setting
     */
    private byte[] compressScreenshot(byte[] originalScreenshot) {
        try {
            // Read original image
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(originalScreenshot);
            java.awt.image.BufferedImage image = javax.imageio.ImageIO.read(bais);

            // Get compression quality from system property (default: 0.7 = 70%)
            float quality = Float.parseFloat(System.getProperty("screenshot.quality", "0.7"));

            // Write compressed image
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
            System.err.println("⚠️  Failed to compress screenshot, using original: " + e.getMessage());
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
     * Take screenshot for @screenshot tagged scenarios
     * Saves to target/screenshots directory instead of test-output
     */
    @After("@screenshot")
    public void takeScreenshot(Scenario scenario) {
        if (driver != null) {
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

                // Save to target/screenshots directory (compressed)
                // Note: Not using scenario.attach() to avoid duplicate screenshots in test-output/
                String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9]", "_");
                String timestamp = String.valueOf(System.currentTimeMillis());
                saveScreenshotToTargetDirectory(screenshot, "TAGGED_" + scenarioName + "_" + timestamp);

                System.out.println("📸 Screenshot captured for @screenshot tagged scenario");
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

    // ==================== CLEANUP HELPER METHODS ====================

    /**
     * Clean old screenshots before test execution
     * Can be controlled via system property: -Dscreenshots.clean=true (default: true)
     */
    private static void cleanOldScreenshots() {
        String cleanScreenshots = System.getProperty("screenshots.clean", "true");

        if (!Boolean.parseBoolean(cleanScreenshots)) {
            System.out.println("⏭️  Skipping screenshots cleanup (screenshots.clean=false)");
            return;
        }

        try {
            // Clean target/screenshots directory
            Path screenshotsPath = Paths.get("target/screenshots");

            if (Files.exists(screenshotsPath)) {
                System.out.println("🧹 Cleaning old screenshots from target/screenshots...");

                // Delete all files in screenshots directory
                Files.walk(screenshotsPath)
                    .sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(screenshotsPath)) // Keep the directory itself
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete: " + path);
                        }
                    });

                System.out.println("✅ target/screenshots cleaned successfully");
            } else {
                System.out.println("📁 Creating target/screenshots directory");
                Files.createDirectories(screenshotsPath);
            }

            // Clean test-output directory (Cucumber's embedded screenshots)
            Path testOutputPath = Paths.get("test-output");
            if (Files.exists(testOutputPath)) {
                System.out.println("🧹 Cleaning old test-output directory...");

                // Delete entire test-output directory
                Files.walk(testOutputPath)
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete: " + path);
                        }
                    });

                System.out.println("✅ test-output cleaned successfully");
            }
        } catch (IOException e) {
            System.err.println("⚠️  Warning: Failed to clean screenshots: " + e.getMessage());
        }
    }

    /**
     * Clean old recordings before test execution
     * Can be controlled via system property: -Drecordings.clean=true (default: true)
     */
    private static void cleanOldRecordings() {
        String cleanRecordings = System.getProperty("recordings.clean", "true");

        if (!Boolean.parseBoolean(cleanRecordings)) {
            System.out.println("⏭️  Skipping recordings cleanup (recordings.clean=false)");
            return;
        }

        try {
            // Clean target/recordings directory
            Path recordingsPath = Paths.get("target/recordings");

            if (Files.exists(recordingsPath)) {
                System.out.println("🧹 Cleaning old recordings from target/recordings...");

                // Delete all files in recordings directory
                Files.walk(recordingsPath)
                    .sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(recordingsPath)) // Keep the directory itself
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete: " + path);
                        }
                    });

                System.out.println("✅ target/recordings cleaned successfully");
            } else {
                System.out.println("📁 Creating target/recordings directory");
                Files.createDirectories(recordingsPath);
            }
        } catch (IOException e) {
            System.err.println("⚠️  Warning: Failed to clean recordings: " + e.getMessage());
        }
    }

    /**
     * Clean old Allure results before test execution
     * Can be controlled via system property: -Dallure.clean=true (default: true)
     */
    private static void cleanOldAllureResults() {
        String cleanResults = System.getProperty("allure.clean", "true");

        if (!Boolean.parseBoolean(cleanResults)) {
            System.out.println("⏭️  Skipping Allure results cleanup (allure.clean=false)");
            return;
        }

        try {
            Path allureResultsPath = Paths.get("allure-results");

            if (Files.exists(allureResultsPath)) {
                System.out.println("🧹 Cleaning old Allure results...");

                // Delete all files in allure-results directory
                Files.walk(allureResultsPath)
                    .sorted(Comparator.reverseOrder())
                    .filter(path -> !path.equals(allureResultsPath)) // Keep the directory itself
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            System.err.println("Failed to delete: " + path);
                        }
                    });

                System.out.println("✅ Allure results cleaned successfully");
            } else {
                System.out.println("📁 Creating allure-results directory");
                Files.createDirectories(allureResultsPath);
            }
        } catch (IOException e) {
            System.err.println("⚠️  Warning: Failed to clean Allure results: " + e.getMessage());
        }
    }

    /**
     * Generate and optionally open Allure report after tests
     * Can be controlled via system properties:
     * - allure.report=true/false (default: false) - Generate report
     * - allure.open=true/false (default: false) - Open report in browser
     */
    private static void generateAllureReport() {
        String generateReport = System.getProperty("allure.report", "false");
        String openReport = System.getProperty("allure.open", "false");

        if (!Boolean.parseBoolean(generateReport) && !Boolean.parseBoolean(openReport)) {
            System.out.println("📊 Allure report generation skipped");
            System.out.println("💡 To generate report, run: mvn allure:report");
            System.out.println("💡 To open report, run: ./scripts/open-report.sh");
            return;
        }

        try {
            System.out.println("========================================");
            System.out.println("GENERATING ALLURE REPORT");
            System.out.println("========================================");

            // Generate Allure report using Maven
            ProcessBuilder processBuilder = new ProcessBuilder("mvn", "allure:report");
            processBuilder.inheritIO(); // Show Maven output
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("✅ Allure report generated successfully");
                System.out.println("📊 Report location: target/allure-report/index.html");

                // Open report in browser if enabled
                if (Boolean.parseBoolean(openReport)) {
                    openAllureReport();
                }
            } else {
                System.err.println("⚠️  Failed to generate Allure report (exit code: " + exitCode + ")");
            }
        } catch (Exception e) {
            System.err.println("⚠️  Error generating Allure report: " + e.getMessage());
        }
    }

    /**
     * Open Allure report in browser using the open-report.sh script
     */
    private static void openAllureReport() {
        try {
            System.out.println("🌐 Opening Allure report in browser...");

            ProcessBuilder processBuilder = new ProcessBuilder("./scripts/open-report.sh");
            processBuilder.start();

            System.out.println("✅ Report server started on http://localhost:8765");
            System.out.println("💡 Press Ctrl+C in the terminal to stop the server");
        } catch (Exception e) {
            System.err.println("⚠️  Failed to open report: " + e.getMessage());
            System.out.println("💡 You can manually open it by running: ./scripts/open-report.sh");
        }
    }

    // ==================== SCREEN RECORDING HELPER METHODS ====================

    /**
     * Check if screen recording is enabled
     * Reads from config.properties or system property (-Dscreen.recording=true)
     */
    private boolean isRecordingEnabled() {
        // First try system property (command line -D flag)
        String recordingEnabled = System.getProperty("screen.recording");

        if (recordingEnabled != null) {
            return Boolean.parseBoolean(recordingEnabled);
        }

        // Fall back to config.properties
        try {
            java.util.Properties config = new java.util.Properties();
            java.io.InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties");

            if (input != null) {
                config.load(input);
                recordingEnabled = config.getProperty("screen.recording", "false");
                input.close();
                return Boolean.parseBoolean(recordingEnabled);
            }
        } catch (Exception e) {
            System.err.println("⚠️  Failed to read screen.recording from config.properties: " + e.getMessage());
        }

        // Default: disabled
        return false;
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
                // For simulators, Appium returns the UDID in the 'udid' capability
                String udid = (String) driver.getCapabilities().getCapability("udid");

                if (udid == null || udid.isEmpty()) {
                    System.err.println("⚠️  Cannot start recording: UDID not found in capabilities");
                    System.err.println("Available capabilities: " + driver.getCapabilities().asMap().keySet());
                    screenRecorder = null;
                    return;
                }

                System.out.println("📹 Starting iOS recording for device: " + udid);
                screenRecorder.startIOSRecording(udid, fileName);
            } else if (platform.equalsIgnoreCase("android")) {
                // Get device serial from capabilities
                String deviceName = (String) driver.getCapabilities().getCapability("deviceName");

                if (deviceName == null || deviceName.isEmpty()) {
                    System.err.println("⚠️  Cannot start recording: Device name not found in capabilities");
                    screenRecorder = null;
                    return;
                }

                System.out.println("📹 Starting Android recording for device: " + deviceName);
                screenRecorder.startAndroidRecording(deviceName, fileName);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to start screen recording: " + e.getMessage());
            e.printStackTrace();
            screenRecorder = null;
        }
    }

    /**
     * Stop screen recording and attach to report
     *
     * NOTE: Cucumber HTML reports display videos at original size (1507x2642).
     * For better viewing:
     * - Use Allure reports (automatically scales videos)
     * - Open video directly from target/recordings/ folder
     * - Use a browser with zoom controls
     */
    private void stopAndAttachRecording(Scenario scenario) {
        try {
            String videoPath = screenRecorder.stopRecording();

            if (videoPath != null) {
                File videoFile = new File(videoPath);
                if (videoFile.exists()) {
                    byte[] videoBytes = Files.readAllBytes(videoFile.toPath());
                    String videoFileName = videoFile.getName();

                    // Attach video to report
                    scenario.attach(videoBytes, "video/mp4", "📹 Test Recording: " + videoFileName);

                    System.out.println("📹 Video attached to report: " + videoPath);
                    System.out.println("💡 View options:");
                    System.out.println("   • Allure report (better video player): mvn allure:serve");
                    System.out.println("   • Open directly: open " + videoPath);
                    System.out.println("   • Cucumber report: Videos shown at original size (1507x2642)");
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to attach video to report: " + e.getMessage());
        }
    }

}
