# Cucumber Tag Filtering

Complete guide to filtering tests by tags using `cucumber.filter.tags`.

---

## Overview

The `cucumber.filter.tags` property controls which scenarios run based on their tags.

**Configured in:** [pom.xml:25](../pom.xml#L25)
```xml
<cucumber.filter.tags>@smoke</cucumber.filter.tags>  <!-- Default tag -->
```

---

## All Methods to Execute with Tags

### **Method 1: Command Line Override (Most Flexible)**

Override the default tag at runtime:

```bash
# Run specific tag
mvn test -Dcucumber.filter.tags="@navigation1"

# Run with Allure report
mvn test -Dcucumber.filter.tags="@navigation1" -Dallure.open=true

# Run default tag (defined in pom.xml)
mvn test
```

---

### **Method 2: Maven Profiles (Predefined Tags)**

Use profiles for common tag combinations:

```bash
# Smoke tests
mvn test -Psmoke

# Navigation tests
mvn test -Pnavigation

# Regression tests
mvn test -Pregression

# iOS tests
mvn test -Pios

# Android tests
mvn test -Pandroid

# navigation1 tag
mvn test -Pnavigation1
```

**Available profiles:** `smoke`, `navigation`, `regression`, `ios`, `android`, `navigation1`

---

### **Method 3: Makefile (Shortest Syntax)**

```bash
# Predefined profiles
make test-smoke
make test-navigation
make test-regression
make test-ios
make test-android

# Custom tag
make test-tag TAG=@navigation1

# With options
make test-tag TAG=@navigation1 ARGS="-Dallure.open=true"
```

---

### **Method 4: Shell Script**

```bash
# Predefined
./test test:smoke
./test test:navigation

# Custom tag
./test test:tag @navigation1
```

---

## Tag Expression Syntax

Cucumber supports powerful tag expressions:

### **Single Tag**
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

### **OR (Any tag matches)**
```bash
mvn test -Dcucumber.filter.tags="@smoke or @regression"
```

### **AND (All tags must match)**
```bash
mvn test -Dcucumber.filter.tags="@ios and @smoke"
```

### **NOT (Exclude tag)**
```bash
mvn test -Dcucumber.filter.tags="not @wip"
```

### **Complex Expressions**
```bash
# iOS smoke or regression tests, but not work-in-progress
mvn test -Dcucumber.filter.tags="@ios and (@smoke or @regression) and not @wip"
```

---

## Setting Default Tag in pom.xml

Edit [pom.xml:25](../pom.xml#L25) to change the default:

```xml
<!-- Run smoke tests by default -->
<cucumber.filter.tags>@smoke</cucumber.filter.tags>

<!-- Run all tests by default -->
<cucumber.filter.tags></cucumber.filter.tags>

<!-- Run iOS smoke tests by default -->
<cucumber.filter.tags>@ios and @smoke</cucumber.filter.tags>
```

Then simply run:
```bash
mvn test  # Runs the default tag
```

---

## Adding New Profile for a Tag

To create a shortcut for a specific tag:

### **1. Add Profile to pom.xml**

```xml
<profile>
    <id>YOUR_PROFILE_NAME</id>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <systemPropertyVariables>
                        <cucumber.filter.tags>@YOUR_TAG</cucumber.filter.tags>
                    </systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

### **2. Add to Makefile (Optional)**

```makefile
test-your-profile: ## Run YOUR_TAG tests
	@echo "$(BLUE)Running @YOUR_TAG tests...$(NC)"
	mvn clean test -PYOUR_PROFILE_NAME $(ARGS)
```

### **3. Use It**

```bash
# Maven
mvn test -PYOUR_PROFILE_NAME

# Makefile
make test-your-profile
```

---

## Examples

### **Run navigation1 tag (3 ways)**

```bash
# Command line
mvn test -Dcucumber.filter.tags="@navigation1"

# Profile (if configured)
mvn test -Pnavigation1

# Makefile
make test-tag TAG=@navigation1
```

### **Run iOS smoke tests**

```bash
# Command line
mvn test -Dcucumber.filter.tags="@ios and @smoke"

# Sequential profiles (runs both profiles' tags)
mvn test -Pios,smoke  # This runs @ios OR @smoke (not AND)

# Better: use command line for AND logic
mvn test -Dcucumber.filter.tags="@ios and @smoke"
```

### **Run all except WIP**

```bash
mvn test -Dcucumber.filter.tags="not @wip"
```

### **Run with report**

```bash
# Any method + Allure options
mvn test -Dcucumber.filter.tags="@navigation1" -Dallure.open=true
mvn test -Psmoke -Dallure.report=true
make test-tag TAG=@navigation1 ARGS="-Dallure.open=true"
```

---

## Tag Organization Best Practices

### **Recommended Tag Structure**

```gherkin
@platform  # ios, android
@type      # smoke, regression, e2e
@feature   # navigation, search, profile
@status    # wip, skip, flaky
```

### **Example Scenario**

```gherkin
@ios @smoke @navigation @navigation1
Scenario: Verify Explorer page elements are visible
  Given I am on the Explorer page
  Then I should see "Today Header"
```

### **Common Tag Patterns**

```bash
# Platform-specific smoke tests
mvn test -Dcucumber.filter.tags="@ios and @smoke"

# All mobile regression (iOS or Android)
mvn test -Dcucumber.filter.tags="(@ios or @android) and @regression"

# Everything except WIP and flaky
mvn test -Dcucumber.filter.tags="not @wip and not @flaky"

# Quick sanity check
mvn test -Dcucumber.filter.tags="@smoke"
```

---

## Comparison: All Methods

| Method | Syntax | Flexibility | Use Case |
|--------|--------|-------------|----------|
| **Command Line** | `mvn test -Dcucumber.filter.tags="@tag"` | ⭐⭐⭐⭐⭐ | Ad-hoc testing, complex expressions |
| **Profile** | `mvn test -Pprofile` | ⭐⭐⭐ | Common test suites, CI/CD |
| **Makefile** | `make test-tag TAG=@tag` | ⭐⭐⭐⭐ | Daily work, convenience |
| **Default (pom.xml)** | `mvn test` | ⭐⭐ | Standardized default behavior |

---

## CI/CD Integration

### **GitHub Actions**

```yaml
- name: Run Smoke Tests
  run: mvn test -Psmoke -Dallure.report=true

- name: Run Platform-Specific Tests
  run: mvn test -Dcucumber.filter.tags="@${{ matrix.platform }} and @smoke"
  strategy:
    matrix:
      platform: [ios, android]
```

### **Jenkins**

```groovy
stage('Smoke Tests') {
    steps {
        sh 'mvn test -Psmoke -Dallure.report=true'
    }
}

stage('Regression') {
    steps {
        sh 'mvn test -Pregression -Dallure.report=true'
    }
}
```

---

## Quick Reference

```bash
# Default (runs @smoke by default in your pom.xml)
mvn test

# Override with specific tag
mvn test -Dcucumber.filter.tags="@navigation1"

# Use profile
mvn test -Pnavigation1

# Use Makefile
make test-tag TAG=@navigation1

# Complex expression
mvn test -Dcucumber.filter.tags="@ios and @smoke and not @wip"

# With auto-report
mvn test -Dcucumber.filter.tags="@navigation1" -Dallure.open=true
```

---

## See Also

- [pom.xml](../pom.xml) - Maven configuration
- [HOOKS.md](HOOKS.md) - Hook system and Allure options
- [SCRIPTS.md](SCRIPTS.md) - All execution methods
- [Cucumber Tag Expressions](https://cucumber.io/docs/cucumber/api/#tag-expressions) - Official docs
