package com.javafxdemo.first_midterm.controllers;

import com.javafxdemo.first_midterm.utils.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;

public class InitialMenuController {
    MainController father;
    ImageTransformer imageTransformer;
    BufferedImage initialImage;

    //Non FXML methods here:
    public ImageTransformer getImageTransformer(){
        return this.imageTransformer;
    }

    public void setMainController(MainController father){
        this.father = father;
    }

    @FXML
    Button uploadButton;

    @FXML
    private Label uploadText;

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
}
