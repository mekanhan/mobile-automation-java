package mobile.automation.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Manages Appium Server lifecycle
 * Automatically starts and stops the Appium server for tests
 */
public class AppiumServerManager {

    private static Process appiumProcess;
    private static final String APPIUM_HOST = "127.0.0.1";
    private static final int APPIUM_PORT = 4723;
    private static final String APPIUM_URL = "http://" + APPIUM_HOST + ":" + APPIUM_PORT;
    private static final int MAX_STARTUP_WAIT_MS = 30000; // 30 seconds
    private static final int CHECK_INTERVAL_MS = 500; // 0.5 seconds

    /**
     * Start Appium server if not already running
     */
    public static void startServer() {
        if (isServerRunning()) {
            System.out.println("Appium server is already running at " + APPIUM_URL);
            return;
        }

        System.out.println("Starting Appium server...");

        try {
            // Find appium executable
            String appiumCommand = findAppiumCommand();

            if (appiumCommand == null) {
                throw new RuntimeException("Appium not found. Please install Appium: npm install -g appium");
            }

            System.out.println("Using Appium command: " + appiumCommand);

            // Build process
            ProcessBuilder processBuilder = new ProcessBuilder(
                appiumCommand,
                "--address", APPIUM_HOST,
                "--port", String.valueOf(APPIUM_PORT),
                "--relaxed-security"
            );

            // Redirect error stream to output for debugging
            processBuilder.redirectErrorStream(true);

            // Start the process
            appiumProcess = processBuilder.start();

            // Start a thread to consume output (prevents process from hanging)
            startOutputReader();

            // Wait for server to be ready
            if (!waitForServerStartup()) {
                stopServer();
                throw new RuntimeException("Appium server failed to start within " + MAX_STARTUP_WAIT_MS + "ms");
            }

            System.out.println("Appium server started successfully at " + APPIUM_URL);

            // Add shutdown hook to stop server when JVM exits
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (appiumProcess != null && appiumProcess.isAlive()) {
                    System.out.println("Shutdown hook: Stopping Appium server...");
                    stopServer();
                }
            }));

        } catch (IOException e) {
            throw new RuntimeException("Failed to start Appium server", e);
        }
    }

    /**
     * Stop Appium server
     */
    public static void stopServer() {
        if (appiumProcess != null && appiumProcess.isAlive()) {
            System.out.println("Stopping Appium server...");
            appiumProcess.destroy();

            try {
                // Wait for process to terminate
                if (!appiumProcess.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    System.out.println("Force killing Appium server...");
                    appiumProcess.destroyForcibly();
                }
                System.out.println("Appium server stopped successfully");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while stopping Appium server");
            }

            appiumProcess = null;
        }
    }

    /**
     * Check if Appium server is running
     */
    public static boolean isServerRunning() {
        try {
            URL url = new URL(APPIUM_URL + "/status");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);

            int responseCode = connection.getResponseCode();
            connection.disconnect();

            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for Appium server to start up
     */
    private static boolean waitForServerStartup() {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < MAX_STARTUP_WAIT_MS) {
            if (isServerRunning()) {
                return true;
            }

            try {
                Thread.sleep(CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        return false;
    }

    /**
     * Find appium command on the system
     */
    private static String findAppiumCommand() {
        String[] possibleCommands = {
            "appium",
            "/usr/local/bin/appium",
            "/opt/homebrew/bin/appium",
            System.getProperty("user.home") + "/.nvm/versions/node/v22.20.0/bin/appium",
            System.getProperty("user.home") + "/node_modules/.bin/appium"
        };

        for (String command : possibleCommands) {
            if (commandExists(command)) {
                return command;
            }
        }

        // Try to find using 'which' command
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"which", "appium"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String path = reader.readLine();
            reader.close();
            process.waitFor();

            if (path != null && !path.isEmpty()) {
                return path;
            }
        } catch (Exception e) {
            // Ignore
        }

        return null;
    }

    /**
     * Check if a command exists
     */
    private static boolean commandExists(String command) {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{command, "--version"});
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Start thread to read Appium server output
     */
    private static void startOutputReader() {
        Thread outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(appiumProcess.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    // Print Appium server logs with prefix
                    if (line.contains("error") || line.contains("Error") || line.contains("ERROR")) {
                        System.err.println("[Appium Server] " + line);
                    } else if (line.contains("Appium REST http interface listener started")) {
                        System.out.println("[Appium Server] " + line);
                    }
                    // Suppress other verbose logs to keep console clean
                }
            } catch (IOException e) {
                // Process terminated, this is expected
            }
        });

        outputThread.setDaemon(true);
        outputThread.start();
    }

    /**
     * Get Appium server URL
     */
    public static String getServerUrl() {
        return APPIUM_URL;
    }

    /**
     * Get Appium server status information
     */
    public static void printServerStatus() {
        System.out.println("========== Appium Server Status ==========");
        System.out.println("URL: " + APPIUM_URL);
        System.out.println("Running: " + isServerRunning());
        System.out.println("Process Alive: " + (appiumProcess != null && appiumProcess.isAlive()));
        System.out.println("==========================================");
    }
}
