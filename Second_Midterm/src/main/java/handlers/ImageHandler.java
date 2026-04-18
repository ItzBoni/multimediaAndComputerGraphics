package handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public class ImageHandler implements FileHandler {

    private BufferedImage image;

    @Override
    public boolean loadFromFile(File source) {
        if (source == null) return false;
        try {
            image = ImageIO.read(source);
            return image != null;
        } catch (IOException e) {
            System.err.println("Failed to load image: " + e.getMessage());
            return false;
        }
    }

    @Override
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

    @Override
    public void decodeFromBase64(String base64) {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            image = ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            System.err.println("Decoding failed: " + e.getMessage());
        }
    }

    @Override
    public boolean saveToFile(File destination) {
        if (image == null || destination == null) return false;
        try {
            ImageIO.write(image, "jpg", destination);
            return true;
        } catch (IOException e) {
            System.err.println("Save failed: " + e.getMessage());
            return false;
        }
    }

    public BufferedImage getImage()           { return image; }
    public void setImage(BufferedImage image) { this.image = image; }
}