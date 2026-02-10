import org.jetbrains.annotations.NotNull;
import utils.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Compressor {
    private final BufferedImage image;
    private ArrayList<Color> colors;
    private String filename;

    //Constructor and setters for maximum encapsulation
    public Compressor(@NotNull BufferedImage image, @NotNull String filename){
        this.image = image;
        setColorSets(new ArrayList<Color>());
        setFilename(filename);
    }

    private void setFilename(String filename){ this.filename = filename;   }

    private void setColorSets(ArrayList<Color> colors){ this.colors = colors; }

    public void compressImage(){
        //compressionMask is part of my solution as a lossy compression algorithm.
        //To be used with crushColor method

        //SETTINGS:
        // 0xFF = Lossless
        // 0xF0 = High Quality (16 colors per channel)
        // 0xE0 = Retro Look (8 colors per channel)

        int compressionMask = 0xF0;

        //Squash the colors and add to an ArrayList
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++){
                int pixelVal = image.getRGB(x,y);
                Color crushed = BitShifter.crushColor(pixelVal, compressionMask);

                this.colors.add(crushed);
            }
        }

        //Construct final file
        FileHandler.saveCompressedImage(this.filename, this.image.getWidth(), this.image.getHeight(), this.colors);
    }
}
