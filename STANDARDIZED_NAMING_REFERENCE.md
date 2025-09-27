# Standardized Naming Reference

## 🎯 Final Naming Convention

Your framework now uses completely standardized naming throughout:

### 📱 Platform-Specific Tags

| Old Tag | New Tag | Purpose |
|---------|---------|---------|
| `@iosSchoolApp` | `@iosMobileApp1` | iOS tests for business/educational app |
| `@androidSchoolApp` | `@androidMobileApp1` | Android tests for business/educational app |
| `@iosThrillshare` | `@iosMobileApp2` | iOS tests for social/sharing app |
| `@androidThrillshare` | `@androidMobileApp2` | Android tests for social/sharing app |

### 🏗️ Application Names

| Old Name | New Name | App Type |
|----------|----------|-----------|
| `schoolApp` | `mobileApp1` | Business/Educational Application |
| `thrillshare` | `mobileApp2` | Social/Sharing Application |

### 📂 Directory Structure

```
src/
├── android/
│   ├── mobileApp1/           # (was schoolApp)
│   │   ├── features/
│   │   ├── step-definitions/
│   │   └── page-objects/
│   └── mobileApp2/           # (was thrillshare)
│       ├── features/
│       ├── step-definitions/
│       └── page-objects/
└── ios/
    ├── mobileApp1/           # (was schoolApp)
    │   ├── features/
    │   ├── step-definitions/
    │   └── page-objects/
    └── mobileApp2/           # (was thrillshare)
        ├── features/
        ├── step-definitions/
        └── page-objects/

utils/
├── androidMobileApp1Configs/ # (was androidSchoolAppConfigs)
├── iosMobileApp1Configs/     # (was iosSchoolAppConfigs)
├── androidMobileApp2Configs/ # (was androidThrillshareConfigs)
└── iosMobileApp2Configs/     # (was iosThrillshareConfigs)
```

### 📜 NPM Scripts

#### Mobile App 1 (Business/Educational App)
```json
"ios:app1:smoke": "... --tags='@iosMobileApp1 and @smoke'",
"android:app1:smoke": "... --tags='@androidMobileApp1 and @smoke'",
"ios:app1:regression": "... --tags='@iosMobileApp1 and @regression'",
"android:app1:regression": "... --tags='@androidMobileApp1 and @regression'"
```

#### Mobile App 2 (Social/Sharing App)
```json
"ios:app2:smoke": "... --tags='@iosMobileApp2 and @smoke'",
"android:app2:smoke": "... --tags='@androidMobileApp2 and @smoke'",
"ios:app2:regression": "... --tags='@iosMobileApp2 and @regression'",
"android:app2:regression": "... --tags='@androidMobileApp2 and @regression'"
```

### 🏷️ Feature File Tags

#### Correct Tag Usage:
```gherkin
Feature: Mobile App 1 Login Authentication
  @smoke @login @iosMobileApp1 @androidMobileApp1
  Scenario: Successful login to Mobile App 1
    # Test steps...

Feature: Mobile App 2 Social Features  
  @regression @social @iosMobileApp2 @androidMobileApp2
  Scenario: Create post in Mobile App 2
    # Test steps...
```

### 📄 File Naming Patterns

| Component | Pattern | Example |
|-----------|---------|---------|
| Page Objects | `[platform]MobileApp[1|2]-page.js` | `iosMobileApp1-page.js` |
| Config Files | `[platform]-mobileApp[1|2]-config.js` | `android-mobileApp2-config.js` |
| Feature Files | `[platform]MobileApp[1|2].feature` | `androidMobileApp1.feature` |

### 🔧 Import/Require Statements

```javascript
// Page Object Imports
const IosMobileApp1Page = require('./mobileApp1/page-objects/iosMobileApp1-page');
const AndroidMobileApp2Page = require('./mobileApp2/page-objects/androidMobileApp2-page');

// Config Imports
const mobileApp1Config = require('./utils/iosMobileApp1Configs/ios-mobileApp1-config');
const mobileApp2Config = require('./utils/androidMobileApp2Configs/android-mobileApp2-config');

// Step Definition Paths
'./src/ios/mobileApp1/step-definitions'
'./src/android/mobileApp2/step-definitions'
```

### 🎛️ Cucumber Runner Configuration

```javascript
// App detection logic
if (appName.includes('iosMobileApp1') || appName.includes('androidMobileApp1')) {
  // Handle Mobile App 1
  program.steps = appName.includes('ios') 
    ? './src/ios/mobileApp1/step-definitions'
    : './src/android/mobileApp1/step-definitions';
} else if (appName.includes('iosMobileApp2') || appName.includes('androidMobileApp2')) {
  // Handle Mobile App 2  
  program.steps = appName.includes('ios')
    ? './src/ios/mobileApp2/step-definitions'
    : './src/android/mobileApp2/step-definitions';
}
```

### 🧪 Test Execution Examples

```bash
# Run smoke tests for Mobile App 1 on iOS
npm run ios:app1:smoke

# Run regression tests for Mobile App 2 on Android  
npm run android:app2:regression

# Run all smoke tests across both apps and platforms
npm run test:all:smoke

# Run specific tags
npm test -- --tags='@iosMobileApp1 and @login'
npm test -- --tags='@androidMobileApp2 and @social'
```

### ✅ Benefits of Standardized Naming

1. **Consistency**: No more mixed naming conventions
2. **Clarity**: Platform and app clearly identified in tags
3. **Scalability**: Easy to add mobileApp3, mobileApp4, etc.
4. **Maintainability**: Predictable file and directory structure
5. **Portfolio Ready**: Professional, generic naming suitable for sharing

### 🚀 Quick Reference Commands

When working with the framework, use these patterns:

```bash
# iOS Mobile App 1 (Business/Educational)
npm run ios:app1:[simulator|device|smoke|regression]

# Android Mobile App 1 (Business/Educational)  
npm run android:app1:[emulator|device|smoke|regression]

# iOS Mobile App 2 (Social/Sharing)
npm run ios:app2:[simulator|device|smoke|regression]

# Android Mobile App 2 (Social/Sharing)
npm run android:app2:[emulator|device|smoke|regression]
```

### 📋 Verification Checklist

After applying all changes, verify:

- [ ] All feature files use `@iosMobileApp1`, `@androidMobileApp1`, `@iosMobileApp2`, `@androidMobileApp2` tags
- [ ] All directories follow `mobileApp1`/`mobileApp2` pattern  
- [ ] All file names use standardized patterns
- [ ] All import/require statements point to correct paths
- [ ] NPM scripts use consistent naming
- [ ] Page object classes follow naming convention
- [ ] Configuration files are properly named
- [ ] No old references (`schoolApp`, `thrillshare`, etc.) remain

Your framework now has **completely standardized, portfolio-ready naming** that clearly demonstrates your architectural skills! 🎊