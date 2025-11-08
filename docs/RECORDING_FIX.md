# Screen Recording Fix - Corrupted Video Files

## Problem

Screen recordings were being created but the video files were corrupted:
- File size: 18 MB (indicating data was written)
- Duration: 0 seconds
- Cannot be played in QuickTime Player or VLC
- File exists but video container not properly closed

## Root Cause

The issue was in `ScreenRecorder.java` in the `stopRecording()` method:

```java
// OLD CODE - WRONG!
recordingProcess.destroy();  // ❌ Forcefully kills process
recordingProcess.waitFor();
```

**Problem:** Using `Process.destroy()` immediately terminates the `xcrun simctl recordVideo` process without giving it time to:
1. Finalize the video container (MP4)
2. Write the video header/metadata
3. Close file handles properly
4. Flush buffered data

This results in a video file with raw data but no proper MP4 structure, making it unplayable.

## Solution

Send **SIGINT** (interrupt signal) instead of forcefully destroying the process:

```java
// NEW CODE - CORRECT!
// Get process ID
long pid = recordingProcess.pid();

// Send SIGINT (equivalent to Ctrl+C)
String[] command = {"kill", "-2", String.valueOf(pid)};
Process killProcess = Runtime.getRuntime().exec(command);
killProcess.waitFor();

// Wait for process to terminate gracefully
boolean terminated = recordingProcess.waitFor(5, TimeUnit.SECONDS);
```

**Why this works:**
- SIGINT (signal 2) is a graceful shutdown signal
- `xcrun simctl recordVideo` handles SIGINT properly
- Gives the process time to finalize the video file
- Writes proper MP4 container headers
- Results in playable video files

## Changes Made

### 1. Added `stopIOSRecordingGracefully()` method

```java
private void stopIOSRecordingGracefully() {
    long pid = recordingProcess.pid();
    System.out.println("📹 Sending interrupt signal to PID: " + pid);
    
    // Send SIGINT (kill -2)
    String[] command = {"kill", "-2", String.valueOf(pid)};
    Process killProcess = Runtime.getRuntime().exec(command);
    killProcess.waitFor();
}
```

### 2. Updated `stopRecording()` method

```java
public String stopRecording() {
    // Detect platform
    if (System.getProperty("android.recording.path") == null) {
        // iOS - use graceful stop
        stopIOSRecordingGracefully();
    } else {
        // Android - destroy() is fine
        recordingProcess.destroy();
    }
    
    // Wait for termination (max 5 seconds)
    boolean terminated = recordingProcess.waitFor(5, TimeUnit.SECONDS);
    
    if (!terminated) {
        recordingProcess.destroyForcibly();  // Last resort
    }
    
    // Increased wait time for finalization
    Thread.sleep(2000);  // Was 1000ms, now 2000ms
    
    // Verify file size
    File videoFile = new File(videoFilePath);
    System.out.println("📊 Video file size: " + formatFileSize(fileSize));
}
```

### 3. Added file verification

```java
// Verify video file exists and has content
File videoFile = new File(videoFilePath);
if (videoFile.exists()) {
    long fileSize = videoFile.length();
    System.out.println("📊 Video file size: " + formatFileSize(fileSize));
    
    if (fileSize < 1000) {
        System.err.println("⚠️  Warning: Video file very small - may be corrupted");
    }
}
```

### 4. Added helper method

```java
private String formatFileSize(long bytes) {
    if (bytes < 1024) return bytes + " B";
    int exp = (int) (Math.log(bytes) / Math.log(1024));
    char pre = "KMGTPE".charAt(exp - 1);
    return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
}
```

### 5. Fixed deprecated API warning

Changed from:
```java
Runtime.getRuntime().exec("ffmpeg -version");  // ❌ Deprecated
```

To:
```java
String[] command = {"ffmpeg", "-version"};
Runtime.getRuntime().exec(command);  // ✅ Correct
```

## Testing

### Before Fix
```bash
mvn test -Dscreen.recording=true

# Result:
# - File created: iOS_test_20241029.mp4 (18 MB)
# - Duration: 0:00
# - Cannot play in QuickTime/VLC
# - ffprobe shows: "moov atom not found"
```

