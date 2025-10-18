package mobile.automation.pages.app1;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import mobile.automation.pages.base.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

/**
 * iOS Explorer Page Object for Wikipedia App
 * Represents the main Explore/Home screen with Featured Articles and Top Read sections
 */
public class IOSExplorerPage extends BasePage {

    // ============================================
    // ELEMENT IDENTIFIERS (Accessibility IDs)
    // ============================================
    
    // Navigation Bar Elements
    private static final String TABS_BUTTON = "Tabs";
    private static final String PROFILE_BUTTON = "profile-button";
    private static final String SEARCH_FIELD = "Search Wikipedia";
    
    // Featured Article Section
    private static final String OVERFLOW_BUTTON = "overflow";
    private static final String FEATURED_ARTICLE_TITLE = "Neutral Milk Hotel";
    private static final String FEATURED_ARTICLE_SUBTITLE = "American indie rock band";
    private static final String SAVE_FOR_LATER_BUTTON = "Save for later";
    
    // Tab Bar Elements
    private static final String TAB_EXPLORE = "Explore";
    private static final String TAB_PLACES = "Places";
    private static final String TAB_SAVED = "Saved";
    private static final String TAB_HISTORY = "History";
    private static final String TAB_SEARCH = "Search";
    
    // Section Headers (Static Text)
    private static final String HEADER_TODAY = "Today";
    private static final String HEADER_FEATURED_ARTICLE = "Featured article";
    private static final String HEADER_TOP_READ = "Top read";

    // ============================================
    // WEB ELEMENTS (Using @iOSXCUITFindBy)
    // ============================================
    
    @iOSXCUITFindBy(accessibility = TABS_BUTTON)
    private WebElement tabsButton;
    
    @iOSXCUITFindBy(accessibility = PROFILE_BUTTON)
    private WebElement profileButton;
    
    @iOSXCUITFindBy(accessibility = SEARCH_FIELD)
    private WebElement searchField;
    
    @iOSXCUITFindBy(accessibility = OVERFLOW_BUTTON)
    private WebElement overflowButton;
    
    @iOSXCUITFindBy(accessibility = SAVE_FOR_LATER_BUTTON)
    private WebElement saveForLaterButton;
    
    // Tab Bar Elements
    @iOSXCUITFindBy(accessibility = TAB_EXPLORE)
    private WebElement tabExplore;
    
    @iOSXCUITFindBy(accessibility = TAB_PLACES)
    private WebElement tabPlaces;
    
    @iOSXCUITFindBy(accessibility = TAB_SAVED)
    private WebElement tabSaved;
    
    @iOSXCUITFindBy(accessibility = TAB_HISTORY)
    private WebElement tabHistory;
    
    @iOSXCUITFindBy(accessibility = TAB_SEARCH)
    private WebElement tabSearch;

    // ============================================
    // CONSTRUCTOR
    // ============================================
    
    public IOSExplorerPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ============================================
    // NAVIGATION BAR ACTIONS
    // ============================================
    
    /**
     * Taps on the Tabs button in the navigation bar
     */
    public void tapTabsButton() {
        tap(tabsButton);
    }
    
    /**
     * Taps on the Profile button in the navigation bar
     */
    public void tapProfileButton() {
        tap(profileButton);
    }
    
    /**
     * Taps on the search field to activate search
     */
    public void tapSearchField() {
        tap(searchField);
    }
    
    /**
     * Enters text into the search field
     * @param searchText The text to search for
     */
    public void enterSearchText(String searchText) {
        sendKeys(searchField, searchText);
    }

    // ============================================
    // FEATURED ARTICLE ACTIONS
    // ============================================
    
    /**
     * Taps the overflow menu button on the featured article
     */
    public void tapFeaturedArticleOverflow() {
        tap(overflowButton);
    }
    
    /**
     * Taps the "Save for later" button on the featured article
     */
    public void tapSaveForLater() {
        tap(saveForLaterButton);
    }
    
    /**
     * Taps on the featured article title to open the article
     */
    public void tapFeaturedArticleTitle() {
        tapByAccessibilityId(FEATURED_ARTICLE_TITLE);
    }

    // ============================================
    // TAB BAR NAVIGATION
    // ============================================
    
    /**
     * Navigates to the Explore tab
     */
    public void goToExploreTab() {
        tap(tabExplore);
    }
    
