import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import utils.FileHandler;

import javax.imageio.ImageIO;

public class Homework03 {
    public static void main(String[] args) throws IOException {
        BufferedImage image = FileHandler.readImage();
        Compressor comp = null;
        String imageName = "compressed_image";

        if (image != null){
            comp = new Compressor(image, imageName);
            comp.compressImage();
        }
    }
}
