package models;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataParser {
    /**
     * Extracts a GPS coordinate from the flat exiftool output string.
     * Exiftool default format: "19 deg 26' 0.00\" N"
     * Converts DMS → decimal degrees; negates for S or W.
     */
    public static double parseGPS(String raw, String tagName) {
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
    public static LocalDateTime parseDate(String raw) {
        String[] candidates = {"Create Date", "Date/Time Original", "Date Created", "File Creation Date/Time"};

        Pattern p = Pattern.compile("(\\d{4}):(\\d{2}):(\\d{2}) (\\d{2}):(\\d{2}):(\\d{2})");

        for (String tag : candidates) {
            int tagIdx = raw.indexOf(tag);
            if (tagIdx < 0) continue;

            // Ensure we didn't match mid-word (e.g. "Date Created" inside "File Creation Date/Time")
            if (tagIdx > 0 && raw.charAt(tagIdx - 1) != '\n') continue;

            int colonIdx = raw.indexOf(": ", tagIdx + tag.length());
            if (colonIdx < 0) continue;

            int end = Math.min(colonIdx + 2 + 30, raw.length());
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
    public static LocalDateTime fallbackTime(File f) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(f.lastModified()), ZoneId.systemDefault());
    }
}
