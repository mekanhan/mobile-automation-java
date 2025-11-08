# Page Object Model (POM) Best Practices

This document explains the proper Page Object Model pattern used in this framework and how to effectively use element locators in step definitions.

## Table of Contents
- [Overview](#overview)
- [POM Structure](#pom-structure)
- [Element Locator Constants](#element-locator-constants)
- [Accessing Locators in Step Definitions](#accessing-locators-in-step-definitions)
- [Complete Example](#complete-example)
- [Anti-Patterns to Avoid](#anti-patterns-to-avoid)

---

## Overview

The Page Object Model (POM) is a design pattern that creates an object repository for storing web/mobile elements. This framework uses a **hybrid approach** combining:

1. **Static final constants** for element locators (Accessibility IDs and XPath)
2. **@iOSXCUITFindBy / @AndroidFindBy annotations** for WebElement declarations
3. **Public getter methods** to expose locators to step definitions
4. **Page methods** for common actions on elements

---

## POM Structure

### Page Class Template

```java
public class YourPage extends BasePage {

    // ============================================
    // ELEMENT IDENTIFIERS (Accessibility IDs AND/OR XPATHS)
    // ============================================

    private static final String SEARCH_BUTTON = "Search";
    private static final String USERNAME_FIELD = "username-field";
    private static final String LOGIN_BUTTON = "//XCUIElementTypeButton[@label='Login']";
    private static final String WELCOME_MESSAGE = "//android.widget.TextView[@text='Welcome']";

    // ============================================
    // WEB ELEMENTS (Using @iOSXCUITFindBy / @AndroidFindBy)
    // ============================================

    @iOSXCUITFindBy(accessibility = SEARCH_BUTTON)
    private WebElement searchButton;

    @iOSXCUITFindBy(accessibility = USERNAME_FIELD)
    private WebElement usernameField;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public YourPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ============================================
    // ACTION METHODS
    // ============================================

    public void tapSearchButton() {
        tap(searchButton);
    }

    public void enterUsername(String username) {
        sendKeys(usernameField, username);
    }

    // ============================================
    // PUBLIC GETTERS FOR LOCATORS
    // (Use these in step definitions to access element locators)
    // ============================================

    public String getSearchButtonLocator() { return SEARCH_BUTTON; }
    public String getUsernameFieldLocator() { return USERNAME_FIELD; }
    public String getLoginButtonLocator() { return LOGIN_BUTTON; }
    public String getWelcomeMessageLocator() { return WELCOME_MESSAGE; }
}
```

---

## Element Locator Constants

### Why Use Constants?

✅ **Benefits:**
- **Maintainability**: Change locator in one place
- **Reusability**: Use same locator for multiple operations
- **Type Safety**: Compile-time checking
- **Readability**: Clear naming conventions
- **No Magic Strings**: All locators defined at the top of the class

### Naming Conventions

```java
// Format: [ELEMENT_TYPE]_[ELEMENT_NAME] or [SECTION]_[ELEMENT_NAME]
private static final String BUTTON_LOGIN = "login-btn";
private static final String FIELD_USERNAME = "username";
private static final String TAB_SETTINGS = "Settings";
private static final String HEADER_WELCOME = "Welcome";

// For XPath, use descriptive names
private static final String XPATH_SUBMIT_BUTTON = "//android.widget.Button[@text='Submit']";
private static final String XPATH_FIRST_LIST_ITEM = "//android.widget.ListView/android.widget.LinearLayout[1]";
```

---

## Accessing Locators in Step Definitions

### Pattern 1: Using Page Getter Methods (Recommended)

**Page Object:**
```java
// IOSExplorerPage.java
private static final String TAB_PLACES = "Places";
private static final String SEARCH_FIELD = "Search Wikipedia";

public String getTabPlacesLocator() { return TAB_PLACES; }
public String getSearchFieldLocator() { return SEARCH_FIELD; }
```

**Step Definition:**
```java
// WikipediaSteps.java
@Given("I am on the explorer page")
public void iAmOnExplorerPage() {
    explorerPage = new IOSExplorerPage(driver);
}

@When("I tap on {string}")
public void iTapOn(String element) {
    // Get locator from page object
    String locator = getLocatorForElement(element);
    explorerPage.click(locator);
}

@Then("I verify {string} is visible")
public void iVerifyIsVisible(String element) {
    String locator = getLocatorForElement(element);
    Assert.assertTrue(explorerPage.isElementVisible(locator));
}

// Helper method to map friendly names to locators
private String getLocatorForElement(String elementName) {
    switch (elementName) {
        case "Places Tab":
            return explorerPage.getTabPlacesLocator();
        case "Search Field":
            return explorerPage.getSearchFieldLocator();
        case "Today Header":
            return explorerPage.getHeaderTodayLocator();
        default:
            // If it looks like a locator (XPath), use it directly
            if (elementName.startsWith("/") || elementName.startsWith("(")) {
                return elementName;
            }
            // Otherwise assume it's an accessibility ID
            return elementName;
    }
}
```

**Feature File:**
```gherkin
Scenario: Navigate to Places tab
  Given I am on the explorer page
  When I tap on "Places Tab"
  Then I verify "Places Tab" is visible
```

---

### Pattern 2: Using Locators Directly (For Dynamic XPath)

**Step Definition:**
```java
@When("I tap on the button with text {string}")
public void iTapOnButtonWithText(String buttonText) {
    // Build XPath dynamically
    String xpath = "//android.widget.Button[@text='" + buttonText + "']";
    explorerPage.click(xpath);  // Smart detection handles XPath
}
```

**Feature File:**
```gherkin
When I tap on the button with text "Submit"
```

---

### Pattern 3: Direct Page Methods (Best for Common Actions)

**Page Object:**
```java
public void goToPlacesTab() {
    tap(tabPlaces);
}

public void searchFor(String searchTerm) {
    tapSearchField();
    enterSearchText(searchTerm);
    hideKeyboard();
}
```

**Step Definition:**
```java
@When("I navigate to {string} tab")
public void iNavigateToTab(String tabName) {
    explorerPage.navigateToTab(tabName);
}

@When("I search for {string}")
public void iSearchFor(String searchTerm) {
    explorerPage.searchFor(searchTerm);
}
```

**Feature File:**
```gherkin
When I navigate to "Places" tab
When I search for "Selenium"
```

---

## Complete Example

### Page Object: IOSExplorerPage.java

```java
public class IOSExplorerPage extends BasePage {

    // ============================================
    // ELEMENT IDENTIFIERS
    // ============================================

    private static final String TAB_PLACES = "Places";
    private static final String TAB_EXPLORE = "Explore";
    private static final String SEARCH_FIELD = "Search Wikipedia";
    private static final String HEADER_TODAY = "Today";
    private static final String LOGIN_JOIN = "//XCUIElementTypeStaticText[@name=\"Log in / Join Wikipedia\"]";

    // ============================================
    // WEB ELEMENTS
    // ============================================

    @iOSXCUITFindBy(accessibility = TAB_PLACES)
    private WebElement tabPlaces;

    @iOSXCUITFindBy(accessibility = SEARCH_FIELD)
    private WebElement searchField;

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public IOSExplorerPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ============================================
    // ACTION METHODS
    // ============================================

    public void goToPlacesTab() {
        tap(tabPlaces);
    }

    public void tapSearchField() {
        tap(searchField);
    }

    public void navigateToTab(String tabName) {
        switch (tabName.toLowerCase()) {
            case "places":
                goToPlacesTab();
                break;
            // ... other tabs
            default:
                throw new IllegalArgumentException("Unknown tab: " + tabName);
        }
    }

    // ============================================
    // PUBLIC GETTERS FOR LOCATORS
    // ============================================

    public String getTabPlacesLocator() { return TAB_PLACES; }
    public String getTabExploreLocator() { return TAB_EXPLORE; }
    public String getSearchFieldLocator() { return SEARCH_FIELD; }
    public String getHeaderTodayLocator() { return HEADER_TODAY; }
    public String getLoginJoinLocator() { return LOGIN_JOIN; }
}
```

### Step Definition: WikipediaSteps.java

```java
public class WikipediaSteps {
    private AppiumDriver driver;
    private IOSExplorerPage explorerPage;

    @Given("I am on the explorer page")
    public void iAmOnExplorerPage() {
        explorerPage = new IOSExplorerPage(driver);
        Assert.assertTrue(explorerPage.isOnExplorerPage());
    }

    // Method 1: Using page methods directly (BEST for common actions)
    @When("I navigate to {string} tab")
    public void iNavigateToTab(String tabName) {
        explorerPage.navigateToTab(tabName);
    }

    // Method 2: Using locator getters (GOOD for verification)
    @Then("I verify {string} is visible")
    public void iVerifyIsVisible(String element) {
        String locator = getLocatorForElement(element);
        Assert.assertTrue(explorerPage.isElementVisible(locator));
    }

    // Method 3: Direct XPath (GOOD for dynamic elements)
    @When("I tap on button with text {string}")
    public void iTapOnButtonWithText(String text) {
        String xpath = "//XCUIElementTypeButton[@label='" + text + "']";
        explorerPage.click(xpath);
    }

    // Helper method to map friendly names to locators
    private String getLocatorForElement(String elementName) {
        switch (elementName) {
            case "Places Tab":
                return explorerPage.getTabPlacesLocator();
            case "Search Field":
                return explorerPage.getSearchFieldLocator();
            case "Today Header":
                return explorerPage.getHeaderTodayLocator();
            case "Login Join Link":
                return explorerPage.getLoginJoinLocator();
            default:
                // Smart detection: XPath vs Accessibility ID
                return elementName;
        }
    }
}
```

### Feature File: explorer.feature

```gherkin
Feature: Wikipedia Explorer Page

  Scenario: Navigate to Places tab
    Given I am on the explorer page
    When I navigate to "Places" tab
    Then I verify "Places Tab" is visible

  Scenario: Verify page elements
    Given I am on the explorer page
    Then I verify "Search Field" is visible
    And I verify "Today Header" is visible

  Scenario: Dynamic XPath usage
    Given I am on the explorer page
    When I tap on button with text "Login"
    Then I verify "//XCUIElementTypeStaticText[@name='Welcome']" is visible
```

---

## Anti-Patterns to Avoid

### ❌ Anti-Pattern 1: Hardcoded Strings in Methods

**BAD:**
```java
protected String mapFriendlyNameToAccessibilityId(String friendlyName) {
    switch (friendlyName) {
        case "Today Header":
            return "Today";  // ❌ Hardcoded string
        case "Search Field":
            return "Search Wikipedia";  // ❌ Hardcoded string
        default:
            return friendlyName;
    }
}
```

**GOOD:**
```java
// Define at class level
private static final String HEADER_TODAY = "Today";
private static final String SEARCH_FIELD = "Search Wikipedia";

// Expose via getter
public String getHeaderTodayLocator() { return HEADER_TODAY; }
public String getSearchFieldLocator() { return SEARCH_FIELD; }
```

---

### ❌ Anti-Pattern 2: Hardcoded Locators in Step Definitions

**BAD:**
```java
@When("I tap on Places tab")
public void iTapOnPlacesTab() {
    page.click("Places");  // ❌ What if "Places" changes to "places-tab"?
}
```

**GOOD:**
```java
@When("I tap on Places tab")
public void iTapOnPlacesTab() {
    String locator = page.getTabPlacesLocator();  // ✅ Single source of truth
    page.click(locator);
}

// OR even better - use page method
@When("I tap on Places tab")
public void iTapOnPlacesTab() {
    page.goToPlacesTab();  // ✅ Best approach
}
```

---

### ❌ Anti-Pattern 3: Switch Statements in Page Objects

**BAD:**
```java
public boolean isElementVisible(String elementName) {
    switch (elementName) {
        case "Article Title":
            return isElementDisplayedByAccessibilityId("Neutral Milk Hotel");  // ❌ Hardcoded
        case "Search Field":
            return isElementDisplayed(searchField);
        default:
            return isElementDisplayedByAccessibilityId(elementName);
    }
}
```

**GOOD:**
```java
// Let step definitions handle the mapping
// Page object just provides getters
public String getArticleTitleLocator() { return ARTICLE_TITLE; }

// In step definition
private String getLocatorForElement(String elementName) {
    switch (elementName) {
        case "Article Title":
            return page.getArticleTitleLocator();  // ✅ Uses constant
        case "Search Field":
            return page.getSearchFieldLocator();
        default:
            return elementName;
    }
}
```

---

## Key Takeaways

1. **Always use static final constants** for element locators at the top of page classes
2. **Provide public getter methods** to expose locators to step definitions
3. **Implement page methods** for common actions (e.g., `goToPlacesTab()`)
4. **Keep step definitions thin** - logic belongs in page objects
5. **Use smart detection** - BasePage automatically handles Accessibility ID vs XPath
6. **Single source of truth** - Change locator once, affects all usages
7. **Avoid hardcoded strings** - They make maintenance difficult

---

## Related Documentation

- [BasePage Method Chain](BASEPAGE_METHOD_CHAIN.md)
- [Project Structure](PROJECT_STRUCTURE.md)
- [Quick Start Guide](QUICK_START.md)

---

**Last Updated:** 2025-10-29
