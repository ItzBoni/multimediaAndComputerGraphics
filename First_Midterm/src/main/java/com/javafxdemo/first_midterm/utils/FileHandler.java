package src.main.java.com.javafxdemo.first_midterm.utils;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileHandler implements Handlers{
    //Method to input an image into the GUI
    public static BufferedImage importImage(Window stage){
        BufferedImage image = null;
        FileChooser fileChooser = new FileChooser();

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
    public static void saveImage(BufferedImage image, Window stage){

    }
}
