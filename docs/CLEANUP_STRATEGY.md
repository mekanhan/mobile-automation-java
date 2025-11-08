# Test Artifacts Cleanup Strategy

## Overview

The framework automatically cleans old test artifacts before each test session to ensure fresh results and prevent disk space issues.

---

## 🧹 What Gets Cleaned

### Automatic Cleanup (@BeforeAll)

When you run tests, the following directories are cleaned **before** the test suite starts:

| Directory | Content | Cleanup Method |
|-----------|---------|----------------|
| `target/screenshots/` | Manual & failure screenshots | `cleanOldScreenshots()` |
| `target/recordings/` | Screen recording videos | `cleanOldRecordings()` |
| `test-output/` | Cucumber embedded screenshots | `cleanOldScreenshots()` |
| `allure-results/` | Allure test results | `cleanOldAllureResults()` |

**Implementation:** `Hooks.java` (Lines 38-56)

```java
@BeforeAll
public static void startAppiumServer() {
    System.out.println("INITIALIZING TEST SUITE");

    // Clean old test artifacts
    cleanOldScreenshots();      // ✅ Screenshots + test-output
    cleanOldRecordings();       // ✅ Video recordings (NEW!)
    cleanOldAllureResults();    // ✅ Allure results

    // Start Appium server
    AppiumServerManager.startServer();
}
```

---

## 📁 Directory Structure

### Before Cleanup
```
mobile-automation-java/
├── target/
│   ├── screenshots/
│   │   ├── FAILURE_scenario1_1698765432000.png  ← Old
│   │   ├── FAILURE_scenario2_1698765433000.png  ← Old
│   │   └── TAGGED_scenario3_1698765434000.png   ← Old
│   ├── recordings/
│   │   ├── iOS_test_scenario1_20241029_120000.mp4  ← Old (18 MB)
│   │   ├── iOS_test_scenario2_20241029_120130.mp4  ← Old (12 MB)
│   │   └── iOS_test_scenario3_20241029_120245.mp4  ← Old (15 MB)
│   └── ...
├── test-output/
│   ├── embedded1.png             ← Old (Cucumber)
│   ├── embedded2.png             ← Old (Cucumber)
│   └── ...
├── allure-results/
│   ├── [uuid]-container.json     ← Old
│   ├── [uuid]-result.json        ← Old
│   ├── [uuid]-attachment.png     ← Old
│   └── ...
```

### After Cleanup
```
mobile-automation-java/
├── target/
│   ├── screenshots/              ← Empty (ready for new screenshots)
│   ├── recordings/               ← Empty (ready for new recordings)
│   └── ...
├── allure-results/               ← Empty (ready for new results)
```

---

## ⚙️ Configuration

### Control Cleanup via System Properties

#### Default Behavior (Cleanup Enabled)
```bash
# All cleanups enabled
mvn test

# Or explicitly
mvn test -Dscreenshots.clean=true -Drecordings.clean=true -Dallure.clean=true
```

#### Disable Screenshot Cleanup
```bash
# Keep old screenshots
mvn test -Dscreenshots.clean=false
```

**Use case:** Comparing screenshots across multiple test runs

#### Disable Recordings Cleanup
```bash
# Keep old video recordings
mvn test -Drecordings.clean=false
```

**Use case:** Building a video library of test executions

#### Disable Allure Cleanup
```bash
# Keep old Allure results
mvn test -Dallure.clean=false
```

**Use case:** Accumulating test history

#### Disable All Cleanups
```bash
# Keep everything
mvn test -Dscreenshots.clean=false -Drecordings.clean=false -Dallure.clean=false
```

---

## 🔍 Cleanup Methods

### 1. cleanOldScreenshots()

**Location:** `Hooks.java` (Lines 209-243)

```java
private static void cleanOldScreenshots() {
    String cleanScreenshots = System.getProperty("screenshots.clean", "true");

    if (!Boolean.parseBoolean(cleanScreenshots)) {
        System.out.println("⏭️  Skipping screenshots cleanup");
        return;
    }

    Path screenshotsPath = Paths.get("target/screenshots");

    if (Files.exists(screenshotsPath)) {
        System.out.println("🧹 Cleaning old screenshots...");
        Files.walk(screenshotsPath)
            .sorted(Comparator.reverseOrder())
            .filter(path -> !path.equals(screenshotsPath))
            .forEach(Files::delete);
        System.out.println("✅ Screenshots cleaned successfully");
    } else {
        System.out.println("📁 Creating screenshots directory");
        Files.createDirectories(screenshotsPath);
    }
}
```

