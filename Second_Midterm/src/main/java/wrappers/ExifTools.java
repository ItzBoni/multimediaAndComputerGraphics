package wrappers;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class ExifTools {
    public static boolean convertToJpg(BufferedImage image, File destination) {
        return false;
    }

    public static String extractMetadata(String fileLocation){
        String metadata = null;
        StringBuilder response = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder("exiftools", fileLocation);
        pb.redirectErrorStream(true);

        try {
            final Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        metadata = response.toString();

        return metadata;
    }
}
