# WebDriverAgent Troubleshooting Guide

## Overview
WebDriverAgent (WDA) is a critical component for iOS testing with Appium. The error code 65 from xcodebuild indicates that WDA failed to build, typically due to code signing issues or Xcode configuration problems.

## Quick Diagnostics

Run these commands to diagnose your setup:

```bash
# Check overall iOS setup
make verify-ios

# Check WDA specific configuration
make check-wda

# Get setup instructions
make setup-wda
```

## Common Error: Code 65 (xcodebuild failed)

### Cause
This error occurs when WebDriverAgent cannot be built, usually due to:
1. **Missing code signing** - WDA needs to be signed with a valid Apple Developer team ID
2. **Xcode not configured** - First-time WDA setup requires manual Xcode configuration
3. **Team ID not set** - Even free Apple IDs need to be configured in Xcode

### Solution: Initial WDA Setup

#### Step 1: Install Appium XCUITest Driver
```bash
appium driver install xcuitest
```

#### Step 2: Locate WebDriverAgent Project
```bash
# Find the WDA location
appium driver list --installed

# Typical location:
# ~/.appium/node_modules/appium-xcuitest-driver/node_modules/appium-webdriveragent
```

#### Step 3: Open WDA in Xcode
```bash
open ~/.appium/node_modules/appium-xcuitest-driver/node_modules/appium-webdriveragent/WebDriverAgent.xcodeproj
```

> **Note:** The exact path may vary based on your Appium installation. Use `appium driver list --installed` to find it.

#### Step 4: Configure Code Signing in Xcode

1. **Select the WebDriverAgentRunner target**
   - In the project navigator (left sidebar), select the WebDriverAgent project
   - In the main area, select "WebDriverAgentRunner" from the targets list

2. **Go to Signing & Capabilities tab**
   - Click on the "Signing & Capabilities" tab at the top

3. **Enable automatic signing**
   - Check the box: "Automatically manage signing"
   - Select your Team from the dropdown
     - If you don't see a team, click "Add an Account" to add your Apple ID
     - You can use a free Apple ID (no paid developer account needed)

4. **Repeat for WebDriverAgentLib target**
   - Select "WebDriverAgentLib" target
   - Enable "Automatically manage signing"
   - Select the same Team

5. **Build the project**
   ```
   Press Cmd+B to build
   ```
   - The build should succeed without errors
   - If you see signing errors, ensure your Apple ID is verified in Xcode preferences

#### Step 5: Verify the Setup
```bash
# Run your tests again
mvn test -Dcucumber.filter.tags="@smoke" -Dtest=TestRunner

# Or using make
make test-smoke
```

## Advanced Troubleshooting

### Issue: "No provisioning profiles found"

**Solution:**
1. Open Xcode Preferences (Cmd+,)
2. Go to Accounts tab
3. Select your Apple ID
4. Click "Download Manual Profiles" or "Refresh"
5. Try building WDA again in Xcode

### Issue: "Team ID not found"

**Solution:**
```bash
# Find your Team ID
security find-identity -v -p codesigning

# Add to your test configuration if needed
# (Usually automatic if configured in Xcode)
```

### Issue: "Derived Data Path errors"

The framework uses a custom derived data path for faster WDA setup. If this causes issues:

**Edit** [IOSCapabilities.java](../src/main/java/mobile/automation/config/IOSCapabilities.java:55):

```java
// Comment out or remove this line if causing issues
// options.setDerivedDataPath(derivedDataPath);
```

### Issue: "usePrebuiltWDA not working"

If WDA keeps rebuilding:

1. Ensure WDA was successfully built in Xcode at least once
2. Check the capability in [IOSCapabilities.java](../src/main/java/mobile/automation/config/IOSCapabilities.java:52):
   ```java
   options.setCapability("usePrebuiltWDA", true);  // Reuse existing WDA
   ```

## Real Device Setup

Testing on real iOS devices requires additional configuration:

