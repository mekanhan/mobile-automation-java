#!/bin/bash

# Mobile Automation Test Runner with Auto Report Generation
# Usage: ./run-tests-with-report.sh @navigation1

TAG=${1:-@smoke}  # Default to @smoke if no tag provided

echo "=========================================="
echo "Running tests with tag: $TAG"
echo "=========================================="

# Run tests
mvn clean test -Dcucumber.filter.tags="$TAG"

# Check if tests passed
if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "Tests PASSED! Generating Allure report..."
    echo "=========================================="

    # Generate and serve Allure report
    mvn allure:serve
else
    echo ""
    echo "=========================================="
    echo "Tests FAILED! Generating reports anyway..."
    echo "=========================================="

    # Still generate report to see failures
    mvn allure:serve
fi
