package com.example.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

/**
 * Configuration manager using Owner library for property management
 */
public class ConfigManager {
    private static TestConfig testConfig;
    
    static {
        testConfig = ConfigFactory.create(TestConfig.class);
    }
    
    public static TestConfig getConfig() {
        return testConfig;
    }
    
    @Config.Sources({
        "classpath:config/${env}.properties",
        "classpath:config/default.properties"
    })
    public interface TestConfig extends Config {
        
        @Key("appium.server.url")
        @DefaultValue("http://127.0.0.1:4723/wd/hub")
        String appiumServerUrl();
        
        @Key("platform.name")
        @DefaultValue("iOS")
        String platformName();
        
        @Key("platform.version")
        @DefaultValue("15.5")
        String platformVersion();
        
        @Key("device.name")
        @DefaultValue("iPhone 13")
        String deviceName();
        
        @Key("app.path")
        String appPath();
        
        @Key("bundle.id")
        String bundleId();
        
        @Key("app.package")
        String appPackage();
        
        @Key("app.activity")
        String appActivity();
        
        @Key("automation.name")
        @DefaultValue("XCUITest")
        String automationName();
        
        @Key("new.command.timeout")
        @DefaultValue("60")
        int newCommandTimeout();
        
        @Key("implicit.wait")
        @DefaultValue("10")
        int implicitWait();
        
        @Key("explicit.wait")
        @DefaultValue("30")
        int explicitWait();
        
        // API Configuration
        @Key("api.base.url")
        String apiBaseUrl();
        
        @Key("auth.url")
        String authUrl();
        
        @Key("media.api.url")
        String mediaApiUrl();
        
        @Key("backend.api.url")
        String backendApiUrl();
        
        @Key("client.secret")
        String clientSecret();
        
        // Test Data
        @Key("test.user.username")
        String testUsername();
        
        @Key("test.user.password")
        String testPassword();
        
        @Key("staff.username")
        String staffUsername();
        
        @Key("staff.password")
        String staffPassword();
        
        @Key("class.id.ios")
        String iosClassId();
        
        @Key("class.id.android")
        String androidClassId();
        
        @Key("user.rooms.id")
        String userRoomsId();
        
        // Environment
        @Key("environment")
        @DefaultValue("dev")
        String environment();
        
        @Key("test.parallel")
        @DefaultValue("false")
        boolean isParallelExecution();
        
        @Key("thread.count")
        @DefaultValue("1")
        int threadCount();
        
        // Reporting
        @Key("report.path")
        @DefaultValue("target/reports")
        String reportPath();
        
        @Key("screenshot.on.failure")
        @DefaultValue("true")
        boolean screenshotOnFailure();
    }
}