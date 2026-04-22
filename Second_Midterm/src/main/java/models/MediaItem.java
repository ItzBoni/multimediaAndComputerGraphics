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

    public MediaItem(String fileLocation) {
        setAttributes(fileLocation);
    }

    public void setAttributes(String fileLocation) {
        this.file    = new File(fileLocation);
        this.isVideo = isVideoFile(this.file);

        //Creates a new name for the representative frame of video and saves it into the video's folder
        if(this.isVideo()){
            System.out.println("I'm getting into the video");
            String fileName = this.file.getName();
            String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
            String frameDirectory = fileLocation.substring(0, fileLocation.length() - fileName.length());
            String framePath = frameDirectory + nameWithoutExt + "_repFrame.jpg";

            FFmpeg.saveRepresentativeFrame(this.file, framePath);
            this.representativeFrame = new File(framePath);
        }

        if (isHeicFile(this.file)) {
            String fileName  = this.file.getName();
            String nameNoExt = fileName.substring(0, fileName.lastIndexOf('.'));
            String dir       = fileLocation.substring(0, fileLocation.length() - fileName.length());
            this.file = ExifTool.convertToJpeg(this.file, dir + nameNoExt + "_converted.jpg");
        }

        String raw  = ExifTool.extractMetadata(fileLocation);
        this.gpsLat = MetadataParser.parseGPS(raw, "GPS Latitude");
        this.gpsLon = MetadataParser.parseGPS(raw, "GPS Longitude");

        LocalDateTime dt = MetadataParser.parseDate(raw);
        this.createdAt   = (dt != null) ? dt : MetadataParser.fallbackTime(this.file);
    }

    private static boolean isHeicFile(File f) {
        String name = f.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        return dot >= 0 && HEIC_EXTS.contains(name.substring(dot + 1));
    }

    private static boolean isVideoFile(File f) {
        String name = f.getName().toLowerCase();

        int dot = name.lastIndexOf('.');
        if (dot < 0) return false;
        return VIDEO_EXTS.contains(name.substring(dot + 1));
    }

    public void setDescription(String d)       { this.description = d; }

    // ── getters ──────────────────────────────────────────────────────────────

    public boolean isVideo() { return isVideo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public double getGpsLat() { return gpsLat; }
    public double getGpsLon() { return gpsLon; }
    public double[] getGps() {
        return new double[]{this.gpsLat, this.gpsLon};
    }
    public String getDescription() { return description; }
    public File getRepresentativeFrame() { return representativeFrame; }
    public File getFile() { return file; }
}
