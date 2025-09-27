#!/bin/bash

# Quick Calculator Demo - Minimal setup
echo "🧮 Quick Calculator Demo"
echo "========================"

# Run basic smoke test
mvn test -Dtest=CalculatorTestRunner -Dcucumber.filter.tags="@calculator and @basic" -q

echo ""
echo "✅ Quick demo completed!"
echo "📊 Check target/cucumber-reports/calculator/ for detailed reports"