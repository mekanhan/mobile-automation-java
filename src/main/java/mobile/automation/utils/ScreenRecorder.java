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
            System.out.println("⏹️  Stopping screen recording...");

            // For iOS: Send SIGINT to properly close the video file
            // Using destroy() kills the process too quickly and corrupts the video
            if (System.getProperty("android.recording.path") == null) {
                // This is iOS recording - need to stop gracefully
                stopIOSRecordingGracefully();
            } else {
                // Android recording - destroy is fine
                recordingProcess.destroy();
            }

            // Wait for process to terminate (max 5 seconds)
            boolean terminated = recordingProcess.waitFor(5, java.util.concurrent.TimeUnit.SECONDS);

            if (!terminated) {
                System.err.println("⚠️  Recording process did not terminate, forcing...");
                recordingProcess.destroyForcibly();
                recordingProcess.waitFor();
            }

            // For Android, pull the video from device
            if (System.getProperty("android.recording.path") != null) {
                pullAndroidVideo();
            }

            isRecording = false;
            System.out.println("✅ Screen recording stopped: " + videoFilePath);

            // Wait for file to be fully written and finalized
            Thread.sleep(2000);

            // Verify video file exists and has content
            java.io.File videoFile = new java.io.File(videoFilePath);
            if (videoFile.exists()) {
                long fileSize = videoFile.length();
                System.out.println("📊 Video file size: " + formatFileSize(fileSize));

                if (fileSize < 1000) {
                    System.err.println("⚠️  Warning: Video file is very small (" + fileSize + " bytes) - may be corrupted");
                }
            } else {
                System.err.println("❌ Video file not found: " + videoFilePath);
            }

            return videoFilePath;

        } catch (Exception e) {
            System.err.println("❌ Failed to stop screen recording: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Stop iOS recording gracefully by sending SIGINT
     * This allows xcrun simctl to properly finalize the video file
     */
    private void stopIOSRecordingGracefully() {
        try {
            // Get the process ID
            long pid = recordingProcess.pid();
            System.out.println("📹 Sending interrupt signal to recording process (PID: " + pid + ")");

            // Send SIGINT (Ctrl+C) to allow graceful shutdown
            // This is equivalent to pressing Ctrl+C in terminal
            String[] command = {"kill", "-2", String.valueOf(pid)};
            Process killProcess = Runtime.getRuntime().exec(command);
            killProcess.waitFor();

            System.out.println("✅ Interrupt signal sent, waiting for process to finalize video...");

        } catch (Exception e) {
            System.err.println("⚠️  Failed to send interrupt signal, using destroy(): " + e.getMessage());
            recordingProcess.destroy();
        }
    }

    /**
     * Format file size in human-readable format
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "KMGTPE".charAt(exp - 1);
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
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
            String[] command = {"ffmpeg", "-version"};
            Process process = Runtime.getRuntime().exec(command);
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
