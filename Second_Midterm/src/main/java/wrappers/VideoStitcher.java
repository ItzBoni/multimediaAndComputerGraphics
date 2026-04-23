package wrappers;

import models.MediaItem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoStitcher {

    public static void stitch(ArrayList<MediaItem> visualMedia, String directory, File audioFile) {
        File outputDir = new File(directory);
        if (!outputDir.exists()) outputDir.mkdirs();

        // Compute how many seconds each item should occupy
        double totalSeconds  = Double.parseDouble(getVideoDuration(audioFile.getAbsolutePath()));
        double durationEach  = totalSeconds / visualMedia.size();

        // 1. Create one silent clip per item
        for (int i = 0; i < visualMedia.size(); i++) {
            createTempFile(visualMedia.get(i), directory, i, durationEach);
        }

        // 2. Concat silent clips → slideshow.mp4
        File silentVideo = new File(outputDir, "slideshow.mp4");
        File concatList  = buildMediaList(visualMedia, directory);

        executeCommand(new ArrayList<>(Arrays.asList(
                "ffmpeg", "-loglevel", "error",
                "-f", "concat", "-safe", "0",
                "-i", concatList.getAbsolutePath(),
                "-c:v", "copy", "-an",
                "-y", silentVideo.getAbsolutePath()
        )), "Slideshow Concat");

        // 3. Merge slideshow + single audio → final_voiced.mp4
        File outputFile = new File(outputDir, "final_voiced.mp4");

        executeCommand(new ArrayList<>(Arrays.asList(
                "ffmpeg", "-loglevel", "error",
                "-i", silentVideo.getAbsolutePath(),
                "-i", audioFile.getAbsolutePath(),
                "-c:v", "copy",
                "-c:a", "aac", "-b:a", "128k", "-ar", "48000", "-ac", "2",
                "-shortest", "-y",
                outputFile.getAbsolutePath()
        )), "Audio Merge");

        // 4. Cleanup
        for (int j = 0; j < visualMedia.size(); j++) {
            new File(outputDir, "temp_" + j + "_voiced.mp4").delete();
        }
        concatList.delete();
        silentVideo.delete();
    }

    private static void createTempFile(MediaItem item, String directory, int counter, double duration) {
        File outputDir  = new File(directory);
        File outputFile = new File(outputDir, "temp_" + counter + "_voiced.mp4");

        // For images use the file directly; for videos use the file directly (trimmed to duration)
        String input = item.isVideo()
                ? item.getFile().getAbsolutePath()
                : (item.getRepresentativeFrame() != null
                        ? item.getRepresentativeFrame().getAbsolutePath()
                        : item.getFile().getAbsolutePath());

        List<String> command = new ArrayList<>();
        if (!item.isVideo()) {
            // -loop 1 turns a still image into an infinite video stream
            command.addAll(Arrays.asList("ffmpeg", "-loglevel", "error", "-loop", "1", "-i", input));
        } else {
            command.addAll(Arrays.asList("ffmpeg", "-loglevel", "error", "-i", input));
        }

        command.addAll(Arrays.asList(
                "-t", String.valueOf(duration),
                "-vf", "scale=1080:1920:force_original_aspect_ratio=decrease,pad=1080:1920:(ow-iw)/2:(oh-ih)/2:black,format=yuv420p",
                "-c:v", "libx264",
                "-preset", "ultrafast",
                "-force_key_frames", "expr:gte(t,n_forced*2)",
                "-an",   // silent — audio is added in a single pass later
                "-y",
                outputFile.getAbsolutePath()
        ));

        executeCommand(command, "Clip #" + counter);
    }

    private static String getVideoDuration(String videoPath) {
        try {
            Process process = new ProcessBuilder(
                    "ffprobe", "-v", "error", "-show_entries", "format=duration",
                    "-of", "csv=p=0", videoPath
            ).start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line = reader.readLine();
                if (line != null && !line.isEmpty()) {
                    try {
                        double duration = Double.parseDouble(line.trim());
                        return String.valueOf((int) Math.ceil(duration));
                    } catch (NumberFormatException e) {
                        System.err.println("Failed to parse duration: " + line);
                    }
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to get video duration: " + e.getMessage());
        }
        return "10";
    }

    private static void executeCommand(List<String> command, String taskName) {
        try {
            Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println("[" + taskName + "]: " + line);
                }
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) throw new RuntimeException(taskName + " failed with code " + exitCode);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static File buildMediaList(ArrayList<MediaItem> visualMedia, String directory) {
        File outputDir = new File(directory);
        File concatList = new File(outputDir, "concat_list.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(concatList))) {
            for (int j = 0; j < visualMedia.size(); j++) {
                File tempFile = new File(outputDir, "temp_" + j + "_voiced.mp4");
                String path = tempFile.getAbsolutePath().replace("\\", "/");
                writer.write("file '" + path + "'");
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return concatList;
    }
}