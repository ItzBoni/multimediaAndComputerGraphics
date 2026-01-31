import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.*;

public class ImageGenerator {
    private String imgName;



    public void generateImage(BufferedImage image) {
        String directory = "./Classwork01/public/" + imgName + ".jpg";

        File outputImage = new File(directory);
        try{
            ImageIO.write(image, "jpg",outputImage);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
