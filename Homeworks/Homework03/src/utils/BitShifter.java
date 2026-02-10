package utils;

import java.awt.*;
import java.util.ArrayList;

public class BitShifter {
    public static Color crushColor(int rgb, int mask) {
        // Extract components using standard bit shifting
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        //Apply the mask to crush the info
        r &= mask;
        g &= mask;
        b &= mask;

        //Return the clean, quantized color
        return new Color(r, g, b);
    }
}