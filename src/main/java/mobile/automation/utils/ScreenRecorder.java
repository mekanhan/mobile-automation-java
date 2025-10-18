package mobile.automation.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Screen Recording Utility using ffmpeg
 * Records iOS Simulator and Android Emulator screens during test execution
 */
public class ScreenRecorder {

    private Process recordingProcess;
    private String videoFilePath;
    private static final String RECORDING_DIR = "target/recordings/";
    private boolean isRecording = false;

    /**
     * Start screen recording for iOS Simulator
     * @param deviceUDID The simulator UDID
     * @param fileName The base name for the video file (e.g., "wikipedia_iOS_explorer_1")
     */
    public void startIOSRecording(String deviceUDID, String fileName) {
        try {
            // Create recordings directory if it doesn't exist
            File recordingDir = new File(RECORDING_DIR);
            if (!recordingDir.exists()) {
                recordingDir.mkdirs();
            }

            // Generate video file path with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            videoFilePath = RECORDING_DIR + fileName + "_" + timestamp + ".mp4";

            // Start recording using xcrun simctl (built-in iOS Simulator recording)
            String[] command = {
                "xcrun", "simctl", "io", deviceUDID, "recordVideo",
                "--codec=h264", "--force", videoFilePath
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            recordingProcess = processBuilder.start();

            isRecording = true;
            System.out.println("📹 Screen recording started: " + videoFilePath);

        } catch (Exception e) {
            System.err.println("Failed to start iOS screen recording: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Start screen recording for Android Emulator/Device
     * @param deviceSerial The device serial number (from adb devices)
     * @param fileName The base name for the video file (e.g., "app_Android_feature_1")
     */
    public void startAndroidRecording(String deviceSerial, String fileName) {
        try {
            // Create recordings directory if it doesn't exist
            File recordingDir = new File(RECORDING_DIR);
            if (!recordingDir.exists()) {
                recordingDir.mkdirs();
            }

            // Generate video file path with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String deviceVideoPath = "/sdcard/" + fileName + ".mp4";
            videoFilePath = RECORDING_DIR + fileName + "_" + timestamp + ".mp4";

            // Start recording using adb screenrecord
            String[] command = {
                "adb", "-s", deviceSerial, "shell", "screenrecord",
                "--bit-rate", "4000000", deviceVideoPath
            };

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            recordingProcess = processBuilder.start();

            isRecording = true;
            System.out.println("📹 Screen recording started: " + videoFilePath);

            // Store the device path for later retrieval
            System.setProperty("android.recording.path", deviceVideoPath);

        } catch (Exception e) {
            System.err.println("Failed to start Android screen recording: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Stop screen recording and save the video
     * @return The path to the saved video file
     */
    public String stopRecording() {
        if (!isRecording || recordingProcess == null) {
            System.out.println("No active recording to stop");
            return null;
        }

        try {
            // Stop the recording process
            recordingProcess.destroy();

            // Wait for process to terminate
            recordingProcess.waitFor();

            // For Android, pull the video from device
            if (System.getProperty("android.recording.path") != null) {
                pullAndroidVideo();
            }

            isRecording = false;
            System.out.println("⏹️  Screen recording stopped: " + videoFilePath);

            // Wait a bit for file to be fully written
            Thread.sleep(1000);

            return videoFilePath;

        } catch (Exception e) {
            System.err.println("Failed to stop screen recording: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Pull video file from Android device to local machine
     */
    private void pullAndroidVideo() {
        try {
            String devicePath = System.getProperty("android.recording.path");

            // Pull video from device
            String[] pullCommand = {"adb", "pull", devicePath, videoFilePath};
            Process pullProcess = Runtime.getRuntime().exec(pullCommand);
            pullProcess.waitFor();

            // Delete video from device
            String[] deleteCommand = {"adb", "shell", "rm", devicePath};
            Process deleteProcess = Runtime.getRuntime().exec(deleteCommand);
            deleteProcess.waitFor();

            System.clearProperty("android.recording.path");

        } catch (Exception e) {
            System.err.println("Failed to pull Android video: " + e.getMessage());
        }
    }

    /**
     * Generate file name based on test context
     * Format: {appName}_{platform}_{feature}_{scenarioNumber}
     * Example: wikipedia_iOS_explorer_1
     *
     * @param appName Application name (e.g., "wikipedia")
     * @param platform Platform name (e.g., "iOS", "Android")
     * @param featureName Feature name (e.g., "explorer", "login")
     * @param scenarioNumber Scenario number in feature file
     * @return Formatted file name
     */
    public static String generateFileName(String appName, String platform, String featureName, int scenarioNumber) {
        return String.format("%s_%s_%s_%d",
            appName.toLowerCase().replaceAll("[^a-z0-9]", ""),
            platform,
            featureName.toLowerCase().replaceAll("[^a-z0-9]", ""),
            scenarioNumber
        );
    }

    /**
     * Generate file name from scenario name
     * Extracts info from scenario and generates appropriate file name
     *
     * @param scenarioName Full scenario name from Cucumber
     * @param platform Platform (iOS/Android)
     * @return Formatted file name
     */
    public static String generateFileNameFromScenario(String scenarioName, String platform) {
        // Clean scenario name - remove special characters
        String cleanName = scenarioName.toLowerCase()
            .replaceAll("[^a-z0-9\\s]", "")
            .replaceAll("\\s+", "_");

        // Limit length to avoid too long filenames
        if (cleanName.length() > 50) {
            cleanName = cleanName.substring(0, 50);
        }

        return String.format("%s_%s", platform, cleanName);
    }

    /**
     * Check if ffmpeg is installed (for alternative recording method)
     */
    public static boolean isFFmpegInstalled() {
        try {
            Process process = Runtime.getRuntime().exec("ffmpeg -version");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            return line != null && line.contains("ffmpeg");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if recording is currently active
     */
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * Get the current video file path
     */
    public String getVideoFilePath() {
        return videoFilePath;
    }

    /**
     * Delete old recordings (keep last N days)
     * @param daysToKeep Number of days to keep recordings
     */
    public static void cleanOldRecordings(int daysToKeep) {
        try {
            File recordingDir = new File(RECORDING_DIR);
            if (!recordingDir.exists()) {
                return;
            }

            long cutoffTime = System.currentTimeMillis() - (daysToKeep * 24L * 60 * 60 * 1000);

            File[] files = recordingDir.listFiles();
            if (files != null) {
                int deletedCount = 0;
                for (File file : files) {
                    if (file.isFile() && file.lastModified() < cutoffTime) {
                        if (file.delete()) {
                            deletedCount++;
                        }
                    }
                }
                if (deletedCount > 0) {
                    System.out.println("🗑️  Cleaned up " + deletedCount + " old recording(s)");
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to clean old recordings: " + e.getMessage());
        }
    }
}
