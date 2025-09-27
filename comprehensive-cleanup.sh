#!/bin/bash

# Comprehensive Mobile App Naming Cleanup Script
# This script standardizes all naming to mobileApp1 and mobileApp2

echo "========================================="
echo "Comprehensive Mobile App Cleanup Script"
echo "========================================="
echo ""
echo "This script will standardize ALL naming to:"
echo "  - schoolApp/mobileApp -> mobileApp1"
echo "  - thrillshare -> mobileApp2"
echo "  - iosSchoolApp -> iosMobileApp1"
echo "  - androidSchoolApp -> androidMobileApp1"
echo "  - iosThrillshare -> iosMobileApp2"
echo "  - androidThrillshare -> androidMobileApp2"
echo ""

# Set the base directory
BASE_DIR="/home/mekanhan/github/testing-auto-mobile"
cd "$BASE_DIR" || exit 1

# Create comprehensive backup
echo "Creating comprehensive backup..."
tar -czf ../testing-auto-mobile-final-backup-$(date +%Y%m%d_%H%M%S).tar.gz . 2>/dev/null || true

# Function to replace ALL text patterns in files
replace_all_patterns() {
    echo "Replacing ALL naming patterns in files..."
    
    # Find all text files and replace content comprehensively
    find . -type f \( -name "*.js" -o -name "*.json" -o -name "*.feature" -o -name "*.md" -o -name "*.yml" -o -name "*.yaml" -o -name "*.txt" -o -name "*.csv" -o -name "*.xml" -o -name "*.conf" -o -name "*.config" \) \
        -not -path "./node_modules/*" \
        -not -path "./.git/*" \
        -exec sed -i.bak \
            -e 's/iosSchoolApp/iosMobileApp1/g' \
            -e 's/iOSSchoolApp/iOSMobileApp1/g' \
            -e 's/IOSSchoolApp/IOSMobileApp1/g' \
            -e 's/androidSchoolApp/androidMobileApp1/g' \
            -e 's/AndroidSchoolApp/AndroidMobileApp1/g' \
            -e 's/ANDROIDSCHOOLAPP/ANDROIDMOBILEAPP1/g' \
            -e 's/iosThrillshare/iosMobileApp2/g' \
            -e 's/iosThrillShare/iosMobileApp2/g' \
            -e 's/IOSThrillshare/IOSMobileApp2/g' \
            -e 's/androidThrillshare/androidMobileApp2/g' \
            -e 's/androidThrillShare/androidMobileApp2/g' \
            -e 's/AndroidThrillshare/AndroidMobileApp2/g' \
            -e 's/ANDROIDTHRILLSHARE/ANDROIDMOBILEAPP2/g' \
            -e 's/schoolApp/mobileApp1/g' \
            -e 's/SchoolApp/MobileApp1/g' \
            -e 's/schoolapp/mobileapp1/g' \
            -e 's/SCHOOLAPP/MOBILEAPP1/g' \
            -e 's/thrillshare/mobileApp2/g' \
            -e 's/Thrillshare/MobileApp2/g' \
            -e 's/ThrillShare/MobileApp2/g' \
            -e 's/THRILLSHARE/MOBILEAPP2/g' \
            -e 's/mobileApp/mobileApp1/g' \
            -e 's/MobileApp/MobileApp1/g' \
            {} \; 2>/dev/null || true
    
    # Remove backup files
    find . -name "*.bak" -type f -delete 2>/dev/null || true
}

# Function to rename directories comprehensively
rename_all_directories() {
    echo "Renaming ALL directories..."
    
    # Rename from deepest to shallowest to avoid path issues
    # Handle schoolApp variations
    find . -depth -type d -name "*schoolApp*" -o -name "*SchoolApp*" | while read -r dir; do
        newdir=$(echo "$dir" | sed -e 's/schoolApp/mobileApp1/g' -e 's/SchoolApp/MobileApp1/g')
        if [ "$dir" != "$newdir" ] && [ -d "$dir" ]; then
            echo "  Renaming: $dir -> $newdir"
            mv "$dir" "$newdir" 2>/dev/null || true
        fi
    done
    
    # Handle thrillshare variations
    find . -depth -type d -name "*thrillshare*" -o -name "*Thrillshare*" -o -name "*ThrillShare*" | while read -r dir; do
        newdir=$(echo "$dir" | sed -e 's/thrillshare/mobileApp2/g' -e 's/Thrillshare/MobileApp2/g' -e 's/ThrillShare/MobileApp2/g')
        if [ "$dir" != "$newdir" ] && [ -d "$dir" ]; then
            echo "  Renaming: $dir -> $newdir"
            mv "$dir" "$newdir" 2>/dev/null || true
        fi
    done
    
    # Handle mobileApp -> mobileApp1 (if any bare mobileApp exists)
    find . -depth -type d -name "mobileApp" | while read -r dir; do
        newdir=$(echo "$dir" | sed 's/mobileApp$/mobileApp1/')
        if [ "$dir" != "$newdir" ] && [ -d "$dir" ]; then
            echo "  Renaming: $dir -> $newdir"
            mv "$dir" "$newdir" 2>/dev/null || true
        fi
    done
}

