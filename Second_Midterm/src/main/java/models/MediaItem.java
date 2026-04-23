package models;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data model representing a media file with its extracted metadata.
 * Construction and initialisation logic lives in MediaItemFactory.
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

    public MediaItem(File file) {
        this.file    = file;
        this.isVideo = isVideoFile(file);
    }

    public static boolean isHeicFile(File f) {
        String name = f.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        return dot >= 0 && HEIC_EXTS.contains(name.substring(dot + 1));
    }

    public static boolean isVideoFile(File f) {
        String name = f.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        if (dot < 0) return false;
        return VIDEO_EXTS.contains(name.substring(dot + 1));
    }

    // ── setters ──────────────────────────────────────────────────────────────

    public void setFile(File file)                       { this.file = file; }
    public void setCreatedAt(LocalDateTime dt)           { this.createdAt = dt; }
    public void setGpsLat(double lat)                    { this.gpsLat = lat; }
    public void setGpsLon(double lon)                    { this.gpsLon = lon; }
    public void setRepresentativeFrame(File f)           { this.representativeFrame = f; }
    public void setDescription(String d)                 { this.description = d; }

    // ── getters ──────────────────────────────────────────────────────────────

    public boolean isVideo()                             { return isVideo; }
    public LocalDateTime getCreatedAt()                  { return createdAt; }
    public double getGpsLat()                            { return gpsLat; }
    public double getGpsLon()                            { return gpsLon; }
    public double[] getGps()                             { return new double[]{this.gpsLat, this.gpsLon}; }
    public String getDescription()                       { return description; }
    public File getRepresentativeFrame()                 { return representativeFrame; }
    public File getFile()                                { return file; }

    public String getFilePath() {
        return this.file.getPath();
    }
}
