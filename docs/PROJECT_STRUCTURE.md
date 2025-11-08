# Project Structure

## Overview

This mobile automation project follows a clean, organized structure with npm-style test scripts for convenience.

## Directory Layout

```
mobile-automation-java/
├── src/
│   ├── main/
│   │   ├── java/                    # Page objects, utilities, helpers
│   │   └── resources/               # Config files, test data
│   └── test/
│       ├── java/                    # Test runners, hooks, step definitions
│       └── resources/
│           └── features/            # Cucumber feature files
│               ├── wikipedia/       # Wikipedia app features
│               └── app2/            # Android app features
│
├── scripts/                         # ✨ Helper scripts
│   ├── README.md                    # Scripts documentation
│   ├── open-report.sh              # Report viewer with HTTP server
│   └── run-tests.sh                # Simple test runner
│
├── docs/                           # Documentation
│   ├── SCRIPTS.md                  # Script usage guide
│   ├── TEST_COMMANDS.md            # Test execution reference
│   └── PROJECT_STRUCTURE.md        # This file
│
├── allure-results/                 # Test execution results (git-ignored)
├── target/                         # Maven build output (git-ignored)
│   └── allure-report/              # Generated HTML reports
│
├── Makefile                        # ✨ Quick commands (recommended)
├── test                            # ✨ Main test runner (npm-style)
├── pom.xml                         # Maven configuration
└── testng.xml                      # TestNG configuration
```

## Key Files

### Root Level

- **`Makefile`** - Quick test commands (e.g., `make test-smoke`)
- **`test`** - Shell script wrapper (e.g., `./test test:smoke`)
- **`pom.xml`** - Maven project configuration with test profiles
- **`testng.xml`** - TestNG suite configuration

### Scripts Directory (`scripts/`)

All helper scripts are organized here:

- **`test-runner.sh`** - Main test execution script with npm-style commands
- **`open-report.sh`** - Opens Allure report in browser with HTTP server
- **`run-tests.sh`** - Simple test runner with result cleanup
- **`README.md`** - Scripts documentation

### Documentation (`docs/`)

- **`SCRIPTS.md`** - Complete guide to running tests
- **`TEST_COMMANDS.md`** - Quick reference for test execution
- **`PROJECT_STRUCTURE.md`** - This file

## Running Tests

You have 3 options:

### 1. Makefile (Shortest)
```bash
make test-smoke
make test-tag TAG=@navigation1
make report-open
```

### 2. Shell Script
```bash
./test test:smoke
./test test:tag @navigation1
./test report:open
```

### 3. Maven Direct
```bash
mvn test -Psmoke
mvn test -Dcucumber.filter.tags="@navigation1"
mvn allure:report
```

## Adding New Scripts

1. Create script in `scripts/` directory
2. Make it executable: `chmod +x scripts/your-script.sh`
3. Document it in `scripts/README.md`
4. Optionally add a Makefile target for convenience

## Git Ignored Items

The following are automatically ignored:
- `target/` - Maven build output
- `allure-results/` - Test execution results (JSON files)
- `test-output/` - TestNG output
- `*.log` - Log files
- IDE-specific files (`.idea/`, `.vscode/`, etc.)

## Best Practices

1. **Use Makefile for daily work** - Shortest commands
2. **Clean old results** - Scripts auto-clean before each run
3. **Organize by app** - Keep iOS/Android features separate
4. **Document scripts** - Update docs when adding new scripts
5. **Use profiles for CI/CD** - Maven profiles work everywhere

## Quick Reference

```bash
# Most common commands
make test-smoke              # Quick smoke test
make test-tag TAG=@your-tag  # Test specific tag
make report-open             # View latest report
make clean                   # Clean everything
make help                    # Show all commands
```

See [SCRIPTS.md](SCRIPTS.md) for detailed usage.
