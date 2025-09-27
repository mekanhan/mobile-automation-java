#!/bin/bash

# ========================================
# Mobile Automation Complete Setup Script
# ========================================
# Run this script to install all requirements for mobile automation

set -e  # Exit on any error

echo "🚀 Mobile Automation Framework - Complete Setup"
echo "==============================================="
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

print_step() {
    echo ""
    echo -e "${YELLOW}========================================${NC}"
    echo -e "${YELLOW} STEP: $1${NC}"
    echo -e "${YELLOW}========================================${NC}"
}

# Check if running on WSL
if grep -qi microsoft /proc/version; then
    print_warning "Detected WSL environment. Some Android emulator features may not work."
    print_status "Consider using a physical Android device instead."
fi

# 1. Update system packages
print_step "1. Updating System Packages"
print_status "Updating package lists..."
sudo apt update

# 2. Install Java JDK 11
print_step "2. Installing Java JDK 11"
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    print_warning "Java already installed: $JAVA_VERSION"
else
    print_status "Installing OpenJDK 11..."
    sudo apt install openjdk-11-jdk -y
fi

# Verify Java installation
if java -version &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    print_success "Java installed: $JAVA_VERSION"
    export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
else
    print_error "Java installation failed"
    exit 1
fi

# 3. Install Maven
print_step "3. Installing Maven"
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version | head -n 1)
    print_warning "Maven already installed: $MVN_VERSION"
else
    print_status "Installing Maven..."
    sudo apt install maven -y
fi

# Verify Maven installation
if mvn -version &> /dev/null; then
    MVN_VERSION=$(mvn -version | head -n 1)
    print_success "Maven installed: $MVN_VERSION"
else
    print_error "Maven installation failed"
    exit 1
fi

# 4. Install Android Studio dependencies
print_step "4. Installing Android Studio Dependencies"
print_status "Installing required libraries..."
sudo apt install -y libc6:i386 libncurses5:i386 libstdc++6:i386 lib32z1 libbz2-1.0:i386

# 5. Download and Install Android Studio
print_step "5. Installing Android Studio"
ANDROID_STUDIO_VERSION="2023.3.1.18"
ANDROID_STUDIO_URL="https://redirector.gvt1.com/edgedl/android/studio/ide-zips/2023.3.1.18/android-studio-2023.3.1.18-linux.tar.gz"

if [ ! -d "/opt/android-studio" ]; then
    print_status "Downloading Android Studio..."
    cd /tmp
    wget -O android-studio.tar.gz "$ANDROID_STUDIO_URL"
    
    print_status "Extracting Android Studio..."
    sudo tar -xzf android-studio.tar.gz -C /opt/
    
    print_status "Creating Android Studio desktop entry..."
    sudo cp /opt/android-studio/bin/studio.desktop /usr/share/applications/
    sudo chmod +x /usr/share/applications/studio.desktop
    
    # Create symlink for command line access
    sudo ln -sf /opt/android-studio/bin/studio.sh /usr/local/bin/android-studio
    
    print_success "Android Studio installed to /opt/android-studio"
else
    print_warning "Android Studio already installed"
fi

# 6. Set up Android SDK environment
print_step "6. Setting up Android SDK Environment"
export ANDROID_HOME="$HOME/Android/Sdk"
export PATH="$PATH:$ANDROID_HOME/emulator"
export PATH="$PATH:$ANDROID_HOME/tools"
export PATH="$PATH:$ANDROID_HOME/tools/bin"
export PATH="$PATH:$ANDROID_HOME/platform-tools"

# Create Android SDK directory
mkdir -p "$ANDROID_HOME"

print_status "Android SDK path set to: $ANDROID_HOME"

# 7. Install Appium
print_step "7. Installing Appium"
if command -v appium &> /dev/null; then
    APPIUM_VERSION=$(appium --version)
    print_warning "Appium already installed: $APPIUM_VERSION"
else
    print_status "Installing Appium globally..."
    sudo npm install -g appium
fi

# Install Appium drivers
print_status "Installing Appium UiAutomator2 driver..."
appium driver install uiautomator2

# Verify Appium installation
if appium --version &> /dev/null; then
    APPIUM_VERSION=$(appium --version)
    print_success "Appium installed: $APPIUM_VERSION"
else
    print_error "Appium installation failed"
    exit 1
fi

# 8. Install Appium Doctor for diagnostics
print_step "8. Installing Appium Doctor"
if command -v appium-doctor &> /dev/null; then
    print_warning "Appium Doctor already installed"
else
    print_status "Installing Appium Doctor..."
    sudo npm install -g appium-doctor
fi

# 9. Create environment setup script
print_step "9. Creating Environment Setup Script"
cat > ~/.android_env << 'EOF'
# Android Development Environment
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
    print_status "Added Android environment to ~/.bashrc"
fi

print_success "Environment setup script created at ~/.android_env"

# 10. Final setup instructions
print_step "10. Next Steps - Manual Configuration Required"
echo ""
print_status "✅ Automated installation completed!"
echo ""
print_warning "📋 MANUAL STEPS REQUIRED:"
echo ""
echo "1. 🚀 Launch Android Studio:"
echo "   - Run: android-studio"
echo "   - Complete the setup wizard"
echo "   - Install Android SDK (API 30-34 recommended)"
echo "   - Create an Android Virtual Device (AVD)"
echo ""
echo "2. 🔄 Reload environment variables:"
echo "   - Run: source ~/.bashrc"
echo "   - Or restart your terminal"
echo ""
echo "3. 📱 Set up Android emulator:"
echo "   - Open Android Studio"
echo "   - Go to Tools > AVD Manager"
echo "   - Create a new virtual device (Pixel 7, API 34)"
echo ""
echo "4. ✅ Test the setup:"
echo "   - Run: cd $(pwd)"
echo "   - Run: ./verify-setup.sh"
echo ""
print_success "Setup script completed! Follow the manual steps above."