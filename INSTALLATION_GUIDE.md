# 📲 Complete Mobile Automation Setup Guide

## 🎯 Overview

This guide will help you install everything needed to run the Calculator demo, starting from a fresh system.

## 📋 Prerequisites Check

Before starting, verify what you already have:

```bash
# Check existing installations
node --version    # You have: v22.17.0 ✅
npm --version     # You have: 10.9.2 ✅
java -version     # Need to install ❌
mvn -version      # Need to install ❌
adb devices       # Need to install ❌
```

## 🚀 Installation Steps

### Step 1: Run the Automated Setup Script

```bash
# Make the script executable and run it
chmod +x setup-mobile-automation.sh
./setup-mobile-automation.sh
```

This script will install:
- ✅ Java JDK 11
- ✅ Maven 
- ✅ Android Studio
- ✅ Appium and drivers
- ✅ Required system dependencies

### Step 2: Manual Android Studio Configuration

After the script completes:

#### 2.1 Launch Android Studio
```bash
android-studio
```

#### 2.2 Complete Setup Wizard
1. **Welcome Screen**: Click "Next"
2. **Install Type**: Choose "Standard" installation
3. **Select UI Theme**: Choose your preference
4. **SDK Components**: Verify these are selected:
   - Android SDK
   - Android SDK Platform
   - Android Virtual Device
5. **Accept Licenses**: Accept all license agreements
6. **Download**: Wait for components to download (may take 10-20 minutes)

#### 2.3 Configure SDK
1. Go to **File > Settings > Appearance & Behavior > System Settings > Android SDK**
2. **SDK Platforms tab**: Install these Android versions:
   - ✅ Android 14.0 (API 34) - Recommended
   - ✅ Android 13.0 (API 33)
   - ✅ Android 12.0 (API 31)
3. **SDK Tools tab**: Ensure these are installed:
   - ✅ Android SDK Build-Tools
   - ✅ Android Emulator
   - ✅ Android SDK Platform-Tools
   - ✅ Intel x86 Emulator Accelerator (if using Intel CPU)

### Step 3: Create Android Virtual Device (AVD)

#### 3.1 Open AVD Manager
- In Android Studio: **Tools > AVD Manager**
- Or click the AVD Manager icon in the toolbar

#### 3.2 Create New AVD
1. Click **"Create Virtual Device"**
2. **Select Hardware**: Choose **"Pixel 7"** (recommended)
3. **Select System Image**: 
   - Choose **API Level 34** (Android 14.0)
   - Download if not already available
4. **AVD Configuration**:
   - Name: `Pixel_7_API_34`
   - Startup orientation: Portrait
   - Click **"Finish"**

#### 3.3 Test the Emulator
1. Click the **Play button** next to your AVD
2. Wait for emulator to boot (first boot takes 2-3 minutes)
3. Verify Calculator app is installed (should be in app drawer)

### Step 4: Configure Environment Variables

#### 4.1 Reload Environment
```bash
# Reload environment variables
source ~/.bashrc

# Or restart your terminal
```

#### 4.2 Verify Environment Variables
```bash
echo $JAVA_HOME
echo $ANDROID_HOME
echo $PATH | grep android
```

Should show:
```
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ANDROID_HOME=/home/yourusername/Android/Sdk
PATH includes Android SDK paths
```

### Step 5: Start Appium Server

#### 5.1 Open New Terminal
```bash
# Start Appium server
appium --port 4723
```

Keep this terminal running. You should see:
```
[Appium] Welcome to Appium v2.x.x
[Appium] Appium REST http interface listener started on 0.0.0.0:4723
```

### Step 6: Verify Complete Setup

#### 6.1 Run Verification Script
```bash
# In a new terminal, navigate to your project
cd /home/mekanhan/github/mobile-framework-modern

# Run verification
./verify-setup.sh
```

#### 6.2 Expected Output
You should see all green checkmarks (✅) for:
- ✅ Java JDK 11+
- ✅ Maven
- ✅ Node.js and npm
- ✅ Appium and UiAutomator2 driver
- ✅ Android SDK and ANDROID_HOME
- ✅ ADB with connected device/emulator
- ✅ Maven project compilation
- ✅ Appium server running

### Step 7: Run the Calculator Demo

#### 7.1 Full Demo
```bash
./run-calculator-demo.sh
```

#### 7.2 Quick Demo
```bash
./run-quick-demo.sh
```

## 🐛 Troubleshooting

### Common Issues & Solutions

#### Java Issues
```bash
# If JAVA_HOME not set
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> ~/.bashrc
```

#### Android SDK Issues
```bash
# If ANDROID_HOME not set
export ANDROID_HOME=$HOME/Android/Sdk
echo 'export ANDROID_HOME=$HOME/Android/Sdk' >> ~/.bashrc

# Add Android tools to PATH
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator
```

#### Emulator Issues
```bash
# Check available AVDs
emulator -list-avds

# Start specific emulator
emulator -avd Pixel_7_API_34

# Check emulator is running
adb devices
```

#### Appium Issues
```bash
# Install Appium globally
sudo npm install -g appium

# Install UiAutomator2 driver
appium driver install uiautomator2

# Check Appium doctor
appium-doctor --android
```

#### Permission Issues
```bash
# Make scripts executable
chmod +x *.sh

# Fix ADB permissions (if needed)
sudo usermod -a -G plugdev $USER
```

### WSL-Specific Issues

If running on WSL (Windows Subsystem for Linux):

1. **Emulator won't start**: Use a physical Android device instead
2. **USB debugging**: Enable USB debugging on physical device
3. **ADB connection**: Use `adb connect <device-ip>` for wireless debugging

## ✅ Verification Checklist

Before running the demo, ensure:

- [ ] Java 11+ installed and JAVA_HOME set
- [ ] Maven installed and working
- [ ] Android Studio configured with SDK
- [ ] Emulator created and running (or physical device connected)
- [ ] Appium server running on port 4723
- [ ] ADB can see your device (`adb devices`)
- [ ] Calculator app visible on device/emulator
- [ ] Environment variables loaded (`source ~/.bashrc`)
- [ ] Project compiles successfully (`mvn clean compile`)

## 🎉 Success!

When everything is working, you should be able to:

1. **Run verification**: `./verify-setup.sh` shows all green checkmarks
2. **Run demo**: `./run-calculator-demo.sh` executes successfully
3. **See automation**: Watch the Calculator app perform operations automatically
4. **View reports**: HTML report opens showing test results

**Your mobile automation framework is now ready for demonstration!**