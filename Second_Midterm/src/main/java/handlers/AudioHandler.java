package handlers;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

public class AudioHandler implements FileHandler {

    private byte[] audioBytes;
    private FileType fileType;

    @Override
    public boolean loadFromFile(File source) {
        if (source == null) return false;
        try {
            audioBytes = Files.readAllBytes(source.toPath());
            fileType = detectFileType(source);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to load audio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String encodeToBase64() {
        if (audioBytes == null) return null;
        return Base64.getEncoder().encodeToString(audioBytes);
    }

    @Override
    public void decodeFromBase64(String base64) {
        audioBytes = Base64.getDecoder().decode(base64);
        fileType = FileType.MP3;
    }

    @Override
    public boolean saveToFile(File destination) {
        if (audioBytes == null || destination == null) return false;
        try {
            destination.getParentFile().mkdirs();
            Files.write(destination.toPath(), audioBytes);
            return true;
        } catch (Exception e) {
            System.err.println("Save failed: " + e.getMessage());
            return false;
        }
    }

    public byte[] getAudioBytes()           { return audioBytes; }
    public void setAudioBytes(byte[] bytes) { this.audioBytes = bytes; }

    private FileType detectFileType(File file) {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".mp3"))  return FileType.MP3;
        if (name.endsWith(".wav"))  return FileType.WAV;
        if (name.endsWith(".aac"))  return FileType.AAC;
        if (name.endsWith(".ogg"))  return FileType.OGG;
        if (name.endsWith(".flac")) return FileType.FLAC;

        return FileType.UNKNOWN;
    }
}