# Element Map Pattern - Implementation Guide

## Overview

This document explains the **Element Map Pattern** - the recommended approach for managing element locators in this automation framework.

---

## Why Element Map Pattern?

### ✅ Advantages

1. **Clean Architecture** - Each page manages its own elements
2. **No Type Casting** - No `instanceof` checks needed
3. **Flexible** - Supports constants, friendly names, and direct locators
4. **Scalable** - Easy to add new pages without modifying common code
5. **Simple** - Easy to understand and maintain

### ❌ Avoids These Problems

- ❌ Hardcoded strings scattered everywhere
- ❌ Fragile `instanceof` checks in step definitions
- ❌ Central resolver that needs updating for every page
- ❌ Type casting and null checks

---

## How It Works

```
FEATURE FILE                    PAGE OBJECT                     BASE PAGE
┌─────────────────┐            ┌──────────────────┐            ┌─────────────────┐
│ "HEADER_TODAY"  │  ────────> │ ELEMENT_MAP      │  ────────> │ Smart Detection │
│                 │            │ .get("HEADER_    │            │ XPath vs        │
│                 │            │  TODAY")         │            │ AccessibilityId │
│                 │            │ → "Today"        │            │                 │
└─────────────────┘            └──────────────────┘            └─────────────────┘
        │                               │                               │
        │                               │                               ▼
        │                               │                      ┌─────────────────┐
        │                               │                      │ Appium Driver   │
        │                               │                      │ findElement()   │
        └───────────────────────────────┴──────────────────────> isDisplayed()  │
                                                                └─────────────────┘
```

---

## Implementation

### 1. Page Object: IOSExplorerPage.java

#### Step 1: Define Element Constants

```java
public class IOSExplorerPage extends BasePage {

    // ============================================
    // ELEMENT IDENTIFIERS
    // ============================================

    private static final String HEADER_TODAY = "Today";
    private static final String TAB_PLACES = "Places";
    private static final String SEARCH_FIELD = "Search Wikipedia";
    private static final String LOGIN_JOIN = "//XCUIElementTypeStaticText[@name=\"Log in / Join Wikipedia\"]";
```

#### Step 2: Create Element Map

```java
    // ============================================
    // ELEMENT LOCATOR MAP
    // ============================================

    private static final Map<String, String> ELEMENT_MAP;

    static {
        ELEMENT_MAP = new HashMap<>();

        // Constant names (from feature files)
        ELEMENT_MAP.put("HEADER_TODAY", HEADER_TODAY);
        ELEMENT_MAP.put("TAB_PLACES", TAB_PLACES);
        ELEMENT_MAP.put("SEARCH_FIELD", SEARCH_FIELD);

        // Friendly names (alternative names)
        ELEMENT_MAP.put("Today Header", HEADER_TODAY);
        ELEMENT_MAP.put("Places Tab", TAB_PLACES);
        ELEMENT_MAP.put("Search Field", SEARCH_FIELD);
    }
```

#### Step 3: Provide Getter Method

```java
    /**
     * Gets element locator by friendly name or constant name
     * If name not in map, returns the name itself (direct locator)
     */
    public String getElementLocator(String elementName) {
        return ELEMENT_MAP.getOrDefault(elementName, elementName);
    }
```

#### Step 4: Override isElementVisible (Optional but Recommended)

```java
    /**
     * Checks if element is visible
     * Automatically resolves element names using the map
     */
    @Override
    public boolean isElementVisible(String elementName) {
        String locator = getElementLocator(elementName);
        return super.isElementVisible(locator);
    }
```

---

### 2. Step Definitions: CommonSteps.java

**No resolver needed!** Just call page methods directly:

```java
public class CommonSteps {

    private BasePage currentPage;

    /**
     * Verify element is visible
     * Element name can be: constant, friendly name, or direct locator
     */
    @Then("I should see {string}")
    public void iShouldSee(String elementName) {
        Assert.assertTrue(currentPage.isElementVisible(elementName),
                "Element '" + elementName + "' is not visible");
    }

    /**
     * Click/Tap on element
     * Element name can be: constant, friendly name, or direct locator
     */
    @When("I tap on {string}")
    public void iTapOn(String elementName) {
        currentPage.click(elementName);
    }
}
```

**That's it!** No `instanceof` checks, no resolvers, no type casting.

---

### 3. BasePage: Already Handles Everything

```java
public abstract class BasePage {

    /**
     * Smart detection - automatically handles XPath vs Accessibility ID
     */
    public boolean isElementVisible(String locator) {
        if (isXPath(locator)) {
            return isElementDisplayedByXPath(locator);
        } else {
            return isElementDisplayedByAccessibilityId(locator);
        }
    }

    private boolean isXPath(String locator) {
        return locator != null && (locator.startsWith("/") || locator.startsWith("("));
    }
}
```

---

## Usage Examples

### Example 1: Using Constants

