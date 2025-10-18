# Test Execution Commands

Quick reference for running tests and generating reports.

---

## Running Tests

### Run all tests
```bash
mvn test
```

### Run tests with specific tag
```bash
mvn test -Dcucumber.filter.tags="@navigation1"
```

### Run multiple tags (OR)
```bash
mvn test -Dcucumber.filter.tags="@smoke or @regression"
```

### Run multiple tags (AND)
```bash
mvn test -Dcucumber.filter.tags="@ios and @smoke"
```

### Exclude tags (NOT)
```bash
mvn test -Dcucumber.filter.tags="not @wip"
```

### Clean and run tests
```bash
mvn clean test -Dcucumber.filter.tags="@navigation1"
```

### Run tests with screen recording
```bash
mvn test -Dcucumber.filter.tags="@navigation1" -Dscreen.recording=true
```

---

## Viewing Reports

After running tests, you'll have 3 different HTML reports available:

### 1. **Allure Report** (Recommended - Most Feature-Rich)

#### Option A: Auto-generate and open (requires Allure CLI installed)
```bash
mvn clean test -Dcucumber.filter.tags="@navigation1" && mvn allure:serve
```

#### Option B: Generate static HTML report (No server needed)
```bash
mvn clean test -Dcucumber.filter.tags="@navigation1" && mvn allure:report
open target/allure-report/index.html
```

#### Generate Allure report from existing results
```bash
mvn allure:report
open target/allure-report/index.html
```

---

### 2. **Cucumber HTML Report** (Simple & Native)
```bash
open target/cucumber-reports/cucumber.html
```

---

### 3. **ExtentReports** (Interactive & Rich)
```bash
open test-output/SparkReport/Index.html
```

---

## One-Line Commands

### Run tests + Auto-open Allure report
```bash
mvn clean test -Dcucumber.filter.tags="@navigation1" && mvn allure:report && open target/allure-report/index.html
```

### Run tests + Open all 3 reports
```bash
mvn clean test -Dcucumber.filter.tags="@navigation1" && mvn allure:report && open target/allure-report/index.html && open target/cucumber-reports/cucumber.html && open test-output/SparkReport/Index.html
```

---

## Compilation Only

### Compile main code
```bash
mvn clean compile
```

### Compile tests
```bash
mvn clean test-compile
```

---

## Useful Aliases (Add to ~/.zshrc)

Add these to your `~/.zshrc` file for quick access:

```bash
# Mobile Automation Test Aliases
alias test-all='mvn clean test'
alias test-smoke='mvn clean test -Dcucumber.filter.tags="@smoke"'
alias test-report='mvn clean test && mvn allure:report && open target/allure-report/index.html'
alias test-tag='mvn clean test -Dcucumber.filter.tags='
alias compile-tests='mvn clean test-compile'
alias open-allure='open target/allure-report/index.html'
alias open-cucumber='open target/cucumber-reports/cucumber.html'
alias open-extent='open test-output/SparkReport/Index.html'
```

After adding, reload your terminal:
```bash
source ~/.zshrc
```

Then you can use:
```bash
test-smoke              # Run smoke tests
test-tag "@navigation1" # Run specific tag
test-report             # Run tests and open Allure report
open-allure             # Open existing Allure report
```

---

## Debugging

### Run tests with debug logging
```bash
mvn test -X -Dcucumber.filter.tags="@navigation1"
```

### Run tests with stack traces
```bash
mvn test -e -Dcucumber.filter.tags="@navigation1"
```

### Skip tests (compile only)
```bash
mvn clean compile -DskipTests
```

---

## Report Locations

| Report Type | Location |
|------------|----------|
| Allure | `target/allure-report/index.html` |
| Cucumber HTML | `target/cucumber-reports/cucumber.html` |
| ExtentReports | `test-output/SparkReport/Index.html` |
| Allure Results | `target/allure-results/` |
| Screenshots | Embedded in reports |

---

## Platform Selection

### Run on iOS (default)
```bash
mvn test -Dplatform=ios
```

### Run on Android
```bash
mvn test -Dplatform=android
```

---

## Tips

- Always use `mvn clean test` to ensure fresh results
- Allure report shows historical trends across test runs
- ExtentReports generates in real-time during test execution
- Screenshots are automatically attached to failed tests in all reports
- Use tags to organize and filter your test execution
