package mobile.automation.pages.wikipedia;

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
    // ELEMENT IDENTIFIERS (Accessibility IDs AND OR XPATHS)
    // ============================================
    
    // Header Elements
    private static final String WIKIPEDIA_LOGO = "wikipedia";
    // Navigation Bar Elements
    private static final String TABS_BUTTON = "Tabs";
    private static final String PROFILE_BUTTON = "profile-button";
    private static final String SEARCH_FIELD = "Search Wikipedia";

    // Profile Elements
    private static final String LOGIN_JOIN = "//XCUIElementTypeStaticText[@name=\"Log in / Join Wikipedia\"]";
    private static final String DONATE = "//XCUIElementTypeStaticText[@name=\"Donate\"]";
    private static final String SETTINGS = "//XCUIElementTypeStaticText[@name=\"Settings\"]";

    // Tab Section Elements
    private static final String ADD_NEW_TAB = "add";
    private static final String MAIN_PAGE_TAB = "Main Page";
    private static final String MAIN_PAGE_DESCRIPTION = "Main Page Wikipedia's daily highlights";
    private static final String DONE_BUTTON = "Done";


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

    // ============================================
    // ELEMENT LOCATOR MAP
    // (Maps friendly element names to actual locators)
    // ============================================

    /**
     * Element name to locator mapping
     * Maps both constant names (e.g., "HEADER_TODAY") and friendly names (e.g., "Today Header")
     * to actual accessibility IDs or XPaths
     */
    private static final java.util.Map<String, String> ELEMENT_MAP;

    static {
        ELEMENT_MAP = new java.util.HashMap<>();

        // Header Elements - using constant names
        ELEMENT_MAP.put("HEADER_TODAY", HEADER_TODAY);
        ELEMENT_MAP.put("HEADER_FEATURED_ARTICLE", HEADER_FEATURED_ARTICLE);
        ELEMENT_MAP.put("HEADER_TOP_READ", HEADER_TOP_READ);

        // Header Elements - using friendly names
        ELEMENT_MAP.put("Today Header", HEADER_TODAY);
        ELEMENT_MAP.put("Featured Article", HEADER_FEATURED_ARTICLE);
        ELEMENT_MAP.put("Top Read", HEADER_TOP_READ);

        // Tab Bar Elements - constant names
        ELEMENT_MAP.put("TAB_EXPLORE", TAB_EXPLORE);
        ELEMENT_MAP.put("TAB_PLACES", TAB_PLACES);
        ELEMENT_MAP.put("TAB_SAVED", TAB_SAVED);
        ELEMENT_MAP.put("TAB_HISTORY", TAB_HISTORY);
        ELEMENT_MAP.put("TAB_SEARCH", TAB_SEARCH);

        // Tab Bar Elements - friendly names
        ELEMENT_MAP.put("Explore Tab", TAB_EXPLORE);
        ELEMENT_MAP.put("Places Tab", TAB_PLACES);
        ELEMENT_MAP.put("Saved Tab", TAB_SAVED);
        ELEMENT_MAP.put("History Tab", TAB_HISTORY);
        ELEMENT_MAP.put("Search Tab", TAB_SEARCH);

        // Navigation Elements - constant names
        ELEMENT_MAP.put("SEARCH_FIELD", SEARCH_FIELD);
        ELEMENT_MAP.put("TABS_BUTTON", TABS_BUTTON);
        ELEMENT_MAP.put("PROFILE_BUTTON", PROFILE_BUTTON);
        ELEMENT_MAP.put("WIKIPEDIA_LOGO", WIKIPEDIA_LOGO);

        // Navigation Elements - friendly names
        ELEMENT_MAP.put("Search Field", SEARCH_FIELD);
        ELEMENT_MAP.put("Tabs Button", TABS_BUTTON);
        ELEMENT_MAP.put("Profile Button", PROFILE_BUTTON);
        ELEMENT_MAP.put("Wikipedia Logo", WIKIPEDIA_LOGO);

        // Profile Elements (XPath) - constant names
        ELEMENT_MAP.put("LOGIN_JOIN", LOGIN_JOIN);
        ELEMENT_MAP.put("DONATE", DONATE);
        ELEMENT_MAP.put("SETTINGS", SETTINGS);

        // Profile Elements - friendly names
        ELEMENT_MAP.put("Login Join Link", LOGIN_JOIN);
        ELEMENT_MAP.put("Donate", DONATE);
        ELEMENT_MAP.put("Settings", SETTINGS);

        // Featured Article Elements - constant names
        ELEMENT_MAP.put("FEATURED_ARTICLE_TITLE", FEATURED_ARTICLE_TITLE);
        ELEMENT_MAP.put("SAVE_FOR_LATER_BUTTON", SAVE_FOR_LATER_BUTTON);
        ELEMENT_MAP.put("OVERFLOW_BUTTON", OVERFLOW_BUTTON);

        // Featured Article Elements - friendly names
        ELEMENT_MAP.put("Article Title", FEATURED_ARTICLE_TITLE);
        ELEMENT_MAP.put("Save For Later", SAVE_FOR_LATER_BUTTON);
        ELEMENT_MAP.put("Overflow Button", OVERFLOW_BUTTON);

        // Tab Section Elements
        ELEMENT_MAP.put("ADD_NEW_TAB", ADD_NEW_TAB);
        ELEMENT_MAP.put("Add New Tab", ADD_NEW_TAB);

        // Common Elements
        ELEMENT_MAP.put("MAIN_PAGE_TAB", MAIN_PAGE_TAB);
        ELEMENT_MAP.put("Main Page Tab", MAIN_PAGE_TAB);
        ELEMENT_MAP.put("MAIN_PAGE_DESCRIPTION", MAIN_PAGE_DESCRIPTION);
        ELEMENT_MAP.put("DONE_BUTTON", DONE_BUTTON);
        ELEMENT_MAP.put("Done", DONE_BUTTON);
    }

    /**
     * Gets element locator by friendly name or constant name
     * If the name is not in the map, returns the name itself
     * (assumes it's a direct locator like XPath or accessibility ID)
     *
     * @param elementName The friendly name, constant name, or direct locator
     * @return The actual locator (accessibility ID or XPath)
     */
    public String getElementLocator(String elementName) {
        return ELEMENT_MAP.getOrDefault(elementName, elementName);
    }

    /**
     * Checks if element is visible using friendly name, constant, or direct locator
     * This method overrides the base class to use the element map
     *
     * @param elementName The friendly name, constant name, or direct locator
     * @return true if element is visible, false otherwise
     */
    @Override
    public boolean isElementVisible(String elementName) {
        String locator = getElementLocator(elementName);
        return super.isElementVisible(locator);
    }

    /**
     * Click on element using friendly name, constant, or direct locator
     * This method overrides the base class to use the element map
     *
     * @param elementName The friendly name, constant name, or direct locator
     */
    @Override
    public void click(String elementName) {
        String locator = getElementLocator(elementName);
        super.click(locator);
    }

    /**
     * Enter text into field using friendly name, constant, or direct locator
     * This method overrides the base class to use the element map
     *
     * @param elementName The friendly name, constant name, or direct locator
     * @param text The text to enter
     */
    @Override
    public void enterText(String elementName, String text) {
        String locator = getElementLocator(elementName);
        super.enterText(locator, text);
    }

    /**
     * Get text from element using friendly name, constant, or direct locator
     * This method overrides the base class to use the element map
     *
     * @param elementName The friendly name, constant name, or direct locator
     * @return The text of the element
     */
    @Override
    public String getText(String elementName) {
        String locator = getElementLocator(elementName);
        return super.getText(locator);
    }

    // ============================================
    // PUBLIC GETTERS FOR LOCATORS
    // (Alternative approach - use these in step definitions if needed)
    // ============================================

    public String getTabPlacesLocator() { return TAB_PLACES; }
    public String getTabExploreLocator() { return TAB_EXPLORE; }
    public String getTabSavedLocator() { return TAB_SAVED; }
    public String getTabHistoryLocator() { return TAB_HISTORY; }
    public String getTabSearchLocator() { return TAB_SEARCH; }
    public String getSearchFieldLocator() { return SEARCH_FIELD; }
    public String getHeaderTodayLocator() { return HEADER_TODAY; }
    public String getHeaderFeaturedArticleLocator() { return HEADER_FEATURED_ARTICLE; }
    public String getHeaderTopReadLocator() { return HEADER_TOP_READ; }
    public String getTabsButtonLocator() { return TABS_BUTTON; }
    public String getProfileButtonLocator() { return PROFILE_BUTTON; }
    public String getLoginJoinLocator() { return LOGIN_JOIN; }
    public String getDonateLocator() { return DONATE; }
    public String getSettingsLocator() { return SETTINGS; }
    public String getFeaturedArticleTitleLocator() { return FEATURED_ARTICLE_TITLE; }
}