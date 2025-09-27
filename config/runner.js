#!/usr/bin/env node

'use strict';

const fs = require('fs-plus');
const path = require('path');
const program = require('commander');
const pjson = require('../package.json');
const cucumber = require('@cucumber/cucumber');
const dotenv = require('dotenv');

// Load environment variables
dotenv.config();

// Parse command line arguments
function collectPaths(value, paths) {
  paths.push(value);
  return paths;
}

function coerceInt(value, defaultValue) {
  const int = parseInt(value);
  return typeof int === 'number' ? int : defaultValue;
}

// Default configuration with environment variable support
const config = {
  steps: './src/step-definitions',
  pageObjects: './src/page-objects',
  sharedObjects: './src/support',
  featureFiles: './src/features',
  reports: './reports',
  browser: process.env.DEFAULT_PLATFORM || 'ios',
  browserTeardownStrategy: 'always',
  timeout: parseInt(process.env.DEFAULT_TIMEOUT) || 30000,
};

// Load config file if exists
const configFileName = path.resolve(process.cwd(), 'config.json');
if (fs.isFileSync(configFileName)) {
  Object.assign(config, require(configFileName));
}

program
  .version(pjson.version)
  .description(pjson.description)
  .option('-s, --steps <path>', 'path to step definitions', config.steps)
  .option('-p, --pageObjects <path>', 'path to page objects', config.pageObjects)
  .option('-o, --sharedObjects [paths]', 'path to shared objects (repeatable)', collectPaths, [config.sharedObjects])
  .option('-b, --browser <path>', 'platform to test (ios/android)', config.browser)
  .option('-k, --browser-teardown <optional>', 'browser teardown strategy (always, clear, none)', config.browserTeardownStrategy)
  .option('-r, --reports <path>', 'output path for reports', config.reports)
  .option('-d, --disableLaunchReport [optional]', 'disable auto-opening browser with test report')
  .option('-t, --tags <tagName>', 'cucumber tags to run', collectPaths, [])
  .option('-f, --featureFiles <paths>', 'comma-separated feature files or directory path', config.featureFiles)
  .option('-x, --timeOut <n>', 'step timeout in milliseconds', coerceInt, config.timeout)
  .option('-n, --noScreenshot [optional]', 'disable screenshot capture on error')
  .option('-w, --worldParameters <JSON>', 'JSON parameters for cucumber world constructor')
  .option('-e, --env <environment>', 'test environment (dev, staging, prod)', process.env.TEST_ENV || 'staging')
  .option('--parallel <n>', 'number of parallel instances', coerceInt, parseInt(process.env.PARALLEL_INSTANCES) || 1)
  .parse(process.argv);

const options = program.opts();

// Configure globals based on platform
global.appName = options.browser;
global.browserTeardownStrategy = options.browserTeardown;
global.testEnvironment = options.env;

// Set platform-specific paths
const platformPath = appName.includes('ios') ? 'ios' : 'android';
const stepsPath = path.join('./src', platformPath, 'step-definitions');
const pageObjectsPath = path.join('./src', platformPath, 'page-objects');
const featuresPath = path.join('./src', platformPath, 'features');

// Check if platform-specific directories exist, otherwise use common
if (fs.existsSync(stepsPath)) {
  program.steps = stepsPath;
}
if (fs.existsSync(pageObjectsPath)) {
  global.pageObjectPath = path.resolve(pageObjectsPath);
}
if (fs.existsSync(featuresPath) && !options.featureFiles) {
  program.featureFiles = featuresPath;
}

// Configure reporting
global.reportPath = path.resolve(options.reports);
global.reportsPath = reportPath;

if (!fs.existsSync(options.reports)) {
  fs.makeTreeSync(options.reports);
}

// Set global configuration
global.disableLaunchReport = options.disableLaunchReport;
global.noScreenshot = options.noScreenshot;
global.DEFAULT_TIMEOUT = options.timeOut;

// Shared object paths for page object imports
global.sharedObjectPaths = options.sharedObjects.map(item => path.resolve(item));

// Prepare cucumber arguments
const cucumberArgs = [];

// Add feature files
if (options.featureFiles) {
  const features = options.featureFiles.split(',');
  features.forEach(feature => cucumberArgs.push(feature.trim()));
}

// Configure formatters
cucumberArgs.push('--format', 'pretty');
cucumberArgs.push('--format', `json:${path.join(reportPath, 'cucumber-report.json')}`);

// Add world configuration
cucumberArgs.push('--require', path.resolve(__dirname, '../utils/world.js'));

// Add step definitions
cucumberArgs.push('--require', path.resolve(program.steps));

// Add tags if specified
if (options.tags && options.tags.length > 0) {
  options.tags.forEach(tag => {
    cucumberArgs.push('--tags', tag);
  });
}

// Add world parameters
if (options.worldParameters) {
  cucumberArgs.push('--world-parameters', options.worldParameters);
}

// Add strict mode
cucumberArgs.push('--strict');

// Configure parallel execution
if (options.parallel > 1) {
  cucumberArgs.push('--parallel', options.parallel.toString());
}

// Initialize and run cucumber
async function runTests() {
  console.log(`
╔════════════════════════════════════════╗
║   Mobile Automation Framework v${pjson.version}   ║
╠════════════════════════════════════════╣
║ Platform:     ${appName.padEnd(25)} ║
║ Environment:  ${testEnvironment.padEnd(25)} ║
║ Timeout:      ${(options.timeOut + 'ms').padEnd(25)} ║
║ Parallel:     ${options.parallel.toString().padEnd(25)} ║
╚════════════════════════════════════════╝
`);

  console.log('Starting test execution...\n');

  try {
    // Import and run cucumber with async/await
    const { runCucumber } = cucumber;
    const { success } = await runCucumber({
      argv: cucumberArgs,
      cwd: process.cwd(),
      stdout: process.stdout,
      stderr: process.stderr,
      env: process.env
    });

    // Generate reports
    if (!options.disableLaunchReport) {
      console.log('\nGenerating test reports...');
      require('../utils/reportGenerator').generate(reportPath);
    }

    // Exit with appropriate code
    process.exit(success ? 0 : 1);
  } catch (error) {
    console.error('Error running tests:', error);
    process.exit(1);
  }
}

// Handle async execution
if (require.main === module) {
  runTests().catch(error => {
    console.error('Fatal error:', error);
    process.exit(1);
  });
}

module.exports = { runTests, config };