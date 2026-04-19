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
        String description = ai.getDescription(base64);

        item.setDescription(description);
    }
}
