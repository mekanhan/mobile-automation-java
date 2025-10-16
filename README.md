📱 Mobile Automation Framework
A robust, scalable mobile automation framework supporting Android and iOS platforms for native, hybrid, and web applications using Appium, Java, TestNG, and Cucumber BDD.
🚀 Key Features

Cross-Platform Support: Android and iOS automation in a single framework
Multiple App Support: Structured to handle multiple applications
BDD with Cucumber: Write tests in Gherkin for better collaboration
Page Object Model: Maintainable and reusable page components
Parallel Execution: Run tests in parallel on multiple devices
Cloud Integration: Ready for BrowserStack, Sauce Labs integration
Detailed Reporting: Allure and ExtentReports integration
CI/CD Ready: Pre-configured for Jenkins and GitHub Actions

## 🏗️ Architecture

This framework follows Page Object Model design pattern with clear separation between test logic and page objects.

### Project Structure
```
📦 mobile-automation-java
 ┣ 📂 src
 ┃ ┣ 📂 main
 ┃ ┃ ┣ 📂 java/mobile/automation
 ┃ ┃ ┃ ┣ 📂 config         → Device capabilities
 ┃ ┃ ┃ ┣ 📂 driver         → Driver management
 ┃ ┃ ┃ ┣ 📂 pages          → Page objects
 ┃ ┃ ┃ ┗ 📂 utils          → Helper utilities
 ┃ ┃ ┗ 📂 resources        → Config files
 ┃ ┗ 📂 test
 ┃   ┣ 📂 java             → Test implementation
 ┃   ┗ 📂 resources        → Test resources
 ┣ 📜 pom.xml              → Maven configuration
 ┗ 📜 README.md            → Project documentation
```

### Key Components

- **Config**: Manages capabilities for Android/iOS
- **Driver**: Handles WebDriver lifecycle
- **Pages**: Page Object Model implementation
- **Utils**: Reusable utility functions

🛠️ Tech Stack
TechnologyVersionPurposeJava17Programming languageAppium8.6.0Mobile automationSelenium4.16.1Web automation coreTestNG7.9.0Test frameworkCucumber7.15.0BDD frameworkMaven3.8+Build toolAllure2.25.0ReportingLog4j22.22.0Logging
📋 Prerequisites
System Requirements

Java JDK: 11 or higher
Node.js: 16.x or higher
Maven: 3.8 or higher
Appium: 2.x

Platform-Specific Requirements
Android

Android Studio
Android SDK (API Level 24+)
Set ANDROID_HOME environment variable
Android Emulator or Real Device

iOS (macOS only)

Xcode 14+
Xcode Command Line Tools
iOS Simulator or Real Device
Valid Apple Developer account (for real devices)

🔧 Installation
1. Clone the Repository
bashgit clone https://github.com/mekanhan/mobile-automation-java.git
cd mobile-automation-java
1. Install Dependencies
bash# Install Maven dependencies
mvn clean install

# Install Appium
npm install -g appium

# Install Appium drivers
appium driver install uiautomator2  # For Android
appium driver install xcuitest      # For iOS (macOS only)
3. Set Environment Variables
Windows
bashsetx JAVA_HOME "C:\Program Files\Java\jdk-17"
setx ANDROID_HOME "C:\Users\%USERNAME%\AppData\Local\Android\Sdk"
setx PATH "%PATH%;%ANDROID_HOME%\platform-tools;%ANDROID_HOME%\tools"
macOS/Linux
bashexport JAVA_HOME=$(/usr/libexec/java_home)
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools:$ANDROID_HOME/tools
4. Verify Installation
bash# Run environment check
mvn test -Dtest=EnvironmentCheck

# Verify Appium
appium --version
appium driver list
🏃 Running Tests
Start Appium Server
bashappium --port 4723 --log-level info
Run All Tests
bashmvn clean test
Run Platform-Specific Tests
bash# Android only
mvn test -Dcucumber.filter.tags="@android"

# iOS only
mvn test -Dcucumber.filter.tags="@ios"
Run App-Specific Tests
bash# App1 tests
mvn test -Dcucumber.filter.tags="@app1"

# Specific feature
mvn test -Dcucumber.options="src/test/resources/features/app1/login.feature"
Run with Custom Configuration
bashmvn test -Dplatform=android -Ddevice=real -Dapp=app1
📊 Test Reports
Generate Allure Report
bash# Generate report
mvn allure:report

# Open report in browser
mvn allure:serve
Report Locations

Allure Report: target/site/allure-maven-plugin/index.html
Cucumber Report: target/cucumber-reports/index.html
TestNG Report: target/surefire-reports/index.html

🔑 Configuration
Main Configuration File
Edit src/main/resources/config.properties:
properties# Platform Configuration
platform=android
device.type=emulator

# Android Settings
android.device.name=Pixel_6_API_33
android.platform.version=13

# iOS Settings
ios.device.name=iPhone 15
ios.platform.version=17.0

# App Configuration
app.name=app1
app1.android.path=src/main/resources/apps/android/app1.apk
app1.ios.path=src/main/resources/apps/ios/app1.app
📱 Supported Applications
AppDescriptionPackage/Bundle IDApp1E-commerce Democom.example.app1App2Social Media Democom.example.app2
🧪 Writing Tests
Cucumber Feature File
gherkin@app1 @android @ios
Feature: Login Functionality

  @smoke
  Scenario: Successful login with valid credentials
    Given I launch the application
    When I login with username "test@example.com" and password "Test123!"
    Then I should see the home screen
Step Definition
java@When("I login with username {string} and password {string}")
public void loginWithCredentials(String username, String password) {
    loginPage.enterUsername(username);
    loginPage.enterPassword(password);
    loginPage.clickLogin();
}
🐛 Troubleshooting
Common Issues
Appium Server Connection Error
bash# Check if Appium is running
lsof -ti:4723

# Kill existing process and restart
kill -9 $(lsof -ti:4723)
appium --port 4723
Android Device Not Found
bash# List connected devices
adb devices

# Restart ADB server
adb kill-server
adb start-server
iOS Simulator Issues
bash# List available simulators
xcrun simctl list devices

# Reset simulator
xcrun simctl erase all
🚀 CI/CD Integration
GitHub Actions
yaml- name: Run Tests
  run: mvn clean test -Dplatform=${{ matrix.platform }}
Jenkins
groovystage('Test') {
    steps {
        sh 'mvn clean test -Dplatform=${PLATFORM}'
    }
}
📈 Best Practices

Page Object Model: Keep page elements and actions separate
Wait Strategies: Use explicit waits over implicit waits
Parallel Execution: Run tests in parallel for faster feedback
Clean Code: Follow Java coding standards
Version Control: Commit regularly with meaningful messages

🤝 Contributing

Fork the repository
Create your feature branch (git checkout -b feature/AmazingFeature)
Commit your changes (git commit -m 'Add some AmazingFeature')
Push to the branch (git push origin feature/AmazingFeature)
Open a Pull Request

📚 Resources

Appium Documentation
Cucumber Documentation
TestNG Documentation
Project Wiki

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
👨‍💻 Author
Mekan Hanov

GitHub: mekanhan
LinkedIn: /in/mekanhan
Email: mekjanhan@gmail.com

🙏 Acknowledgments

Thanks to the Appium community for excellent documentation
Inspired by best practices from leading automation frameworks


⭐ If you find this project helpful, please give it a star!

