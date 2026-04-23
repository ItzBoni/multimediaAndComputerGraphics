package models;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data model representing a media file with extracted metadata.
 *
 * Stores:
 * - File reference and type information
 * - Geographic coordinates (GPS latitude/longitude)
 * - Creation date and time
 * - AI-generated description
 * - Representative frame (for videos)
 *
 * Construction and metadata extraction are handled by MediaItemFactory.
 * This class provides storage and accessor methods only.
 */
public class MediaItem {

    private static final Set<String> VIDEO_EXTS = new HashSet<>(
        Arrays.asList("mp4", "mov", "avi", "mkv", "flv", "webm")
    );

    private static final Set<String> HEIC_EXTS = new HashSet<>(
        Arrays.asList("heic", "heif")
    );

    private File file;
    private boolean isVideo;
    private LocalDateTime createdAt;
    private double gpsLat;
    private double gpsLon;
    private String description;
    private File representativeFrame;

    /**
     * Creates a MediaItem from a file.
     *
     * Determines if the file is a video based on extension.
     * Does NOT extract metadata; that is done by MediaItemFactory.
     *
     * @param file the media file
     */
    public MediaItem(File file) {
        this.file    = file;
        this.isVideo = isVideoFile(file);
    }

    /**
     * Checks if a file is in HEIC/HEIF format (Apple image format).
     *
     * @param f the file to check
     * @return true if the file is a HEIC/HEIF file
     */
    public static boolean isHeicFile(File f) {
        String name = f.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        return dot >= 0 && HEIC_EXTS.contains(name.substring(dot + 1));
    }

    /**
     * Checks if a file is a video based on extension.
     *
     * Supported video formats: MP4, MOV, AVI, MKV, FLV, WebM
     *
     * @param f the file to check
     * @return true if the file is a video file
     */
    public static boolean isVideoFile(File f) {
        String name = f.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        if (dot < 0) return false;
        return VIDEO_EXTS.contains(name.substring(dot + 1));
    }

    // ── Setters ──────────────────────────────────────────────────────────────

    /**
     * Sets the file reference.
     *
     * @param file the media file
     */
    public void setFile(File file) { this.file = file; }

    /**
     * Sets the creation date/time of the media.
     *
     * @param dt the creation timestamp
     */
    public void setCreatedAt(LocalDateTime dt) { this.createdAt = dt; }

    /**
     * Sets the GPS latitude coordinate.
     *
     * @param lat latitude value
     */
    public void setGpsLat(double lat) { this.gpsLat = lat; }

    /**
     * Sets the GPS longitude coordinate.
     *
     * @param lon longitude value
     */
    public void setGpsLon(double lon) { this.gpsLon = lon; }

    /**
     * Sets the representative frame file (extracted from video).
     *
     * @param f the representative frame image file
     */
    public void setRepresentativeFrame(File f) { this.representativeFrame = f; }

    /**
     * Sets the AI-generated description of the media.
     *
     * @param d the description text
     */
    public void setDescription(String d) { this.description = d; }

    // ── Getters ──────────────────────────────────────────────────────────────

    /**
     * Checks if this media item is a video.
     *
     * @return true if this is a video file
     */
    public boolean isVideo() { return isVideo; }

    /**
     * Gets the creation date/time.
     *
     * @return the creation timestamp
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /**
     * Gets the GPS latitude coordinate.
     *
     * @return latitude value
     */
    public double getGpsLat() { return gpsLat; }

    /**
     * Gets the GPS longitude coordinate.
     *
     * @return longitude value
     */
    public double getGpsLon() { return gpsLon; }

    /**
     * Gets both GPS coordinates as an array [latitude, longitude].
     *
     * @return array of [lat, lon]
     */
    public double[] getGps() { return new double[]{this.gpsLat, this.gpsLon}; }

    /**
     * Gets the AI-generated description.
     *
     * @return the description text
     */
    public String getDescription() { return description; }

    /**
     * Gets the representative frame file (for videos).
     *
     * @return the representative frame image file
     */
    public File getRepresentativeFrame() { return representativeFrame; }

    /**
     * Gets the media file.
     *
     * @return the File object
     */
    public File getFile() { return file; }

    /**
     * Gets the absolute file path.
     *
     * @return the full path to the file
     */
    public String getFilePath() {
        return this.file.getPath();
    }
}
