package wrappers;

import java.io.*;

/**
 * Wrapper for FFmpeg CLI operations.
 *
 * Handles single-file operations:
 * - Extracting representative frames from videos
 * - Converting HEIC/HEIF images to JPEG
 *
 * Multi-file video stitching is handled by VideoStitcher.
 *
 * Executes FFmpeg as a subprocess and captures output.
 * Requires FFmpeg to be installed and available in PATH.
 */
public class FFmpeg {

    /**
     * Extracts a representative frame from a video file.
     *
     * Saves a single frame at 00:00:01 (1 second mark) as JPEG.
     * Used as thumbnail/representative image for the video.
     *
     * @param videoFile the video file to extract from
     * @param outputDirectory the path where the frame will be saved
     * @throws RuntimeException if FFmpeg execution fails
     */
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

    /**
     * Converts a HEIC/HEIF image file to JPEG format.
     *
     * HEIC is Apple's modern image format. This conversion ensures compatibility
     * with broader image processing tools and APIs.
     *
     * @param inputFile the HEIC/HEIF image file to convert
     * @param outputPath the path where the converted JPEG will be saved
     * @return File object pointing to the converted JPEG file
     * @throws RuntimeException if FFmpeg execution fails
     */
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
