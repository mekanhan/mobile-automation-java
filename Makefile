# Makefile for Mobile Automation Tests
# Similar to package.json scripts but more powerful
# Usage: make <target>

.PHONY: help test-smoke test-navigation test-regression test-ios test-android test-all report-open report-generate report-serve report-clean clean compile

# Default target
.DEFAULT_GOAL := help

# Colors
GREEN  := \033[0;32m
BLUE   := \033[0;34m
YELLOW := \033[0;33m
NC     := \033[0m # No Color

# Note: Allure results are automatically cleaned by hooks
# To disable auto-clean, use: make test-smoke ARGS="-Dallure.clean=false"

# Test commands (ARGS can pass additional Maven options)
test-smoke: ## Run smoke tests (@smoke). Use: make test-smoke ARGS="-Dallure.open=true"
	@echo "$(BLUE)Running smoke tests...$(NC)"
	mvn clean test -Psmoke $(ARGS)

test-navigation: ## Run navigation tests (@navigation)
	@echo "$(BLUE)Running navigation tests...$(NC)"
	mvn clean test -Pnavigation $(ARGS)

test-regression: ## Run regression tests (@regression)
	@echo "$(BLUE)Running regression tests...$(NC)"
	mvn clean test -Pregression $(ARGS)

test-ios: ## Run iOS tests (@ios)
	@echo "$(BLUE)Running iOS tests...$(NC)"
	mvn clean test -Pios $(ARGS)

test-android: ## Run Android tests (@android)
	@echo "$(BLUE)Running Android tests...$(NC)"
	mvn clean test -Pandroid $(ARGS)

test-all: ## Run all tests
	@echo "$(BLUE)Running all tests...$(NC)"
	mvn clean test $(ARGS)

# Custom tag (usage: make test-tag TAG=@your-tag ARGS="-Dallure.open=true")
test-tag: ## Run tests with custom tag (usage: make test-tag TAG=@navigation1)
	@if [ -z "$(TAG)" ]; then \
		echo "$(YELLOW)Error: Please provide TAG variable$(NC)"; \
		echo "Usage: make test-tag TAG=@your-tag"; \
		exit 1; \
	fi
	@echo "$(BLUE)Running tests with tag: $(TAG)$(NC)"
	mvn clean test -Dcucumber.filter.tags="$(TAG)" $(ARGS)

# Report commands
report-generate:  ## Generate Allure report
	@echo "$(BLUE)Generating Allure report...$(NC)"
	mvn allure:report
	@echo "$(GREEN)Report generated at: target/allure-report/$(NC)"

report-open:  ## Open existing Allure report in browser
	@./scripts/open-report.sh

report-serve:  ## Generate and serve Allure report with built-in server
	mvn allure:serve

report-clean:  ## Clean all Allure results and reports
	@echo "$(BLUE)Cleaning Allure results...$(NC)"
	@rm -rf allure-results/* target/allure-report
	@echo "$(GREEN)Results cleaned!$(NC)"

# Utility commands
clean:  ## Clean project and all results
	@echo "$(BLUE)Cleaning project...$(NC)"
	mvn clean
	@rm -rf allure-results/*
	@echo "$(GREEN)Project cleaned!$(NC)"

compile:  ## Compile project
	@echo "$(BLUE)Compiling project...$(NC)"
	mvn clean compile

# Help command - auto-generated from comments
help:  ## Show this help message
	@echo "$(GREEN)Available targets:$(NC)"
	@echo ""
	@echo "$(BLUE)Test Commands:$(NC)"
	@grep -E '^test-[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(YELLOW)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(BLUE)Report Commands:$(NC)"
	@grep -E '^report-[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(YELLOW)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(BLUE)Utility Commands:$(NC)"
	@grep -E '^(clean|compile):.*?## .*$$' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## "}; {printf "  $(YELLOW)%-20s$(NC) %s\n", $$1, $$2}'
	@echo ""
	@echo "$(BLUE)Examples:$(NC)"
	@echo "  make test-smoke"
	@echo "  make test-tag TAG=@navigation1"
	@echo "  make test-smoke ARGS=\"-Dallure.open=true\""
	@echo "  make test-tag TAG=@smoke ARGS=\"-Dallure.report=true\""
	@echo "  make report-open"
	@echo ""
	@echo "$(BLUE)Hook Options (via ARGS):$(NC)"
	@echo "  -Dallure.clean=false     Keep old results (accumulate history)"
	@echo "  -Dallure.report=true     Generate report after tests"
	@echo "  -Dallure.open=true       Generate and open report in browser"