### Prerequisites
- Paid Apple Developer account ($99/year) or free provisioning profile
- Device UDID
- Team ID from developer.apple.com

### Configuration

1. **Get your device UDID:**
   ```bash
   idevice_id -l
   # or
   xcrun xctrace list devices
   ```

2. **Get your Team ID:**
   - Login to https://developer.apple.com
   - Go to Account > Membership
   - Copy your Team ID

3. **Update test configuration:**

   Edit [config.properties](../src/main/resources/config.properties):
   ```properties
   # Uncomment and set these values
   ios.udid=YOUR_DEVICE_UDID
   ios.xcodeOrgId=YOUR_TEAM_ID
   ios.xcodeSigningId=iPhone Developer
   ```

4. **Update WDA Bundle ID in Xcode:**
   - Open WDA project in Xcode
   - Select WebDriverAgentRunner target
   - Change Bundle Identifier to something unique (e.g., com.yourname.WebDriverAgentRunner)
   - Enable automatic signing with your Team

## Debugging Tips

### 1. Enable Xcode Logs
The framework is now configured to show Xcode logs for easier debugging:

[IOSCapabilities.java](../src/main/java/mobile/automation/config/IOSCapabilities.java:64):
```java
options.setShowXcodeLog(true);  // Shows detailed WDA build logs
```

### 2. Check Appium Server Logs
When running tests, check the Appium server console for detailed error messages:
```bash
# Run Appium with debug logs
appium --log-level debug
```

### 3. Increase Timeouts
If WDA takes time to build/launch:

Edit [config.properties](../src/main/resources/config.properties):
```properties
ios.wdaLaunchTimeout=120000      # Increase to 120 seconds
ios.wdaConnectionTimeout=120000
```

### 4. Clean Build and Retry
```bash
# Clean derived data
rm -rf ~/Library/Developer/Xcode/DerivedData/WebDriverAgent*

# Rebuild WDA in Xcode
# Then retry your tests
```

## Verification Checklist

Before running tests, verify:

- [ ] Xcode is installed and up to date
- [ ] Xcode Command Line Tools installed: `xcode-select --install`
- [ ] iOS Simulator is available: `xcrun simctl list devices`
- [ ] Appium is running: `lsof -i :4723`
- [ ] XCUITest driver is installed: `appium driver list --installed`
- [ ] WDA builds successfully in Xcode (Cmd+B)
- [ ] Code signing is configured for WebDriverAgentRunner
- [ ] Your Apple ID is verified in Xcode

## Quick Commands Reference

```bash
# Verify entire iOS setup
make verify-ios

# Check WDA configuration
make check-wda

# See WDA setup instructions
make setup-wda

# Run smoke tests
make test-smoke

# Run with Xcode logs enabled (already enabled in code)
mvn test -Dcucumber.filter.tags="@smoke"
```

## Common WDA Errors and Solutions

| Error Code | Issue | Solution |
|------------|-------|----------|
| 65 | Code signing failure | Configure signing in Xcode for WebDriverAgentRunner |
| 70 | Provisioning profile issue | Add Apple ID in Xcode Preferences > Accounts |
| Connection timeout | WDA not launching | Increase `wdaLaunchTimeout` in config.properties |
| "No such file" | WDA not found | Run `appium driver install xcuitest` |
| Session creation failed | WDA crashed | Clean derived data and rebuild WDA in Xcode |

## Additional Resources

- [Appium XCUITest Driver Documentation](https://appium.io/docs/en/drivers/ios-xcuitest/)
- [WebDriverAgent Setup Guide](https://appium.io/docs/en/drivers/ios-xcuitest-real-devices/)
- [iOS Setup Guide](./ios_setup_guide.md)

## Still Having Issues?

1. Run `make check-wda` and share the output
2. Check Appium server logs for detailed error messages
3. Verify Xcode build succeeds manually (Cmd+B in WDA project)
4. Ensure your iOS Simulator version matches the `platformVersion` in config
