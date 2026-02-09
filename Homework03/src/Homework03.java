import java.awt.image.BufferedImage;
import utils.FileHandler;

public class Homework03 {
    public static void main(String[] args) {
        BufferedImage image = FileHandler.readImage();

        if (image != null){ Compressor comp = new Compressor(image, "../public/compressed.santi"); }
    }
}
