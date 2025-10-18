# iOS Testing Setup Guide
Complete guide for setting up and running iOS tests on iPhone 16 Pro with iOS 18.6

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [App Placement](#app-placement)
3. [Configuration](#configuration)
4. [Running Tests](#running-tests)
5. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### 1. Xcode & Command Line Tools
```bash
# Check Xcode is installed
xcodebuild -version

# Install Command Line Tools
xcode-select --install
```

### 2. Node.js & npm
```bash
# Check Node.js (v16+ required)
node --version

# Check npm
npm --version
```

### 3. Appium 2.x
```bash
# Install Appium 2.x globally
npm install -g appium

# Install XCUITest driver
appium driver install xcuitest

# Verify installation
appium driver list
```

### 4. Java Development Kit
```bash
# Check Java version (17+ required)
java -version

# Set JAVA_HOME if needed
export JAVA_HOME=$(/usr/libexec/java_home)
```

### 5. Maven
```bash
# Check Maven
mvn --version
```

---

## App Placement

### Step 1: Build Wikipedia App in Xcode

#### For iOS Simulator (.app file)

1. **Open project in Xcode**

2. **Select Simulator as destination:**
   - Choose "Any iOS Simulator (arm64)" from device dropdown
   - OR Select specific simulator: iPhone 16 Pro

3. **Build the app:**
   ```bash
   # From terminal
   cd /path/to/wikipedia-ios

   # Clean build
   xcodebuild clean -project Wikipedia.xcodeproj -scheme Wikipedia

   # Build for simulator
   xcodebuild build -project Wikipedia.xcodeproj \
     -scheme Wikipedia \
     -sdk iphonesimulator \
     -configuration Debug \
     -derivedDataPath build
   ```

4. **Locate the .app file:**
   ```bash
   # Find the built .app
   find ~/Library/Developer/Xcode/DerivedData -name "Wikipedia.app" -path "*/Build/Products/Debug-iphonesimulator/*"

   # OR if using custom derivedDataPath
   find ./build -name "Wikipedia.app"
   ```

### Step 2: Copy App to Framework

```bash
# Navigate to framework directory
cd /path/to/mobile-automation-java

# Copy .app to framework
cp -R /path/to/Wikipedia.app src/main/resources/apps/ios/wikipedia.app

# Verify it's there
ls -la src/main/resources/apps/ios/
```

### Step 3: Get Bundle ID

```bash
# Extract bundle ID from app
/usr/libexec/PlistBuddy -c "Print :CFBundleIdentifier" \
  src/main/resources/apps/ios/wikipedia.app/Info.plist
```

Output example: `org.wikimedia.wikipedia`

---

## Configuration

### 1. Update config.properties

Edit `src/main/resources/config.properties`:

```properties
# Platform
platform=ios

# Appium Server
appium.server.url=http://127.0.0.1:4723

# iOS Device (Simulator)
ios.deviceName=iPhone 16 Pro
ios.platformVersion=18.6
ios.automationName=XCUITest

# App Configuration
ios.app.path=src/main/resources/apps/ios/wikipedia.app
ios.bundleId=org.wikimedia.wikipedia  # YOUR ACTUAL BUNDLE ID

# iOS Settings
ios.noReset=false  # Fresh install each test
ios.autoAcceptAlerts=true
```

### 2. Verify iOS Simulator

```bash
# List available simulators
xcrun simctl list devices available | grep "iPhone 16 Pro"

# Expected output:
# iPhone 16 Pro (UUID) (Shutdown)
```

If iPhone 16 Pro with iOS 18.6 doesn't exist:

```bash
# Create new simulator
xcrun simctl create "iPhone 16 Pro" "iPhone 16 Pro" iOS18.6

# OR download iOS 18.6 runtime first
xcodebuild -downloadPlatform iOS -buildVersion 18.6
```

---

## Running Tests

### 1. Start Appium Server

**Terminal 1:**
```bash
# Start Appium server
appium --port 4723 --log-level info

# You should see:
# [Appium] Welcome to Appium v2.x.x
# [Appium] Appium REST http interface listener started on 0.0.0.0:4723
```

### 2. Run Tests

**Terminal 2:**
```bash
cd /path/to/mobile-automation-java

# Install dependencies (first time only)
mvn clean install -DskipTests

# Run all iOS tests
mvn clean test -Dplatform=ios

# Run smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"

# Run specific feature
mvn test -Dcucumber.options="src/test/resources/features/app1/iosExplorer.feature"

# Run with custom bundle ID
mvn test -Dplatform=ios -Dios.bundleId=org.wikimedia.wikipedia
```

### 3. View Results

```bash
# Generate Allure report
mvn allure:serve

# Open in browser
open target/site/allure-maven-plugin/index.html
```

---

## Troubleshooting

### Issue 1: "WebDriverAgent not found"

**Symptom:**
```
Error: Could not find WebDriverAgent
```

**Solution:**
```bash
# Install WebDriverAgent manually
cd /usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent

# Open in Xcode
open WebDriverAgent.xcodeproj

# Build for Testing (Cmd+Shift+U)
# OR from command line:
xcodebuild build-for-testing \
  -project WebDriverAgent.xcodeproj \
  -scheme WebDriverAgentRunner \
  -sdk iphonesimulator
```

### Issue 2: "App not found at path"

**Symptom:**
```
Error: App '/path/to/wikipedia.app' could not be found
```

**Solution:**
```bash
# Verify app exists
ls -la src/main/resources/apps/ios/wikipedia.app

# Check path in capabilities
# Use absolute path if relative doesn't work
/full/path/to/mobile-automation-java/src/main/resources/apps/ios/wikipedia.app

# Verify app is valid
xcrun simctl install booted src/main/resources/apps/ios/wikipedia.app
```

### Issue 3: "Simulator not booting"

**Symptom:**
```
Error: Timed out waiting for device to boot
```

**Solution:**
```bash
# Boot simulator manually
xcrun simctl boot "iPhone 16 Pro"

# OR use Simulator app
open -a Simulator

# Check simulator status
xcrun simctl list devices | grep "iPhone 16 Pro"

# Reset simulator if needed
xcrun simctl shutdown all
xcrun simctl erase "iPhone 16 Pro"
```

### Issue 4: "Invalid bundle ID"

**Symptom:**
```
Error: App with bundle identifier 'com.mekan.wikipedia' not found
```

**Solution:**
```bash
# Get actual bundle ID
/usr/libexec/PlistBuddy -c "Print :CFBundleIdentifier" \
  src/main/resources/apps/ios/wikipedia.app/Info.plist

# Update in config.properties
ios.bundleId=ACTUAL_BUNDLE_ID_HERE

# OR pass via command line
mvn test -Dios.bundleId=org.wikimedia.wikipedia
```

### Issue 5: "Connection refused to Appium"

**Symptom:**
```
Error: Could not connect to Appium server at http://127.0.0.1:4723
```

**Solution:**
```bash
# Check if Appium is running
lsof -ti:4723

# If no output, start Appium
appium --port 4723

# If port is busy, kill process and restart
kill -9 $(lsof -ti:4723)
appium --port 4723
```

### Issue 6: "Tests pass but app doesn't appear"

**Symptom:**
- Tests run successfully
- No errors
- But app UI not visible

**Solution:**
```properties
# In config.properties, ensure:
ios.isHeadless=false  # Show simulator window

# In terminal, open Simulator manually
open -a Simulator

# Select correct device
# Hardware → Device → iOS 18.6 → iPhone 16 Pro
```

### Issue 7: "Maven dependencies not resolving"

**Solution:**
```bash
# Clean Maven cache
mvn dependency:purge-local-repository

# Re-download dependencies
mvn clean install

# Update Maven
brew upgrade maven

# Check internet connection for Maven Central
```

---

## Quick Start Checklist

- [ ] Xcode installed with Command Line Tools
- [ ] Node.js & npm installed
- [ ] Appium 2.x installed globally
- [ ] XCUITest driver installed
- [ ] Java 17+ configured
- [ ] Maven installed
- [ ] Wikipedia.app built for simulator
- [ ] App copied to `src/main/resources/apps/ios/wikipedia.app`
- [ ] Bundle ID verified and configured
- [ ] config.properties updated
- [ ] iPhone 16 Pro iOS 18.6 simulator exists
- [ ] Appium server running on port 4723
- [ ] Tests executed successfully

---

## Running Your First Test

```bash
# Terminal 1: Start Appium
appium --port 4723

# Terminal 2: Run smoke tests
cd /path/to/mobile-automation-java
mvn test -Dcucumber.filter.tags="@smoke"

# Watch the simulator launch and tests execute!
```

---

## Advanced Configuration

### Custom Appium Server URL
```bash
mvn test -Dappium.server.url=http://192.168.1.100:4723
```

### Run on Real Device
```properties
# In config.properties
ios.udid=YOUR_DEVICE_UDID
ios.xcodeOrgId=YOUR_TEAM_ID
ios.xcodeSigningId=iPhone Developer

# Get UDID
xcrun xctrace list devices

# Get Team ID from Xcode
# Xcode → Preferences → Accounts → Your Team → View Details
```

### Performance Tuning
```properties
# Reuse WDA to speed up tests
ios.useNewWDA=false

# Set derived data path for WDA caching
# (done automatically in IOSCapabilities.java)
```

---

## Need Help?

1. **Check Appium logs:** Look for errors in Appium server output
2. **Check Test logs:** Review console output during test execution
3. **Enable debug logging:** Set `log.level=DEBUG` in config.properties
4. **Review capabilities:** IOSCapabilities.printCapabilities() shows all settings

---

**Happy Testing!** 🚀
