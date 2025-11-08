# Quick Start Guide

Get up and running in minutes!

## Running Tests

### **Default (Auto-clean, No Report)**
```bash
make test-smoke
```
- ✅ Cleans old results
- ✅ Runs tests
- ⏭️ Doesn't generate report

---

### **With Auto-Report**
```bash
make test-smoke ARGS="-Dallure.report=true"
```
- ✅ Cleans old results  
- ✅ Runs tests
- ✅ Generates HTML report

---

### **With Auto-Report + Open**
```bash
make test-smoke ARGS="-Dallure.open=true"
```
- ✅ Cleans old results
- ✅ Runs tests
- ✅ Generates HTML report
- ✅ Opens in browser automatically

---

## View Existing Report

```bash
make report-open
```

Opens last generated report in browser at http://localhost:8765

---

## Common Commands

```bash
# Run specific tag
make test-tag TAG=@navigation1

# Run with auto-report
make test-tag TAG=@navigation1 ARGS="-Dallure.report=true"

# Run navigation tests
make test-navigation

# Run all tests
make test-all

# Clean project
make clean

# Show all commands
make help
```

---

## Next Steps

📖 **Read More:**
- [HOOKS.md](HOOKS.md) - Hook system documentation
- [SCRIPTS.md](SCRIPTS.md) - Complete script guide
- [TEST_COMMANDS.md](TEST_COMMANDS.md) - Maven commands
