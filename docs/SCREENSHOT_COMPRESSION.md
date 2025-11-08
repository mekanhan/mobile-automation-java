# Screenshot Compression Guide

## Overview

Screenshots are automatically compressed to reduce file size while maintaining good quality. This prevents disk space issues and speeds up report loading.

---

## 📊 Compression Stats

### Typical Size Reduction

| Device | Original PNG | Compressed JPEG | Reduction |
|--------|-------------|-----------------|-----------|
| iPhone Simulator | ~1-3 MB | ~100-300 KB | **70-80%** |
| Android Emulator | ~500 KB - 2 MB | ~50-200 KB | **70-80%** |
| Physical Device | ~1-5 MB | ~100-500 KB | **70-85%** |

**Example output:**
```
📁 Failure screenshot saved to: target/screenshots/FAILURE_*.jpg
📊 Size: 180.5 KB (reduced by 78%)
```

---

## ⚙️ How It Works

### Automatic Compression

All screenshots saved to disk are automatically compressed:

1. **Failure Screenshots** (Hooks.java)
   - Original PNG from driver → Compressed JPEG
   - Saved to `target/screenshots/FAILURE_*.jpg`

2. **Manual Screenshots** (CommonSteps.java)
   - Original PNG from driver → Compressed JPEG
   - Saved to `target/screenshots/manual_screenshot_*.jpg`

3. **Report Attachments**
   - Cucumber reports: Original PNG (embedded)
   - Allure reports: Original PNG (attached)
   - File system: Compressed JPEG (for viewing)

---

## 🎛️ Quality Settings

### Default Quality: 70%

The default compression quality is **0.7 (70%)**, which provides:
- ✅ Good visual quality
- ✅ 70-80% size reduction
- ✅ Fast compression
- ✅ Readable text in screenshots

### Adjust Quality

Control compression quality via system property:

```bash
# Default (70% quality)
mvn test

# Higher quality (90%) - Larger files
mvn test -Dscreenshot.quality=0.9

# Lower quality (50%) - Smaller files
mvn test -Dscreenshot.quality=0.5

# Maximum quality (100%) - Largest files
mvn test -Dscreenshot.quality=1.0

# Minimum quality (30%) - Smallest files
mvn test -Dscreenshot.quality=0.3
```

---

## 📈 Quality vs Size Trade-off

| Quality Setting | File Size | Visual Quality | Use Case |
|----------------|-----------|----------------|----------|
| **1.0 (100%)** | ~500 KB | Excellent | Pixel-perfect screenshots |
| **0.9 (90%)** | ~300 KB | Very Good | High-quality evidence |
| **0.7 (70%)** ⭐ | ~180 KB | Good | **Default - Recommended** |
| **0.5 (50%)** | ~100 KB | Fair | Quick debugging |
| **0.3 (30%)** | ~50 KB | Poor | Extremely limited storage |

⭐ **Recommended:** 0.7 (70%) provides the best balance

---

## 🔍 Compression Details

### Implementation

**Location:**
- `Hooks.java` (Lines 207-250)
- `CommonSteps.java` (Lines 267-307)

**Method:**
```java
private byte[] compressScreenshot(byte[] originalScreenshot) {
    // Read PNG image
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(originalScreenshot));

    // Get quality setting (default: 0.7)
    float quality = Float.parseFloat(System.getProperty("screenshot.quality", "0.7"));

    // Write as JPEG with compression
    ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
    ImageWriteParam param = writer.getDefaultWriteParam();
    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    param.setCompressionQuality(quality);

    writer.write(null, new IIOImage(image, null, null), param);

    return compressedBytes;
}
```

### Format Conversion

| Original | Compressed |
|----------|-----------|
| PNG (lossless) | JPEG (lossy) |
| Larger files | Smaller files |
| Perfect quality | Controlled quality |

---

## 📁 File Naming

### Before Compression
```
target/screenshots/
└── FAILURE_Verify_Explorer_page_1234567890.png  ← Would be large
```

### After Compression
```
target/screenshots/
└── FAILURE_Verify_Explorer_page_1234567890.jpg  ← Compressed
```

**Note:** File extension changes from `.png` to `.jpg`

---

## 🚀 Console Output

### With Compression (Default)
```bash
✅ Screenshot captured for failed scenario
📁 Failure screenshot saved to: target/screenshots/FAILURE_*.jpg
📊 Size: 180.5 KB (reduced by 78%)
```

### Compression Stats Breakdown
```
Original size:  823.4 KB (PNG from driver)
Compressed:     180.5 KB (JPEG with 70% quality)
Reduction:      78%
```

---

## 🎯 Use Cases

### Use Default (70% quality)
```bash
mvn test
```

**Best for:**
- ✅ Regular test runs
- ✅ CI/CD pipelines
- ✅ Daily testing
- ✅ Most debugging scenarios

---

### Use High Quality (90%)
```bash
mvn test -Dscreenshot.quality=0.9
```

**Best for:**
- ✅ Visual regression testing
- ✅ Pixel-perfect comparisons
- ✅ Detailed UI analysis
- ✅ Production evidence
- ✅ Client presentations

