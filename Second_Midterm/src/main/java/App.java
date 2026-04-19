import api.AICaller;
import handlers.ConversionHandler;
import models.MediaItem;

import java.io.File;

public class App {
    public static void main(String[] args) {
        MediaItem item = new MediaItem("C:/Users/luchy/Videos/videochistoso.mp4");

        File fileToEncode = item.isVideo() ? item.getRepresentativeFrame() : item.getFile();

        // 3. Encode to Base64
        String base64 = ConversionHandler.encodeToBase64(fileToEncode);

        // 4. Send to ChatGPT
        AICaller ai = new AICaller();
        String description = ai.descriptionRequest(base64);
        item.setDescription(description);

        byte[] tts = ai.audioRequest(description);
        String image = ai.imageRequest(description);

        File audioFile = new File("C:/Users/luchy/Audio/output.mp3");
        audioFile.getParentFile().mkdirs(); // Creates "Audio" folder if missing

        File imageFile = new File("C:/Users/luchy/Images/output.jpg");
        imageFile.getParentFile().mkdirs(); // Creates "Images" folder if missing

        File decodedFile = ConversionHandler.decodeFromBase64(tts, audioFile);
        File decodedImage = ConversionHandler.decodeFromBase64(image, imageFile);
    }
}
