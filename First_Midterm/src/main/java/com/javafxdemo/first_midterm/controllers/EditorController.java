package com.javafxdemo.first_midterm.controllers;

import com.javafxdemo.first_midterm.utils.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

public class EditorController {
    ImageTransformer imageTransformer;
    MainController father;

    public void setImageTransformer(ImageTransformer imgTransformer){
        this.imageTransformer = imgTransformer;
    }

    public void setMainController(MainController father){
        this.father = father;
    }

    public Image convertToFxImage(BufferedImage image){
        return SwingFXUtils.toFXImage(image,null);
    }

    @FXML
    public void initialize(){
        this.imageTransformer = father.getImageTransformer();
    }
}
