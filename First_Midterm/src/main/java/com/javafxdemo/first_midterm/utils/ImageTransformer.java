package com.javafxdemo.first_midterm.utils;

import java.awt.image.BufferedImage;

public interface ImageTransformer {
    public void invertColors();
    public void cut(int x1, int y1, int x2, int y2);
    public void rotate(int x1, int y1, int x2, int y2, double theta);
}
