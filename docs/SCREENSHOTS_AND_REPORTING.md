# Screenshots and Reporting Guide

## Overview

This framework provides **automatic** and **manual** screenshot capture capabilities integrated with Cucumber and Allure reports.

---

## 📸 Screenshot Capabilities

### 1. Automatic Screenshots (via Hooks) ✅

Screenshots are **automatically** captured in these scenarios:

| Trigger | Implementation | Status |
|---------|---------------|--------|
| **Test Failure** | `@After` hook | ✅ Always enabled |
| **@screenshot Tag** | `@After("@screenshot")` hook | ✅ Tag-based |
| **End of Scenario** | Can be enabled in hooks | ⚠️ Optional |

### 2. Manual Screenshots (via Step Definitions) ✅

Take screenshots **on-demand** during test execution:

```gherkin
When I take a screenshot
When I capture a screenshot
```

---

## 🎯 When Should Screenshots Be Taken?

### Automatic Screenshots (Recommended)

#### 1. On Test Failure (Always Enabled)
```java
// Hooks.java - Line 148
@After
public void tearDown(Scenario scenario) {
    if (scenario.isFailed() && driver != null) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "Screenshot on Failure");
    }
}
```

**When it triggers:**
- ✅ Any assertion failure
- ✅ Any exception during test
- ✅ Element not found errors
- ✅ Timeout exceptions

**Where screenshots go:**
- 📁 Attached to Cucumber HTML report
- 📊 Attached to Allure report
- 📹 Saved alongside test results

---

#### 2. With @screenshot Tag
```gherkin
@screenshot
Scenario: Verify login page layout
  Given I am on the login page
  Then I should see "Login Button"
```

```java
// Hooks.java - Line 182
@After("@screenshot")
public void takeScreenshot(Scenario scenario) {
    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    scenario.attach(screenshot, "image/png", "Scenario Screenshot");
}
```

**Use cases:**
- ✅ Visual regression testing
- ✅ Documenting UI states
- ✅ Creating test evidence
- ✅ Debugging specific scenarios

---

### Manual Screenshots (Use Sparingly)

#### 3. On-Demand via Step Definition

```gherkin
@debugging
Scenario: Debug complex workflow
  Given I am on the Explorer page
  When I take a screenshot
  When I tap on "Profile Button"
  And I take a screenshot
  Then I should see "Settings"
  And I capture a screenshot
```

**Implementation:** CommonSteps.java (Lines 205-237)
```java
@When("I take a screenshot")
@When("I capture a screenshot")
public void iTakeScreenshot() {
    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    String fileName = "manual_screenshot_" + System.currentTimeMillis();

    // Save to file system
    saveScreenshotToFile(screenshot, fileName);

    // Attach to Allure if available
    Allure.addAttachment(fileName, "image/png", "png", screenshot);
}
```

**Where screenshots are saved:**
```
target/
├── screenshots/
│   ├── manual_screenshot_1698765432000.png
│   ├── manual_screenshot_1698765433000.png
│   └── manual_screenshot_1698765434000.png
├── allure-results/
│   └── [screenshots attached as attachments]
└── cucumber-reports/
    └── [screenshots embedded in HTML]
```

**Use cases:**
- ✅ Debugging complex scenarios
- ✅ Capturing intermediate states
- ✅ Before/after comparisons
- ✅ Manual test evidence

---

## 📋 Screenshot Strategy Best Practices

### ✅ DO Use Automatic Screenshots For:

1. **All Test Failures** (Default - Already enabled)
   ```gherkin
   Scenario: Login with invalid credentials
     When I enter "wrong@email.com" into "username"
     And I tap on "Login Button"
     Then I should see "Error Message"
     # ✅ Screenshot captured automatically if this fails
   ```

2. **Visual Validation Tests** (Use @screenshot tag)
   ```gherkin
   @screenshot @visual
   Scenario: Verify dashboard layout
     Given I am on the dashboard
     Then all widgets should be visible
     # ✅ Screenshot captured at end of scenario
   ```

3. **Critical User Journeys** (Use @screenshot tag)
   ```gherkin
   @screenshot @smoke @critical
   Scenario: Complete purchase flow
     Given I add item to cart
     When I proceed to checkout
     And I complete payment
     Then I should see "Order Confirmation"
     # ✅ Screenshot captured for evidence
   ```

---

### ⚠️ Use Manual Screenshots Sparingly For:

1. **Debugging Complex Workflows**
   ```gherkin
   @debug
   Scenario: Debug multi-step process
     When I navigate to settings
     And I take a screenshot
     When I toggle dark mode
     And I take a screenshot
     When I return to home
     And I take a screenshot
   ```

2. **Before/After State Verification**
   ```gherkin
   Scenario: Verify state change
     Given I am on the profile page
     When I capture a screenshot
     And I update my profile picture
     Then I capture a screenshot
     # Compare both screenshots manually
   ```

---

### ❌ DON'T Use Screenshots For:

1. **Every Test Step** (Too many screenshots)
   ```gherkin
   # ❌ BAD - Too many screenshots!
   When I take a screenshot
   And I tap on "Button"
   And I take a screenshot
   And I enter "text" into "field"
   And I take a screenshot
   ```

2. **Performance Tests** (Slows down execution)
   ```gherkin
   # ❌ BAD - Screenshots slow down tests
   @performance
   Scenario: Load test
     When I take a screenshot  # ❌ Adds overhead
   ```

3. **Passing Tests Without Evidence Need**
   ```gherkin
   # ❌ BAD - Screenshot not needed for passing test
   @screenshot
   Scenario: Simple assertion
     Then I should see "Title"
   ```

