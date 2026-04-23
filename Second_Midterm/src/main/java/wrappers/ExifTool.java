package wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Wrapper for ExifTool CLI.
 *
 * Extracts metadata from media files including:
 * - GPS coordinates (latitude, longitude)
 * - Creation dates
 * - Camera information
 * - File details
 *
 * Requires ExifTool to be installed and accessible in PATH.
 */
public class ExifTool {

    /**
     * Extracts all available metadata from a media file.
     *
     * Returns raw ExifTool output as plain text with format:
     * "TagName: value"
     *
     * Output is parsed by MetadataParser to extract specific fields.
     *
     * @param fileLocation path to the media file
     * @return complete metadata output as a multiline string
     * @throws RuntimeException if ExifTool execution fails
     */
    public static String extractMetadata(String fileLocation){
        String metadata = null;
        StringBuilder response = new StringBuilder();
        ProcessBuilder pb = new ProcessBuilder("exiftool", fileLocation);
        pb.redirectErrorStream(true);

        try {
            final Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
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
