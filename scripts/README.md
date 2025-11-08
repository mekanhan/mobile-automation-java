# Scripts Directory

This directory contains helper scripts for running tests and managing reports.

## Scripts Overview

### Main Test Runner
The main test runner is located at the **project root** as `../test` for convenience.

**Usage:**
```bash
../test <command>
```

**Examples:**
```bash
../test test:smoke
../test report:open
../test help
```

### `open-report.sh`
Opens the Allure HTML report in a browser using a local HTTP server.

**Usage:**
```bash
./open-report.sh
```

**Features:**
- Starts Python HTTP server on port 8765
- Automatically opens browser
- Press Ctrl+C to stop

### `run-tests.sh`
Legacy test runner (kept for reference). Use `test-runner.sh` instead.

## Directory Structure

```
scripts/
├── README.md           # This file
├── open-report.sh      # Report viewer with HTTP server
└── run-tests.sh        # Simple test runner

../test                 # Main test runner (in project root)
```

## Adding New Scripts

When adding new scripts:

1. Place them in this directory
2. Make them executable: `chmod +x script-name.sh`
3. Document them in this README
4. Update the root `test` wrapper if needed

## Best Practices

- All scripts should have clear comments
- Use `set -e` to exit on errors
- Add colored output for better UX
- Include help/usage messages
- Test on both macOS and Linux if possible
