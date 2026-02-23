package com.javafxdemo.first_midterm.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
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
    private StackPane contentArea;

    @FXML
    public void initialize() throws IOException{
        //Load initial view controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
        views.put(0, loader.load());
        InitialMenuController initController = loader.getController();
        this.imageTransformer = initController.getImageTransformer();
        initController.setMainController(this);

        //Load editor view
        loader = new FXMLLoader(getClass().getResource("/editor.fxml"));
        views.put(1, loader.load());
        EditorController editor = loader.getController();
        editor.setMainController(this);
    }
}