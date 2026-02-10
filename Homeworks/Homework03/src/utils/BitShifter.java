package utils;

import java.awt.*;
import java.util.ArrayList;

public class BitShifter {
    public static Color crushColor(int rgb, int mask) {
        // 1. Extract components using standard bit shifting
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb) & 0xFF;

        // 2. Apply the mask (The "Crushing" step)
        // Example: 11110101 & 11110000 = 11110000
        r &= mask;
        g &= mask;
        b &= mask;

        // 3. Return the clean, quantized color
        return new Color(r, g, b);
    }
}