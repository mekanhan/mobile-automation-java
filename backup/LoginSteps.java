package com.example.steps;

import com.example.config.ConfigManager;
import com.example.pages.common.DashboardPage;
import com.example.pages.common.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.*;

/**
 * Step definitions for login functionality
 * Handles authentication scenarios for both iOS and Android
 */
public class LoginSteps {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSteps.class);
    
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private ConfigManager.TestConfig config;
    
    public LoginSteps() {
        this.config = ConfigManager.getConfig();
    }
    
    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        LOGGER.info("Navigating to login page");
        loginPage = new LoginPage();
        loginPage.waitForPageToLoad();
        
        assertTrue(loginPage.isLoginPageDisplayed(), "Login page should be displayed");
        LOGGER.info("Login page is displayed and ready");
    }
    
    @Given("I have valid user credentials")
    public void i_have_valid_user_credentials() {
        LOGGER.info("Verifying valid user credentials are available");
        assertNotNull(config.testUsername(), "Test username should be configured");
        assertNotNull(config.testPassword(), "Test password should be configured");
        LOGGER.info("Valid user credentials are available");
    }
    
    @Given("I have valid staff credentials")
    public void i_have_valid_staff_credentials() {
        LOGGER.info("Verifying valid staff credentials are available");
        assertNotNull(config.staffUsername(), "Staff username should be configured");
        assertNotNull(config.staffPassword(), "Staff password should be configured");
        LOGGER.info("Valid staff credentials are available");
    }
    
    @When("I enter valid username and password")
    public void i_enter_valid_username_and_password() {
        LOGGER.info("Entering valid user credentials");
        String username = config.testUsername();
        String password = config.testPassword();
        
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        LOGGER.info("Valid credentials entered for user: {}", username);
    }
    
    @When("I enter staff username and password")
    public void i_enter_staff_username_and_password() {
        LOGGER.info("Entering staff credentials");
        String username = config.staffUsername();
        String password = config.staffPassword();
        
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        LOGGER.info("Staff credentials entered for user: {}", username);
    }
    
    @When("I enter username {string} and password {string}")
    public void i_enter_username_and_password(String username, String password) {
        LOGGER.info("Entering custom credentials for user: {}", username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        LOGGER.info("Custom credentials entered");
    }
    
    @When("I tap the login button")
    public void i_tap_the_login_button() {
        LOGGER.info("Tapping login button");
        loginPage.clickLogin();
        LOGGER.info("Login button tapped");
    }
    
    @When("I login with valid credentials")
    public void i_login_with_valid_credentials() {
        LOGGER.info("Performing complete login with valid credentials");
        String username = config.testUsername();
        String password = config.testPassword();
        
        loginPage.login(username, password);
        LOGGER.info("Login completed for user: {}", username);
    }
    
    @When("I login as staff user")
    public void i_login_as_staff_user() {
        LOGGER.info("Performing complete login as staff user");
        String username = config.staffUsername();
        String password = config.staffPassword();
        
        loginPage.login(username, password);
        LOGGER.info("Staff login completed for user: {}", username);
    }
    
    @When("I tap forgot password link")
    public void i_tap_forgot_password_link() {
        LOGGER.info("Tapping forgot password link");
        loginPage.clickForgotPassword();
        LOGGER.info("Forgot password link tapped");
    }
    
    @Then("I should be successfully logged in")
    public void i_should_be_successfully_logged_in() {
        LOGGER.info("Verifying successful login");
        dashboardPage = new DashboardPage();
        dashboardPage.waitForPageToLoad();
        
        assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed after successful login");
        LOGGER.info("Login verification completed - user is successfully logged in");
    }
    
    @Then("I should see the main dashboard")
    public void i_should_see_the_main_dashboard() {
        LOGGER.info("Verifying main dashboard is visible");
        dashboardPage = new DashboardPage();
        
        assertTrue(dashboardPage.isDashboardDisplayed(), "Main dashboard should be visible");
        LOGGER.info("Main dashboard is visible and accessible");
    }
    
    @Then("I should see an error message")
    public void i_should_see_an_error_message() {
        LOGGER.info("Verifying error message is displayed");
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid login");
        
        String errorMessage = loginPage.getErrorMessage();
        assertFalse(errorMessage.isEmpty(), "Error message should not be empty");
        LOGGER.info("Error message displayed: {}", errorMessage);
    }
    
    @Then("I should see error message {string}")
    public void i_should_see_error_message(String expectedMessage) {
        LOGGER.info("Verifying specific error message: {}", expectedMessage);
        assertTrue(loginPage.isErrorMessageDisplayed(), "Error message should be displayed");
        
        String actualMessage = loginPage.getErrorMessage();
        assertTrue(actualMessage.contains(expectedMessage), 
            String.format("Error message should contain '%s', but was '%s'", expectedMessage, actualMessage));
        LOGGER.info("Expected error message verified: {}", expectedMessage);
    }
    
    @Then("I should remain on the login page")
    public void i_should_remain_on_the_login_page() {
        LOGGER.info("Verifying user remains on login page");
        assertTrue(loginPage.isLoginPageDisplayed(), "User should remain on login page after failed login");
        LOGGER.info("User correctly remains on login page");
    }
}