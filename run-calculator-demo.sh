#!/bin/bash

# ========================================
# Calculator Demo Runner Script
# ========================================
# Automated script to run Calculator app demo tests

set -e  # Exit on any error

echo "🚀 Mobile Automation Framework - Calculator Demo"
echo "================================================="
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
print_status "Checking prerequisites..."

# 1. Check Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    print_success "Java found: $JAVA_VERSION"
else
    print_error "Java not found. Please install Java 11+"
    exit 1
fi

# 2. Check Maven
if command -v mvn &> /dev/null || command -v mvnd &> /dev/null; then
    # Determine which Maven command to use
    if command -v mvnd &> /dev/null; then
        MVN_CMD="mvnd"
    else
        MVN_CMD="mvn"
    fi
    MVN_VERSION=$($MVN_CMD -version | head -n 1)
    print_success "Maven found: $MVN_VERSION"
else
    print_error "Maven not found. Please install Maven"
    exit 1
fi

# 3. Check ADB (Android Debug Bridge)
if command -v adb &> /dev/null; then
    print_success "ADB found"
    
    # Check for connected devices
    DEVICE_COUNT=$(adb devices | grep -c "device$" || true)
    if [ "$DEVICE_COUNT" -eq 0 ]; then
        print_warning "No Android devices/emulators found"
        print_status "Please connect a device or start an emulator"
        print_status "To start an emulator: emulator -avd <AVD_NAME>"
        exit 1
    else
        print_success "Found $DEVICE_COUNT Android device(s)"
        adb devices | grep "device$"
    fi
else
    print_error "ADB not found. Please install Android SDK"
    exit 1
fi

# 4. Check Appium server
print_status "Checking Appium server connection..."
if curl -s "http://127.0.0.1:4723/status" > /dev/null 2>&1; then
    print_success "Appium server is running on port 4723"
else
    print_error "Appium server not running on port 4723"
    print_status "Please start Appium server:"
    print_status "  npm install -g appium"
    print_status "  appium --port 4723"
    exit 1
fi

echo ""
print_status "All prerequisites met! Starting Calculator demo..."
echo ""

# Clean and compile
print_status "Cleaning and compiling project..."
$MVN_CMD clean compile -q

if [ $? -ne 0 ]; then
    print_error "Maven compilation failed"
    exit 1
fi

print_success "Compilation successful"

# Run Calculator demo tests
print_status "Running Calculator automation demo..."
echo ""

mvnd test -Dtest=CalculatorTestRunner -Dcucumber.filter.tags="@calculator and @smoke"

# Check test results
if [ $? -eq 0 ]; then
    echo ""
    print_success "🎉 Calculator demo completed successfully!"
    echo ""
    print_status "📊 Test Reports Generated:"
    print_status "  HTML Report: target/cucumber-reports/calculator/cucumber.html"
    print_status "  JSON Report: target/cucumber-reports/calculator/cucumber.json"
    print_status "  JUnit XML: target/cucumber-reports/calculator/cucumber.xml"
    
    # Open HTML report if on desktop environment
    if command -v xdg-open &> /dev/null; then
        print_status "Opening HTML report..."
        xdg-open target/cucumber-reports/calculator/cucumber.html &
    elif command -v open &> /dev/null; then
        print_status "Opening HTML report..."
        open target/cucumber-reports/calculator/cucumber.html &
    fi
    
else
    echo ""
    print_error "❌ Calculator demo failed"
    print_status "Check logs above for error details"
    print_status "Common issues:"
    print_status "  - Calculator app not installed (try different app package)"
    print_status "  - Device screen locked"
    print_status "  - Appium server connection issues"
    print_status "  - Element locators changed (different Android version)"
    exit 1
fi

echo ""
print_status "Demo completed. Thank you for watching! 🚀"