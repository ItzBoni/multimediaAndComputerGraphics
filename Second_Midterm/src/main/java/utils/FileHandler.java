package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class FileHandler {
    public static BufferedImage imgImport(File file){
        BufferedImage image = null;
        if (file != null) {
            try {
                image = ImageIO.read(file);
            } catch (IOException e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }

        return image;
    }

    /**
     * Decodes a Base64 audio string and saves it to disk.
     *
     * @param base64Audio  The Base64-encoded audio string
     * @param destination  The file to save to
     * @return             true if saved successfully, false otherwise
     */
    public static boolean saveAudio(String base64Audio, File destination) {
        if (base64Audio == null || destination == null) {
            System.err.println("saveAudio: null argument provided.");
            return false;
        }

        try {
            byte[] audioBytes = Base64.getDecoder().decode(base64Audio);
            Files.write(destination.toPath(), audioBytes);
            System.out.println("Audio saved to: " + destination.getAbsolutePath());
            return true;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Base64 string: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Failed to save audio: " + e.getMessage());
        }

        return false;
    }

    /**
     * Returns a FileType for the uploaded image
     */
    public static FileType getFileType(File file){
        if (file == null) return null;

        String name = file.getName().toLowerCase();

        if (name.endsWith(".png"))  return FileType.PNG;
        if (name.endsWith(".jpg") ||
                name.endsWith(".jpeg")) return FileType.JPEG;
        if (name.endsWith(".gif"))  return FileType.GIF;
        if (name.endsWith(".bmp"))  return FileType.BMP;

        return null;
    }

    /**
     * Encodes an image into Base64
     */
    public static String toBase64(BufferedImage image) {
        if (image == null) return null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            System.err.println("Failed to encode image: " + e.getMessage());
            return null;
        }
    }
}
