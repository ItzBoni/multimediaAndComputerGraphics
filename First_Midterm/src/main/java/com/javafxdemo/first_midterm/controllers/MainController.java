package com.javafxdemo.first_midterm.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import com.javafxdemo.first_midterm.utils.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class MainController {
    private BufferedImage initialImage; //Initial image that user uploads
    private BufferedImage resultImage; // Result image to send back to the user
    private ImageTransformer imageTransformer; //Instanced as a member of the interface
    private HashMap<Integer, Node> views = new HashMap<>();

    @FXML
    private Label uploadText;

    @FXML
    private Button uploadButton;

    @FXML
    protected void onUploadButtonClick(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        initialImage = FileHandler.importImage(currentStage);

        // 2. ONLY initialize the tool if an image was actually selected
        if (initialImage != null) {
            imageTransformer = new ImageTransformerTool(this.initialImage);
            System.out.println("Image loaded and Transformer initialized!");
            uploadText.setText("Image loaded and Transformer initialized");
        }

        uploadButton.setVisible(false);
    }

    @FXML
    public void initialize() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
        views.put(0, loader.load());

        loader = new FXMLLoader();
    }
}