```gherkin
@smoke @verification
Scenario: Verify page elements using constants
  Given I am on the Explorer page
  Then I should see "HEADER_TODAY"
  And I should see "TAB_PLACES"
  And I should see "SEARCH_FIELD"
  When I tap on "TABS_BUTTON"
```

**Flow:**
1. `"HEADER_TODAY"` → `IOSExplorerPage.getElementLocator("HEADER_TODAY")`
2. Map lookup → `"Today"`
3. `BasePage.isElementVisible("Today")`
4. Smart detection → Not XPath → `isElementDisplayedByAccessibilityId("Today")`

---

### Example 2: Using Friendly Names

```gherkin
@smoke @friendly-names
Scenario: Verify page elements using friendly names
  Given I am on the Explorer page
  Then I should see "Today Header"
  And I should see "Places Tab"
  And I should see "Search Field"
```

**Flow:**
1. `"Today Header"` → `IOSExplorerPage.getElementLocator("Today Header")`
2. Map lookup → `"Today"`
3. Same as Example 1

---

### Example 3: Using Direct XPath

```gherkin
@smoke @xpath
Scenario: Verify elements using direct XPath
  Given I am on the Explorer page
  When I tap on "PROFILE_BUTTON"
  Then I should see "//XCUIElementTypeStaticText[@name='Settings']"
```

**Flow:**
1. `"//XCUIElementTypeStaticText[@name='Settings']"` → `getElementLocator(...)`
2. Not in map → Returns `"//XCUIElementTypeStaticText[@name='Settings']"`
3. `BasePage.isElementVisible("//XCUIElementTypeStaticText[@name='Settings']")`
4. Smart detection → IS XPath → `isElementDisplayedByXPath(...)`

---

### Example 4: Mixed Usage

```gherkin
@smoke @mixed
Scenario: Mix all three approaches
  Given I am on the Explorer page
  # Constant
  Then I should see "HEADER_TODAY"
  # Friendly name
  And I should see "Places Tab"
  # Direct accessibility ID
  And I should see "Tabs"
  # Direct XPath
  And I should see "//XCUIElementTypeButton[@label='Done']"
```

---

## Adding New Elements

### Step 1: Add Constant to Page Object

```java
// IOSExplorerPage.java
private static final String MY_NEW_BUTTON = "my-button-id";
```

### Step 2: Add to Element Map

```java
static {
    ELEMENT_MAP = new HashMap<>();
    // ... existing mappings

    // New element - constant name
    ELEMENT_MAP.put("MY_NEW_BUTTON", MY_NEW_BUTTON);

    // New element - friendly name (optional)
    ELEMENT_MAP.put("My Button", MY_NEW_BUTTON);
}
```

### Step 3: Use in Feature File

```gherkin
# Using constant
Then I should see "MY_NEW_BUTTON"

# Using friendly name
Then I should see "My Button"

# Using direct locator
Then I should see "my-button-id"
```

**No changes needed** in step definitions!

---

## Available Elements in IOSExplorerPage

### Header Elements
| Constant Name | Friendly Name | Actual Locator |
|--------------|---------------|----------------|
| `HEADER_TODAY` | `Today Header` | `"Today"` |
| `HEADER_FEATURED_ARTICLE` | `Featured Article` | `"Featured article"` |
| `HEADER_TOP_READ` | `Top Read` | `"Top read"` |

### Tab Bar Elements
| Constant Name | Friendly Name | Actual Locator |
|--------------|---------------|----------------|
| `TAB_EXPLORE` | `Explore Tab` | `"Explore"` |
| `TAB_PLACES` | `Places Tab` | `"Places"` |
| `TAB_SAVED` | `Saved Tab` | `"Saved"` |
| `TAB_HISTORY` | `History Tab` | `"History"` |
| `TAB_SEARCH` | `Search Tab` | `"Search"` |

### Navigation Elements
| Constant Name | Friendly Name | Actual Locator |
|--------------|---------------|----------------|
| `SEARCH_FIELD` | `Search Field` | `"Search Wikipedia"` |
| `TABS_BUTTON` | `Tabs Button` | `"Tabs"` |
| `PROFILE_BUTTON` | `Profile Button` | `"profile-button"` |
| `WIKIPEDIA_LOGO` | `Wikipedia Logo` | `"wikipedia"` |

### Profile Elements (XPath)
| Constant Name | Friendly Name | Actual Locator |
|--------------|---------------|----------------|
| `LOGIN_JOIN` | `Login Join Link` | `//XCUIElementTypeStaticText[@name="Log in / Join Wikipedia"]` |
| `DONATE` | `Donate` | `//XCUIElementTypeStaticText[@name="Donate"]` |
| `SETTINGS` | `Settings` | `//XCUIElementTypeStaticText[@name="Settings"]` |

---

