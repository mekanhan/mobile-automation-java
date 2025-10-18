# Common Steps Quick Reference

## Overview
This is a quick reference guide for all available atomic step definitions in `CommonSteps.java`. These steps can be used across **all** feature files and pages without creating new step definitions.

---

## 🎯 Action Steps

### Click/Tap Actions
```gherkin
When I click on "element_name"
When I tap "element_name"
When I tap on "element_name"
```

**Example:**
```gherkin
When I tap "Search Field"
When I click on "Profile Button"
When I tap on "Save Button"
```

---

### Text Entry
```gherkin
When I enter "text" into "field_name"
When I type "text" into "field_name"
When I clear and enter "text" into "field_name"
```

**Example:**
```gherkin
When I enter "Appium" into "Search Field"
When I type "test@example.com" into "Email Field"
When I clear and enter "new text" into "Username Field"
```

---

### Wait Actions
```gherkin
When I wait for "element_name" to appear
When I wait for "element_name" to be visible
When I wait for "element_name" to disappear
When I wait for {int} seconds
```

**Example:**
```gherkin
When I wait for "Loading Spinner" to appear
When I wait for "Loading Spinner" to disappear
When I wait for "Success Message" to be visible
When I wait for 3 seconds
```

---

## 📱 Mobile Gestures

### Swipe Actions
```gherkin
When I swipe up
When I swipe down
When I swipe left
When I swipe right
```

**Example:**
```gherkin
When I swipe up
And I swipe up
Then I should see "Bottom Content"
```

---

### Scroll Actions
```gherkin
When I scroll to "element_name"
```

**Example:**
```gherkin
When I scroll to "Terms and Conditions"
Then "Terms and Conditions" should be visible
```

---

## ✅ Assertion Steps

### Visibility Assertions
```gherkin
Then I should see "element_name"
Then "element_name" should be visible
Then "element_name" is visible
Then I should not see "element_name"
Then "element_name" should not be visible
```

**Example:**
```gherkin
Then I should see "Welcome Message"
And "Login Button" should be visible
But "Error Message" should not be visible
```

---

### State Assertions
```gherkin
Then "element_name" should be enabled
```

**Example:**
```gherkin
Then "Submit Button" should be enabled
And "Save Button" should be visible
```

---

### Text Assertions
```gherkin
Then "element_name" should contain text "expected_text"
Then "element_name" contains "expected_text"
Then "element_name" should have text "exact_text"
Then "element_name" text is "exact_text"
Then the page should contain "text"
```

**Example:**
```gherkin
Then "Article Title" should contain text "Neutral Milk"
And "Article Subtitle" contains "American indie"
And "Header" should have text "Wikipedia"
And the page should contain "Featured article"
```

---

## 🛠️ Utility Steps

### Keyboard
```gherkin
When I hide the keyboard
```

**Example:**
```gherkin
When I enter "search term" into "Search Field"
And I hide the keyboard
```

---

### Screenshots
```gherkin
When I take a screenshot
```

**Example:**
```gherkin
When I tap "Profile Button"
And I take a screenshot
```

---

## 📋 Complete Example Scenarios

### Example 1: Simple Navigation
```gherkin
Scenario: Navigate to profile
  Given I am on the Explorer page
  When I tap "Profile Button"
  Then "Profile Header" should be visible
  And I should see "User Name"
```

### Example 2: Search Flow
```gherkin
Scenario: Search for article
  Given I am on the Explorer page
  When I tap on "Search Field"
  And I enter "Mobile Testing" into "Search Field"
  And I hide the keyboard
  Then "Search Field" should contain text "Mobile Testing"
  And "Search Results" should be visible
```

### Example 3: Form Submission
```gherkin
Scenario: Submit login form
  Given I am on the Login page
  When I enter "user@test.com" into "Email Field"
  And I enter "password123" into "Password Field"
  And I tap "Login Button"
  Then I should see "Dashboard"
  And "Welcome Message" should contain text "user@test.com"
```

### Example 4: Scrolling and Interaction
```gherkin
Scenario: Accept terms and conditions
  Given I am on the Registration page
  When I scroll to "Terms Checkbox"
  And I tap "Terms Checkbox"
  And I scroll to "Submit Button"
  And I tap "Submit Button"
  Then "Confirmation Message" should be visible
```

### Example 5: Multiple Verifications
```gherkin
Scenario: Verify page elements
  Given I am on the Explorer page
  Then I should see "Wikipedia Logo"
  And "Search Field" should be visible
  And "Tabs Button" should be enabled
  And "Featured Article" is visible
  And "Today Header" should have text "Today"
```

### Example 6: Complex Flow with Waits
```gherkin
Scenario: Save article after loading
  Given I am on the Explorer page
  When I wait for "Featured Article" to appear
  And I tap on "Article Image"
  And I wait for 2 seconds
  Then "Article Content" should be visible
  When I tap "Save Button"
  And I wait for "Saved Confirmation" to appear
  Then "Saved Confirmation" should contain text "Saved"
  And I wait for "Saved Confirmation" to disappear
```

---

## 🎨 Style Guidelines

### Element Names
- ✅ **DO:** Use descriptive, business-friendly names
  - `"Search Field"`, `"Submit Button"`, `"Welcome Message"`
- ❌ **DON'T:** Use technical IDs or selectors
  - `"search_input_id"`, `"btn_submit"`, `"//XCUIElement"`

### Readability
- ✅ **DO:** Write natural, flowing scenarios
  ```gherkin
  When I tap "Login Button"
  And I wait for "Dashboard" to appear
  Then I should see "Welcome Message"
  ```
- ❌ **DON'T:** Write technical, implementation-focused scenarios
  ```gherkin
  When I click element at xpath "//button[@id='login']"
  And I wait 5 seconds
  Then element with id "dashboard" is displayed
  ```

---

## 🔧 Customization

### Adding New Common Steps

If you need a new atomic step that could be reused across pages:

1. **Add to CommonSteps.java:**
```java
@When("I long press {string}")
public void iLongPress(String elementName) {
    currentPage.longPress(elementName);
}
```

2. **Implement in BasePage.java:**
```java
public void longPress(String elementName) {
    By locator = getLocatorFor(elementName);
    // Long press implementation
}
```

3. **Use in any feature file:**
```gherkin
When I long press "Menu Item"
```

---

## 📊 Step Categories Summary

| Category | Count | Examples |
|----------|-------|----------|
| **Actions** | 6 | tap, enter, wait, scroll |
| **Gestures** | 5 | swipe up/down/left/right, scroll |
| **Assertions** | 8 | should see, should be visible, should contain text |
| **Utilities** | 3 | hide keyboard, screenshot, wait seconds |

**Total Atomic Steps: ~22**

---

## 💡 Tips

1. **Combine atomic steps** for complex workflows
2. **Use explicit waits** instead of hard waits when possible
3. **Name elements consistently** across all pages
4. **Keep scenarios focused** on one feature/behavior
5. **Reuse these steps** before creating custom ones

---

## 🚀 Next Steps

1. Review `iosExplorer.feature` for real examples
2. Check `step_definitions_strategy.md` for architecture details
3. Start writing scenarios using these atomic steps
4. Add page-specific steps only when needed

---

**Happy Testing!** 🎉