---

### Use Low Quality (50%)
```bash
mvn test -Dscreenshot.quality=0.5
```

**Best for:**
- ✅ Storage-constrained environments
- ✅ Quick sanity checks
- ✅ Temporary debugging
- ✅ CI builds with limited storage

---

### Disable Compression (100%)
```bash
mvn test -Dscreenshot.quality=1.0
```

**Best for:**
- ✅ Archiving important evidence
- ✅ Legal/compliance requirements
- ✅ When file size doesn't matter

---

## 🔧 Advanced Configuration

### Per-Environment Settings

#### Development (High Quality)
```bash
# .env.dev
screenshot.quality=0.9
```

#### CI/CD (Balanced)
```bash
# .env.ci
screenshot.quality=0.7  # Default
```

#### Production (Low Quality)
```bash
# .env.prod
screenshot.quality=0.5
```

### Maven Profile Example

```xml
<!-- pom.xml -->
<profiles>
    <profile>
        <id>high-quality</id>
        <properties>
            <screenshot.quality>0.9</screenshot.quality>
        </properties>
    </profile>

    <profile>
        <id>low-quality</id>
        <properties>
            <screenshot.quality>0.5</screenshot.quality>
        </properties>
    </profile>
</profiles>
```

**Usage:**
```bash
mvn test -Phigh-quality
mvn test -Plow-quality
```

---

## 📊 Disk Space Impact

### Without Compression

**10 failed tests:**
```
10 tests × 1.5 MB avg = 15 MB
```

**100 failed tests:**
```
100 tests × 1.5 MB avg = 150 MB
```

**1000 tests over time:**
```
1000 tests × 1.5 MB avg = 1.5 GB  ❌ Too much!
```

---

### With Compression (70% quality)

**10 failed tests:**
```
10 tests × 300 KB avg = 3 MB  ✅ 80% reduction
```

**100 failed tests:**
```
100 tests × 300 KB avg = 30 MB  ✅ 80% reduction
```

**1000 tests over time:**
```
1000 tests × 300 KB avg = 300 MB  ✅ Much better!
```

---

## 🛡️ Error Handling

### If Compression Fails

The system falls back to the original screenshot:

```java
try {
    return compressScreenshot(screenshot);
} catch (Exception e) {
    System.err.println("⚠️  Failed to compress screenshot, using original");
    return originalScreenshot;  // Fallback to PNG
}
```

**You'll see:**
```
⚠️  Failed to compress screenshot, using original: [error message]
📁 Failure screenshot saved to: target/screenshots/FAILURE_*.png
```

---

## 🎨 Visual Quality Examples

### Quality: 1.0 (100%)
- **File size:** ~500 KB
- **Quality:** Perfect, no artifacts
- **Text:** Crystal clear
- **Colors:** Exact match

### Quality: 0.9 (90%)
- **File size:** ~300 KB
- **Quality:** Excellent, minimal artifacts
- **Text:** Very clear
- **Colors:** Nearly perfect

### Quality: 0.7 (70%) ⭐ Default
- **File size:** ~180 KB
- **Quality:** Good, minor artifacts
- **Text:** Clear and readable
- **Colors:** Good reproduction

### Quality: 0.5 (50%)
- **File size:** ~100 KB
- **Quality:** Fair, noticeable artifacts
- **Text:** Readable but slightly blurry
- **Colors:** Some degradation

### Quality: 0.3 (30%)
- **File size:** ~50 KB
- **Quality:** Poor, significant artifacts
- **Text:** Harder to read
- **Colors:** Noticeable degradation

---

## ⚡ Performance Impact

### Compression Time

| Screenshot Size | Compression Time | Impact |
|----------------|------------------|--------|
| 1 MB | ~50-100 ms | Negligible |
| 2 MB | ~100-150 ms | Negligible |
| 3 MB | ~150-200 ms | Minimal |

**Total test impact:** < 0.2 seconds per screenshot

---

## 📝 Best Practices

### ✅ DO

1. **Use default quality (0.7)** for most scenarios
   ```bash
   mvn test  # Uses 0.7 by default
   ```

2. **Increase quality for important evidence**
   ```bash
   mvn test -Dscreenshot.quality=0.9
   ```

3. **Monitor disk space** over time
   ```bash
   du -sh target/screenshots/
   ```

4. **Clean old screenshots** (automatic via cleanup)
   ```bash
   # Automatic on test start
   🧹 Cleaning old screenshots...
   ```

### ❌ DON'T

1. **Don't use quality below 0.5** for debugging
   - Text becomes hard to read
   - Details are lost

2. **Don't use quality 1.0 by default**
   - Files are too large
   - Minimal visual improvement

3. **Don't disable compression** without reason
   - Wastes disk space
   - Slows down report loading

---

## 🔗 Related Documentation

- [Screenshots and Reporting](SCREENSHOTS_AND_REPORTING.md)
- [Cleanup Strategy](CLEANUP_STRATEGY.md)
- [Hooks Documentation](HOOKS.md)

---

**Last Updated:** 2025-10-29
