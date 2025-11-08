# Test Scripts Reference

This project has multiple ways to run tests, similar to `package.json` scripts in Node.js projects.

## Quick Start

```bash
# Show all available commands
make help
# OR
./scripts.sh help
```

---

## Option 1: Makefile (Recommended)

The most concise syntax, similar to npm scripts.

### Usage
```bash
make <command>
```

### Available Commands

**Test Commands:**
```bash
make test-smoke          # Run smoke tests
make test-navigation     # Run navigation tests
make test-regression     # Run regression tests
make test-ios           # Run iOS-specific tests
make test-android       # Run Android-specific tests
make test-all           # Run all tests
make test-tag TAG=@tag  # Run tests with custom tag
```

**Report Commands:**
```bash
make report-generate    # Generate HTML report
make report-open        # Open report in browser
make report-serve       # Start Allure server and open report
make report-clean       # Delete all results
```

**Utility Commands:**
```bash
make clean             # Clean Maven project + results
make compile           # Compile project
make help              # Show help
```

**Examples:**
```bash
make test-smoke
make test-tag TAG=@navigation1
make report-open
```

---

## Option 2: Shell Script

More verbose but works everywhere.

### Usage
```bash
./test <command>
```

### Available Commands

**Test Commands:**
```bash
./test test:smoke          # Run smoke tests
./test test:navigation     # Run navigation tests
./test test:regression     # Run regression tests
./test test:ios           # Run iOS tests
./test test:android       # Run Android tests
./test test:all           # Run all tests
./test test:tag @tag      # Run tests with custom tag
```

**Report Commands:**
```bash
./test report:generate    # Generate HTML report
./test report:open        # Open report in browser
./test report:serve       # Start Allure server
./test report:clean       # Delete all results
```

**Utility Commands:**
```bash
./test clean             # Clean project
./test compile           # Compile project
./test help              # Show help
```

**Examples:**
```bash
./test test:smoke
./test test:tag @navigation1
./test report:open
```

---

## Option 3: Maven Profiles

Pure Maven approach (no shell scripts needed).

### Usage
```bash
mvn test -P<profile>
```

### Available Profiles

```bash
mvn test -Psmoke        # Run smoke tests
mvn test -Pnavigation   # Run navigation tests
mvn test -Pregression   # Run regression tests
mvn test -Pios         # Run iOS tests
mvn test -Pandroid     # Run Android tests
```

**Custom tags:**
```bash
mvn test -Dcucumber.filter.tags="@navigation1"
```

---

## Option 4: Direct Maven Commands

Traditional Maven approach without profiles.

```bash
# Run specific tag
mvn clean test -Dcucumber.filter.tags="@smoke"

# Generate report
mvn allure:report

# Serve report
mvn allure:serve
```

---

## Comparison: npm scripts vs This Project

| npm scripts          | Makefile           | Shell Script         | Maven            |
|---------------------|--------------------|--------------------|------------------|
| `npm test`          | `make test-all`    | `./test test:all` | `mvn test`   |
| `npm run test:smoke`| `make test-smoke`  | `./test test:smoke` | `mvn test -Psmoke` |
| Custom script       | `make test-tag TAG=@foo` | `./test test:tag @foo` | `mvn test -Dcucumber.filter.tags="@foo"` |

---

## Which One Should I Use?

**Makefile** (Recommended):
- ✅ Shortest syntax
- ✅ Industry standard
- ✅ Auto-cleans old results
- ✅ Colored output
- ✅ Tab completion in most shells

**scripts.sh**:
- ✅ Works on any Unix system
- ✅ More readable for beginners
- ✅ Easy to customize

**Maven Profiles**:
- ✅ Pure Java ecosystem
- ✅ Works in CI/CD without shell
- ✅ IDE integration

**Direct Maven**:
- ✅ Maximum flexibility
- ✅ Good for CI/CD
- ⚠️ More verbose

---

## CI/CD Integration

For GitHub Actions / Jenkins / GitLab CI:

```yaml
# Makefile approach
- run: make test-smoke

# Maven approach (better for CI)
- run: mvn clean test -Psmoke
```

---

## Quick Reference Card

Save this for daily use:

```bash
# Run smoke tests
make test-smoke

# Run specific tag
make test-tag TAG=@navigation1

# View report
make report-open

# Clean everything
make clean

# See all commands
make help
```
