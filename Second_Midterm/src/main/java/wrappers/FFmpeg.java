package wrappers;

import java.io.*;

/**
 * Responsible for wrapping single-file FFmpeg CLI operations.
 * Multi-file stitching pipeline lives in VideoStitcher.
 */
public class FFmpeg {

    public static void saveRepresentativeFrame(File videoFile, String outputDirectory) {
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-i", videoFile.getAbsolutePath(),
                "-ss", "00:00:01",
                "-frames:v", "1",
                outputDirectory
        );
        StringBuilder response = new StringBuilder();
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
            System.out.println("Failed to generate representative frame");
            throw new RuntimeException(e);
        }

        System.out.println(response.toString());
    }

    public static File convertHeicToJpeg(File inputFile, String outputPath) {
        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-y",
                "-i", inputFile.getAbsolutePath(),
                outputPath
        );
        pb.redirectErrorStream(true);

        try {
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                while (reader.readLine() != null) {}
            }
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to convert HEIC to JPEG");
            throw new RuntimeException(e);
        }

        return new File(outputPath);
    }
}
