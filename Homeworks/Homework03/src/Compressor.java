import org.jetbrains.annotations.NotNull;
import utils.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Compressor {
    private final BufferedImage image;
    private Set<Color> colors;
    private HashMap<Color, Integer> colorIndices;
    private String filename;
    ArrayList<Integer> compressedIndices;

    //Constructor and setters for maximum encapsulation
    public Compressor(@NotNull BufferedImage image, @NotNull String filename){
        this.image = image;
        setColorSets(new HashSet<Color>());
        setColorIndices(new HashMap<Color, Integer>());
        setFilename(filename);
        setCompressedIndices(new ArrayList<>());
    }

    private void setFilename(String filename){ this.filename = filename;   }

    private void setColorSets(Set<Color> colors){ this.colors = colors; }

    private void setColorIndices(HashMap<Color, Integer> colorIndices){ this.colorIndices = colorIndices; }

    private void setCompressedIndices(ArrayList<Integer> compressedIndices){ this.compressedIndices = compressedIndices; }

    public void compressImage(){
        //compressionMask is part of my solution as a lossy compression algorithm.
        //To be used with crushColor method

        //SETTINGS:
        // 0xFF = Lossless
        // 0xF0 = High Quality (16 colors per channel)
        // 0xE0 = Retro Look (8 colors per channel)

        int compressionMask = 0xF0;

        //Add each unique color to a set
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++){
                int pixelVal = image.getRGB(x,y);
                Color crushed = BitShifter.crushColor(pixelVal, compressionMask);

                this.colors.add(crushed);
            }
        }

        //Create a palette (maps a color to a specific number in a map)
        Color[] colorArray = this.colors.toArray(new Color[colors.size()]);
        for (int i = 0; i < colorArray.length; i++) {
            colorIndices.put(colorArray[i], i);
        }

        //Calculate bit depth needed (depending on the amount of colors present in the image)
        int paletteSize = colorArray.length;
        int bitsPerPixel = (int) Math.max(1, Math.ceil(Math.log(paletteSize) / Math.log(2)));

        //Transform each pixel into an index
        this.compressedIndices = new ArrayList<>();
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++){
                int pixelVal = image.getRGB(x,y);
                Color crushed = BitShifter.crushColor(pixelVal, compressionMask);
                int index = colorIndices.get(crushed);

                compressedIndices.add(index);
            }
        }

        //PACK BITS THIS IS THE MOST IMPORTANT STEP DON'T FAIL ON ME PLS
        byte[] binaryData = BitShifter.packIndices(compressedIndices, bitsPerPixel);

        //Construct final file
        FileHandler.saveCompressedImage(this.filename, this.image.getWidth(), this.image.getHeight(), colorArray, binaryData);
    }
}
