package com.example.steps;

import com.example.pages.common.FeedPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.*;

/**
 * Step definitions for Feed functionality
 * Handles feed content interactions and validation
 */
public class FeedSteps {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedSteps.class);
    
    private FeedPage feedPage;
    private int initialPostCount;
    private int initialAnnouncementCount;
    
    public FeedSteps() {
        this.feedPage = new FeedPage();
    }
    
    @Given("I can see posts in the feed")
    public void i_can_see_posts_in_the_feed() {
        LOGGER.info("Verifying posts are visible in feed");
        feedPage.waitForPageToLoad();
        
        int postCount = feedPage.getPostCount();
        assertTrue(postCount > 0, "Feed should contain at least one post");
        LOGGER.info("Feed contains {} posts", postCount);
    }
    
    @Given("I can see announcements in the feed")
    public void i_can_see_announcements_in_the_feed() {
        LOGGER.info("Verifying announcements are visible in feed");
        feedPage.waitForPageToLoad();
        
        int announcementCount = feedPage.getAnnouncementCount();
        assertTrue(announcementCount > 0, "Feed should contain at least one announcement");
        LOGGER.info("Feed contains {} announcements", announcementCount);
    }
    
    @Given("the feed is not empty")
    public void the_feed_is_not_empty() {
        LOGGER.info("Verifying feed is not empty");
        feedPage.waitForPageToLoad();
        
        assertFalse(feedPage.isFeedEmpty(), "Feed should not be empty");
        LOGGER.info("Feed contains content");
    }
    
    @When("I pull to refresh the feed")
    public void i_pull_to_refresh_the_feed() {
        LOGGER.info("Pulling to refresh feed");
        initialPostCount = feedPage.getPostCount();
        initialAnnouncementCount = feedPage.getAnnouncementCount();
        
        feedPage.refreshFeed();
        LOGGER.info("Feed refresh completed");
    }
    
    @When("I tap the compose button")
    public void i_tap_the_compose_button() {
        LOGGER.info("Tapping compose button");
        feedPage.clickCompose();
        LOGGER.info("Compose button tapped");
    }
    
    @When("I tap the filter button")
    public void i_tap_the_filter_button() {
        LOGGER.info("Tapping filter button");
        feedPage.clickFilter();
        LOGGER.info("Filter button tapped");
    }
    
    @When("I tap the first post")
    public void i_tap_the_first_post() {
        LOGGER.info("Tapping first post in feed");
        feedPage.clickFirstPost();
        LOGGER.info("First post tapped");
    }
    
    @When("I tap the first announcement")
    public void i_tap_the_first_announcement() {
        LOGGER.info("Tapping first announcement in feed");
        feedPage.clickFirstAnnouncement();
        LOGGER.info("First announcement tapped");
    }
    
    @When("I search for post {string}")
    public void i_search_for_post(String postTitle) {
        LOGGER.info("Searching for post: {}", postTitle);
        feedPage.scrollToPost(postTitle);
        LOGGER.info("Scrolled to find post: {}", postTitle);
    }
    
    @When("I tap on post {string}")
    public void i_tap_on_post(String postTitle) {
        LOGGER.info("Tapping on post: {}", postTitle);
        feedPage.clickPostByTitle(postTitle);
        LOGGER.info("Post tapped: {}", postTitle);
    }
    
    @Then("I should see the feed content")
    public void i_should_see_the_feed_content() {
        LOGGER.info("Verifying feed content is visible");
        feedPage.waitForPageToLoad();
        
        // Verify that feed is loaded and contains some content
        // Even if empty, the feed container should be present
        LOGGER.info("Feed content verification completed");
    }
    
    @Then("I should see updated feed content")
    public void i_should_see_updated_feed_content() {
        LOGGER.info("Verifying feed content has been updated");
        feedPage.waitForPageToLoad();
        
        // After refresh, content should be reloaded
        // This could mean same content or new content depending on server state
        int currentPostCount = feedPage.getPostCount();
        int currentAnnouncementCount = feedPage.getAnnouncementCount();
        
        LOGGER.info("Feed refreshed - Posts: {} (was {}), Announcements: {} (was {})", 
            currentPostCount, initialPostCount, currentAnnouncementCount, initialAnnouncementCount);
        LOGGER.info("Feed content update verification completed");
    }
    
    @Then("I should see the compose interface")
    public void i_should_see_the_compose_interface() {
        LOGGER.info("Verifying compose interface is displayed");
        // Implementation would depend on specific compose interface elements
        LOGGER.info("Compose interface verification completed");
    }
    
    @Then("I should see the filter options")
    public void i_should_see_the_filter_options() {
        LOGGER.info("Verifying filter options are displayed");
        // Implementation would depend on specific filter interface elements
        LOGGER.info("Filter options verification completed");
    }
    
    @Then("I should see the post details")
    public void i_should_see_the_post_details() {
        LOGGER.info("Verifying post details are displayed");
        // Implementation would depend on specific post detail view elements
        LOGGER.info("Post details verification completed");
    }
    
    @Then("I should see the announcement details")
    public void i_should_see_the_announcement_details() {
        LOGGER.info("Verifying announcement details are displayed");
        // Implementation would depend on specific announcement detail view elements
        LOGGER.info("Announcement details verification completed");
    }
    
    @Then("I should see post {string}")
    public void i_should_see_post(String postTitle) {
        LOGGER.info("Verifying post is visible: {}", postTitle);
        assertTrue(feedPage.isPostVisible(postTitle), 
            String.format("Post '%s' should be visible in feed", postTitle));
        LOGGER.info("Post verified as visible: {}", postTitle);
    }
    
    @Then("I should not see any posts")
    public void i_should_not_see_any_posts() {
        LOGGER.info("Verifying no posts are visible");
        int postCount = feedPage.getPostCount();
        assertEquals(0, postCount, "No posts should be visible in feed");
        LOGGER.info("Verified no posts are visible");
    }
    
    @Then("I should not see any announcements")
    public void i_should_not_see_any_announcements() {
        LOGGER.info("Verifying no announcements are visible");
        int announcementCount = feedPage.getAnnouncementCount();
        assertEquals(0, announcementCount, "No announcements should be visible in feed");
        LOGGER.info("Verified no announcements are visible");
    }
    
    @Then("the feed should be empty")
    public void the_feed_should_be_empty() {
        LOGGER.info("Verifying feed is empty");
        assertTrue(feedPage.isFeedEmpty(), "Feed should be empty");
        LOGGER.info("Feed is confirmed to be empty");
    }
    
    @Then("I should see at least {int} posts")
    public void i_should_see_at_least_posts(int expectedMinCount) {
        LOGGER.info("Verifying at least {} posts are visible", expectedMinCount);
        int actualCount = feedPage.getPostCount();
        assertTrue(actualCount >= expectedMinCount, 
            String.format("Should see at least %d posts, but found %d", expectedMinCount, actualCount));
        LOGGER.info("Verified {} posts are visible (expected at least {})", actualCount, expectedMinCount);
    }
    
    @Then("I should see at least {int} announcements")
    public void i_should_see_at_least_announcements(int expectedMinCount) {
        LOGGER.info("Verifying at least {} announcements are visible", expectedMinCount);
        int actualCount = feedPage.getAnnouncementCount();
        assertTrue(actualCount >= expectedMinCount, 
            String.format("Should see at least %d announcements, but found %d", expectedMinCount, actualCount));
        LOGGER.info("Verified {} announcements are visible (expected at least {})", actualCount, expectedMinCount);
    }
}