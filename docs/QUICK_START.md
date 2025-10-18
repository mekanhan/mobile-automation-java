# Quick Start Guide

Get up and running with Wikipedia iOS automation in 5 minutes!

## Prerequisites Check

```bash
# Verify all tools are installed
appium --version        # Should be 2.x
node --version          # Should be 16+
mvn --version          # Should be 3.8+
java -version          # Should be 17+
xcodebuild -version    # Should show Xcode version
```

---

## Setup Steps

### 1. Place Your App (2 minutes)

```bash
# Copy your Wikipedia.app to the framework
cp -R /path/to/Wikipedia.app src/main/resources/apps/ios/wikipedia.app

# Verify it's there
ls src/main/resources/apps/ios/wikipedia.app
```

### 2. Get Bundle ID (30 seconds)

```bash
# Extract bundle ID
/usr/libexec/PlistBuddy -c "Print :CFBundleIdentifier" \
  src/main/resources/apps/ios/wikipedia.app/Info.plist
```

### 3. Update Configuration (1 minute)

Edit `src/main/resources/config.properties`:

```properties
# Update ONLY this line with your actual bundle ID:
ios.bundleId=org.wikimedia.wikipedia  # ← Your bundle ID from step 2
```

Everything else is pre-configured for iPhone 16 Pro iOS 18.6!

---

## Run Tests (2 minutes)

### Terminal 1: Start Appium
```bash
appium --port 4723
```

### Terminal 2: Run Tests
```bash
# Install dependencies (first time only)
mvn clean install

# Run smoke tests
mvn test -Dcucumber.filter.tags="@smoke"
```

**That's it!** 🎉 Watch your tests run on the iPhone 16 Pro simulator.

---

## What You Get

✅ **Pre-configured for iPhone 16 Pro, iOS 18.6**
✅ **Optimized step definitions** - Atomic, reusable steps
✅ **Element mapping** - Business-friendly names
✅ **Automatic screenshots** on test failures
✅ **Comprehensive reporting** with Allure
✅ **Ready-to-use scenarios** for Wikipedia Explorer page

---

## Project Structure

```
mobile-automation-java/
├── src/
│   ├── main/
│   │   ├── java/mobile/automation/
│   │   │   ├── config/
│   │   │   │   └── IOSCapabilities.java      ← iPhone 16 Pro iOS 18.6
│   │   │   ├── driver/
│   │   │   │   └── DriverManager.java        ← Driver lifecycle
│   │   │   ├── pages/
│   │   │   │   ├── base/BasePage.java        ← Core actions
│   │   │   │   └── app1/IOSExplorerPage.java ← Wikipedia elements
│   │   │   └── utils/
│   │   │       └── WaitUtils.java            ← Wait strategies
│   │   └── resources/
│   │       ├── apps/
│   │       │   └── ios/
│   │       │       └── wikipedia.app         ← Place your app here
│   │       └── config.properties             ← Configuration
│   └── test/
│       ├── java/
│       │   ├── hooks/Hooks.java              ← Test lifecycle
│       │   └── steps/
│       │       ├── CommonSteps.java          ← Reusable steps
│       │       └── App1Steps.java            ← App-specific steps
│       └── resources/features/app1/
│           └── iosExplorer.feature           ← Test scenarios
└── docs/
    ├── ios_setup_guide.md                    ← Complete setup guide
    ├── step_definitions_strategy.md          ← Architecture details
    └── common_steps_reference.md             ← Step reference
```

---

## Available Test Scenarios

```gherkin
@smoke
Scenario: Verify Explorer page elements are visible
  Then I should see "Today Header"
  And I should see "Featured Article"
  And I should see "Search Field"

@smoke @search
Scenario: Search for an article
  When I tap on "Search Field"
  And I enter "Appium" into "Search Field"
  Then "Search Field" should contain text "Appium"

@navigation @tabs
Scenario: Navigate to Places tab
  When I navigate to "Places" tab
  Then I should see "Places Tab"
```

