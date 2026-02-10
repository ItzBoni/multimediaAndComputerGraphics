import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.*;

public class ImageGenerator {
    public static void generateImage(BufferedImage image, String imgName) {
        String directory = "./Classwork02/public/" + imgName + ".png";

        File outputImage = new File(directory);
        try{
            ImageIO.write(image, "png",outputImage);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
