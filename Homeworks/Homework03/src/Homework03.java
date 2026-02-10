import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import utils.FileHandler;

import javax.imageio.ImageIO;

public class Homework03 {
    public static void main(String[] args) throws IOException {
        BufferedImage image = FileHandler.readImage();
        Compressor comp = null;
        String filepath = "C:/Users/luchy/OneDrive/Desktop/MULTIMEDIA AND COMPUTER GRAPHICS/Homeworks/Homework03/publicgamer.santi";

        if (image != null){
            comp = new Compressor(image, filepath);
            comp.compressImage();
        }

        BufferedImage recoveredImage = Decompressor.decompressImage(filepath);

        // 3. Verify / Show
        if (recoveredImage != null) {
            // Pass it to your GUI or save it as png to verify
            ImageIO.write(recoveredImage, "PNG", new File("verify.PNG"));
        }
    }
}
