package com.example.hooks;

import com.example.core.SimpleDriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple test hooks for managing driver lifecycle
 */
public class SimpleTestHooks {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTestHooks.class);
    
    @Before
    public void beforeScenario() {
        LOGGER.info("Starting scenario");
    }
    
    @After
    public void afterScenario() {
        LOGGER.info("Cleaning up after scenario");
        try {
            if (SimpleDriverManager.isDriverInitialized()) {
                SimpleDriverManager.quitDriver();
                LOGGER.info("Driver quit successfully");
            }
        } catch (Exception e) {
            LOGGER.warn("Error during driver cleanup", e);
        }
    }
}