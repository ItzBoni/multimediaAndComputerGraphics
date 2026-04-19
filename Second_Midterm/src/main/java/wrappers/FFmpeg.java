package wrappers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FFmpeg {
    public static void saveRepresentativeFrame(String directory){
        ProcessBuilder pb = new ProcessBuilder("ffmpeg", directory);
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
            throw new RuntimeException(e);
        }

        String answer = response.toString();
    }
}
