# BasePage Method Chain Documentation

This document provides a visual guide to understanding how methods flow from step definitions through the BasePage class to Appium driver calls.

## Table of Contents
- [Overview](#overview)
- [Smart Locator Detection](#smart-locator-detection)
- [Complete Method Flow](#complete-method-flow)
- [Method Chains by Action Type](#method-chains-by-action-type)
- [Usage Examples](#usage-examples)

---

## Overview

The BasePage class (`src/main/java/mobile/automation/pages/base/BasePage.java`) provides a unified interface for interacting with mobile elements using either **Accessibility IDs** or **XPath** locators. The smart detection system automatically determines the locator type and routes to the appropriate methods.

---

## Smart Locator Detection

### isXPath() Method
**Location:** `BasePage.java:408-410`

Automatically detects if a locator string is an XPath expression:

```java
private boolean isXPath(String locator) {
    return locator != null && (locator.startsWith("/") || locator.startsWith("("));
}
```

**Detection Rules:**
- **XPath**: Starts with `/`, `//`, or `(`
  - Examples: `//android.widget.Button`, `//*[@text='Login']`, `(//div)[1]`
- **Accessibility ID**: Everything else
  - Examples: `Login Button`, `search-field`, `Submit`

---

## Complete Method Flow

```
FEATURE FILE (Gherkin)
│
├─ "Login Button"              (Accessibility ID)
├─ "//android.widget.Button"   (XPath)
└─ "//XCUIElementTypeButton"   (XPath)
                                     │
                                     ▼
                            ┌────────────────────────┐
                            │   STEP DEFINITION      │
                            │  page.isElementVisible()│
                            │  page.click()          │
                            │  page.enterText()      │
                            └────────────────────────┘
                                     │
                                     ▼
                            ┌────────────────────────┐
                            │   BASEPAGE (Public)    │
                            │  Smart Methods         │
                            │  - isElementVisible()  │
                            │  - click()             │
                            │  - enterText()         │
                            └────────────────────────┘
                                     │
                        ┌────────────┴────────────┐
                        │    isXPath(locator)?    │
                        │    Line 408-410         │
                        └─────────────────────────┘
                        │                         │
                   YES  │                         │  NO
                        ▼                         ▼
        ┌───────────────────────────┐   ┌─────────────────────────────┐
        │  XPath Methods            │   │  Accessibility ID Methods   │
        │  - tapByXPath()           │   │  - tapByAccessibilityId()   │
        │  - isElementDisplayed     │   │  - sendKeysByAccessibility  │
        │    ByXPath()              │   │    Id()                     │
        │                           │   │  - isElementDisplayedBy     │
        └───────────────────────────┘   │    AccessibilityId()        │
                        │               └─────────────────────────────┘
                        │                         │
                        └────────────┬────────────┘
                                     ▼
                            ┌────────────────────────┐
                            │  Core Protected Methods│
                            │  - tap(element)        │
                            │  - sendKeys(element)   │
                            │  - isElementDisplayed()│
                            └────────────────────────┘
                                     │
                                     ▼
                            ┌────────────────────────┐
                            │  Wait Methods          │
                            │  - waitForElementToBe  │
                            │    Clickable()         │
                            │  - waitForElementToBe  │
                            │    Visible()           │
                            └────────────────────────┘
                                     │
                                     ▼
                            ┌────────────────────────┐
                            │  WebDriverWait         │
                            │  (10 sec timeout)      │
                            └────────────────────────┘
                                     │
                                     ▼
                            ┌────────────────────────┐
                            │  WebElement Actions    │
                            │  - element.click()     │
                            │  - element.sendKeys()  │
                            │  - element.isDisplayed()│
                            └────────────────────────┘
                                     │
                                     ▼
                            ┌────────────────────────┐
                            │  APPIUM DRIVER         │
                            │  (Native Mobile API)   │
                            └────────────────────────┘
```

---

## Method Chains by Action Type

### 1. TAP/CLICK Action Chain

```
┌─────────────────────────────────────────────────────────────┐
│ STEP DEFINITION                                             │
│ page.click("Login Button")                                  │
│ page.click("//android.widget.Button[@text='Submit']")       │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │   click()       │
                    │   Line 421-423  │
                    │   (Public)      │
                    └─────────────────┘
                              │
              ┌───────────────┴───────────────┐
              │                               │
              ▼                               ▼
┌──────────────────────────┐    ┌──────────────────────────┐
│ tapByAccessibilityId()   │    │ tapByXPath()             │
│ Line 52-55               │    │ Line 61-64               │
│ (Protected)              │    │ (Protected)              │
└──────────────────────────┘    └──────────────────────────┘
              │                               │
              │    ┌──────────────────────┐   │
              └───>│ tap(element)         │<──┘
                   │ Line 43-46           │
                   │ (Protected)          │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ waitForElementToBe   │
                   │ Clickable()          │
                   │ Line 198-200         │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ element.click()      │
                   └──────────────────────┘
```

**Methods Involved:**
- `click(String)` - Line 421-423 (Public, Smart)
- `tapByAccessibilityId(String)` - Line 52-55 (Protected)
- `tapByXPath(String)` - Line 61-64 (Protected)
- `tap(WebElement)` - Line 43-46 (Protected)
- `waitForElementToBeClickable(WebElement)` - Line 198-200 (Protected)

---

### 2. VISIBILITY CHECK Chain

```
┌─────────────────────────────────────────────────────────────┐
│ STEP DEFINITION                                             │
│ page.isElementVisible("Search Field")                       │
│ page.isElementVisible("//XCUIElementTypeButton")            │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │isElementVisible()│
                    │   Line 436-442  │
                    │   (Public)      │
                    └─────────────────┘
                              │
              ┌───────────────┴───────────────┐
              │                               │
              ▼                               ▼
┌──────────────────────────┐    ┌──────────────────────────┐
│ isElementDisplayedBy     │    │ isElementDisplayedBy     │
│ AccessibilityId()        │    │ XPath()                  │
│ Line 160-167             │    │ Line 174-181             │
│ (Protected)              │    │ (Protected)              │
└──────────────────────────┘    └──────────────────────────┘
              │                               │
              │    ┌──────────────────────┐   │
              └───>│ isElementDisplayed() │<──┘
                   │ Line 146-153         │
                   │ (Protected)          │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ waitForElementToBe   │
                   │ Visible()            │
                   │ Line 190-192         │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ element.isDisplayed()│
                   └──────────────────────┘
```

**Methods Involved:**
- `isElementVisible(String)` - Line 436-442 (Public, Smart)
- `isElementDisplayedByAccessibilityId(String)` - Line 160-167 (Protected)
- `isElementDisplayedByXPath(String)` - Line 174-181 (Protected)
- `isElementDisplayed(WebElement)` - Line 146-153 (Protected)
- `waitForElementToBeVisible(WebElement)` - Line 190-192 (Protected)

---

### 3. TEXT INPUT Chain

```
┌─────────────────────────────────────────────────────────────┐
│ STEP DEFINITION                                             │
│ page.enterText("username", "john@example.com")              │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │  enterText()    │
                    │   Line 428      │
                    │   (Public)      │
                    └─────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ sendKeysByAccessibility│
                   │ Id()                 │
                   │ Line 86-89           │
                   │ (Protected)          │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ sendKeys(element,    │
                   │ text)                │
                   │ Line 75-79           │
                   │ (Protected)          │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ waitForElementToBe   │
                   │ Visible()            │
                   │ Line 190-192         │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ element.clear()      │
                   │ element.sendKeys()   │
                   └──────────────────────┘
```

**Methods Involved:**
- `enterText(String, String)` - Line 428 (Public)
- `sendKeysByAccessibilityId(String, String)` - Line 86-89 (Protected)
- `sendKeys(WebElement, String)` - Line 75-79 (Protected)
- `waitForElementToBeVisible(WebElement)` - Line 190-192 (Protected)

---

### 4. GET TEXT Chain

```
┌─────────────────────────────────────────────────────────────┐
│ STEP DEFINITION                                             │
│ String text = page.getText("Welcome Message")              │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
                    ┌─────────────────┐
                    │  getText()      │
                    │   Line 426-428  │
                    │   (Public)      │
                    └─────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ getTextByAccessibility│
                   │ Id()                 │
                   │ Line 121-124         │
                   │ (Protected)          │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ getText(element)     │
                   │ Line 111-114         │
                   │ (Protected)          │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ waitForElementToBe   │
                   │ Visible()            │
                   │ Line 190-192         │
                   └──────────────────────┘
                              │
                              ▼
                   ┌──────────────────────┐
                   │ element.getText()    │
                   └──────────────────────┘
```

**Methods Involved:**
- `getText(String)` - Line 426-428 (Public)
- `getTextByAccessibilityId(String)` - Line 121-124 (Protected)
- `getText(WebElement)` - Line 111-114 (Protected)
- `waitForElementToBeVisible(WebElement)` - Line 190-192 (Protected)

---

## Usage Examples

### Example 1: Using Accessibility ID

```gherkin
# Feature File
Given I am on the login page
When I enter "john@example.com" into "username"
And I click "Login Button"
Then I verify "Welcome Message" is visible
```

```java
// Step Definition
@When("I enter {string} into {string}")
public void enterTextIntoField(String text, String field) {
    page.enterText(field, text);
    // Calls: enterText() → sendKeysByAccessibilityId() → sendKeys()
}

@When("I click {string}")
public void clickElement(String element) {
    page.click(element);
    // Calls: click() → tapByAccessibilityId() → tap()
}

@Then("I verify {string} is visible")
public void verifyElementVisible(String element) {
    Assert.assertTrue(page.isElementVisible(element));
    // Calls: isElementVisible() → isElementDisplayedByAccessibilityId()
}
```

---

### Example 2: Using XPath

```gherkin
# Feature File
Given I am on the product page
When I click "//android.widget.Button[@text='Add to Cart']"
Then I verify "//android.widget.TextView[@text='Item added']" is visible
```

```java
// Step Definition (SAME as Example 1!)
@When("I click {string}")
public void clickElement(String element) {
    page.click(element);
    // Detects XPath → tapByXPath() → tap()
}

@Then("I verify {string} is visible")
public void verifyElementVisible(String element) {
    Assert.assertTrue(page.isElementVisible(element));
    // Detects XPath → isElementDisplayedByXPath()
}
```

---

### Example 3: Mixed Usage in Same Test

```gherkin
# Feature File - Mix of Accessibility IDs and XPath
Scenario: Search for a product
  Given I am on the home page
  When I click "Search Icon"
  And I enter "iPhone" into "//android.widget.EditText[@resource-id='search_field']"
  And I click "//android.widget.Button[@text='Search']"
  Then I verify "Search Results" is visible
```

The **same step definitions** handle both locator types automatically!

---

## Method Visibility Levels

### Public Methods (Step Definition Layer)
Used directly in Cucumber step definitions:
- `click(String)` - Smart click with auto-detection
- `enterText(String, String)` - Enter text into field
- `getText(String)` - Get text from element
- `isElementVisible(String)` - Check visibility with auto-detection
- `isElementVisibleByXPath(String)` - Explicit XPath visibility check
- `isElementEnabled(String)` - Check if element is enabled
- `swipeUp()`, `swipeDown()`, `swipeLeft()`, `swipeRight()` - Swipe gestures
- `scrollToElement(String)` - Scroll to element
- `hideKeyboard()` - Hide keyboard

### Protected Methods (Page Object Layer)
Used within page classes that extend BasePage:
- `tap(WebElement)` - Tap on element
- `tapByAccessibilityId(String)` - Tap by accessibility ID
- `tapByXPath(String)` - Tap by XPath
- `sendKeys(WebElement, String)` - Send keys to element
- `sendKeysByAccessibilityId(String, String)` - Send keys by accessibility ID
- `getText(WebElement)` - Get text from element
- `getTextByAccessibilityId(String)` - Get text by accessibility ID
- `isElementDisplayed(WebElement)` - Check if element is displayed
- `isElementDisplayedByAccessibilityId(String)` - Check visibility by accessibility ID
- `isElementDisplayedByXPath(String)` - Check visibility by XPath
- `waitForElementToBeVisible(WebElement)` - Wait for element visibility
- `waitForElementToBeClickable(WebElement)` - Wait for element to be clickable

### Private Methods (Internal Helpers)
Internal use only:
- `isXPath(String)` - Detect if locator is XPath
- `swipe(int, int, int, int, int)` - Generic swipe implementation

---

## Key Benefits

1. **Unified Interface**: Use the same step definitions for both Accessibility IDs and XPath
2. **Automatic Detection**: No need to specify locator type explicitly
3. **Wait Strategies**: Built-in explicit waits for all interactions
4. **Error Handling**: Try-catch blocks prevent test failures on missing elements
5. **Code Reusability**: Share step definitions across different platforms (iOS/Android)
6. **Maintainability**: Update locator strategy without changing step definitions

---

## Related Documentation

- [Project Structure](PROJECT_STRUCTURE.md)
- [Cucumber Tags](CUCUMBER_TAGS.md)
- [Hooks](HOOKS.md)
- [Scripts](SCRIPTS.md)

---

**Last Updated:** 2025-10-28
