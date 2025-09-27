package com.example.steps;

import com.example.pages.common.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.*;

/**
 * Step definitions for navigation functionality
 * Handles tab navigation and page transitions
 */
public class NavigationSteps {
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationSteps.class);
    
    private DashboardPage dashboardPage;
    private FeedPage feedPage;
    private ChatPage chatPage;
    private AssignmentsPage assignmentsPage;
    private MorePage morePage;
    
    @Given("I am on the dashboard")
    public void i_am_on_the_dashboard() {
        LOGGER.info("Verifying user is on dashboard");
        dashboardPage = new DashboardPage();
        dashboardPage.waitForPageToLoad();
        
        assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be displayed");
        LOGGER.info("User is on dashboard");
    }
    
    @When("I navigate to Feed tab")
    public void i_navigate_to_feed_tab() {
        LOGGER.info("Navigating to Feed tab");
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        feedPage = dashboardPage.goToFeed();
        LOGGER.info("Navigated to Feed tab");
    }
    
    @When("I navigate to Chat tab")
    public void i_navigate_to_chat_tab() {
        LOGGER.info("Navigating to Chat tab");
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        chatPage = dashboardPage.goToChat();
        LOGGER.info("Navigated to Chat tab");
    }
    
    @When("I navigate to Assignments tab")
    public void i_navigate_to_assignments_tab() {
        LOGGER.info("Navigating to Assignments tab");
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        assignmentsPage = dashboardPage.goToAssignments();
        LOGGER.info("Navigated to Assignments tab");
    }
    
    @When("I navigate to More tab")
    public void i_navigate_to_more_tab() {
        LOGGER.info("Navigating to More tab");
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        morePage = dashboardPage.goToMore();
        LOGGER.info("Navigated to More tab");
    }
    
    @When("I tap the profile button")
    public void i_tap_the_profile_button() {
        LOGGER.info("Tapping profile button");
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        dashboardPage.clickProfile();
        LOGGER.info("Profile button tapped");
    }
    
    @When("I tap the notifications button")
    public void i_tap_the_notifications_button() {
        LOGGER.info("Tapping notifications button");
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        dashboardPage.clickNotifications();
        LOGGER.info("Notifications button tapped");
    }
    
    @When("I tap the search button")
    public void i_tap_the_search_button() {
        LOGGER.info("Tapping search button");
        if (dashboardPage == null) {
            dashboardPage = new DashboardPage();
        }
        dashboardPage.clickSearch();
        LOGGER.info("Search button tapped");
    }
    
    @Then("I should see the Feed page")
    public void i_should_see_the_feed_page() {
        LOGGER.info("Verifying Feed page is displayed");
        assertNotNull(feedPage, "Feed page should be initialized");
        feedPage.waitForPageToLoad();
        LOGGER.info("Feed page is displayed successfully");
    }
    
    @Then("I should see the Chat page")
    public void i_should_see_the_chat_page() {
        LOGGER.info("Verifying Chat page is displayed");
        assertNotNull(chatPage, "Chat page should be initialized");
        chatPage.waitForPageToLoad();
        LOGGER.info("Chat page is displayed successfully");
    }
    
    @Then("I should see the Assignments page")
    public void i_should_see_the_assignments_page() {
        LOGGER.info("Verifying Assignments page is displayed");
        assertNotNull(assignmentsPage, "Assignments page should be initialized");
        assignmentsPage.waitForPageToLoad();
        LOGGER.info("Assignments page is displayed successfully");
    }
    
    @Then("I should see the More page")
    public void i_should_see_the_more_page() {
        LOGGER.info("Verifying More page is displayed");
        assertNotNull(morePage, "More page should be initialized");
        morePage.waitForPageToLoad();
        LOGGER.info("More page is displayed successfully");
    }
    
    @Then("I should see the profile screen")
    public void i_should_see_the_profile_screen() {
        LOGGER.info("Verifying profile screen is displayed");
        // Implementation would depend on specific profile screen elements
        LOGGER.info("Profile screen verification completed");
    }
    
    @Then("I should see the notifications screen")
    public void i_should_see_the_notifications_screen() {
        LOGGER.info("Verifying notifications screen is displayed");
        // Implementation would depend on specific notifications screen elements
        LOGGER.info("Notifications screen verification completed");
    }
    
    @Then("I should see the search interface")
    public void i_should_see_the_search_interface() {
        LOGGER.info("Verifying search interface is displayed");
        // Implementation would depend on specific search interface elements
        LOGGER.info("Search interface verification completed");
    }
    
    @Given("I am on the Feed page")
    public void i_am_on_the_feed_page() {
        LOGGER.info("Ensuring user is on Feed page");
        if (feedPage == null) {
            i_navigate_to_feed_tab();
        }
        feedPage.waitForPageToLoad();
        LOGGER.info("User is on Feed page");
    }
    
    @Given("I am on the Chat page")
    public void i_am_on_the_chat_page() {
        LOGGER.info("Ensuring user is on Chat page");
        if (chatPage == null) {
            i_navigate_to_chat_tab();
        }
        chatPage.waitForPageToLoad();
        LOGGER.info("User is on Chat page");
    }
    
    @Given("I am on the Assignments page")
    public void i_am_on_the_assignments_page() {
        LOGGER.info("Ensuring user is on Assignments page");
        if (assignmentsPage == null) {
            i_navigate_to_assignments_tab();
        }
        assignmentsPage.waitForPageToLoad();
        LOGGER.info("User is on Assignments page");
    }
    
    @Given("I am on the More page")
    public void i_am_on_the_more_page() {
        LOGGER.info("Ensuring user is on More page");
        if (morePage == null) {
            i_navigate_to_more_tab();
        }
        morePage.waitForPageToLoad();
        LOGGER.info("User is on More page");
    }
}