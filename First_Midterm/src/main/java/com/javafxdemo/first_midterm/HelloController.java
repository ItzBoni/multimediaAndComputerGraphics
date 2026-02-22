package src.main.java.com.javafxdemo.first_midterm;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import src.main.java.com.javafxdemo.first_midterm.utils.FileHandler;
import src.main.java.com.javafxdemo.first_midterm.utils.ImageTransformer;
import src.main.java.com.javafxdemo.first_midterm.utils.ImageTransformerTool;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController {
    private BufferedImage image;
    private ImageTransformer imageTransformer;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onWelcomeButtonClick(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        image = FileHandler.importImage(currentStage);

        // 2. ONLY initialize the tool if an image was actually selected
        if (image != null) {
            imageTransformer = new ImageTransformerTool(this.image);
            System.out.println("Image loaded and Transformer initialized!");
        }
    }
}