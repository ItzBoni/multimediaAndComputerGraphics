import utils.BitShifter; // Make sure this is imported!

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

public class Decompressor {

    public static BufferedImage decompressImage(String filename) {
        // Use try-with-resources to automatically close the file
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filename))) {

            // ==========================================
            // 1. Read Header Info
            // ==========================================
            int width = dis.readInt();
            int height = dis.readInt();
            int paletteSize = dis.readInt();

            // ==========================================
            // 2. Read the Palette
            // ==========================================
            Color[] palette = new Color[paletteSize];
            for (int i = 0; i < paletteSize; i++) {
                // Read 3 bytes (Red, Green, Blue)
                // & 0xFF ensures we treat the byte as unsigned (0-255)
                int r = dis.readByte() & 0xFF;
                int g = dis.readByte() & 0xFF;
                int b = dis.readByte() & 0xFF;
                palette[i] = new Color(r, g, b);
            }

            // ==========================================
            // 3. Read the Compressed Data Body
            // ==========================================
            int dataLength = dis.readInt();
            byte[] compressedData = new byte[dataLength];
            dis.readFully(compressedData); // Reads exactly 'dataLength' bytes

            // ==========================================
            // 4. Unpack the Bits
            // ==========================================
            // We recalculate bitsPerPixel using the exact same math as the Compressor
            int bitsPerPixel = (int) Math.max(1, Math.ceil(Math.log(paletteSize) / Math.log(2)));
            int totalPixels = width * height;

            // Call your BitPacker utility to turn the bytes back into integers
            int[] indices = BitShifter.unpackIndices(compressedData, totalPixels, bitsPerPixel);

            // ==========================================
            // 5. Reconstruct the Image
            // ==========================================
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int currentIndex = 0;

            // CRITICAL: We must match the loop order of your Compressor!
            // Your compressor did: for(x) { for(y) }
            // So we must also do:  for(x) { for(y) }
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++){

                    // 1. Get the palette index for this pixel
                    int colorIndex = indices[currentIndex++];

                    // 2. Look up the real Color
                    Color c = palette[colorIndex];

                    // 3. Paint the pixel
                    image.setRGB(x, y, c.getRGB());
                }
            }

            System.out.println("Decompression successful!");
            return image;

        } catch (IOException e) {
            System.err.println("Error reading compressed file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}