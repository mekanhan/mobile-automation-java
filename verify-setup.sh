#!/bin/bash

# ========================================
# Mobile Automation Setup Verification
# ========================================
# Verify all components are properly installed

echo "🔍 Mobile Automation Setup Verification"
echo "========================================"
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_success() {
    echo -e "${GREEN}✅${NC} $1"
}

print_error() {
    echo -e "${RED}❌${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}⚠️${NC} $1"
}

print_info() {
    echo -e "${BLUE}ℹ️${NC} $1"
}

# Track overall success
OVERALL_SUCCESS=true

echo "Checking all required components..."
echo ""

# 1. Check Java
echo "1. Java JDK:"
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    print_success "Java found: $JAVA_VERSION"
    
    # Check if it's Java 11+
    MAJOR_VERSION=$(echo $JAVA_VERSION | cut -d. -f1)
    if [ "$MAJOR_VERSION" -ge 11 ]; then
        print_success "Java version is 11+ ✓"
    else
        print_error "Java version should be 11+, found: $JAVA_VERSION"
        OVERALL_SUCCESS=false
    fi
else
    print_error "Java not found. Please install Java 11+"
    OVERALL_SUCCESS=false
fi

# 2. Check JAVA_HOME
echo ""
echo "2. JAVA_HOME Environment:"
if [ -n "$JAVA_HOME" ]; then
    print_success "JAVA_HOME set: $JAVA_HOME"
else
    print_warning "JAVA_HOME not set. Add to ~/.bashrc: export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64"
fi

# 3. Check Maven
echo ""
echo "3. Maven:"
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version | head -n 1)
    print_success "Maven found: $MVN_VERSION"
else
    print_error "Maven not found. Please install Maven"
    OVERALL_SUCCESS=false
fi

# 4. Check Node.js and npm
echo ""
echo "4. Node.js and npm:"
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version)
    print_success "Node.js found: $NODE_VERSION"
else
    print_error "Node.js not found"
    OVERALL_SUCCESS=false
fi

if command -v npm &> /dev/null; then
    NPM_VERSION=$(npm --version)
    print_success "npm found: $NPM_VERSION"
else
    print_error "npm not found"
    OVERALL_SUCCESS=false
fi

# 5. Check Appium
echo ""
echo "5. Appium:"
if command -v appium &> /dev/null; then
    APPIUM_VERSION=$(appium --version)
    print_success "Appium found: $APPIUM_VERSION"
    
    # Check Appium drivers
    if appium driver list | grep -q "uiautomator2"; then
        print_success "UiAutomator2 driver installed ✓"
    else
        print_warning "UiAutomator2 driver not installed. Run: appium driver install uiautomator2"
    fi
else
    print_error "Appium not found. Please install: npm install -g appium"
    OVERALL_SUCCESS=false
fi

# 6. Check Android SDK
echo ""
echo "6. Android SDK:"
if [ -n "$ANDROID_HOME" ]; then
    print_success "ANDROID_HOME set: $ANDROID_HOME"
    
    if [ -d "$ANDROID_HOME" ]; then
        print_success "Android SDK directory exists ✓"
    else
        print_warning "Android SDK directory doesn't exist. Install SDK through Android Studio"
    fi
else
    print_warning "ANDROID_HOME not set. Add to ~/.bashrc: export ANDROID_HOME=\$HOME/Android/Sdk"
fi

# 7. Check ADB
echo ""
echo "7. Android Debug Bridge (ADB):"
if command -v adb &> /dev/null; then
    ADB_VERSION=$(adb version | head -n 1)
    print_success "ADB found: $ADB_VERSION"
    
    # Check connected devices
    DEVICE_COUNT=$(adb devices | grep -c "device$" || echo "0")
    if [ "$DEVICE_COUNT" -gt 0 ]; then
        print_success "$DEVICE_COUNT Android device(s) connected ✓"
        adb devices | grep "device$" | while read device; do
            print_info "  - $device"
        done
    else
        print_warning "No Android devices connected. Connect a device or start an emulator"
    fi
else
    print_warning "ADB not found. Install Android SDK and add platform-tools to PATH"
fi

# 8. Check Android Studio
echo ""
echo "8. Android Studio:"
if [ -d "/opt/android-studio" ] || command -v android-studio &> /dev/null; then
    print_success "Android Studio installation found ✓"
else
    print_warning "Android Studio not found in /opt/android-studio"
fi

# 9. Check Appium Doctor
echo ""
echo "9. Appium Doctor Diagnostics:"
if command -v appium-doctor &> /dev/null; then
    print_success "Appium Doctor found ✓"
    print_info "Running Appium Doctor for Android..."
    appium-doctor --android
else
    print_warning "Appium Doctor not found. Install: npm install -g appium-doctor"
fi

# 10. Test Maven project compilation
echo ""
echo "10. Maven Project Test:"
if [ -f "pom.xml" ]; then
    print_info "Testing Maven compilation..."
    if mvn clean compile -q; then
        print_success "Maven project compiles successfully ✓"
    else
        print_error "Maven compilation failed"
        OVERALL_SUCCESS=false
    fi
else
    print_warning "No pom.xml found in current directory"
fi

# 11. Test Appium server connection
echo ""
echo "11. Appium Server Test:"
print_info "Testing Appium server connection..."
if curl -s "http://127.0.0.1:4723/status" > /dev/null 2>&1; then
    print_success "Appium server is running on port 4723 ✓"
    
    # If devices are connected, suggest running the demo
    if [ "$DEVICE_COUNT" -gt 0 ]; then
        echo ""
        print_success "🎉 Setup verification complete! Ready to run Calculator demo:"
        print_info "Run: ./run-calculator-demo.sh"
    fi
else
    print_warning "Appium server not running. Start with: appium --port 4723"
fi

# Final summary
echo ""
echo "========================================"
if [ "$OVERALL_SUCCESS" = true ]; then
    print_success "🎉 All critical components verified successfully!"
    echo ""
    print_info "Next steps:"
    print_info "1. Start Appium server: appium --port 4723"
    print_info "2. Connect Android device or start emulator"
    print_info "3. Run Calculator demo: ./run-calculator-demo.sh"
else
    print_error "❌ Some components need attention. Please fix the issues above."
    echo ""
    print_info "Common fixes:"
    print_info "1. Reload environment: source ~/.bashrc"
    print_info "2. Install missing components with: ./setup-mobile-automation.sh"
    print_info "3. Set up Android SDK through Android Studio"
fi
echo "========================================"