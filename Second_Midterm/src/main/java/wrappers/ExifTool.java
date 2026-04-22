package wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExifTool {
    public static File convertToJpeg(File inputFile, String outputPath) {
        System.out.println("I'm getting in convertToJpeg");
        ProcessBuilder pb = new ProcessBuilder(
                "exiftool",
                "-o", outputPath,
                inputFile.getAbsolutePath()
        );
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to convert file to JPEG");
            throw new RuntimeException(e);
        }

        System.out.println("Convert to JPEG worked");

        return new File(outputPath);
    }

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