    /**
     * Navigates to the Places tab
     */
    public void goToPlacesTab() {
        tap(tabPlaces);
    }
    
    /**
     * Navigates to the Saved tab
     */
    public void goToSavedTab() {
        tap(tabSaved);
    }
    
    /**
     * Navigates to the History tab
     */
    public void goToHistoryTab() {
        tap(tabHistory);
    }
    
    /**
     * Navigates to the Search tab
     */
    public void goToSearchTab() {
        tap(tabSearch);
    }

    // ============================================
    // VERIFICATION METHODS
    // ============================================
    
    /**
     * Verifies if the Explorer page is displayed
     * @return true if the page is displayed, false otherwise
     */
    public boolean isExplorerPageDisplayed() {
        return isElementDisplayed(searchField);
    }
    
    /**
     * Verifies if the "Today" section is visible
     * @return true if the Today section is visible
     */
    public boolean isTodaySectionVisible() {
        return isElementDisplayedByAccessibilityId(HEADER_TODAY);
    }
    
    /**
     * Verifies if the Featured Article section is visible
     * @return true if the Featured Article section is visible
     */
    public boolean isFeaturedArticleVisible() {
        return isElementDisplayedByAccessibilityId(HEADER_FEATURED_ARTICLE);
    }
    
    /**
     * Verifies if the Top Read section is visible
     * @return true if the Top Read section is visible
     */
    public boolean isTopReadSectionVisible() {
        return isElementDisplayedByAccessibilityId(HEADER_TOP_READ);
    }
    
    /**
     * Gets the text of the search field
     * @return The placeholder text of the search field
     */
    public String getSearchFieldText() {
        return getText(searchField);
    }

    // ============================================
    // SCROLL ACTIONS
    // ============================================
    
    /**
     * Scrolls down to view more content
     */
    public void scrollDownToTopReadSection() {
        scrollDown();
    }
    
    /**
     * Scrolls up to the top of the page
     */
    public void scrollToTop() {
        scrollUp();
    }

    // ============================================
    // LEGACY COMPATIBILITY METHODS
    // (For backward compatibility with existing step definitions)
    // ============================================

    /**
     * Check if on Explorer page (legacy method name)
     */
    public boolean isOnExplorerPage() {
        return isExplorerPageDisplayed();
    }

    /**
     * Navigate to a tab by name
     */
    public void navigateToTab(String tabName) {
        switch (tabName.toLowerCase()) {
            case "explore":
                goToExploreTab();
                break;
            case "places":
                goToPlacesTab();
                break;
            case "saved":
                goToSavedTab();
                break;
            case "history":
                goToHistoryTab();
                break;
            case "search":
                goToSearchTab();
                break;
            default:
                throw new IllegalArgumentException("Unknown tab: " + tabName);
        }
    }

    /**
     * Search for content
     */
    public void searchFor(String searchTerm) {
        tapSearchField();
        enterSearchText(searchTerm);
        hideKeyboard();
    }

    /**
     * Save featured article
     */
    public void saveFeaturedArticle() {
        tapSaveForLater();
    }

    /**
     * Open article overflow menu
     */
    public void openArticleMenu() {
        tapFeaturedArticleOverflow();
    }

    /**
     * Tap on featured article
     */
    public void tapFeaturedArticle() {
        tapFeaturedArticleTitle();
    }

    /**
     * Open tabs
     */
    public void openTabs() {
        tapTabsButton();
    }

    /**
     * Open profile
     */
    public void openProfile() {
        tapProfileButton();
    }

    /**
     * Get featured article title
     */
    public String getFeaturedArticleTitle() {
        return getTextByAccessibilityId("Neutral Milk Hotel");
    }

    /**
     * Check if element is visible by name (for step definitions)
     */
    public boolean isElementVisible(String elementName) {
        // Map friendly names to accessibility IDs
        switch (elementName) {
            case "Article Title":
                return isElementDisplayedByAccessibilityId("Neutral Milk Hotel");
            case "Search Field":
                return isElementDisplayed(searchField);
            case "Today Header":
                return isElementDisplayedByAccessibilityId("Today");
            case "Featured Article":
                return isElementDisplayedByAccessibilityId("Featured article");
            default:
                return isElementDisplayedByAccessibilityId(elementName);
        }
    }
}