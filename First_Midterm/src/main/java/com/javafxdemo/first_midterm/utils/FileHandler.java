package com.javafxdemo.first_midterm.utils;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileHandler implements Handlers{
    private static final FileChooser fileChooser = new FileChooser();
    //Static method to input an image into the GUI
    public static BufferedImage importImage(Window stage){
        BufferedImage image = null;

        fileChooser.setTitle("Select Image File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                image = ImageIO.read(selectedFile);
            } catch (IOException e) {
                System.err.println("Error loading image: " + e.getMessage());
            }
        }

        return image;
    }

    //Method to save the edited image
    public static String saveImage(BufferedImage image, Window stage){
        File imageLocation = fileChooser.showSaveDialog(stage);

        if (imageLocation != null){
            try{
                ImageIO.write(image, "jpg", imageLocation);
                return "Your image is in this directory: "+imageLocation;
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
