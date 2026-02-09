import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.ArrayList;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //Stores all images inside an arraylist for easier printing of all at the same time.
        ArrayList<BufferedImage> imagesArray = new ArrayList<BufferedImage>();
        for (int i = 0; i < 3; i++) {
            imagesArray.add(new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB));
        }

        //Code for diagonal flag (idk how this is called)
        for (int y = 0; y<imagesArray.get(0).getHeight(); y++){
            for (int x = 0; x<imagesArray.get(0).getWidth(); x++){
                if(y <= (int)x*3/4){
                    imagesArray.get(0).setRGB(x,y,Color.red.getRGB());
                } else {
                    imagesArray.get(0).setRGB(x,y,Color.blue.getRGB());
                }

            }
        }

        //Clock assignment
        float radius = (float) imagesArray.get(1).getWidth()/4;;
        float smallRadius = (float) imagesArray.get(1).getWidth()/5;
        float height = (float) imagesArray.get(1).getHeight() /2;
        float width = (float) imagesArray.get(1).getWidth()/2;
        int hour = 10;
        int minute = 5;

        //Code for the clock's circle
        for (double theta = 0; theta < 360; theta+=(1/radius)){
            double thetaRadians = Math.toRadians(theta);
            int x = (int) (width + radius*Math.cos(thetaRadians));
            int y = (int) (height + radius*Math.sin(thetaRadians));

            imagesArray.get(1).setRGB(x,y,Color.white.getRGB());
        }

        //Code for the dots in the circle
        for (int theta = 0; theta < 360; theta++){
            double thetaRadians = Math.toRadians(theta);
            int smallX = (int) (width + smallRadius*Math.cos(thetaRadians));
            int smallY = (int) (height + smallRadius*Math.sin(thetaRadians));
            if (theta % 30 == 0) {
                imagesArray.get(1).setRGB(smallX,smallY,Color.white.getRGB());
            }
        }

        //To set up the time given the variables above.
        float hourRadius = (float) imagesArray.get(1).getWidth()/8;
        float minuteRadius = (float) imagesArray.get(1).getWidth()/5;
        double hourAngle = Math.toRadians( -90 + ((hour%12)*30));
        double minuteAngle = Math.toRadians( -90 + ((minute%60)*6));

        //Drawing the hours
        for (int r = 0; r < hourRadius; r++) {
            int x = (int) (width + r*Math.cos(hourAngle));
            int y = (int) (height + r*Math.sin(hourAngle));

            imagesArray.get(1).setRGB(x,y,Color.white.getRGB());
        }

        //Drawing the minutes
        for (int r = 0; r < minuteRadius; r++) {
            int x = (int) (width + r*Math.cos(minuteAngle));
            int y = (int) (height + r*Math.sin(minuteAngle));

            imagesArray.get(1).setRGB(x,y,Color.white.getRGB());
        }


        //Code for the landscape

        //Background and grass
        for (int y = 0; y<imagesArray.get(2).getHeight(); y++){
            for (int x = 0; x<imagesArray.get(2).getWidth(); x++){

                //Checks with sine function to draw grass.
                if ( y > (15*Math.sin(0.1*x) + imagesArray.get(2).getHeight()/1.3)) {
                    imagesArray.get(2).setRGB(x,y,Color.green.getRGB());
                } else {
                    imagesArray.get(2).setRGB(x,y,Color.white.getRGB());
                }
            }
        }

        //Code for sun
        height = (float) imagesArray.get(2).getHeight()/5;
        width = (float) imagesArray.get(2).getWidth()/6;

        //Big sun rays
        for (int theta = 0; theta<360; theta+=90) {
            double thetaRadians = Math.toRadians( (double) theta);

            for (int r = 0; r<height; r++){
                int x = (int) (width + r*Math.cos(thetaRadians));
                int y = (int) (height + r*Math.sin(thetaRadians));

                imagesArray.get(2).setRGB(x,y,Color.red.getRGB());
            }
        }

        //Small sun rays
        for (int theta = 45; theta<360; theta+=90) {
            double thetaRadians = Math.toRadians( (double) theta);

            for (int r = 0; r<height; r++){
                int x = (int) (width + r*Math.cos(thetaRadians));
                int y = (int) (height + r*Math.sin(thetaRadians));

                imagesArray.get(2).setRGB(x,y,Color.red.getRGB());
            }
        }

        //Sun circle
        for (int theta = 0; theta < 360; theta++){
            double thetaRadians = Math.toRadians( (double) theta);
            for (int r = 0; r < imagesArray.get(2).getHeight()/10; r++){
                int x = (int) (width + r*Math.cos(thetaRadians));
                int y = (int) (height + r*Math.sin(thetaRadians));

                imagesArray.get(2).setRGB(x,y,Color.yellow.getRGB());
            }
        }

        //Code block to print all three images at the same time
        for (int i = 0; i < 3; i++) {
            String directory = "./Classwork01/public/img" + i + ".jpg";

            File outputImage = new File(directory);
            try{
                ImageIO.write(imagesArray.get(i), "jpg",outputImage);
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}