### After Fix
```bash
mvn test -Dscreen.recording=true

# Expected output:
📹 Sending interrupt signal to recording process (PID: 12345)
✅ Interrupt signal sent, waiting for process to finalize video...
✅ Screen recording stopped: target/recordings/iOS_test_20241029.mp4
📊 Video file size: 5.23 MB
```

### Verify Video is Playable

```bash
# Check video metadata
ffprobe target/recordings/iOS_test_*.mp4

# Should show:
# Duration: 00:00:15.42
# Video: h264 (Main) ... 1920x1080
# Audio: none

# Play video
open target/recordings/iOS_test_*.mp4
# Should open in QuickTime and play correctly
```

## Why It Was Failing

**xcrun simctl recordVideo process:**
1. Opens output file for writing
2. Continuously writes video frames
3. When interrupted (SIGINT):
   - Stops capturing new frames
   - Writes MP4 container metadata (moov atom)
   - Finalizes file structure
   - Closes file handle
   - Exits cleanly

**When using destroy():**
1. Process killed immediately (SIGKILL)
2. No time to write metadata
3. File left with raw frame data only
4. No MP4 container structure
5. Video players can't parse the file

## Platform Differences

### iOS (xcrun simctl)
- **Requires:** SIGINT for graceful shutdown
- **Signal:** `kill -2` (SIGINT)
- **Finalization:** ~1-2 seconds
- **File format:** MP4 with H.264 codec

### Android (adb screenrecord)
- **Works with:** `destroy()` is acceptable
- **Reason:** adb handles the recording, not the shell process
- **Finalization:** Happens on device side
- **File format:** MP4 with device codec

## Best Practices

1. **Always use SIGINT for iOS recordings**
   ```java
   kill -2 <PID>  // Not kill -9
   ```

2. **Wait for graceful termination**
   ```java
   recordingProcess.waitFor(5, TimeUnit.SECONDS);
   ```

3. **Verify file after recording**
   ```bash
   ffprobe -v error target/recordings/*.mp4
   ```

4. **Check file size is reasonable**
   - Too small (<1 MB for 10+ sec test): Likely corrupted
   - Expected: ~500 KB - 2 MB per minute for 1080p

## Troubleshooting

### Video still corrupted after fix?

**Check console output:**
```
⚠️ Failed to send interrupt signal, using destroy()
```
If you see this, the graceful stop failed. Possible causes:
- Process already terminated
- Insufficient permissions
- System doesn't support kill command

**Manual test:**
```bash
# Start recording manually
xcrun simctl io booted recordVideo --codec=h264 test.mp4

# In another terminal, after a few seconds:
ps aux | grep recordVideo  # Get PID
kill -2 <PID>              # Send SIGINT

# Check video
open test.mp4  # Should play correctly
```

### Video still 0 seconds?

**Possible causes:**
1. Recording was too short (< 1 second)
2. Simulator not running or not visible
3. No screen changes during recording
4. Disk space full

**Debug:**
```bash
# Check simulator status
xcrun simctl list | grep Booted

# Check disk space
df -h

# Try manual recording
xcrun simctl io booted recordVideo test.mp4
# Press Ctrl+C after a few seconds
# Verify: open test.mp4
```

## References

- [xcrun simctl documentation](https://developer.apple.com/documentation/xcode)
- [Unix signals reference](https://man7.org/linux/man-pages/man7/signal.7.html)
- SIGINT = Signal 2 (Interrupt from keyboard)
- SIGTERM = Signal 15 (Termination signal)
- SIGKILL = Signal 9 (Kill signal - cannot be caught)

## Summary

✅ **Fixed:** Changed from `destroy()` to SIGINT signal  
✅ **Result:** Videos are now properly finalized and playable  
✅ **Bonus:** Added file size verification and better error messages  
✅ **Compatibility:** Still works for Android (unchanged)  

Videos should now be playable in QuickTime, VLC, and embedded in test reports! 🎬
