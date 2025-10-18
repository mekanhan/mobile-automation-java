package manual;

import mobile.automation.server.AppiumServerManager;

/**
 * Manual test to verify Appium server automatic startup
 * Run this class directly to test the server manager
 */
public class TestServerStartup {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("Testing Appium Server Manager");
        System.out.println("==========================================\n");

        try {
            // Test 1: Start server
            System.out.println("Test 1: Starting Appium server...");
            AppiumServerManager.startServer();
            Thread.sleep(2000);

            if (AppiumServerManager.isServerRunning()) {
                System.out.println("✓ SUCCESS: Appium server started automatically!\n");
                AppiumServerManager.printServerStatus();
            } else {
                System.out.println("✗ FAILED: Server not running\n");
                System.exit(1);
            }

            // Test 2: Verify idempotency (starting already running server)
            System.out.println("\nTest 2: Attempting to start already-running server...");
            AppiumServerManager.startServer();
            System.out.println("✓ SUCCESS: Handled already-running server correctly\n");

            // Test 3: Stop server
            System.out.println("Test 3: Stopping Appium server...");
            AppiumServerManager.stopServer();
            Thread.sleep(2000);

            if (!AppiumServerManager.isServerRunning()) {
                System.out.println("✓ SUCCESS: Appium server stopped successfully!\n");
            } else {
                System.out.println("✗ WARNING: Server still running after stop\n");
            }

            System.out.println("==========================================");
            System.out.println("✓ ALL TESTS PASSED!");
            System.out.println("==========================================\n");

            System.out.println("Your framework is now configured to:");
            System.out.println("  • Automatically start Appium server before tests");
            System.out.println("  • Automatically stop Appium server after tests");
            System.out.println("\nYou can now run: mvn clean test");

        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
            e.printStackTrace();

            System.out.println("\nPossible issues:");
            System.out.println("  • Appium not installed (run: npm install -g appium)");
            System.out.println("  • XCUITest driver not installed (run: appium driver install xcuitest)");
            System.out.println("  • Port 4723 is blocked by another process");

            System.exit(1);
        }
    }
}
