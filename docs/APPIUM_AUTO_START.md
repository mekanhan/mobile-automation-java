# Automatic Appium Server Startup

This framework now includes **automatic Appium server management** - you no longer need to manually start the Appium server before running tests!

## How It Works

The framework automatically:
1. **Starts** the Appium server before running any tests
2. **Verifies** the server is running and accessible
3. **Stops** the server after all tests complete

This is handled by the `AppiumServerManager` class integrated into the test lifecycle via Cucumber hooks.

## Architecture

```
┌─────────────────────────────────────────┐
│         mvn clean test                  │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│    @BeforeAll Hook (Hooks.java)         │
│  → AppiumServerManager.startServer()    │
│  → Detects if server already running    │
│  → Starts server on port 4723           │
│  → Waits for server to be ready         │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│         Run All Test Scenarios          │
│    (Server stays running throughout)    │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│     @AfterAll Hook (Hooks.java)         │
│  → AppiumServerManager.stopServer()     │
│  → Gracefully shuts down server         │
└─────────────────────────────────────────┘
```

## Key Components

### 1. AppiumServerManager
Location: `src/main/java/mobile/automation/server/AppiumServerManager.java`

**Features:**
- Automatically detects Appium installation path
- Starts server on `http://127.0.0.1:4723`
- Verifies server health before proceeding
- Gracefully stops server after tests
- Thread-safe server output handling
- Automatic shutdown hook for unexpected termination

**Key Methods:**
```java
AppiumServerManager.startServer()     // Start Appium server
AppiumServerManager.stopServer()      // Stop Appium server
AppiumServerManager.isServerRunning() // Check if server is running
AppiumServerManager.getServerUrl()    // Get server URL
```

### 2. Test Hooks Integration
Location: `src/test/java/hooks/Hooks.java`

The hooks use Cucumber's `@BeforeAll` and `@AfterAll` annotations to manage server lifecycle:

```java
@BeforeAll
public static void startAppiumServer() {
    AppiumServerManager.startServer();
}

@AfterAll
public static void stopAppiumServer() {
    AppiumServerManager.stopServer();
}
```

## Prerequisites

### Required Software

1. **Node.js and npm** (for Appium)
   ```bash
   node --version  # Should be v14 or higher
   npm --version
   ```

2. **Appium** (globally installed)
   ```bash
   npm install -g appium
   appium --version
   ```

3. **XCUITest Driver** (for iOS testing)
   ```bash
   appium driver install xcuitest
   appium driver list --installed
   ```

4. **Xcode and iOS Simulator** (for iOS testing)
   - Xcode 14+ installed
   - iOS Simulator configured

### Verification

Run the verification test:
```bash
mvn test-compile && java -cp "target/classes:target/test-classes" manual.TestServerStartup
```

Expected output:
```
✓ SUCCESS: Appium server started automatically!
✓ SUCCESS: Handled already-running server correctly
✓ SUCCESS: Appium server stopped successfully!
✓ ALL TESTS PASSED!
```

## Usage

### Running Tests

Simply run:
```bash
mvn clean test
```

The framework will:
1. Compile your code
2. Automatically start Appium server
3. Run all test scenarios
4. Automatically stop Appium server

### Running Specific Tests

Run tests by tags:
```bash
mvn clean test -Dcucumber.options="--tags @smoke"
```

### Configuration

The server configuration is in `AppiumServerManager.java`:
- **Host:** `127.0.0.1` (localhost)
- **Port:** `4723` (default Appium port)
- **Startup Timeout:** 30 seconds
- **Server Flags:** `--relaxed-security` enabled

## Troubleshooting

### Issue: "Appium not found"

**Solution:** Install Appium globally
```bash
npm install -g appium
which appium  # Verify installation
```

### Issue: "XCUITest driver not installed"

**Solution:** Install the driver
```bash
appium driver install xcuitest
appium driver list --installed
```

### Issue: "Port 4723 already in use"

**Solution:** The framework detects running servers. If there's a conflict:
```bash
# Kill any existing Appium processes
lsof -ti:4723 | xargs kill -9

# Or use a different port (requires code modification)
```

### Issue: "Server fails to start within 30 seconds"

**Possible causes:**
- Slow system startup
- Missing dependencies
- Network/firewall issues

**Solution:** Check Appium installation
```bash
appium  # Start manually to see error messages
```

### Issue: Tests run but server logs don't appear

This is **normal behavior**. The framework suppresses verbose Appium logs to keep the console clean. Only important messages (errors, startup confirmation) are shown.

## Advanced Configuration

### Custom Server URL

If you need to use a different server URL, modify `DriverManager.java`:

```java
private static final String APPIUM_SERVER_URL = "http://localhost:4723";
```

### External Appium Server

To use an external Appium server (cloud service, remote machine), you can:

1. **Disable auto-start:** Comment out the `@BeforeAll` hook in `Hooks.java`
2. **Set custom URL:** Update `APPIUM_SERVER_URL` in `DriverManager.java`

### Manual Server Control

You can still manually control the server in your code:

```java
// Start server programmatically
AppiumServerManager.startServer();

// Check status
if (AppiumServerManager.isServerRunning()) {
    System.out.println("Server is ready!");
}

// Stop server programmatically
AppiumServerManager.stopServer();
```

## Benefits

✅ **No manual server management** - Framework handles everything
✅ **Faster development** - Just run `mvn test` and go
✅ **CI/CD ready** - Works in automated pipelines
✅ **Error handling** - Graceful failures with helpful messages
✅ **Clean shutdown** - Server stops properly even on failures
✅ **Idempotent** - Safe to call start multiple times

## Migration Notes

### Before (Manual)
```bash
# Terminal 1
appium

# Terminal 2
mvn clean test
```

### After (Automatic)
```bash
# Just one command!
mvn clean test
```

## CI/CD Integration

This feature makes CI/CD integration seamless:

```yaml
# Example GitHub Actions
- name: Run Mobile Tests
  run: |
    npm install -g appium
    appium driver install xcuitest
    mvn clean test
```

No need for background processes or separate server startup steps!

## Support

If you encounter issues:
1. Check Appium installation: `appium --version`
2. Verify drivers: `appium driver list --installed`
3. Run verification test: `java -cp "target/classes:target/test-classes" manual.TestServerStartup`
4. Check server logs manually: `appium --log-level debug`

---

**Happy Testing!** 🚀
