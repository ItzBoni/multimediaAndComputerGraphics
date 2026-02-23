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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafxdemo/first_midterm/initial-menu.fxml"));
        views.put(0, loader.load());
        InitialMenuController initController = loader.getController();
        if (initController != null) {
            initController.setMainController(this);
        }

        //Load editor view
        loader = new FXMLLoader(getClass().getResource("/com/javafxdemo/first_midterm/editor.fxml"));
        views.put(1, loader.load());
        EditorController editor = loader.getController();
        if (editor != null) {
            editor.setMainController(this);
        }

        addAllNodesToRoot();
        navigateToView(0);
    }

    @FXML void addAllNodesToRoot(){
        for(Node n: views.values()){
            contentArea.getChildren().add(n);
        }
    }

    @FXML public void navigateToView(Integer view){
        for(Integer i : this.views.keySet()){
            if (i != view){
                this.views.get(i).setVisible(false);
                this.views.get(i).setManaged(false);
                continue;
            }

            this.views.get(i).setVisible(true);
            this.views.get(i).setManaged(true);
        }
    }
}