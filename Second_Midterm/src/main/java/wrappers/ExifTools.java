package wrappers;

import java.awt.image.BufferedImage;
import java.io.File;

public class ExifTools {
    public static boolean convertToJpg(BufferedImage image, File destination) {
        return false;
    }

    public static String extractMetadata(String folder){
        String metadata = null;
        ProcessBuilder pb = new ProcessBuilder(
                "exiftools"
        );

        return metadata;
    }
}
