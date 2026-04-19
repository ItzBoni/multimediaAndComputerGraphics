package handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class ConversionHandler {

    private BufferedImage image;

    public String encodeToBase64() {
        if (image == null) return null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            System.err.println("Encoding failed: " + e.getMessage());
            return null;
        }
    }

    public void decodeFromBase64(String base64) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            image = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            System.err.println("Decoding failed: " + e.getMessage());
        }
    }

    public BufferedImage getImage()           { return image; }
    public void setImage(BufferedImage image) { this.image = image; }
}