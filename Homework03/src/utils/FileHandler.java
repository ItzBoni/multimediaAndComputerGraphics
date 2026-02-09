package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
    public static void saveCompressedImage(String filename, int width, int height, Color[] palette, byte[] compressedData){
        // Try-with-resources ensures the file closes automatically
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(filename))) {

            // --- SECTION 1: THE HEADER ---
            // We write integers (4 bytes each)
            dos.writeInt(width);           // 1. Width
            dos.writeInt(height);          // 2. Height
            dos.writeInt(palette.length);  // 3. Number of Colors (Crucial for decoding!)

            // --- SECTION 2: THE PALETTE ---
            // We loop through the palette and write 3 bytes (R, G, B) for each color.
            for (Color c : palette) {
                // writeByte writes the lower 8 bits of the integer
                dos.writeByte(c.getRed());
                dos.writeByte(c.getGreen());
                dos.writeByte(c.getBlue());
                // Note: If you have Alpha (transparency), write it here too!
            }

            // --- SECTION 3: THE DATA ---
            // Optional: Write the length of the data array first (helps safety)
            dos.writeInt(compressedData.length);

            // Write the actual byte array
            dos.write(compressedData);

            System.out.println("Saved successfully to " + filename);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Function to read the compressed file and return it to an image.
    public void readCompressedImage(String filename) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(filename))) {

            // 1. Read Header
            int width = dis.readInt();
            int height = dis.readInt();
            int paletteSize = dis.readInt();

            // 2. Read Palette
            ArrayList<Color> palette = new ArrayList<>();
            for (int i = 0; i < paletteSize; i++) {
                // Read 3 bytes. Convert to unsigned int (0-255) using & 0xFF
                int r = dis.readByte() & 0xFF;
                int g = dis.readByte() & 0xFF;
                int b = dis.readByte() & 0xFF;
                palette.add(new Color(r, g, b));
            }

            // 3. Read Data
            int dataLength = dis.readInt();
            byte[] compressedData = new byte[dataLength];
            dis.readFully(compressedData); // Fills the array completely

            // Now you have everything needed to run your Unpacker!
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Method to grab the image.
    public static BufferedImage readImage(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Input the image filepath");

        String filepath = sc.nextLine();
        BufferedImage image = null;

        try {
            File file = new File(filepath);
            image = ImageIO.read(file);

            // Check if image loaded successfully
            if (image != null) {
                System.out.println("Image loaded successfully!");
            }
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        return image;
    }
}
