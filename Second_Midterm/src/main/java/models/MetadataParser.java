package models;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser for extracting metadata from raw ExifTool output.
 *
 * Handles:
 * - GPS coordinate extraction and conversion (DMS to decimal degrees)
 * - Creation date/time parsing from multiple EXIF tags
 * - Fallback to file modification date
 *
 * ExifTool outputs metadata as flat text lines with format: "TagName: value"
 */
public class MetadataParser {

    /**
     * Extracts and converts a GPS coordinate from ExifTool output.
     *
     * Searches for a specific GPS tag (e.g., "GPS Latitude") in the output.
     * Parses DMS format: "19 deg 26' 0.00\" N"
     * Converts to decimal degrees and applies direction (S/W = negative).
     *
     * @param raw the complete ExifTool output string
     * @param tagName the GPS tag to search for (e.g., "GPS Latitude")
     * @return decimal degrees value, or 0.0 if not found
     */
    public static double parseGPS(String raw, String tagName) {
        int searchFrom = 0;
        int tagIdx;

        while ((tagIdx = raw.indexOf(tagName, searchFrom)) >= 0) {
            if (tagIdx > 0 && raw.charAt(tagIdx - 1) != '\n') {
                searchFrom = tagIdx + 1;
                continue;
            }

            int afterTag = tagIdx + tagName.length();
            int peekIdx = afterTag;
            while (peekIdx < raw.length() && raw.charAt(peekIdx) == ' ') peekIdx++;
            if (peekIdx < raw.length() && Character.isLetter(raw.charAt(peekIdx))) {
                searchFrom = tagIdx + 1;
                continue;
            }

            int colonIdx = raw.indexOf(": ", tagIdx + tagName.length());
            if (colonIdx < 0) return 0.0;

            int end = Math.min(colonIdx + 2 + 60, raw.length());
            String slice = raw.substring(colonIdx + 2, end);

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

        return 0.0;
    }

    /**
     * Extracts a creation timestamp from ExifTool output.
     *
     * Tries multiple EXIF date tags in priority order:
     * 1. Create Date
     * 2. Date/Time Original
     * 3. Date Created
     * 4. File Creation Date/Time
     *
     * Parses format: "2024:03:15 14:22:01" (YYYY:MM:DD HH:MM:SS)
     *
     * @param raw the complete ExifTool output string
     * @return parsed LocalDateTime, or null if no valid date found
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

    /**
     * Gets the file's last-modified timestamp as a fallback date.
     *
     * Used when no EXIF date information is available in the file.
     * Converts file modification time to system default timezone.
     *
     * @param f the file to get modification time from
     * @return LocalDateTime of file's last modification in system timezone
     */
    public static LocalDateTime fallbackTime(File f) {
        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(f.lastModified()), ZoneId.systemDefault());
    }
}
