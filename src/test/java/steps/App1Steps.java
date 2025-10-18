package steps;

import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import mobile.automation.pages.app1.IOSExplorerPage;
import org.testng.Assert;

/**
 * App-specific step definitions for Wikipedia app
 */
public class App1Steps {

    private IOSExplorerPage explorerPage;
    private CommonSteps commonSteps;

    /**
     * Constructor for dependency injection
     * Cucumber will inject the shared TestContext
     */
    public App1Steps(TestContext testContext) {
        this.explorerPage = testContext.getExplorerPage();
        this.commonSteps = new CommonSteps(testContext);
    }

    // ==================== GIVEN STEPS ====================

    /**
     * User is on Explorer page
     */
    @Given("I am on the Explorer page")
    @Given("I am on the Wikipedia Explorer page")
    public void iAmOnExplorerPage() {
        Assert.assertTrue(explorerPage.isOnExplorerPage(),
                "Not on Explorer page");
    }

    // ==================== WHEN STEPS - Navigation ====================

    /**
     * Navigate to a specific tab
     */
    @When("I navigate to {string} tab")
    @When("I go to {string} tab")
    public void iNavigateToTab(String tabName) {
        explorerPage.navigateToTab(tabName);
    }

    /**
     * Search for content
     */
    @When("I search for {string}")
    public void iSearchFor(String searchTerm) {
        explorerPage.searchFor(searchTerm);
    }

    /**
     * Save featured article
     */
    @When("I save the featured article")
    public void iSaveFeaturedArticle() {
        explorerPage.saveFeaturedArticle();
    }

    /**
     * Open article menu
     */
    @When("I open the article menu")
    public void iOpenArticleMenu() {
        explorerPage.openArticleMenu();
    }

    /**
     * Tap featured article
     */
    @When("I tap on the featured article")
    @When("I open the featured article")
    public void iTapFeaturedArticle() {
        explorerPage.tapFeaturedArticle();
    }

    /**
     * Open tabs view
     */
    @When("I open tabs")
    public void iOpenTabs() {
        explorerPage.openTabs();
    }

    /**
     * Open profile
     */
    @When("I open profile")
    @When("I open my profile")
    public void iOpenProfile() {
        explorerPage.openProfile();
    }

    // ==================== THEN STEPS - Verifications ====================

    /**
     * Verify on Explorer page
     */
    @Then("I should be on the Explorer page")
    public void iShouldBeOnExplorerPage() {
        Assert.assertTrue(explorerPage.isOnExplorerPage(),
                "User is not on Explorer page");
    }

    /**
     * Verify featured article title
     */
    @Then("the featured article title should be {string}")
    public void featuredArticleTitleShouldBe(String expectedTitle) {
        String actualTitle = explorerPage.getFeaturedArticleTitle();
        Assert.assertEquals(actualTitle, expectedTitle,
                "Featured article title mismatch");
    }

    /**
     * Verify featured article is displayed
     */
    @Then("I should see the featured article")
    public void iShouldSeeFeaturedArticle() {
        Assert.assertTrue(explorerPage.isElementVisible("Article Title"),
                "Featured article is not visible");
    }
}