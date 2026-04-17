package models;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MediaItem {

    private static final Set<String> VIDEO_EXTS = new HashSet<>(
        Arrays.asList("mp4", "mov", "avi", "mkv", "flv", "webm")
    );

    private final File file;
    private final boolean isVideo;
    private final LocalDateTime createdAt;
    private final double gpsLat;
    private final double gpsLon;
    private String description;
    private File representativeFrame;

    public MediaItem(File file, boolean isVideo, LocalDateTime createdAt,
                     double gpsLat, double gpsLon) {
        this.file = file;
        this.isVideo = isVideo;
        this.createdAt = createdAt;
        this.gpsLat = gpsLat;
        this.gpsLon = gpsLon;
    }

    public File getFile()             { return file; }
    public boolean isVideo()          { return isVideo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public double getGpsLat()         { return gpsLat; }
    public double getGpsLon()         { return gpsLon; }
    public String getDescription()    { return description; }
    public File getRepresentativeFrame() { return representativeFrame; }

    public void setDescription(String d)        { this.description = d; }
    public void setRepresentativeFrame(File f)  { this.representativeFrame = f; }

    public boolean hasGps() { return gpsLat != 0.0 || gpsLon != 0.0; }

    public static boolean isVideoFile(File f) {
        String name = f.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        if (dot < 0) return false;
        return VIDEO_EXTS.contains(name.substring(dot + 1));
    }
}
