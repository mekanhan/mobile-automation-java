# POM Element Verification Guide

This guide explains how to properly verify elements on a page using the Page Object Model pattern.

## Table of Contents
- [The Problem](#the-problem)
- [The Solution](#the-solution)
- [Implementation Steps](#implementation-steps)
- [Complete Working Example](#complete-working-example)
- [Best Practices](#best-practices)

---

## The Problem

Your feature file uses constant names like:
```gherkin
Then I should see "HEADER_TODAY"
And  I should see "TAB_PLACES"
```

But your Page Object has these defined as:
```java
private static final String HEADER_TODAY = "Today";
private static final String TAB_PLACES = "Places";
```

**Question:** How do we map "HEADER_TODAY" (feature file) to the actual locator "Today" (page object)?

---

## The Solution

Create a **locator resolver** method that maps constant names to actual locators from the Page Object.

### Architecture Flow

```
FEATURE FILE                  STEP DEFINITION              PAGE OBJECT
│                             │                            │
│ "HEADER_TODAY"     ────────>│ resolveLocator()  ────────>│ getHeaderTodayLocator()
│                             │   returns "Today"          │   returns "Today"
│                             │                            │
│                             │ isElementVisible("Today") ─>│ Uses BasePage method
│                             │                            │   with actual locator
```

---

## Implementation Steps

### Step 1: Ensure Page Object Has Locator Getters

**IOSExplorerPage.java** (Already done ✅)
```java
// Element constants
private static final String HEADER_TODAY = "Today";
private static final String TAB_PLACES = "Places";
private static final String SEARCH_FIELD = "Search Wikipedia";
private static final String TABS_BUTTON = "Tabs";

// Public getters
public String getHeaderTodayLocator() { return HEADER_TODAY; }
public String getTabPlacesLocator() { return TAB_PLACES; }
public String getSearchFieldLocator() { return SEARCH_FIELD; }
public String getTabsButtonLocator() { return TABS_BUTTON; }
```

### Step 2: Update IOSExplorerPage with Locator Resolver

Add a public method to resolve constant names to actual locators:

```java
/**
 * Resolves element constant names to actual locators
 * This method maps feature file constant names (e.g., "HEADER_TODAY")
 * to actual accessibility IDs or XPaths (e.g., "Today")
 *
 * @param constantName The constant name from feature file
 * @return The actual locator (accessibility ID or XPath)
 */
public String resolveLocator(String constantName) {
    switch (constantName) {
        // Header Elements
        case "HEADER_TODAY":
            return HEADER_TODAY;
        case "HEADER_FEATURED_ARTICLE":
            return HEADER_FEATURED_ARTICLE;
        case "HEADER_TOP_READ":
            return HEADER_TOP_READ;

        // Tab Bar Elements
        case "TAB_EXPLORE":
            return TAB_EXPLORE;
        case "TAB_PLACES":
            return TAB_PLACES;
        case "TAB_SAVED":
            return TAB_SAVED;
        case "TAB_HISTORY":
            return TAB_HISTORY;
        case "TAB_SEARCH":
            return TAB_SEARCH;

        // Navigation Elements
        case "SEARCH_FIELD":
            return SEARCH_FIELD;
        case "TABS_BUTTON":
            return TABS_BUTTON;
        case "PROFILE_BUTTON":
            return PROFILE_BUTTON;
        case "WIKIPEDIA_LOGO":
            return WIKIPEDIA_LOGO;

        // Profile Elements (XPath)
        case "LOGIN_JOIN":
            return LOGIN_JOIN;
        case "DONATE":
            return DONATE;
        case "SETTINGS":
            return SETTINGS;

        // Featured Article Elements
        case "FEATURED_ARTICLE_TITLE":
            return FEATURED_ARTICLE_TITLE;

        default:
            // If not a constant, assume it's already a locator
            return constantName;
    }
}
```

### Step 3: Update CommonSteps to Use Resolver

**CommonSteps.java**
```java
/**
 * Verify element is visible
 * Supports both constant names (e.g., "HEADER_TODAY") and direct locators
 */
@Then("I should see {string}")
@Then("{string} should be visible")
@Then("{string} is visible")
public void iShouldSee(String elementName) {
    // Resolve locator from current page if it's a page-specific constant
    String locator = resolveLocator(elementName);

    Assert.assertTrue(currentPage.isElementVisible(locator),
            "Element '" + elementName + "' is not visible");
}

/**
 * Resolves element constant names to actual locators
 * Delegates to page-specific resolvers
 */
private String resolveLocator(String elementName) {
    // If current page is IOSExplorerPage, use its resolver
    if (currentPage instanceof IOSExplorerPage) {
        return ((IOSExplorerPage) currentPage).resolveLocator(elementName);
    }

    // Add other page resolvers as needed
    // else if (currentPage instanceof LoginPage) {
    //     return ((LoginPage) currentPage).resolveLocator(elementName);
    // }

    // If no specific resolver, return as-is (might be direct locator or XPath)
    return elementName;
}
```

### Step 4: Update Other CommonSteps Methods

```java
/**
 * Click/Tap on element
 */
@When("I click on {string}")
@When("I tap {string}")
@When("I tap on {string}")
public void iTapOn(String elementName) {
    String locator = resolveLocator(elementName);
    currentPage.click(locator);
}

/**
 * Scroll to element
 */
@When("I scroll to {string}")
public void iScrollTo(String elementName) {
    String locator = resolveLocator(elementName);
    currentPage.scrollToElement(locator);
}

/**
 * Enter text into field
 */
@When("I enter {string} into {string}")
@When("I type {string} into {string}")
public void iEnterTextInto(String text, String fieldName) {
    String locator = resolveLocator(fieldName);
    currentPage.enterText(locator, text);
}
```

---

## Complete Working Example

### Feature File
```gherkin
@ios @wikipedia @explorer
Feature: Wikipedia Explorer Page

  Background:
    Given I am on the Explorer page

  @smoke @verification
  Scenario: Verify Explorer page elements are visible
    # Using constant names - maps to actual locators via resolver
    Then I should see "HEADER_TODAY"
    And  I should see "HEADER_FEATURED_ARTICLE"
    And  I should see "SEARCH_FIELD"
    And  I should see "TAB_PLACES"
    And  I should see "TABS_BUTTON"

  @smoke @profile
  Scenario: Verify profile elements with XPath
    When I tap on "PROFILE_BUTTON"
    Then I should see "LOGIN_JOIN"
    And  I should see "DONATE"
    And  I should see "SETTINGS"

  @smoke @mixed
  Scenario: Mix constants and direct locators
    # Using constant
    Then I should see "SEARCH_FIELD"
    # Using direct XPath
    And I should see "//XCUIElementTypeStaticText[@name='Today']"
```

### Page Object: IOSExplorerPage.java

```java
public class IOSExplorerPage extends BasePage {

    // ============================================
    // ELEMENT IDENTIFIERS (Accessibility IDs AND/OR XPATHS)
    // ============================================

    private static final String HEADER_TODAY = "Today";
    private static final String HEADER_FEATURED_ARTICLE = "Featured article";
    private static final String TAB_PLACES = "Places";
    private static final String SEARCH_FIELD = "Search Wikipedia";
    private static final String TABS_BUTTON = "Tabs";
    private static final String PROFILE_BUTTON = "profile-button";
    private static final String LOGIN_JOIN = "//XCUIElementTypeStaticText[@name=\"Log in / Join Wikipedia\"]";

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public IOSExplorerPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ============================================
    // LOCATOR RESOLVER
    // ============================================

    /**
     * Resolves element constant names to actual locators
     * Maps feature file constants to real accessibility IDs or XPaths
     */
    public String resolveLocator(String constantName) {
        switch (constantName) {
            case "HEADER_TODAY":
                return HEADER_TODAY;
            case "HEADER_FEATURED_ARTICLE":
                return HEADER_FEATURED_ARTICLE;
            case "TAB_PLACES":
                return TAB_PLACES;
            case "SEARCH_FIELD":
                return SEARCH_FIELD;
            case "TABS_BUTTON":
                return TABS_BUTTON;
            case "PROFILE_BUTTON":
                return PROFILE_BUTTON;
            case "LOGIN_JOIN":
                return LOGIN_JOIN;
            default:
                // Not a constant - return as-is (might be direct locator)
                return constantName;
        }
    }

    // ============================================
    // PUBLIC GETTERS FOR LOCATORS (Alternative approach)
    // ============================================

    public String getHeaderTodayLocator() { return HEADER_TODAY; }
    public String getTabPlacesLocator() { return TAB_PLACES; }
    // ... other getters
}
```

### Step Definition: CommonSteps.java

```java
public class CommonSteps {

    private AppiumDriver driver;
    private BasePage currentPage;
    private TestContext testContext;

    public CommonSteps(TestContext testContext) {
        this.testContext = testContext;
        this.driver = testContext.getDriver();
        this.currentPage = testContext.getCurrentPage();
    }

    // ==================== ASSERTION STEPS ====================

    @Then("I should see {string}")
    @Then("{string} should be visible")
    @Then("{string} is visible")
    public void iShouldSee(String elementName) {
        String locator = resolveLocator(elementName);
        Assert.assertTrue(currentPage.isElementVisible(locator),
                "Element '" + elementName + "' is not visible");
    }

    @When("I tap on {string}")
    public void iTapOn(String elementName) {
        String locator = resolveLocator(elementName);
        currentPage.click(locator);
    }

    @When("I scroll to {string}")
    public void iScrollTo(String elementName) {
        String locator = resolveLocator(elementName);
        currentPage.scrollToElement(locator);
    }

    // ==================== LOCATOR RESOLVER ====================

    /**
     * Resolves element names to actual locators using page-specific resolvers
     */
    private String resolveLocator(String elementName) {
        // Delegate to page-specific resolver
        if (currentPage instanceof IOSExplorerPage) {
            return ((IOSExplorerPage) currentPage).resolveLocator(elementName);
        }
        // Add more page types as needed

        // No resolver available - return as-is
        return elementName;
    }
}
```

---

## How It Works

### Flow Diagram

```
FEATURE FILE: "I should see HEADER_TODAY"
                    ↓
STEP DEFINITION: iShouldSee("HEADER_TODAY")
                    ↓
         resolveLocator("HEADER_TODAY")
                    ↓
      instanceof IOSExplorerPage? YES
                    ↓
    explorerPage.resolveLocator("HEADER_TODAY")
                    ↓
         switch("HEADER_TODAY")
              case "HEADER_TODAY":
                    ↓
           return "Today"  ← Actual accessibility ID
                    ↓
    currentPage.isElementVisible("Today")
                    ↓
         isXPath("Today")? NO
                    ↓
    isElementDisplayedByAccessibilityId("Today")
                    ↓
    driver.findElement(AppiumBy.accessibilityId("Today"))
                    ↓
              WebElement
                    ↓
        element.isDisplayed()
                    ↓
             true/false
```

---

## Best Practices

### ✅ DO

1. **Define constants in Page Objects**
   ```java
   private static final String TAB_PLACES = "Places";
   ```

2. **Provide resolver method in Page Objects**
   ```java
   public String resolveLocator(String constantName) { ... }
   ```

3. **Use resolver in Step Definitions**
   ```java
   String locator = resolveLocator(elementName);
   ```

4. **Support both constants and direct locators**
   ```gherkin
   Then I should see "HEADER_TODAY"
   And I should see "//XCUIElementTypeButton[@label='Login']"
   ```

5. **Keep feature files readable**
   ```gherkin
   # Good - uses meaningful constant
   Then I should see "LOGIN_BUTTON"

   # Also good - direct locator when needed
   Then I should see "//android.widget.Button[@text='Submit']"
   ```

### ❌ DON'T

1. **Don't hardcode locators in step definitions**
   ```java
   // ❌ BAD
   currentPage.isElementVisible("Today");
   ```

2. **Don't put resolver logic in BasePage**
   ```java
   // ❌ BAD - BasePage should be generic
   public class BasePage {
       public String resolveLocator(String name) { ... }
   }
   ```

3. **Don't skip the resolver**
   ```java
   // ❌ BAD - no resolution
   @Then("I should see {string}")
   public void iShouldSee(String elementName) {
       currentPage.isElementVisible(elementName); // Won't work with constants!
   }
   ```

---

## Alternative Approach: Enum-Based Locators

For larger projects, consider using Enums:

```java
public enum ExplorerPageElements {
    HEADER_TODAY("Today"),
    TAB_PLACES("Places"),
    SEARCH_FIELD("Search Wikipedia"),
    LOGIN_JOIN("//XCUIElementTypeStaticText[@name=\"Log in / Join Wikipedia\"]");

    private final String locator;

    ExplorerPageElements(String locator) {
        this.locator = locator;
    }

    public String getLocator() {
        return locator;
    }

    public static String resolve(String name) {
        try {
            return ExplorerPageElements.valueOf(name).getLocator();
        } catch (IllegalArgumentException e) {
            return name; // Not a constant, return as-is
        }
    }
}

// Usage in Page Object
public String resolveLocator(String constantName) {
    return ExplorerPageElements.resolve(constantName);
}
```

---

## Summary

1. **Page Objects** define element locators as constants
2. **Page Objects** provide `resolveLocator()` method to map constant names to actual locators
3. **Step Definitions** call `resolveLocator()` before using any element name
4. **BasePage** remains generic - no page-specific knowledge
5. **Feature Files** can use either constant names or direct locators

This approach maintains:
- ✅ Single source of truth for locators
- ✅ Readable feature files
- ✅ Easy maintenance
- ✅ Proper separation of concerns

---

**Related Documentation:**
- [BasePage Method Chain](BASEPAGE_METHOD_CHAIN.md)
- [Page Object Model](PAGE_OBJECT_MODEL.md)

---

**Last Updated:** 2025-10-29
