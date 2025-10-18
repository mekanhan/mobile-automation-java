# Wikipedia Tips Page Auto-Handling

## Overview

The Wikipedia iOS app shows an onboarding/tips page on **first launch** after a fresh installation. This page consists of 4 screens introducing users to Wikipedia features.

This framework **automatically handles and skips** the tips page, ensuring tests can run on both:
- ✅ Fresh app installations (`noReset=false`, `fullReset=true`)
- ✅ Existing app sessions (tips already dismissed)

## Tips Page Structure

Based on the app's XML structure:

```
┌─────────────────────────────────────┐
│                                     │
│      [Puzzle Globe Image]           │
│                                     │
│   "The free encyclopedia"           │
│                                     │
│   Wikipedia is written              │
│   collaboratively...                │
│                                     │
│   [Learn more about Wikipedia]      │
│                                     │
│  [Skip]         ◉◯◯◯        [Next] │
│              page 1 of 4            │
└─────────────────────────────────────┘
```

### Elements:
- **Skip Button**: Bypasses all 4 pages (fastest)
- **Next Button**: Advances to next page
- **Page Indicator**: Shows current page (1-4)
- **Content**: Introduction text and images

## Implementation

### 1. IOSTipsPage Class

**Location:** `src/main/java/mobile/automation/pages/app1/IOSTipsPage.java`

**Key Methods:**

```java
// Detect if tips page is showing
boolean isOnTipsPage()

// Skip tips immediately (fastest)
void skipTips()

// Safe conditional skip (recommended)
boolean skipIfPresent()

// Alternative: go through all pages
void goThroughAllPages()
```

**Element Locators:**
```java
private final By skipButton = AppiumBy.name("Skip");
private final By nextButton = AppiumBy.name("Next");
private final By pageIndicator = AppiumBy.iOSClassChain("**/XCUIElementTypePageIndicator");
```

### 2. Automatic Handling in Hooks

**Location:** `src/test/java/hooks/Hooks.java`

The framework automatically handles tips in the `@Before` hook:

```java
@Before
public void setUp(Scenario scenario) {
    // Initialize driver
    driver = DriverManager.initializeDriver(platform);

    // Auto-skip tips page if present
    handleTipsPageIfPresent();

    // Continue with test setup...
}
```

**Flow:**
```
Driver Init → Check for Tips Page → Skip if Present → Continue Test
```

## How It Works

### Scenario 1: Fresh Install (noReset=false)
```
1. Driver starts
2. App launches → Tips page appears
3. Framework detects tips page
4. Clicks "Skip" button
5. App navigates to Explorer page
6. Test begins
```

**Console Output:**
```
Tips page detected on first launch
Skipping Wikipedia tips page...
Tips page skipped successfully
```

### Scenario 2: Existing Session (noReset=true)
```
1. Driver starts
2. App launches → No tips page (already dismissed)
3. Framework checks for tips page
4. Tips not found → continues silently
5. Test begins immediately
```

**Console Output:**
```
No tips page detected (app previously launched)
```

## Configuration

### Current Settings (config.properties)

```properties
# App launches fresh each time → Tips will appear
ios.noReset=false
ios.fullReset=false
```

**Effect:** Tips page appears on every test run

### Alternative Configurations

#### Keep App State (No Tips After First Run)
```properties
ios.noReset=true      # Don't reinstall app
ios.fullReset=false   # Keep app data
```
**Effect:** Tips appear only on very first test run

#### Complete Reset (Tips Every Time)
```properties
ios.noReset=false     # Reinstall app
ios.fullReset=true    # Clear all data
```
**Effect:** Tips appear on every test run (cleanest state)

## Usage Examples

### Automatic (Current Implementation)
No code needed in feature files:

```gherkin
Feature: Wikipedia Explorer

  @smoke
  Scenario: Verify Explorer page
    Given I am on the Explorer page
    # Tips are already handled automatically!
    Then I should see "Today Header"
```

### Manual (If Needed)

If you want explicit control in specific scenarios:

```java
// In step definitions
IOSTipsPage tipsPage = new IOSTipsPage(driver);

// Option 1: Skip if present (safe)
tipsPage.skipIfPresent();

// Option 2: Force skip (assumes tips are there)
if (tipsPage.isOnTipsPage()) {
    tipsPage.skipTips();
}

// Option 3: Go through all pages
tipsPage.goThroughAllPages();
```

## Benefits

✅ **Zero Manual Intervention** - Fully automatic
✅ **Safe for All Scenarios** - Works with/without tips page
✅ **Fast** - Immediate skip (no navigation through pages)
✅ **Reliable** - Multiple detection methods
✅ **Clean State** - Every test starts from Explorer page
✅ **No Test Changes** - Existing tests work without modification

## Troubleshooting

### Issue: Tips page not being skipped

**Check:**
1. Verify `noReset` setting in config.properties
2. Check console for "Tips page detected" message
3. Ensure iOS version compatibility (tested on iOS 18.6)

**Debug:**
```java
IOSTipsPage tipsPage = new IOSTipsPage(driver);
System.out.println("Tips page visible: " + tipsPage.isOnTipsPage());
System.out.println("Skip button visible: " + tipsPage.isSkipButtonVisible());
```

### Issue: Test fails waiting for Explorer page

**Possible Cause:** Tips page handling failed

**Solution:** Add explicit wait after driver init:
```java
Thread.sleep(2000); // Allow time for tips handling
```

### Issue: Want to test the tips page itself

**Solution:** Disable automatic handling:

**Option 1 - Temporarily comment out in Hooks:**
```java
@Before
public void setUp(Scenario scenario) {
    driver = DriverManager.initializeDriver(platform);
    // handleTipsPageIfPresent(); // Commented for tips testing
    // ... rest of setup
}
```

**Option 2 - Create separate test:**
```gherbal
@tips-test
Scenario: Verify tips page content
  # Don't use "I am on Explorer page" - that assumes tips are skipped
  Then I should see tips page
  When I click Next
  Then page indicator should show "page 2 of 4"
```

## Future Enhancements

Possible improvements:

1. **Configurable behavior** - Enable/disable via config property
   ```properties
   ios.skipTipsPage=true
   ```

2. **Different skip strategies** - Choose via config
   ```properties
   tips.skipStrategy=immediate  # or "navigate-through"
   ```

3. **Tips page validation** - Optional verification before skipping
   ```java
   tipsPage.verifyContent(); // Ensures tips page is correct
   tipsPage.skipTips();
   ```

4. **Analytics** - Track how often tips appear
   ```java
   TestMetrics.incrementTipsPageAppearances();
   ```

## Related Files

- **Page Object:** `src/main/java/mobile/automation/pages/app1/IOSTipsPage.java`
- **Hooks:** `src/test/java/hooks/Hooks.java`
- **XML Reference:** `src/test/resources/xml_pages/wikipedia_tips_page.xml`
- **Config:** `src/main/resources/config.properties`

## Summary

The tips page handling is:
- ✅ Fully automatic
- ✅ Non-intrusive
- ✅ Safe and reliable
- ✅ Zero maintenance

**You can forget it exists** - it just works! 🎉
