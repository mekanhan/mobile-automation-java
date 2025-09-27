# Cleanup Guide: Removing Company-Specific References

This guide shows you how to clean up your existing framework by replacing company-specific app names with generic ones.

## Quick Replacement Commands

Run these commands from your project root to replace all references:

### 1. Replace in All Text Files

```bash
# Create a backup first
cp -r . ../testing-auto-mobile-backup

# Replace schoolApp with mobileApp1
find . -type f \( -name "*.js" -o -name "*.json" -o -name "*.feature" -o -name "*.md" \) \
  -exec sed -i 's/schoolApp/mobileApp1/g' {} \;
  
find . -type f \( -name "*.js" -o -name "*.json" -o -name "*.feature" -o -name "*.md" \) \
  -exec sed -i 's/SchoolApp/MobileApp1/g' {} \;

# Replace thrillshare with mobileApp2  
find . -type f \( -name "*.js" -o -name "*.json" -o -name "*.feature" -o -name "*.md" \) \
  -exec sed -i 's/thrillshare/mobileApp2/g' {} \;
  
find . -type f \( -name "*.js" -o -name "*.json" -o -name "*.feature" -o -name "*.md" \) \
  -exec sed -i 's/Thrillshare/MobileApp2/g' {} \;
```

### 2. Rename Directories

```bash
# Rename schoolApp directories
mv src/android/schoolApp src/android/mobileApp1
mv src/ios/schoolApp src/ios/mobileApp1
mv utils/androidSchoolAppConfigs utils/androidMobileApp1Configs
mv utils/iosSchoolAppConfigs utils/iosMobileApp1Configs

# Rename thrillshare directories
mv src/android/thrillshare src/android/mobileApp2
mv src/ios/thrillshare src/ios/mobileApp2
mv utils/androidThrillshareConfigs utils/androidMobileApp2Configs
mv utils/iosThrillshareConfigs utils/iosMobileApp2Configs
```

### 3. Rename Files

```bash
# Find and rename files containing schoolApp
find . -name "*schoolApp*" -type f | while read file; do
    mv "$file" "${file//schoolApp/mobileApp1}"
done

find . -name "*SchoolApp*" -type f | while read file; do
    mv "$file" "${file//SchoolApp/MobileApp1}"
done

# Find and rename files containing thrillshare
find . -name "*thrillshare*" -type f | while read file; do
    mv "$file" "${file//thrillshare/mobileApp2}"
done

find . -name "*Thrillshare*" -type f | while read file; do
    mv "$file" "${file//Thrillshare/MobileApp2}"
done
```

## Manual Updates in package.json

Replace these script names in your package.json:

### Old Scripts:
```json
"iosSchoolAppSim1": "...",
"androidSchoolApp_galaxyS22": "...",
"iosThrillshare": "...",
"androidThrillshare": "..."
```

### New Scripts:
```json
"iosMobileApp1Sim1": "...",
"androidMobileApp1_galaxyS22": "...",
"iosMobileApp2": "...",
"androidMobileApp2": "..."
```

## Update Feature File Tags

### Old Tags:
```gherkin
@iosSchoolApp @androidSchoolApp
@iosThrillshare @androidThrillshare
```

### New Tags:
```gherkin
@iosMobileApp1 @androidMobileApp1
@iosMobileApp2 @androidMobileApp2
```

## Update Configuration Files

### In cucumber.js or runner.js:

```javascript
// Old
if (appName.includes('schoolApp')) {
  // ...
}

// New  
if (appName.includes('mobileApp1')) {
  // ...
}
```

## Update Page Object Class Names

### Old:
```javascript
class IosSchoolAppPage { }
class AndroidThrillsharePage { }
```

### New:
```javascript
class IosMobileApp1Page { }
class AndroidMobileApp2Page { }
```

## Complete List of Replacements

| Old Reference | New Reference |
|--------------|---------------|
| schoolApp | mobileApp1 |
| SchoolApp | MobileApp1 |
| schoolapp | mobileapp1 |
| SCHOOLAPP | MOBILEAPP1 |
| thrillshare | mobileApp2 |
| Thrillshare | MobileApp2 |
| ThrillShare | MobileApp2 |
| THRILLSHARE | MOBILEAPP2 |

## Verification

After making these changes, verify:

1. **Check no old references remain:**
```bash
grep -r "schoolApp\|thrillshare" . --exclude-dir=node_modules --exclude-dir=.git
```

2. **Verify directory structure:**
```bash
tree -d src/
```

3. **Test a sample script:**
```bash
npm run iosMobileApp1Sim1 --dry-run
```

## Additional Company Data to Remove

Also search and replace these patterns with generic equivalents:

- Email domains: `@apptegy.com` → `@example.com`
- User names: `studentKelly`, `guardianLana` → `testUser1`, `testUser2`
- Organization names: `Automation` → `TestOrg`
- Any API endpoints or URLs specific to your company
- Any hardcoded passwords or tokens
- Company-specific test data files

## Final Cleanup

1. Remove any proprietary apps from the `apps/` directory
2. Clear out any company-specific test reports in `reports/`
3. Remove any Jenkins jobs that reference internal systems
4. Delete any screenshots or videos with company content

This will leave you with a clean, generic framework suitable for your portfolio!