Run specific scenarios:
```bash
mvn test -Dcucumber.filter.tags="@smoke"
mvn test -Dcucumber.filter.tags="@search"
mvn test -Dcucumber.filter.tags="@navigation"
```

---

## Common Commands

```bash
# Run all iOS tests
mvn test -Dplatform=ios

# Run smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Run specific feature file
mvn test -Dcucumber.options="src/test/resources/features/app1/iosExplorer.feature"

# Generate Allure report
mvn allure:serve

# Clean and rebuild
mvn clean install
```

---

## Troubleshooting

### App not found?
```bash
# Verify app exists
ls -la src/main/resources/apps/ios/wikipedia.app

# Check permissions
chmod -R 755 src/main/resources/apps/ios/wikipedia.app
```

### Simulator not appearing?
```bash
# Open Simulator manually
open -a Simulator

# Select iPhone 16 Pro
# Hardware → Device → iOS 18.6 → iPhone 16 Pro
```

### Appium connection error?
```bash
# Check if Appium is running
lsof -ti:4723

# Restart Appium
kill -9 $(lsof -ti:4723)
appium --port 4723
```

### Wrong bundle ID?
```bash
# Get correct bundle ID
/usr/libexec/PlistBuddy -c "Print :CFBundleIdentifier" \
  src/main/resources/apps/ios/wikipedia.app/Info.plist

# Update config.properties with correct value
```

---

## Next Steps

1. **Review test scenarios:** `src/test/resources/features/app1/iosExplorer.feature`

2. **Explore common steps:** Check `docs/common_steps_reference.md` for all available steps

3. **Add more tests:** Use atomic steps to create new scenarios

4. **Read architecture guide:** `docs/step_definitions_strategy.md` explains the optimization strategy

5. **Customize configuration:** Edit `config.properties` for your needs

---

## Key Features

### ✨ Atomic Step Definitions
```gherkin
When I tap "element"
When I enter "text" into "field"
Then "element" should be visible
```

### 🗺️ Element Mapping
```java
// Friendly names in tests
When I tap "Search Field"

// Maps to actual locator in IOSExplorerPage
elementMap.put("Search Field", searchField);
```

### 🎯 Business-Readable Tests
```gherkin
# ✅ Good
Scenario: Search for article
  When I search for "Appium"
  Then I should see search results

# ❌ Not like this
Scenario: Search for article
  When I tap xpath "//XCUIElement[@name='search']"
  And I enter text using sendKeys
```

---

## Documentation

- **Complete Setup Guide:** `docs/ios_setup_guide.md`
- **Step Definitions Strategy:** `docs/step_definitions_strategy.md`
- **Common Steps Reference:** `docs/common_steps_reference.md`
- **App Placement Guide:** `src/main/resources/apps/README.md`
- **POM Guide:** `docs/pom_guide.md`

---

## Configuration Highlights

### iPhone 16 Pro iOS 18.6 Settings
```properties
ios.deviceName=iPhone 16 Pro
ios.platformVersion=18.6
ios.automationName=XCUITest
```

### Performance Optimizations
```properties
ios.useNewWDA=false           # Reuse WebDriverAgent
ios.wdaLaunchTimeout=60000    # 60s timeout
ios.autoAcceptAlerts=true     # Auto-accept alerts
```

### Test Behavior
```properties
ios.noReset=false     # Fresh install each test
ios.fullReset=false   # Don't uninstall after test
```

---

## Getting Help

**Issue: Test failing?**
- Check Appium server logs for errors
- Review test console output
- Enable debug logging: `log.level=DEBUG` in config.properties

**Issue: Element not found?**
- Verify element name matches mapping in IOSExplorerPage
- Check XML dump: `src/test/resources/xml_pages/wikipedia_Explorer_page.xml`
- Review element locators

**Issue: Configuration problem?**
- Validate config.properties syntax
- Ensure bundle ID matches your app
- Verify app path is correct

---

**Ready to test!** 🚀

Run your first test now:
```bash
# Terminal 1
appium --port 4723

# Terminal 2
mvn test -Dcucumber.filter.tags="@smoke"
```

Watch the magic happen! ✨
