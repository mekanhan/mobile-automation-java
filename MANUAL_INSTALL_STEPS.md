# 🛠️ Manual Installation Steps

Since you're in WSL, here are the exact commands to run step by step:

## ✅ Prerequisites Already Done
- ✅ Node.js v22.17.0 installed
- ✅ npm 10.9.2 installed  
- ✅ Android Studio downloaded to `/tmp/android-studio.tar.gz`

## 🚀 Run These Commands

### 1. Install Java 11
```bash
sudo apt update
sudo apt install openjdk-11-jdk -y
```

### 2. Install Maven
```bash
sudo apt install maven -y
```

### 3. Extract Android Studio
```bash
# Extract Android Studio to /opt/
sudo tar -xzf /tmp/android-studio.tar.gz -C /opt/

# Create symlink for easy access
sudo ln -sf /opt/android-studio/bin/studio.sh /usr/local/bin/android-studio

# Make it executable
sudo chmod +x /opt/android-studio/bin/studio.sh
```

### 4. Install Appium
```bash
# Install Appium globally
sudo npm install -g appium

# Install UiAutomator2 driver
appium driver install uiautomator2

# Install Appium Doctor (optional but helpful)
sudo npm install -g appium-doctor
```

### 5. Set up Environment Variables
```bash
# Add to ~/.bashrc
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> ~/.bashrc
echo 'export ANDROID_HOME=$HOME/Android/Sdk' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/emulator' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/tools' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/tools/bin' >> ~/.bashrc
echo 'export PATH=$PATH:$ANDROID_HOME/platform-tools' >> ~/.bashrc

# Reload environment
source ~/.bashrc
```

### 6. Create Android SDK Directory
```bash
mkdir -p $HOME/Android/Sdk
```

## 🔄 After Installation

### 7. Verify Installation
```bash
cd /home/mekanhan/github/mobile-framework-modern
./verify-setup.sh
```

### 8. Launch Android Studio (First Time Setup)

**Option A: WSL with GUI (if you have X server)**
```bash
android-studio
```

**Option B: WSL without GUI (Use Windows Android Studio)**
Since you're in WSL, it's often easier to:
1. Install Android Studio on Windows
2. Set up the Android SDK there
3. Use ADB forwarding to connect WSL to Windows emulator

### 9. Windows Alternative Setup

If Android Studio GUI doesn't work in WSL:

1. **Download Android Studio for Windows**
   - Go to: https://developer.android.com/studio
   - Install on Windows normally

2. **Set up WSL to use Windows ADB**
   ```bash
   # Add Windows ADB to WSL path (adjust path as needed)
   echo 'export PATH=$PATH:/mnt/c/Users/YourUsername/AppData/Local/Android/Sdk/platform-tools' >> ~/.bashrc
   source ~/.bashrc
   ```

3. **Connect WSL to Windows emulator**
   ```bash
   # In WSL, connect to Windows ADB server
   adb connect 127.0.0.1:5555
   ```

## ✅ Verification Commands

Run these to verify everything works:

```bash
# Check Java
java -version

# Check Maven  
mvn -version

# Check Node.js
node --version

# Check npm
npm --version

# Check Appium
appium --version

# Check environment variables
echo $JAVA_HOME
echo $ANDROID_HOME

# Check Android Studio
ls -la /opt/android-studio/

# Full verification
./verify-setup.sh
```

## 🎯 Expected Output

You should see:
```
java version "11.0.x"
Apache Maven 3.x.x
Node.js v22.17.0
npm 10.9.2
Appium 2.x.x
JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ANDROID_HOME=/home/username/Android/Sdk
```

## 📱 Next Steps After Setup

1. **Start Appium Server**
   ```bash
   appium --port 4723
   ```

2. **Connect Android Device or Use Emulator**
   - Physical device: Enable USB debugging
   - Emulator: Set up through Android Studio

3. **Run Calculator Demo**
   ```bash
   ./run-calculator-demo.sh
   ```

## 🐛 WSL-Specific Notes

- **GUI Apps**: May need X server (like VcXsrv) to run Android Studio
- **Emulator**: May not work in WSL - use physical device or Windows emulator
- **ADB**: Can forward from Windows to WSL
- **Performance**: Physical device recommended for WSL environments

## 🆘 If You Get Stuck

1. **Check the verification script**:
   ```bash
   ./verify-setup.sh
   ```

2. **Common issues**:
   - Permission denied: `chmod +x *.sh`
   - Java not found: Check JAVA_HOME
   - ADB not found: Install Android SDK platform-tools

3. **WSL alternatives**:
   - Use Windows Android Studio + WSL development
   - Use physical Android device instead of emulator