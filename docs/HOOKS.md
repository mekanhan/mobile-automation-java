# Test Hooks Documentation

This project uses **Cucumber Hooks** with automatic Allure report management.

## Overview

Hooks in [src/test/java/hooks/Hooks.java](../src/test/java/hooks/Hooks.java) provide automatic test lifecycle management:

- ✅ **Clean old results** before tests
- ✅ **Start/Stop Appium server** automatically
- ✅ **Generate Allure reports** after tests
- ✅ **Open reports in browser** (optional)
- ✅ **Capture screenshots** on failure
- ✅ **Record videos** (optional)

---

## Hook Lifecycle

```
@BeforeAll
  ├── Clean old Allure results
  └── Start Appium server
      ↓
@Before (each scenario)
  ├── Initialize driver
  ├── Start screen recording (if enabled)
  └── Handle onboarding screens
      ↓
[Test Execution]
      ↓
@After (each scenario)
  ├── Stop screen recording
  ├── Take screenshot (on failure)
  └── Quit driver
      ↓
@AfterAll
  ├── Stop Appium server
  └── Generate Allure report (optional)
```

---

## Allure Report Management

### **Automatic Cleanup (Default)**

By default, old Allure results are **automatically cleaned** before each test run:

```bash
mvn test  # Automatically cleans allure-results/ first
```

### **Keep Old Results (Accumulate)**

To keep historical results:

```bash
mvn test -Dallure.clean=false
```

### **Generate Report After Tests**

To automatically generate HTML report after tests:

```bash
mvn test -Dallure.report=true
```

### **Generate AND Open Report**

To generate report and open in browser:

```bash
mvn test -Dallure.open=true
```

**Note:** `-Dallure.open=true` also enables `-Dallure.report=true`

---

## Usage Examples

### **1. Default Behavior (Clean + No Report)**

```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

**What happens:**
- ✅ Cleans old results
- ✅ Runs tests
- ⏭️ Skips report generation
- 💡 Shows: "To generate report, run: mvn allure:report"

---

### **2. Clean + Generate Report**

```bash
mvn test -Dcucumber.filter.tags="@smoke" -Dallure.report=true
```

**What happens:**
- ✅ Cleans old results
- ✅ Runs tests
- ✅ Generates HTML report
- 📊 Report at: `target/allure-report/index.html`

---

### **3. Clean + Generate + Open Report**

```bash
mvn test -Dcucumber.filter.tags="@smoke" -Dallure.open=true
```

**What happens:**
- ✅ Cleans old results
- ✅ Runs tests
- ✅ Generates HTML report
- ✅ Opens in browser (http://localhost:8765)

---

### **4. Keep History + Generate Report**

```bash
mvn test -Dcucumber.filter.tags="@smoke" -Dallure.clean=false -Dallure.report=true
```

**What happens:**
- ⏭️ Keeps old results (accumulates)
- ✅ Runs tests
- ✅ Generates report with history

---

## Using with Makefile

The Makefile doesn't use hooks by default (scripts clean manually). To enable hooks:

### **With Report Generation**

```bash
make test-smoke ARGS="-Dallure.report=true"
```

### **With Report + Auto-open**

```bash
make test-smoke ARGS="-Dallure.open=true"
```

---

## Configuration Options

All options are controlled via system properties (`-D` flags):

| Property | Default | Description |
|----------|---------|-------------|
| `allure.clean` | `true` | Clean old results before tests |
| `allure.report` | `false` | Generate HTML report after tests |
| `allure.open` | `false` | Open report in browser (implies `allure.report=true`) |

---

## Other Hooks Features

### **Screen Recording**

Enable video recording of test execution:

```bash
mvn test -Dscreen.recording=true
```

Videos are automatically attached to Allure reports.

### **Tag-Specific Hooks**

#### **@resetApp** - Reset app before scenario

```gherkin
@resetApp
Scenario: Test with fresh app state
  Given I am on the Explorer page
```

#### **@screenshot** - Always capture screenshot

```gherkin
@screenshot
Scenario: Capture screenshot regardless of result
  When I navigate to Settings
```

---

## How It Works

### **Before All Tests**

```java
@BeforeAll
public static void startAppiumServer() {
    cleanOldAllureResults();        // Clean old results
    AppiumServerManager.startServer(); // Start Appium
}
```

### **After All Tests**

```java
@AfterAll
public static void stopAppiumServer() {
    AppiumServerManager.stopServer();  // Stop Appium
    generateAllureReport();            // Generate report (if enabled)
}
```

### **Cleanup Logic**

- Deletes all files in `allure-results/` directory
- Keeps the directory itself
- Creates directory if it doesn't exist
- Can be disabled with `-Dallure.clean=false`

### **Report Generation Logic**

- Runs `mvn allure:report` via ProcessBuilder
- Shows Maven output in console
- Optionally opens report with HTTP server
- Gracefully handles errors

---

## CI/CD Integration

### **GitHub Actions / GitLab CI**

```yaml
# Clean + Generate report (don't open)
- name: Run Tests
  run: mvn test -Dallure.report=true

# Publish report as artifact
- name: Upload Report
  uses: actions/upload-artifact@v3
  with:
    name: allure-report
    path: target/allure-report/
```

### **Jenkins**

```groovy
stage('Test') {
    steps {
        sh 'mvn test -Dallure.report=true'
    }
}
stage('Publish Report') {
    steps {
        allure includeProperties: false,
               jdk: '',
               results: [[path: 'allure-results']]
    }
}
```

---

## Troubleshooting

### **Report not cleaning between runs**

Check if cleanup is enabled:
```bash
mvn test -Dallure.clean=true
```

### **Report not generating**

Enable report generation:
```bash
mvn test -Dallure.report=true
```

### **Browser not opening**

Check that script exists:
```bash
ls -la scripts/open-report.sh
chmod +x scripts/open-report.sh
```

### **Port 8765 already in use**

Kill existing server:
```bash
lsof -ti:8765 | xargs kill -9
```

---

## Quick Reference

```bash
# Clean + Run (default)
mvn test

# Keep history
mvn test -Dallure.clean=false

# Generate report
mvn test -Dallure.report=true

# Generate + Open
mvn test -Dallure.open=true

# Full control
mvn test \
  -Dcucumber.filter.tags="@smoke" \
  -Dallure.clean=true \
  -Dallure.report=true \
  -Dallure.open=false
```

---

## See Also

- [SCRIPTS.md](SCRIPTS.md) - Test execution scripts
- [TEST_COMMANDS.md](TEST_COMMANDS.md) - Maven commands reference
- [Hooks.java](../src/test/java/hooks/Hooks.java) - Implementation
