package models;

import wrappers.ExifTool;
import wrappers.FFmpeg;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaItem {

    private static final Set<String> VIDEO_EXTS = new HashSet<>(
        Arrays.asList("mp4", "mov", "avi", "mkv", "flv", "webm")
    );

    private File file;
    private boolean isVideo;
    private LocalDateTime createdAt;
    private double gpsLat;
    private double gpsLon;

    private String description;
    private File representativeFrame;

    public MediaItem(String fileLocation) {
        setAttributes(fileLocation);
    }

    public void setAttributes(String fileLocation) {
        this.file    = new File(fileLocation);
        this.isVideo = isVideoFile(this.file);

        String raw  = ExifTool.extractMetadata(fileLocation);
        this.gpsLat = parseGPS(raw, "GPS Latitude");
        this.gpsLon = parseGPS(raw, "GPS Longitude");

        LocalDateTime dt = parseDate(raw);
        this.createdAt   = (dt != null) ? dt : fallbackTime(this.file);
    }

    /**
     * Extracts a GPS coordinate from the flat exiftool output string.
     * Exiftool default format: "19 deg 26' 0.00\" N"
     * Converts DMS → decimal degrees; negates for S or W.
     */
    private static double parseGPS(String raw, String tagName) {
        int tagIdx = raw.indexOf(tagName);
        if (tagIdx < 0) return 0.0;

        int colonIdx = raw.indexOf(": ", tagIdx + tagName.length());
        if (colonIdx < 0) return 0.0;

        // Grab enough characters to cover the DMS value
        int end = Math.min(colonIdx + 2 + 60, raw.length());
        String slice = raw.substring(colonIdx + 2, end);

        // Match the pattern with Regex
        Pattern p = Pattern.compile("(\\d+) deg (\\d+)' ([\\d.]+)\" ([NSEW])");
        Matcher m = p.matcher(slice);
        if (!m.find()) return 0.0;

        double degrees = Double.parseDouble(m.group(1));
        double minutes = Double.parseDouble(m.group(2));
        double seconds = Double.parseDouble(m.group(3));
        String dir     = m.group(4);

        double decimal = degrees + minutes / 60.0 + seconds / 3600.0;
        if (dir.equals("S") || dir.equals("W")) decimal = -decimal;
        return decimal;
    }

    /**
     * Extracts a creation timestamp from the flat exiftool output string.
     * Tries several common EXIF date tags in priority order.
     * Exiftool default format: "2024:03:15 14:22:01"
     */
    private static LocalDateTime parseDate(String raw) {
        String[] candidates = {"Create Date", "Date/Time Original", "Date Created"};

        Pattern p = Pattern.compile("(\\d{4}):(\\d{2}):(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");

        for (String tag : candidates) {
            int tagIdx = raw.indexOf(tag);
            if (tagIdx < 0) continue;

            int colonIdx = raw.indexOf(": ", tagIdx + tag.length());
            if (colonIdx < 0) continue;

            int end = Math.min(colonIdx + 2 + 25, raw.length());
            String slice = raw.substring(colonIdx + 2, end);

            Matcher m = p.matcher(slice);
            if (m.find()) {
                return LocalDateTime.of(
                    Integer.parseInt(m.group(1)),
                    Integer.parseInt(m.group(2)),
                    Integer.parseInt(m.group(3)),
                    Integer.parseInt(m.group(4)),
                    Integer.parseInt(m.group(5)),
                    Integer.parseInt(m.group(6))
                );
            }
        }
        return null;
    }

    /** Falls back to the file's last-modified timestamp when no EXIF date exists. */
    private static LocalDateTime fallbackTime(File f) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(f.lastModified()), ZoneId.systemDefault());
    }

    public boolean hasGps() { return gpsLat != 0.0 || gpsLon != 0.0; }

    public static boolean isVideoFile(File f) {
        String name = f.getName().toLowerCase();
        String directory = f.getAbsolutePath();

        int dot = name.lastIndexOf('.');
        if (dot < 0) return false;
        else{
            FFmpeg.saveRepresentativeFrame(directory);
            return VIDEO_EXTS.contains(name.substring(dot + 1));
        }
    }

    public void setDescription(String d)       { this.description = d; }
    public void setRepresentativeFrame(File f) { this.representativeFrame = f; }

    // ── getters ──────────────────────────────────────────────────────────────

    public boolean       isVideo()                { return isVideo; }
    public LocalDateTime getCreatedAt()           { return createdAt; }
    public double        getGpsLat()              { return gpsLat; }
    public double        getGpsLon()              { return gpsLon; }
    public String        getDescription()         { return description; }
    public File          getRepresentativeFrame() { return representativeFrame; }
}
