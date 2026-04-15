package handlers;

import java.io.File;

public interface FileHandler {
    String encodeToBase64();
    void decodeFromBase64(String base64);
    boolean saveToFile(File destination);
    boolean loadFromFile(File source);
}