## Complete Flow Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│ FEATURE FILE: Then I should see "HEADER_TODAY"                     │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ STEP DEFINITION: CommonSteps.iShouldSee("HEADER_TODAY")            │
│ Code: Assert.assertTrue(currentPage.isElementVisible(elementName)) │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ PAGE OBJECT: IOSExplorerPage.isElementVisible("HEADER_TODAY")      │
│ Code: String locator = getElementLocator("HEADER_TODAY")           │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ ELEMENT MAP: ELEMENT_MAP.get("HEADER_TODAY")                       │
│ Result: "Today"                                                     │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ PAGE OBJECT: return super.isElementVisible("Today")                │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ BASEPAGE: isElementVisible("Today")                                │
│ Code: if (isXPath("Today")) → false                                │
│       return isElementDisplayedByAccessibilityId("Today")           │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│ BASEPAGE: isElementDisplayedByAccessibilityId("Today")             │
│ Code: driver.findElement(AppiumBy.accessibilityId("Today"))        │
│       element.isDisplayed()                                         │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
                            ┌────────────────┐
                            │ true / false   │
                            └────────────────┘
```

---

## Creating New Page Objects

### Template for New Pages

```java
public class MyNewPage extends BasePage {

    // ============================================
    // ELEMENT IDENTIFIERS
    // ============================================

    private static final String LOGIN_BUTTON = "login-btn";
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    // ============================================
    // ELEMENT LOCATOR MAP
    // ============================================

    private static final Map<String, String> ELEMENT_MAP;

    static {
        ELEMENT_MAP = new HashMap<>();

        // Constants
        ELEMENT_MAP.put("LOGIN_BUTTON", LOGIN_BUTTON);
        ELEMENT_MAP.put("USERNAME_FIELD", USERNAME_FIELD);
        ELEMENT_MAP.put("PASSWORD_FIELD", PASSWORD_FIELD);

        // Friendly names
        ELEMENT_MAP.put("Login Button", LOGIN_BUTTON);
        ELEMENT_MAP.put("Username", USERNAME_FIELD);
        ELEMENT_MAP.put("Password", PASSWORD_FIELD);
    }

    // ============================================
    // CONSTRUCTOR
    // ============================================

    public MyNewPage(AppiumDriver driver) {
        super(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    // ============================================
    // ELEMENT LOCATOR RESOLVER
    // ============================================

    public String getElementLocator(String elementName) {
        return ELEMENT_MAP.getOrDefault(elementName, elementName);
    }

    @Override
    public boolean isElementVisible(String elementName) {
        String locator = getElementLocator(elementName);
        return super.isElementVisible(locator);
    }

    // ============================================
    // PAGE-SPECIFIC METHODS
    // ============================================

    public void login(String username, String password) {
        sendKeys(usernameField, username);
        sendKeys(passwordField, password);
        tap(loginButton);
    }
}
```

---

## Best Practices

### ✅ DO

1. **Define all element locators as constants**
   ```java
   private static final String MY_ELEMENT = "element-id";
   ```

2. **Provide both constant names and friendly names in map**
   ```java
   ELEMENT_MAP.put("MY_ELEMENT", MY_ELEMENT);
   ELEMENT_MAP.put("My Element", MY_ELEMENT);
   ```

3. **Use descriptive names**
   ```java
   // Good
   private static final String HEADER_TODAY = "Today";

   // Bad
   private static final String H1 = "Today";
   ```

4. **Group related elements**
   ```java
   // Header Elements
   ELEMENT_MAP.put("HEADER_TODAY", HEADER_TODAY);
   ELEMENT_MAP.put("HEADER_TOP_READ", HEADER_TOP_READ);

   // Tab Elements
   ELEMENT_MAP.put("TAB_PLACES", TAB_PLACES);
   ```

### ❌ DON'T

1. **Don't hardcode strings in methods**
   ```java
   // Bad
   public void clickLogin() {
       click("login-button");  // ❌ Hardcoded
   }

   // Good
   public void clickLogin() {
       tap(loginButton);  // ✅ Uses WebElement
   }
   ```

2. **Don't use instanceof checks**
   ```java
   // Bad - Don't do this!
   if (currentPage instanceof IOSExplorerPage) {
       // ...
   }
   ```

3. **Don't skip the map for new elements**
   ```java
   // Bad
   click("new-button");  // Not in map!

   // Good
   ELEMENT_MAP.put("NEW_BUTTON", "new-button");
   ```

---

## Benefits Summary

| Feature | Benefit |
|---------|---------|
| **Each page manages its own elements** | ✅ Clean separation of concerns |
| **No instanceof checks** | ✅ Type-safe, no fragile casting |
| **Supports multiple naming styles** | ✅ Constants, friendly names, direct locators |
| **Easy to add new pages** | ✅ No changes to common code |
| **Fallback to direct locators** | ✅ Flexible for dynamic XPath |
| **Works with BasePage smart detection** | ✅ Automatic XPath vs AccessibilityId handling |

---

## Related Documentation

- [BasePage Method Chain](BASEPAGE_METHOD_CHAIN.md)
- [Page Object Model Best Practices](PAGE_OBJECT_MODEL.md)
- [Quick Start Guide](QUICK_START.md)

---

**Last Updated:** 2025-10-29
