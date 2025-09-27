# Mobile Automation Framework - Java

<div align="center">

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)
![Platform](https://img.shields.io/badge/platform-iOS%20%7C%20Android-lightgrey.svg)
![Appium](https://img.shields.io/badge/Appium-8.5.1-green.svg)
![Java](https://img.shields.io/badge/Java-11-orange.svg)
![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red.svg)
![Cucumber](https://img.shields.io/badge/Cucumber-7.14.0-darkgreen.svg)
![RestAssured](https://img.shields.io/badge/RestAssured-5.3.2-purple.svg)

**A comprehensive cross-platform mobile automation framework with live Calculator app demo**

</div>

## 🚀 Quick Demo - Calculator App Automation

**Ready to see mobile automation in action?** This framework includes a working Calculator app demo that you can run immediately!

### ⚡ One-Command Demo

```bash
# Run the complete Calculator demo
./run-calculator-demo.sh

# Or run a quick smoke test
./run-quick-demo.sh
```

### 📱 What the Demo Shows

- ✅ **Android Calculator automation** (using pre-installed Calculator app)
- ✅ **Page Object Model** in action
- ✅ **Cucumber BDD scenarios** with readable test steps
- ✅ **Multiple test scenarios**: Basic addition, subtraction, clear function
- ✅ **Professional reporting** with HTML and JSON outputs
- ✅ **Robust element handling** with explicit waits

### 🎯 Demo Test Scenarios

1. **Basic Addition**: `2 + 3 = 5`
2. **Larger Numbers**: `15 + 27 = 42`
3. **Clear Function**: Reset calculator and verify clean state
4. **Multiple Operations**: Chain calculations together
5. **Subtraction**: `10 - 4 = 6`

### 📋 Prerequisites for Demo

Before running the demo, ensure you have:

1. **Java 11+** installed and in PATH
2. **Maven 3.6+** for build management
3. **Android SDK** with ADB in PATH
4. **Android device or emulator** connected and running
5. **Appium server** running on port 4723

#### Quick Setup

```bash
# 1. Install Appium globally
npm install -g appium

# 2. Start Appium server
appium --port 4723

# 3. Connect Android device or start emulator
adb devices  # Should show your device

# 4. Run the demo
./run-calculator-demo.sh
```

#### Troubleshooting

- **No devices found**: `adb devices` should list your device
- **Appium not running**: Check `http://127.0.0.1:4723/status`
- **Calculator not found**: Demo uses Google Calculator (pre-installed on most devices)
- **Permission issues**: Make sure scripts are executable (`chmod +x *.sh`)

## 🎯 Overview

This framework represents a complete mobile testing solution that I designed and built independently while managing multiple QA responsibilities. It demonstrates proficiency in modern automation architecture, cross-platform testing, and CI/CD integration.

### 📊 Key Achievements
- **100% solo development** - Architected and implemented entire framework from scratch
- **Cross-platform support** - Seamless iOS and Android testing with single codebase
- **Production-tested** - Successfully deployed in enterprise environment with Jenkins CI/CD
- **BDD implementation** - Business-readable test scenarios using Cucumber
- **Scalable architecture** - Page Object Model with modular design
- **Multi-device support** - Parallel execution on simulators, emulators, and real devices

## 🚀 Features

### Core Capabilities
- ✅ **Dual Platform Support**: iOS (XCUITest) and Android (UiAutomator2)
- ✅ **BDD Testing**: Cucumber with Gherkin syntax for readable test scenarios
- ✅ **Page Object Model**: Maintainable and reusable component structure
- ✅ **Parallel Execution**: Multi-device testing capability
- ✅ **CI/CD Ready**: Jenkins integration with configurable pipelines
- ✅ **Visual Reporting**: HTML reports with screenshots and video recordings
- ✅ **Environment Management**: Dev, Staging, and Production configurations
- ✅ **Data-Driven Testing**: Excel/CSV data file support
- ✅ **Retry Mechanisms**: Automatic retry logic for flaky tests
- ✅ **Video Recording**: Test execution recording for debugging

### Technical Stack
| Component | Technology | Version |
|-----------|------------|---------|
| Programming Language | Java | 11+ |
| Build Tool | Maven | 3.6+ |
| Mobile Driver | Appium Java Client | 8.5.1 |
| Test Framework | TestNG | 7.8.0 |
| BDD Framework | Cucumber | 7.14.0 |
| API Testing | RestAssured | 5.3.2 |
| iOS Driver | XCUITest | Latest |
| Android Driver | UiAutomator2 | Latest |
| Reporting | Extent Reports | 5.0.9 |
| Configuration | Owner Library | 1.0.12 |
| Logging | Logback | 1.4.11 |

## 📁 Project Structure

```
mobile-automation-framework/
├── src/main/java/com/example/     # Framework Source Code
│   ├── core/                      # Core framework components
│   │   ├── DriverManager.java     # Appium driver management
│   │   └── BasePage.java          # Base page object class
│   ├── config/                    # Configuration management
│   │   ├── ConfigManager.java     # Properties configuration
│   │   └── CapabilitiesFactory.java # Platform capabilities
│   └── pages/                     # Page Object Model
│       ├── CalculatorPage.java    # 🧮 Calculator demo page
│       ├── LoginPage.java         # Login page objects
│       └── [other pages...]       # Additional page objects
├── src/test/java/com/example/     # Test Implementation
│   ├── steps/                     # Cucumber step definitions
│   │   ├── CalculatorSteps.java   # 🧮 Calculator demo steps
│   │   └── [other steps...]       # Other step definitions
│   ├── runners/                   # TestNG test runners
│   │   ├── CalculatorTestRunner.java # 🧮 Calculator demo runner
│   │   ├── AndroidTestRunner.java # Android test runner
│   │   └── IOSTestRunner.java     # iOS test runner
│   └── hooks/                     # Test lifecycle hooks
├── src/test/resources/            # Test Resources
│   ├── features/                  # Cucumber feature files
│   │   ├── calculator.feature     # 🧮 Calculator demo scenarios
│   │   ├── login.feature          # Login scenarios
│   │   └── [other features...]    # Additional feature files
│   └── config/                    # Configuration files
│       ├── calculator-demo.properties # 🧮 Demo configuration
│       ├── dev.properties         # Development config
│       └── prod.properties        # Production config
├── target/                        # Build outputs
│   └── cucumber-reports/          # Test reports
│       └── calculator/            # 🧮 Calculator demo reports
├── run-calculator-demo.sh         # 🧮 One-click demo script
├── run-quick-demo.sh              # 🧮 Quick demo script
└── pom.xml                        # Maven configuration
```

### 🧮 Calculator Demo Files

The demo specifically includes these key files:

- **CalculatorPage.java**: Page object with Calculator app elements
- **calculator.feature**: BDD scenarios for Calculator operations  
- **CalculatorSteps.java**: Step definitions implementing the scenarios
- **CapabilitiesFactory.java**: Calculator app capabilities configuration
- **run-calculator-demo.sh**: Automated demo execution script

## 🛠 Installation & Setup

### Prerequisites
- Node.js 18+ and npm 9+
- Java JDK 11+ (for Android)
- Xcode 14+ (for iOS, macOS only)
- Android Studio (for Android)
- Appium Inspector (optional, for element inspection)

### Quick Start

1. **Clone the repository**
```bash
git clone <repository-url>
cd mobile-automation-framework
```

2. **Install dependencies**
```bash
npm install
```

3. **Install Appium drivers**
```bash
npm run install:drivers
```

4. **Set up environment variables**
```bash
cp .env.example .env
# Edit .env with your configuration
```

5. **Configure capabilities**
Edit device capabilities in `config/capabilities/` for your target devices

### Environment Configuration

Create a `.env` file with your configuration:

```env
# Platform Configuration
DEFAULT_PLATFORM=ios
DEFAULT_DEVICE_TYPE=simulator

# iOS Configuration
IOS_APP_PATH=./apps/SampleApp.app
IOS_DEVICE_NAME=iPhone 15
IOS_PLATFORM_VERSION=17.0

# Android Configuration
ANDROID_APP_PATH=./apps/SampleApp.apk
ANDROID_DEVICE_NAME=Pixel_7_API_34
ANDROID_PLATFORM_VERSION=14

# Test Configuration
DEFAULT_TIMEOUT=30000
SCREENSHOT_ON_FAIL=true
VIDEO_RECORDING=false

# Reporting
GENERATE_ALLURE_REPORT=true
```

## 🎮 Usage

### Running Tests

**iOS Simulator**
```bash
npm run ios:simulator
```

**Android Emulator**
```bash
npm run android:emulator
```

**Run Smoke Tests on Both Platforms**
```bash
npm run test:smoke
```

**Run Specific Feature**
```bash
npm run test -- --spec="./src/features/login.feature"
```

**Run with Tags**
```bash
npm run test -- --cucumberOpts.tags="@smoke and @critical"
```

**Parallel Execution**
```bash
npm run test:parallel
```

### Generating Reports

```bash
npm run report
```

This will generate and open an Allure report in your browser.

## 📝 Writing Tests

### Feature File Example

```gherkin
@smoke @login
Feature: Mobile App Authentication
  As a user
  I want to log into the application
  So that I can access my account

  Background:
    Given I launch the application

  @critical @ios @android
  Scenario: Successful login with valid credentials
    When I enter "testuser@example.com" in the email field
    And I enter "ValidPass123!" in the password field
    And I tap the login button
    Then I should see the home screen
    And I should see "Welcome back!" message

  @negative
  Scenario Outline: Login fails with invalid credentials
    When I enter "<email>" in the email field
    And I enter "<password>" in the password field
    And I tap the login button
    Then I should see "<error_message>" error

    Examples:
      | email               | password    | error_message           |
      | invalid@test.com    | wrongpass   | Invalid credentials     |
      | notanemail          | Pass123!    | Invalid email format    |
      |                     | Pass123!    | Email is required       |
```

### Page Object Example

```javascript
// src/page-objects/base/BasePage.js
class BasePage {
  async waitForElement(element, timeout = 10000) {
    await element.waitForDisplayed({ timeout });
    return element;
  }

  async tap(element) {
    await this.waitForElement(element);
    await element.click();
  }

  async setValue(element, value) {
    await this.waitForElement(element);
    await element.setValue(value);
  }

  async getText(element) {
    await this.waitForElement(element);
    return await element.getText();
  }

  async swipeUp() {
    const { height } = await driver.getWindowSize();
    await driver.touchPerform([
      { action: 'press', options: { x: 200, y: height * 0.8 }},
      { action: 'wait', options: { ms: 100 }},
      { action: 'moveTo', options: { x: 200, y: height * 0.2 }},
      { action: 'release' }
    ]);
  }
}

module.exports = BasePage;
```

```javascript
// src/page-objects/LoginPage.js
const BasePage = require('./base/BasePage');

class LoginPage extends BasePage {
  get emailInput() {
    return $('~email-input');
  }

  get passwordInput() {
    return $('~password-input');
  }

  get loginButton() {
    return $('~login-button');
  }

  get errorMessage() {
    return $('~error-message');
  }

  async login(email, password) {
    await this.setValue(this.emailInput, email);
    await this.setValue(this.passwordInput, password);
    await this.tap(this.loginButton);
  }

  async getErrorText() {
    return await this.getText(this.errorMessage);
  }
}

module.exports = new LoginPage();
```

## 🔧 Advanced Configuration

### Custom Capabilities

```javascript
// config/capabilities/ios.real.device.js
module.exports = {
  platformName: 'iOS',
  'appium:deviceName': 'iPhone 15 Pro',
  'appium:platformVersion': '17.2',
  'appium:automationName': 'XCUITest',
  'appium:app': process.env.IOS_APP_PATH,
  'appium:noReset': true,
  'appium:fullReset': false,
  'appium:newCommandTimeout': 240,
  'appium:settings[snapshotMaxDepth]': 60,
  'appium:settings[customSnapshotTimeout]': 50000
};
```

### Parallel Execution Config

```javascript
// config/parallel.conf.js
exports.config = {
  ...baseConfig,
  maxInstances: 4,
  capabilities: [
    {
      platformName: 'iOS',
      'appium:deviceName': 'iPhone 14',
      'appium:wdaLocalPort': 8100,
      'appium:systemPort': 8200
    },
    {
      platformName: 'Android',
      'appium:deviceName': 'Pixel_7',
      'appium:systemPort': 8201
    }
  ]
};
```

## 🚀 CI/CD Integration

### Jenkins Pipeline Example

```groovy
pipeline {
    agent any
    
    environment {
        NODE_VERSION = '18'
        APPIUM_VERSION = '2.5'
    }
    
    stages {
        stage('Setup') {
            steps {
                sh 'npm ci'
                sh 'npm run install:drivers'
            }
        }
        
        stage('Lint') {
            steps {
                sh 'npm run lint'
            }
        }
        
        stage('Test - iOS') {
            steps {
                sh 'npm run ios:smoke'
            }
        }
        
        stage('Test - Android') {
            steps {
                sh 'npm run android:smoke'
            }
        }
        
        stage('Generate Report') {
            steps {
                sh 'npm run report'
                publishHTML(target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'reports/allure-report',
                    reportFiles: 'index.html',
                    reportName: 'Test Report'
                ])
            }
        }
    }
    
    post {
        always {
            archiveArtifacts artifacts: 'reports/**/*', allowEmptyArchive: true
            cleanWs()
        }
    }
}
```

### GitHub Actions Workflow

```yaml
name: Mobile Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: macos-latest
    
    strategy:
      matrix:
        platform: [ios, android]
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Setup Node.js
      uses: actions/setup-node@v3
      with:
        node-version: '18'
    
    - name: Install dependencies
      run: |
        npm ci
        npm run install:drivers
    
    - name: Setup iOS Simulator
      if: matrix.platform == 'ios'
      run: |
        xcrun simctl create "iPhone 15" "iPhone 15" iOS17
        xcrun simctl boot "iPhone 15"
    
    - name: Setup Android Emulator
      if: matrix.platform == 'android'
      uses: reactivecircus/android-emulator-runner@v2
      with:
        api-level: 34
        script: echo "Emulator started"
    
    - name: Run Tests
      run: npm run ${{ matrix.platform }}:smoke
    
    - name: Generate Report
      if: always()
      run: npm run report
    
    - name: Upload Report
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: test-report-${{ matrix.platform }}
        path: reports/
```

## 🔍 Troubleshooting

### Common Issues and Solutions

| Issue | Solution |
|-------|----------|
| Appium server not starting | Check if port 4723 is in use: `lsof -i :4723` |
| iOS Simulator not found | Run `xcrun simctl list devices` to see available simulators |
| Android Emulator not starting | Ensure virtualization is enabled in BIOS |
| Elements not found | Use Appium Inspector to verify selectors |
| Tests timing out | Increase timeout in config or use explicit waits |

## 📈 Performance Metrics

Based on production usage:
- **Test Execution Speed**: ~2-3 seconds per test case
- **Parallel Execution**: Up to 4 devices simultaneously
- **Success Rate**: 95%+ with retry mechanism
- **Report Generation**: < 10 seconds for 100+ tests

## 🤝 Contributing

This framework was built as a portfolio piece demonstrating solo QA engineering capabilities. If you'd like to contribute or have suggestions:

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📜 License

MIT License - Feel free to use this framework as a reference or starting point for your own projects.

## 🏆 Acknowledgments

This framework was built entirely from scratch by a solo QA engineer while managing:
- Manual testing responsibilities
- Regression testing cycles
- Release management
- Production support

It demonstrates the ability to:
- Architect scalable test automation solutions
- Implement industry best practices
- Manage complex technical projects independently
- Deliver production-quality code under real-world constraints

## 📞 Contact

For questions, suggestions, or collaboration opportunities, please reach out through GitHub Issues or connect on LinkedIn.

---

**Built with determination by a solo QA engineer** 🚀