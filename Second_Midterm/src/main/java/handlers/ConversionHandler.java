package handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Set;

public class ConversionHandler {

    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp", "heic");
    private static final Set<String> AUDIO_EXTS = Set.of("mp3", "wav", "aac", "flac", "ogg", "m4a");

    public static String encodeToBase64(File file) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            System.err.println("Encoding failed: " + e.getMessage());
            return null;
        }
    }

    public static File decodeByteResponse(String base64, File outputFile) {
        try {
            return writeToFile(Base64.getDecoder().decode(base64), outputFile);
        } catch (Exception e) {
            System.err.println("Decoding failed: " + e.getMessage());
            return null;
        }
    }

    public static File decodeByteResponse(byte[] bytes, File outputFile) {
        try {
            return writeToFile(bytes, outputFile);
        } catch (Exception e) {
            System.err.println("Decoding failed: " + e.getMessage());
            return null;
        }
    }

    private static File writeToFile(byte[] bytes, File outputFile) throws Exception {
        String ext = getExtension(outputFile);

        if (IMAGE_EXTS.contains(ext)) {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            ImageIO.write(image, ext.equals("jpg") ? "jpeg" : ext, outputFile);
        } else if (AUDIO_EXTS.contains(ext)) {
            Files.write(outputFile.toPath(), bytes);
        } else {
            System.err.println("Unsupported file type: " + ext);
            return null;
        }

        return outputFile;
    }

    private static String getExtension(File file) {
        String name = file.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1);
    }
}