---

## 🎨 Screenshot Types Comparison

| Type | Trigger | Location | Use Case | Overhead |
|------|---------|----------|----------|----------|
| **Failure Screenshots** | Test fails | Cucumber/Allure reports | Debugging | Low |
| **Tag-based Screenshots** | @screenshot tag | Cucumber/Allure reports | Evidence/Docs | Medium |
| **Manual Screenshots** | Step definition | target/screenshots + Allure | Debugging | High |

---

## 📊 Integration with Reports

### Cucumber HTML Report

Screenshots are **embedded** in the HTML report:

```
target/
└── cucumber-reports/
    └── cucumber.html  ← Screenshots embedded here
```

**View:** Open `target/cucumber-reports/cucumber.html` in browser

---

### Allure Report

Screenshots are **attached** as separate files:

```
allure-results/
├── [uuid]-attachment.png
├── [uuid]-attachment.png
└── ...
```

**Generate report:**
```bash
mvn allure:report
./scripts/open-report.sh
```

**View:** http://localhost:8765

---

## 🧹 Automatic Cleanup

### Screenshots Cleanup (Enabled by Default)

Screenshots from previous test runs are **automatically cleaned** before each new test session.

**Implementation:** `Hooks.java @BeforeAll` (Lines 209-243)

```java
@BeforeAll
public static void startAppiumServer() {
    cleanOldScreenshots();  // ✅ Cleans target/screenshots/
    cleanOldAllureResults(); // ✅ Cleans allure-results/
}
```

**What gets cleaned:**
- ✅ `target/screenshots/` - All manual screenshots
- ✅ `allure-results/` - All Allure attachments
- ✅ Keeps directories themselves

**Control via system property:**
```bash
# Enable cleanup (default)
mvn test

# Disable cleanup (keep old screenshots)
mvn test -Dscreenshots.clean=false

# Disable both cleanups
mvn test -Dscreenshots.clean=false -Dallure.clean=false
```

**Why cleanup is important:**
1. ✅ Prevents disk space issues
2. ✅ Avoids confusion between old and new results
3. ✅ Keeps test artifacts fresh
4. ✅ Easier to find relevant screenshots

---

## 🛠️ Configuration

### Enable/Disable Screenshots

#### Disable @screenshot Hook (Not Recommended)
```java
// Hooks.java
// @After("@screenshot")  ← Comment out
public void takeScreenshot(Scenario scenario) {
    // ...
}
```

#### Always Take Screenshots (All Scenarios)
```java
// Hooks.java
@After
public void tearDown(Scenario scenario) {
    // Always take screenshot, not just on failure
    if (driver != null) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", "End of Scenario");
    }
}
```

---

## 📝 Example Scenarios

### Example 1: Debug Scenario with Manual Screenshots
```gherkin
@debug @manual-screenshots
Scenario: Debug profile update flow
  Given I am on the Explorer page
  When I tap on "Profile Button"
  And I capture a screenshot
  Then I should see "Login Join Link"
  And I should see "Settings"
  When I tap on "Settings"
  And I capture a screenshot
  Then I should see "Account Settings"
```

**Result:**
- 📁 `target/screenshots/manual_screenshot_*.png` (2 files)
- 📊 Screenshots attached to Allure report
- ✅ Automatic failure screenshot if test fails

---

### Example 2: Visual Validation with @screenshot Tag
```gherkin
@screenshot @visual @smoke
Scenario: Verify Explorer page layout
  Given I am on the Explorer page
  Then I should see "HEADER_TODAY"
  And I should see "SEARCH_FIELD"
  And I should see "TAB_PLACES"
```

**Result:**
- 📊 Screenshot captured at end of scenario
- 📸 Attached to Cucumber and Allure reports
- ✅ Evidence of UI state

---

### Example 3: Automatic Failure Screenshot
```gherkin
@smoke @login
Scenario: Login with invalid credentials
  Given I am on the login page
  When I enter "invalid@email.com" into "username"
  And I enter "wrongpass" into "password"
  And I tap on "Login Button"
  Then I should see "Invalid credentials"
```

**Result:**
- ✅ If fails: Screenshot automatically captured
- 📊 Screenshot shows state at failure
- 🔍 Helps debug what went wrong

---

## 🎯 Recommendations

### For Development/Debugging
```gherkin
# Use manual screenshots liberally
@debug
Scenario: Debug complex issue
  When I take a screenshot
  # ... steps
  And I take a screenshot
```

### For Smoke Tests
```gherkin
# Use @screenshot tag for evidence
@smoke @screenshot
Scenario: Critical user journey
  # ... steps
  # Screenshot captured at end
```

### For Regression Tests
```gherkin
# Rely on automatic failure screenshots
@regression
Scenario: Verify functionality
  # ... steps
  # Screenshot only if test fails
```

### For CI/CD Pipelines
```bash
# Keep automatic screenshots only
# Disable manual screenshots (remove steps)
# Enable Allure report generation
mvn test -Dallure.report=true
```

---

## 🚀 Quick Reference

| Need | Solution |
|------|----------|
| **Screenshot on failure** | ✅ Already enabled (automatic) |
| **Screenshot specific scenario** | Add `@screenshot` tag |
| **Screenshot during test** | Use `When I take a screenshot` |
| **View screenshots** | Open Cucumber HTML or Allure report |
| **Disable screenshots** | Comment out hook (not recommended) |

---

## 🔗 Related Documentation

- [Hooks Documentation](HOOKS.md)
- [Cucumber Tags](CUCUMBER_TAGS.md)
- [Quick Start Guide](QUICK_START.md)

---

**Last Updated:** 2025-10-29
