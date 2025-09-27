#!/bin/bash

# ========================================
# WSL-Specific Mobile Automation Setup
# ========================================
# Setup script that works better in WSL environment

echo "🐧 WSL Mobile Automation Setup"
echo "=============================="
echo ""

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

print_step() {
    echo -e "${YELLOW}📋 $1${NC}"
}

print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

# Check if we're in WSL
if grep -qi microsoft /proc/version; then
    print_success "WSL environment detected"
else
    print_warning "Not running in WSL - some steps may differ"
fi

# 1. Check prerequisites
print_step "Checking Prerequisites"

# Check if Android Studio is downloaded
if [ -f "/tmp/android-studio.tar.gz" ]; then
    print_success "Android Studio already downloaded"
else
    print_error "Android Studio not found. Download first with:"
    echo "cd /tmp && wget -O android-studio.tar.gz 'https://redirector.gvt1.com/edgedl/android/studio/ide-zips/2023.3.1.18/android-studio-2023.3.1.18-linux.tar.gz'"
    exit 1
fi

# 2. Check if we can use sudo
print_step "Checking System Permissions"
if sudo -n true 2>/dev/null; then
    print_success "Sudo access available"
    USE_SUDO=true
else
    print_warning "No sudo access - will provide manual commands"
    USE_SUDO=false
fi

# 3. Install Appium (doesn't need sudo)
print_step "Installing Appium"
if command -v appium &> /dev/null; then
    print_success "Appium already installed"
else
    print_info "Installing Appium globally..."
    if npm install -g appium; then
        print_success "Appium installed"
    else
        print_error "Failed to install Appium. Try: sudo npm install -g appium"
    fi
fi

# Install UiAutomator2 driver
print_info "Installing UiAutomator2 driver..."
if appium driver install uiautomator2; then
    print_success "UiAutomator2 driver installed"
else
    print_warning "UiAutomator2 driver installation may have failed"
fi

# 4. Set up environment variables (user-specific)
print_step "Setting up Environment Variables"

# Create environment setup
cat > ~/.android_env << 'EOF'
# Android Development Environment for Mobile Automation
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools
EOF

# Add to bashrc if not already present
if ! grep -q "source ~/.android_env" ~/.bashrc; then
    echo "source ~/.android_env" >> ~/.bashrc
    print_success "Added Android environment to ~/.bashrc"
else
    print_info "Android environment already in ~/.bashrc"
fi

# Source the environment
source ~/.android_env

# 5. Create Android SDK directory
print_step "Creating Android SDK Directory"
mkdir -p "$ANDROID_HOME"
print_success "Android SDK directory created: $ANDROID_HOME"

# 6. WSL-specific recommendations
print_step "WSL-Specific Setup Recommendations"
echo ""
print_warning "For best WSL experience, consider:"
echo ""
echo "📱 Option 1: Use Physical Android Device"
echo "  - Enable USB debugging on Android device"
echo "  - Connect via USB and/or WiFi ADB"
echo "  - More reliable than emulator in WSL"
echo ""
echo "💻 Option 2: Windows Android Studio + WSL Development"
echo "  - Install Android Studio on Windows"
echo "  - Use Windows emulator"
echo "  - Forward ADB to WSL"
echo ""
echo "🖥️  Option 3: X Server for GUI (Advanced)"
echo "  - Install VcXsrv or similar X server on Windows"
echo "  - Run Android Studio GUI from WSL"

# 7. Manual steps needed
echo ""
print_step "Manual Steps Required"
echo ""
print_error "You need to run these commands manually:"
echo ""
echo "1. Install Java and Maven:"
echo "   sudo apt update"
echo "   sudo apt install openjdk-11-jdk maven -y"
echo ""
echo "2. Extract Android Studio:"
echo "   sudo tar -xzf /tmp/android-studio.tar.gz -C /opt/"
echo "   sudo ln -sf /opt/android-studio/bin/studio.sh /usr/local/bin/android-studio"
echo ""
echo "3. Reload environment:"
echo "   source ~/.bashrc"
echo ""
echo "4. Verify setup:"
echo "   ./verify-setup.sh"

# 8. Create quick verification
print_step "Creating Quick Check"
cat > ~/check-mobile-setup.sh << 'EOF'
#!/bin/bash
echo "🔍 Quick Mobile Setup Check"
echo "=========================="
echo "Java: $(java -version 2>&1 | head -n 1 || echo 'Not installed')"
echo "Maven: $(mvn -version 2>&1 | head -n 1 || echo 'Not installed')"
echo "Node.js: $(node --version || echo 'Not installed')"
echo "npm: $(npm --version || echo 'Not installed')"
echo "Appium: $(appium --version || echo 'Not installed')"
echo "JAVA_HOME: ${JAVA_HOME:-'Not set'}"
echo "ANDROID_HOME: ${ANDROID_HOME:-'Not set'}"
echo "Android Studio: $(ls /opt/android-studio/ 2>/dev/null | head -n 1 || echo 'Not installed')"
EOF

chmod +x ~/check-mobile-setup.sh
print_success "Created quick check script: ~/check-mobile-setup.sh"

# 9. Final summary
echo ""
print_step "Setup Summary"
print_success "✅ Appium installed"
print_success "✅ Environment variables configured"
print_success "✅ Android SDK directory created"
print_success "✅ Quick check script created"
echo ""
print_warning "⏳ Manual steps still needed:"
print_warning "   - Install Java 11 and Maven"
print_warning "   - Extract Android Studio"
print_warning "   - Set up Android device/emulator"
echo ""
print_info "📋 Next steps:"
print_info "1. Run the manual commands above"
print_info "2. Run: ~/check-mobile-setup.sh"
print_info "3. Run: ./verify-setup.sh"
print_info "4. Connect Android device or set up emulator"
print_info "5. Test: ./run-calculator-demo.sh"