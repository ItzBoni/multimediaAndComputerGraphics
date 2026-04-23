package handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Set;

/**
 * Handles encoding/decoding of binary data and base64 conversions.
 *
 * Provides methods for:
 * - Converting files to base64-encoded strings
 * - Decoding base64 or raw bytes back to files
 * - Validating file types (images, audio)
 * - Writing binary data to disk using appropriate handlers
 */
public class ConversionHandler {

    private static final Set<String> IMAGE_EXTS = Set.of("jpg", "jpeg", "png", "gif", "bmp", "webp", "heic");
    private static final Set<String> AUDIO_EXTS = Set.of("mp3", "wav", "aac", "flac", "ogg", "m4a");

    /**
     * Encodes a file's binary data to a base64-encoded String.
     *
     * @param file the file to encode
     * @return base64-encoded string representing the file contents, or null on error
     */
    public static String encodeToBase64(File file) {
        try {
            return Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            System.err.println("Encoding failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Decodes a base64-encoded string to a file.
     *
     * Automatically detects file type and uses appropriate decoder:
     * - Images: decoded via ImageIO
     * - Audio: written as raw bytes
     *
     * @param base64 base64-encoded string data
     * @param outputFile the file to write the decoded data to
     * @return the output file, or null if decoding failed
     */
    public static File decodeByteResponse(String base64, File outputFile) {
        if (base64 == null) {
            System.err.println("Decoding failed for " + outputFile.getName() + ": received null base64 string");
            return null;
        }
        try {
            return writeToFile(Base64.getDecoder().decode(base64), outputFile);
        } catch (Exception e) {
            System.err.println("Decoding failed for " + outputFile.getName() + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Writes raw bytes to a file.
     *
     * Automatically detects file type from extension and uses appropriate writer:
     * - Images: written via ImageIO
     * - Audio: written as raw binary data
     *
     * @param bytes raw byte array data
     * @param outputFile the file to write the data to
     */
    public static void decodeByteResponse(byte[] bytes, File outputFile) {
        if (bytes == null) {
            System.err.println("Decoding failed for " + outputFile.getName() + ": received null byte array");
            return;
        }
        try {
            writeToFile(bytes, outputFile);
        } catch (Exception e) {
            System.err.println("Decoding failed for " + outputFile.getName() + ": " + e.getMessage());
        }
    }

    /**
     * Internal method that writes bytes to a file with format-appropriate handling.
     *
     * @param bytes raw byte data
     * @param outputFile the file to write to
     * @return the written file
     * @throws Exception if file type is unsupported or write fails
     */
    private static File writeToFile(byte[] bytes, File outputFile) throws Exception {
        String ext = getExtension(outputFile);

        if (IMAGE_EXTS.contains(ext)) {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            if (image == null) {
                throw new Exception("ImageIO could not decode bytes as " + ext + " — data may be corrupt or wrong format");
            }
            ImageIO.write(image, ext.equals("jpg") ? "jpeg" : ext, outputFile);
        } else if (AUDIO_EXTS.contains(ext)) {
            Files.write(outputFile.toPath(), bytes);
        } else {
            System.err.println("Unsupported file type: " + ext);
            return null;
        }

        return outputFile;
    }

    /**
     * Extracts the file extension from a filename.
     *
     * @param file the file to extract extension from
     * @return lowercase file extension (without dot), or empty string if no extension
     */
    private static String getExtension(File file) {
        String name = file.getName().toLowerCase();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1);
    }
}