# Function to rename files comprehensively
rename_all_files() {
    echo "Renaming ALL files..."
    
    # Handle schoolApp files
    find . -type f \( -name "*schoolApp*" -o -name "*SchoolApp*" \) | while read -r file; do
        newfile=$(echo "$file" | sed -e 's/schoolApp/mobileApp1/g' -e 's/SchoolApp/MobileApp1/g')
        if [ "$file" != "$newfile" ] && [ -f "$file" ]; then
            echo "  Renaming: $file -> $newfile"
            mv "$file" "$newfile" 2>/dev/null || true
        fi
    done
    
    # Handle thrillshare files
    find . -type f \( -name "*thrillshare*" -o -name "*Thrillshare*" -o -name "*ThrillShare*" \) | while read -r file; do
        newfile=$(echo "$file" | sed -e 's/thrillshare/mobileApp2/g' -e 's/Thrillshare/MobileApp2/g' -e 's/ThrillShare/MobileApp2/g')
        if [ "$file" != "$newfile" ] && [ -f "$file" ]; then
            echo "  Renaming: $file -> $newfile"
            mv "$file" "$newfile" 2>/dev/null || true
        fi
    done
    
    # Handle bare mobileApp files
    find . -type f -name "*mobileApp.*" -not -name "*mobileApp1.*" -not -name "*mobileApp2.*" | while read -r file; do
        newfile=$(echo "$file" | sed 's/mobileApp\./mobileApp1./')
        if [ "$file" != "$newfile" ] && [ -f "$file" ]; then
            echo "  Renaming: $file -> $newfile"
            mv "$file" "$newfile" 2>/dev/null || true
        fi
    done
}

# Function to fix import paths
fix_import_paths() {
    echo "Fixing import paths and require statements..."
    
    find . -type f \( -name "*.js" -o -name "*.ts" -o -name "*.json" \) \
        -not -path "./node_modules/*" \
        -not -path "./.git/*" \
        -exec sed -i \
            -e 's|schoolApp/|mobileApp1/|g' \
            -e 's|thrillshare/|mobileApp2/|g' \
            -e 's|/schoolApp-|/mobileApp1-|g' \
            -e 's|/thrillshare-|/mobileApp2-|g' \
            -e 's|schoolApp-page|mobileApp1-page|g' \
            -e 's|thrillshare-page|mobileApp2-page|g' \
            -e 's|SchoolApp-page|MobileApp1-page|g' \
            -e 's|Thrillshare-page|MobileApp2-page|g' \
            -e 's|require.*schoolApp|require('"'"'./mobileApp1|g' \
            -e 's|require.*thrillshare|require('"'"'./mobileApp2|g' \
            {} \; 2>/dev/null || true
}

# Function to update cucumber.js specific logic
update_cucumber_logic() {
    echo "Updating cucumber.js app detection logic..."
    
    find . -name "cucumber.js" -o -name "runner.js" | while read -r file; do
        if [ -f "$file" ]; then
            sed -i \
                -e 's/appName\.includes.*schoolApp.*/appName.includes('"'"'mobileApp1'"'"')/' \
                -e 's/appName\.includes.*thrillshare.*/appName.includes('"'"'mobileApp2'"'"')/' \
                -e 's/program\.steps.*schoolApp/program.steps = ".\/src\/ios\/mobileApp1\/step-definitions"/' \
                -e 's/program\.steps.*thrillshare/program.steps = ".\/src\/ios\/mobileApp2\/step-definitions"/' \
                -e 's/program\.steps.*android.*schoolApp/program.steps = ".\/src\/android\/mobileApp1\/step-definitions"/' \
                -e 's/program\.steps.*android.*thrillshare/program.steps = ".\/src\/android\/mobileApp2\/step-definitions"/' \
                "$file" 2>/dev/null || true
        fi
    done
}

# Execute all transformations
echo "Starting comprehensive cleanup..."
replace_all_patterns
rename_all_directories
rename_all_files
fix_import_paths
update_cucumber_logic

echo ""
echo "========================================="
echo "Comprehensive Cleanup Complete!"
echo "========================================="
echo ""
echo "Summary of standardized naming:"
echo ""
echo "PLATFORM TAGS:"
echo "  iosSchoolApp -> iosMobileApp1"
echo "  androidSchoolApp -> androidMobileApp1"
echo "  iosThrillshare -> iosMobileApp2"  
echo "  androidThrillshare -> androidMobileApp2"
echo ""
echo "APP NAMES:"
echo "  schoolApp -> mobileApp1"
echo "  thrillshare -> mobileApp2"
echo "  mobileApp -> mobileApp1 (standardized)"
echo ""
echo "DIRECTORIES RENAMED:"
echo "  src/*/schoolApp -> src/*/mobileApp1"
echo "  src/*/thrillshare -> src/*/mobileApp2"
echo "  utils/*SchoolApp* -> utils/*MobileApp1*"
echo "  utils/*Thrillshare* -> utils/*MobileApp2*"
echo ""
echo "IMPORTS & PATHS UPDATED:"
echo "  All require() statements updated"
echo "  All import paths updated"
echo "  All page object references updated"
echo ""
echo "Your framework now has consistent, standardized naming!"
echo "All iosSchoolApp/androidSchoolApp -> iosMobileApp1/androidMobileApp1"
echo "All iosThrillshare/androidThrillshare -> iosMobileApp2/androidMobileApp2"
echo ""
echo "Please run your tests to verify everything works correctly."