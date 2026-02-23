package com.javafxdemo.first_midterm.utils;
import java.awt.image.BufferedImage;
import java.awt.*;

public class ImageTransformerTool implements ImageTransformer{
    //Saves the edits into a new BufferedImage so the user can keep editing
    private BufferedImage resultImage;

    //Constructor and setter(s)
    public ImageTransformerTool(BufferedImage image){
        //Creates a copy of the image so we can manipulate an entirely new file.
        setResultImage(new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB));
        java.awt.Graphics g = this.resultImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
    }

    private void setResultImage(BufferedImage image){
        this.resultImage = image;
    }

    public BufferedImage getResultImage(){
        return this.resultImage;
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
    public void cut(int x1, int y1, int x2, int y2){
        BufferedImage temp = this.resultImage.getSubimage(x1,y1,(x2-x1),(y2-y1));

        this.resultImage = new BufferedImage(temp.getWidth(), temp.getHeight(), BufferedImage.TYPE_INT_RGB);
    }

    @Override
    public void rotate(int x1, int y1, int x2, int y2, double theta){
        int w = x2-x1;
        int h = y2-y1;

        //Stores what we're rotating in a temporary variable
        BufferedImage temp = this.resultImage.getSubimage(x1,y1,w,h);

        //Paints the background of what's left black
        for (int x = x1; x < x2; x++){
            for (int y = y1; y < y2; y++){
                this.resultImage.setRGB(x, y, Color.black.getRGB());
            }
        }

        // We use Math.round to handle floating point precision
        int turns = (int) Math.round(theta / (Math.PI / 2)) % 4;
        if (turns < 0) turns += 4;

        //Create the rotated section
        int newW = (turns % 2 == 0) ? w : h;
        int newH = (turns % 2 == 0) ? h : w;
        BufferedImage rotatedSection = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        //Perform the swap
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rgb = temp.getRGB(x, y);
                if (turns == 1) { // 90 degrees
                    rotatedSection.setRGB(h - 1 - y, x, rgb);
                } else if (turns == 2) { // 180 degrees
                    rotatedSection.setRGB(w - 1 - x, h - 1 - y, rgb);
                } else if (turns == 3) { // 270 degrees
                    rotatedSection.setRGB(y, w - 1 - x, rgb);
                }
            }
        }

        //Overwrite the pixels in the new image
        java.awt.Graphics2D g2d = this.resultImage.createGraphics();
        g2d.drawImage(rotatedSection, x1, y1, null);
        g2d.dispose();
    }
}