**What it does:**
1. Checks system property `screenshots.clean` (default: true)
2. Deletes all files in `target/screenshots/`
3. Keeps the directory itself
4. Creates directory if it doesn't exist

---

### 2. cleanOldRecordings()

**Location:** `Hooks.java` (Lines 370-409)

```java
private static void cleanOldRecordings() {
    String cleanRecordings = System.getProperty("recordings.clean", "true");

    if (!Boolean.parseBoolean(cleanRecordings)) {
        System.out.println("⏭️  Skipping recordings cleanup");
        return;
    }

    Path recordingsPath = Paths.get("target/recordings");

    if (Files.exists(recordingsPath)) {
        System.out.println("🧹 Cleaning old recordings from target/recordings...");
        Files.walk(recordingsPath)
            .sorted(Comparator.reverseOrder())
            .filter(path -> !path.equals(recordingsPath))
            .forEach(Files::delete);
        System.out.println("✅ target/recordings cleaned successfully");
    } else {
        System.out.println("📁 Creating target/recordings directory");
        Files.createDirectories(recordingsPath);
    }
}
```

**What it does:**
1. Checks system property `recordings.clean` (default: true)
2. Deletes all video files in `target/recordings/`
3. Keeps the directory itself
4. Creates directory if it doesn't exist

**Why it's important:**
- Video files are large (5-20 MB each)
- Prevents disk space issues
- Ensures fresh recordings for each session

---

### 3. cleanOldAllureResults()

**Location:** `Hooks.java` (Lines 245-280)

```java
private static void cleanOldAllureResults() {
    String cleanResults = System.getProperty("allure.clean", "true");

    if (!Boolean.parseBoolean(cleanResults)) {
        System.out.println("⏭️  Skipping Allure results cleanup");
        return;
    }

    Path allureResultsPath = Paths.get("allure-results");

    if (Files.exists(allureResultsPath)) {
        System.out.println("🧹 Cleaning old Allure results...");
        Files.walk(allureResultsPath)
            .sorted(Comparator.reverseOrder())
            .filter(path -> !path.equals(allureResultsPath))
            .forEach(Files::delete);
        System.out.println("✅ Allure results cleaned successfully");
    } else {
        System.out.println("📁 Creating allure-results directory");
        Files.createDirectories(allureResultsPath);
    }
}
```

**What it does:**
1. Checks system property `allure.clean` (default: true)
2. Deletes all files in `allure-results/`
3. Keeps the directory itself
4. Creates directory if it doesn't exist

---

## 📊 Console Output

### Successful Cleanup (All Enabled)
```
========================================
INITIALIZING TEST SUITE
========================================
🧹 Cleaning old screenshots from target/screenshots...
✅ target/screenshots cleaned successfully
🧹 Cleaning old test-output directory...
✅ test-output cleaned successfully
🧹 Cleaning old recordings from target/recordings...
✅ target/recordings cleaned successfully
🧹 Cleaning old Allure results...
✅ Allure results cleaned successfully
Appium server initialization completed
```

### Cleanup Skipped (Disabled)
```
========================================
INITIALIZING TEST SUITE
========================================
⏭️  Skipping screenshots cleanup (screenshots.clean=false)
⏭️  Skipping recordings cleanup (recordings.clean=false)
⏭️  Skipping Allure results cleanup (allure.clean=false)
Appium server initialization completed
```

### First Run (Directories Don't Exist)
```
========================================
INITIALIZING TEST SUITE
========================================
📁 Creating screenshots directory
📁 Creating allure-results directory
Appium server initialization completed
```

---

## 🎯 When to Disable Cleanup

### Disable Screenshot Cleanup When:

1. **Comparing screenshots across test runs**
   ```bash
   mvn test -Dscreenshots.clean=false
   ```

