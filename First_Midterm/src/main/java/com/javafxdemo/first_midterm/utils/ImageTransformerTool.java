package src.main.java.com.javafxdemo.first_midterm.utils;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class ImageTransformerTool implements ImageTransformer{
    //Saves the edits into a new BufferedImage so the user can keep editing
    private BufferedImage resultImage;

    //Constructor and setter(s)
    public ImageTransformerTool(@NotNull BufferedImage image){
        setResultImage(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB));
        java.awt.Graphics g = this.resultImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private void setResultImage(BufferedImage image){
        this.resultImage = image;
    }

    //Overrides methods from ImageTransformer. Better code quality here :3
    @Override
    public void invertColors(){
        for (int x  = 0; x < this.resultImage.getWidth(); x++){
            for (int y = 0; y < this.resultImage.getHeight(); y++){
                //Separate every color channel of the pixel
                int rgba = this.resultImage.getRGB(x, y);
                int a = (rgba >> 24) & 0xFF;
                int r = (rgba >> 16) & 0xFF;
                int g = (rgba >> 8) & 0xFF;
                int b = rgba & 0xFF;

                //Math to invert the color of the image
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                // Recompose the pixel
                int invertedRgba = (a << 24) | (r << 16) | (g << 8) | b;
                this.resultImage.setRGB(x, y, invertedRgba);
            }
        }
    }

    @Override
    public void cut(){
    }

    @Override
    public void rotate(){

    }
}
