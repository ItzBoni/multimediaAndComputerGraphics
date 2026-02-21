package src.main.java.com.javafxdemo.first_midterm.utils;

import java.awt.image.BufferedImage;

public interface ImageTransformer {
    public BufferedImage invertColors(BufferedImage image);
    public BufferedImage cut(BufferedImage image);
    public BufferedImage rotate(BufferedImage image);
}