2. **Debugging visual issues over time**
   ```bash
   mvn test -Dscreenshots.clean=false
   ```

3. **Accumulating test evidence**
   ```bash
   mvn test -Dscreenshots.clean=false
   ```

### Disable Allure Cleanup When:

1. **Building test history**
   ```bash
   mvn test -Dallure.clean=false
   ```

2. **Trend analysis over multiple runs**
   ```bash
   mvn test -Dallure.clean=false
   ```

3. **Accumulating test data**
   ```bash
   mvn test -Dallure.clean=false
   ```

---

## ⚠️ Important Notes

### 1. Cleanup Happens Once Per Test Suite

Cleanup runs in `@BeforeAll`, which executes **once** before all scenarios.

```java
@BeforeAll  // ← Runs once per test suite
public static void startAppiumServer() {
    cleanOldScreenshots();
}
```

**Not in:**
```java
@Before  // ← Runs before EACH scenario (wrong place for cleanup)
```

---

### 2. Directories Are Preserved

The cleanup methods **delete files** but **preserve directories**.

```bash
# After cleanup
target/screenshots/       ← Directory exists (empty)
allure-results/          ← Directory exists (empty)
```

This prevents issues with file creation later.

---

### 3. Manual Cleanup (If Needed)

If you need to manually clean:

```bash
# Clean screenshots
rm -rf target/screenshots/*

# Clean Allure results
rm -rf allure-results/*

# Clean all target files
mvn clean
```

---

## 🚀 Best Practices

### ✅ DO

1. **Keep cleanup enabled** (default)
   - Fresh results every run
   - No confusion with old artifacts
   - Prevents disk space issues

2. **Use system properties for special cases**
   ```bash
   mvn test -Dscreenshots.clean=false  # When needed
   ```

3. **Review screenshots after failed tests**
   - Check `target/screenshots/` immediately
   - Screenshots are cleaned on next run

### ❌ DON'T

1. **Don't disable cleanup permanently**
   - Leads to disk space issues
   - Confusing mix of old/new results

2. **Don't rely on old screenshots**
   - They'll be cleaned next run
   - Save important ones elsewhere

3. **Don't modify cleanup code without testing**
   - Could break directory structure
   - Could leave orphaned files

---

## 📈 Disk Space Management

### Expected Disk Usage

| Artifact | Size Per Test | 10 Tests | 100 Tests |
|----------|---------------|----------|-----------|
| Manual Screenshot | ~50-200 KB | ~1-2 MB | ~10-20 MB |
| Failure Screenshot | ~50-200 KB | ~500 KB | ~5-10 MB |
| Allure Results | ~10-50 KB | ~500 KB | ~5 MB |
| **Total** | - | **~2-3 MB** | **~20-35 MB** |

**With cleanup enabled:** Only current run's data
**Without cleanup:** Accumulates over time

---

## 🔍 Troubleshooting

### Problem: Cleanup Not Working

**Check:**
1. System property value
   ```bash
   mvn test -Dscreenshots.clean=true
   ```

2. Console output
   ```
   ✅ Screenshots cleaned successfully  ← Should see this
   ```

3. File permissions
   ```bash
   ls -la target/screenshots/
   ```

### Problem: Directories Don't Exist

**Solution:** Cleanup automatically creates them
```
📁 Creating screenshots directory
```

### Problem: Need to Keep Some Screenshots

**Solution:** Copy them before next run
```bash
cp target/screenshots/*.png ~/saved-screenshots/
```

---

## 📝 Summary

| Feature | Default | Control |
|---------|---------|---------|
| **Screenshot Cleanup** | ✅ Enabled | `-Dscreenshots.clean=true/false` |
| **Allure Cleanup** | ✅ Enabled | `-Dallure.clean=true/false` |
| **When** | @BeforeAll | Once per test suite |
| **What** | Delete files | Keep directories |

**Console messages:**
- 🧹 Cleaning...
- ✅ Cleaned successfully
- ⏭️ Skipping cleanup
- 📁 Creating directory

---

## 🔗 Related Documentation

- [Screenshots and Reporting](SCREENSHOTS_AND_REPORTING.md)
- [Hooks Documentation](HOOKS.md)

---

**Last Updated:** 2025-10-29
