# Screen Recording Guide

Automatically record test executions with video playback in reports.

---

## Features

- ✅ **iOS Simulator Recording** - Built-in using `xcrun simctl`
- ✅ **Android Emulator/Device Recording** - Using `adb screenrecord`
- ✅ **Automatic Video Attachment** - Videos attached to Allure/Cucumber reports
- ✅ **Smart Naming** - Format: `{platform}_{scenarioName}_{timestamp}.mp4`
- ✅ **Example**: `iOS_verify_explorer_page_elements_20250118_123045.mp4`

---

## Prerequisites

### iOS Recording
- macOS with Xcode installed
- iOS Simulator running
- **No additional tools needed** - uses built-in `xcrun simctl`

### Android Recording
- Android SDK installed
- `adb` available in PATH
- Android Emulator or real device connected

---

## Usage

### Enable Screen Recording

Add `-Dscreen.recording=true` to your test command:

```bash
# Run tests with screen recording
mvn test -Dcucumber.filter.tags="@navigation1" -Dscreen.recording=true
```

### Without Screen Recording (Default)

```bash
# Normal test execution (no recording)
mvn test -Dcucumber.filter.tags="@navigation1"
```

---

## Configuration Examples

### Run smoke tests with recording
```bash
mvn test -Dcucumber.filter.tags="@smoke" -Dscreen.recording=true
```

### Run all tests with recording
```bash
mvn test -Dscreen.recording=true
```

### Run specific platform with recording
```bash
# iOS tests with recording
mvn test -Dcucumber.filter.tags="@ios" -Dscreen.recording=true

# Android tests with recording
mvn test -Dcucumber.filter.tags="@android" -Dscreen.recording=true
```

---

## Video Output

### Location
All recordings are saved to:
```
target/recordings/
```

### File Naming Convention
Format: `{platform}_{cleaned_scenario_name}_{timestamp}.mp4`

**Examples:**
- `iOS_verify_explorer_page_elements_visible_20250118_123045.mp4`
- `Android_successful_login_valid_credentials_20250118_124030.mp4`

### File Structure
```
target/
└── recordings/
    ├── iOS_scenario1_20250118_120000.mp4
    ├── iOS_scenario2_20250118_120130.mp4
    └── Android_scenario1_20250118_120230.mp4
```

---

## Viewing Recordings

### In Allure Report
Videos are automatically attached to test results:

1. Run tests with recording
2. Generate Allure report:
   ```bash
   mvn allure:report
   open target/allure-report/index.html
   ```
3. Click on any test → See attached video

### In ExtentReports
Videos are attached as `video/mp4` MIME type

### Direct File Access
```bash
# Open recordings folder
open target/recordings/

# Play specific video
open target/recordings/iOS_test_20250118_120000.mp4
```

---

## Advanced Usage

### Clean Old Recordings

Recordings can accumulate. Clean old files programmatically:

```java
// In your test cleanup
ScreenRecorder.cleanOldRecordings(7); // Keep last 7 days
```

### Custom File Names

The framework generates names automatically from scenario names, but you can customize in code:

```java
String fileName = ScreenRecorder.generateFileName(
    "wikipedia",  // App name
    "iOS",        // Platform
    "explorer",   // Feature
    1             // Scenario number
);
// Result: wikipedia_iOS_explorer_1
```

---

## Technical Details

### iOS Recording
- Uses `xcrun simctl io recordVideo`
- Codec: H.264
- No frame rate limit
- Stops when test completes

### Android Recording
- Uses `adb shell screenrecord`
- Bit rate: 4 Mbps
- Max duration: Android's default (3 minutes, extendable)
- Video pulled from device after recording

---

## Troubleshooting

### iOS: "xcrun: error: unable to find utility"
**Solution:** Install Xcode Command Line Tools
```bash
xcode-select --install
```

### Android: "screenrecord not found"
**Solution:** Ensure Android SDK is installed and adb is in PATH
```bash
# Check adb
adb version

# Add to PATH (macOS/Linux)
export PATH=$PATH:$ANDROID_HOME/platform-tools
```

### Video not attached to report
**Causes:**
1. Recording not enabled (`-Dscreen.recording=true` missing)
2. Device/Simulator not running
3. Insufficient disk space

**Check console output for:**
```
📹 Screen recording started: target/recordings/...
⏹️  Screen recording stopped: target/recordings/...
📹 Video attached to report: target/recordings/...
```

### Recording fails silently
Enable debug logging:
```bash
mvn test -X -Dscreen.recording=true
```

---

## Performance Impact

- **iOS**: Minimal impact (~2-5% CPU)
- **Android**: Low impact (~5-10% CPU)
- **Storage**: ~1-5 MB per minute depending on complexity

**Recommendation:** Enable only for critical tests or CI/CD pipelines

---

## Best Practices

1. **Use for failed tests** - Consider recording only failures:
   ```java
   // Custom hook example
   if (scenario.isFailed() && recordingEnabled) {
       stopAndAttachRecording(scenario);
   }
   ```

2. **Periodic cleanup** - Delete old recordings regularly:
   ```bash
   # Keep last 7 days
   find target/recordings -name "*.mp4" -mtime +7 -delete
   ```

3. **CI/CD Integration** - Always enable in CI:
   ```yaml
   # GitHub Actions example
   - name: Run Tests
     run: mvn test -Dscreen.recording=true
   ```

4. **Selective Recording** - Use tags:
   ```bash
   # Record only @critical tests
   mvn test -Dcucumber.filter.tags="@critical" -Dscreen.recording=true
   ```

---

## Limitations

### iOS
- Requires Simulator (not available for real devices without additional setup)
- macOS only

### Android
- Max 3-minute duration per recording (can be extended)
- Requires USB debugging enabled for real devices

---

## Integration with Reports

### Allure
✅ Automatically attached
- Videos appear in test attachments
- Playable inline in browser
- Download option available

### ExtentReports
✅ Automatically attached
- Embedded video player
- Full playback controls

### Cucumber HTML
⚠️ Link only (browser opens video)

---

## Examples

### Complete Test Run with Reports
```bash
# Run tests with recording, generate all reports
mvn clean test -Dcucumber.filter.tags="@smoke" -Dscreen.recording=true && \
mvn allure:report && \
open target/allure-report/index.html
```

### CI/CD Pipeline
```yaml
name: Test with Recording
on: [push]
jobs:
  test:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v2
      - name: Run Tests
        run: mvn test -Dscreen.recording=true
      - name: Upload Videos
        if: failure()
        uses: actions/upload-artifact@v2
        with:
          name: test-recordings
          path: target/recordings/*.mp4
```

---

## FAQ

**Q: Can I record on real iOS devices?**
A: Yes, but requires additional setup with `ideviceinstaller` and QuickTime. Simulator recording is recommended for automated tests.

**Q: Does recording slow down tests?**
A: Minimal impact. iOS: ~2-5%, Android: ~5-10% CPU overhead.

**Q: Can I disable recording for specific tests?**
A: Yes, simply don't pass `-Dscreen.recording=true`. Recording is opt-in.

**Q: Where are videos stored?**
A: `target/recordings/` directory.

**Q: Can I share recordings?**
A: Yes, they're standard MP4 files. Share the `target/recordings/` folder or individual files.

---

## Support

For issues or questions:
- Check console output for recording status
- Verify prerequisites are installed
- Run with `-X` for debug logs
- Check `target/recordings/` for generated files
