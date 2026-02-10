package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {
    public static void saveCompressedImage(String imgName, int width, int height, ArrayList<Color> colorArray){
        // Try-with-resources ensures the file closes automatically
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int index = 0;

        for(int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                if(index < colorArray.size()){
                    Color c = colorArray.get(index);
                    image.setRGB(x,y,c.getRGB());
                    index++;
                }
            }
        }

        String directory = "./Homeworks/Homework03/public/" + imgName + ".jpg";

        File outputImage = new File(directory);
        try{
            ImageIO.write(image, "jpg",outputImage);
            System.out.println("Your compressed image is in: " + directory);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage readImage() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Input the image filepath:");

        String filepath = sc.nextLine();

        // 1. CLEAN THE INPUT
        // Remove double quotes (common if copying paths) and trim whitespace
        filepath = filepath.replace("\"", "").trim();

        System.out.println("Attempting to read from: [" + filepath + "]"); // DEBUG PRINT

        BufferedImage image = null;

        try {
            File file = new File(filepath);

            // 2. VERIFY FILE EXISTENCE
            if (!file.exists()) {
                System.err.println("ERROR: File not found at that path.");
                System.err.println("Check for typos or hidden file extensions.");
                return null;
            }

            image = ImageIO.read(file);

            if (image != null) {
                System.out.println("Image loaded successfully! (Width: " + image.getWidth() + ")");
            } else {
                System.err.println("ERROR: File exists, but it is not a valid image format.");
            }
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        return image;
    }
}
