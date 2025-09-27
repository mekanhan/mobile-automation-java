package com.example.hooks;

import com.example.api.BackendService;
import com.example.config.ConfigManager;
import com.example.config.CapabilitiesFactory;
import com.example.core.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Hooks for setup and teardown operations
 * Handles driver initialization, API setup, and cleanup
 */
public class TestHooks {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestHooks.class);
    
    private BackendService backendService;
    private ConfigManager.TestConfig config;
    
    @Before
    public void setUp(Scenario scenario) {
        LOGGER.info("Starting test scenario: {}", scenario.getName());
        
        try {
            // Initialize configuration
            config = ConfigManager.getConfig();
            
            // Initialize API services
            backendService = new BackendService();
            
            // Setup test data via API
            setupTestData(scenario);
            
            // Initialize mobile driver
            initializeMobileDriver();
            
            LOGGER.info("Test setup completed successfully for: {}", scenario.getName());
            
        } catch (Exception e) {
            LOGGER.error("Failed to setup test: {}", scenario.getName(), e);
            throw new RuntimeException("Test setup failed", e);
        }
    }
    
    @After
    public void tearDown(Scenario scenario) {
        LOGGER.info("Finishing test scenario: {}", scenario.getName());
        
        try {
            // Take screenshot if test failed
            if (scenario.isFailed() && config.screenshotOnFailure()) {
                takeScreenshot(scenario);
            }
            
            // Cleanup test data via API
            cleanupTestData(scenario);
            
            // Quit driver
            if (DriverManager.isDriverInitialized()) {
                DriverManager.quitDriver();
                LOGGER.info("Driver quit successfully");
            }
            
            LOGGER.info("Test teardown completed for: {}", scenario.getName());
            
        } catch (Exception e) {
            LOGGER.error("Error during test teardown: {}", scenario.getName(), e);
        }
    }
    
    /**
     * Initialize mobile driver based on configuration
     */
    private void initializeMobileDriver() {
        String platform = config.platformName().toLowerCase();
        DesiredCapabilities capabilities = CapabilitiesFactory.createCapabilitiesFromConfig();
        
        DriverManager.initializeDriver(platform, capabilities);
        LOGGER.info("Mobile driver initialized for platform: {}", platform);
    }
    
    /**
     * Setup test data via API calls
     */
    private void setupTestData(Scenario scenario) {
        try {
            String platform = config.platformName().toLowerCase();
            String classId = platform.equals("ios") ? config.iosClassId() : config.androidClassId();
            
            // Only setup data for specific scenarios
            if (needsApiSetup(scenario)) {
                LOGGER.info("Setting up test data via API for scenario: {}", scenario.getName());
                
                // Cleanup existing data first
                boolean cleanupSuccess = backendService.deleteAllRoomsPosts(classId, "staffCoral");
                if (cleanupSuccess) {
                    LOGGER.info("Existing test data cleaned up successfully");
                } else {
                    LOGGER.warn("Failed to cleanup existing test data");
                }
                
                // Create test data based on scenario tags
                if (hasTag(scenario, "@assignments")) {
                    BackendService.AssignmentResponse assignmentResponse = 
                        backendService.createAssignment(classId, "staffCoral");
                    if (assignmentResponse.isSuccess()) {
                        LOGGER.info("Test assignment created: {}", assignmentResponse.getTitle());
                        // Store assignment details for use in test steps
                        scenario.attach(assignmentResponse.getTitle(), "text/plain", "Assignment Title");
                        scenario.attach(assignmentResponse.getInstructions(), "text/plain", "Assignment Instructions");
                    }
                }
                
                if (hasTag(scenario, "@announcements")) {
                    BackendService.AnnouncementResponse announcementResponse = 
                        backendService.createAnnouncement(classId, "staffCoral");
                    if (announcementResponse.isSuccess()) {
                        LOGGER.info("Test announcement created: {}", announcementResponse.getBody());
                        scenario.attach(announcementResponse.getBody(), "text/plain", "Announcement Body");
                    }
                }
                
                if (hasTag(scenario, "@chat")) {
                    BackendService.ChatMessageResponse chatResponse = 
                        backendService.sendChatMessage(classId, "staffCoral", config.userRoomsId());
                    if (chatResponse.isSuccess()) {
                        LOGGER.info("Test chat message sent: {}", chatResponse.getMessage());
                        scenario.attach(chatResponse.getMessage(), "text/plain", "Chat Message");
                        scenario.attach(chatResponse.getChatId(), "text/plain", "Chat ID");
                    }
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("Failed to setup test data via API", e);
        }
    }
    
    /**
     * Cleanup test data after test execution
     */
    private void cleanupTestData(Scenario scenario) {
        try {
            if (needsApiCleanup(scenario)) {
                String platform = config.platformName().toLowerCase();
                String classId = platform.equals("ios") ? config.iosClassId() : config.androidClassId();
                
                LOGGER.info("Cleaning up test data via API for scenario: {}", scenario.getName());
                
                boolean cleanupSuccess = backendService.deleteAllRoomsPosts(classId, "staffCoral");
                if (cleanupSuccess) {
                    LOGGER.info("Test data cleanup completed successfully");
                } else {
                    LOGGER.warn("Failed to cleanup test data");
                }
            }
            
        } catch (Exception e) {
            LOGGER.error("Failed to cleanup test data via API", e);
        }
    }
    
    /**
     * Take screenshot on test failure
     */
    private void takeScreenshot(Scenario scenario) {
        try {
            if (DriverManager.isDriverInitialized()) {
                byte[] screenshot = DriverManager.getDriver().getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot");
                LOGGER.info("Screenshot attached for failed scenario: {}", scenario.getName());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to take screenshot", e);
        }
    }
    
    /**
     * Check if scenario needs API setup
     */
    private boolean needsApiSetup(Scenario scenario) {
        return hasTag(scenario, "@assignments") || 
               hasTag(scenario, "@announcements") || 
               hasTag(scenario, "@chat") ||
               hasTag(scenario, "@apiSetup");
    }
    
    /**
     * Check if scenario needs API cleanup
     */
    private boolean needsApiCleanup(Scenario scenario) {
        return hasTag(scenario, "@apiCleanup") || needsApiSetup(scenario);
    }
    
    /**
     * Check if scenario has specific tag
     */
    private boolean hasTag(Scenario scenario, String tag) {
        return scenario.getSourceTagNames().contains(tag);
    }
}