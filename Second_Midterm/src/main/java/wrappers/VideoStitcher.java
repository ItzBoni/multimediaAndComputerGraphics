package wrappers;

import handlers.ConversionHandler;
import models.MediaItem;

import java.io.*;
import java.util.ArrayList;

/**
 * Responsible for assembling individual media items into a final stitched video.
 * Kept separate from FFmpeg, which only wraps single-file operations.
 */
public class VideoStitcher {

    public static void stitch(ArrayList<MediaItem> visualMedia, String directory) {
        for (int i = 0; i < visualMedia.size(); i++) {
            createTempFile(visualMedia.get(i), directory, i);
        }

        String outputVideo = directory + "final_voiced.mp4";
        File concatList = buildMediaList(visualMedia, directory);

        String videoFilter = "scale=1080:1920:force_original_aspect_ratio=decrease," +
                "pad=1080:1920:(ow-iw)/2:(oh-ih)/2:black";

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-f", "concat",
                "-safe", "0",
                "-i", concatList.getAbsolutePath(),
                "-vf", videoFilter,
                "-af", "loudnorm=I=-15:TP=-1.5:LRA=7",
                "-c:v", "libx264",
                "-c:a", "aac",
                "-y",
                outputVideo
        );

        pb.redirectErrorStream(true);
        StringBuilder response = new StringBuilder();

        try {
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to stitch final video");
            throw new RuntimeException(e);
        } finally {
            for (int j = 0; j < visualMedia.size(); j++) {
                new File(directory + "temp_" + j + "_voiced.mp4").delete();
            }
            concatList.delete();
        }
    }

    private static void createTempFile(MediaItem visualMedia, String directory, int counter) {
        byte[] tempAudio     = visualMedia.getAudio();
        File tempAudioFile   = new File(directory + "temp_" + counter + ".mp3");
        ConversionHandler.decodeByteResponse(tempAudio, tempAudioFile);

        String inputVideo  = visualMedia.getFile().getAbsolutePath();
        String outputVideo = directory + "temp_" + counter + "_voiced.mp4";

        ProcessBuilder pb = new ProcessBuilder(
                "ffmpeg",
                "-i", inputVideo,
                "-i", tempAudioFile.getAbsolutePath(),
                "-map", "0:v",
                "-map", "1:a",
                "-c:v", "copy",
                "-c:a", "aac",
                "-shortest",
                "-y",
                outputVideo
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
            System.out.println("Failed to generate temp video file");
            throw new RuntimeException(e);
        } finally {
            tempAudioFile.delete();
        }
    }

    private static File buildMediaList(ArrayList<MediaItem> visualMedia, String directory) {
        File concatList = new File(directory + "concat_list.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(concatList))) {
            for (int j = 0; j < visualMedia.size(); j++) {
                writer.write("file '" + directory + "temp_" + j + "_voiced.mp4'");
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Failed to write concat list");
            throw new RuntimeException(e);
        }
        return concatList;
    }
}
