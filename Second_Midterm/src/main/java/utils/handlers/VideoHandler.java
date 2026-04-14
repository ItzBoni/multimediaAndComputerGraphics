package utils.handlers;

import wrappers.FFmpeg;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class VideoHandler implements FileHandler {

    private byte[] videoBytes;
    private FileType fileType;

    @Override
    public boolean loadFromFile(File source) {
        if (source == null) return false;
        try {
            videoBytes = Files.readAllBytes(source.toPath());
            fileType = detectFileType(source);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to load video: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String encodeToBase64() {
        if (videoBytes == null) return null;
        return Base64.getEncoder().encodeToString(videoBytes);
    }

    @Override
    public void decodeFromBase64(String base64) {
        videoBytes = Base64.getDecoder().decode(base64);
        fileType = FileType.MP4;
    }

    @Override
    public boolean saveToFile(File destination) {
        if (videoBytes == null || destination == null) return false;
        try {
            destination.getParentFile().mkdirs();
            Files.write(destination.toPath(), videoBytes);
            return true;
        } catch (Exception e) {
            System.err.println("Save failed: " + e.getMessage());
            return false;
        }
    }
    /*
    public String extractAudio(File destination) {
        return FFmpeg.extractAudio(videoBytes, destination);
    }*/

    public byte[] getVideoBytes()           { return videoBytes; }
    public void setVideoBytes(byte[] bytes) { this.videoBytes = bytes; }

    @Override public FileType getFileType() { return fileType; }
    @Override public boolean isLoaded()     { return videoBytes != null; }

    private FileType detectFileType(File file) {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".mp4"))  return FileType.MP4;
        if (name.endsWith(".mov"))  return FileType.MP4;  // treat MOV as MP4
        if (name.endsWith(".avi"))  return FileType.AVI;
        if (name.endsWith(".mkv"))  return FileType.MKV;
        if (name.endsWith(".flv"))  return FileType.FLV;
        if (name.endsWith(".webm")) return FileType.WEBM;

        return FileType.UNKNOWN;
    }
}