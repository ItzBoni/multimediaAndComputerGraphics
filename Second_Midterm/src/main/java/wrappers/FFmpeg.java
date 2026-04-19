package wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFmpeg {
    public static void saveRepresentativeFrame(File videoFile, String outputDirectory){
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

        String answer = response.toString();

        System.out.println(answer);
    }
}
