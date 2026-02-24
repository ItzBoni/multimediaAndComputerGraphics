package com.javafxdemo.first_midterm.controllers;

import com.javafxdemo.first_midterm.utils.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class EditorController {
    ImageTransformer imageTransformer;
    MainController father;

    @FXML TextField initial_x;
    @FXML TextField initial_y;
    @FXML TextField final_x;
    @FXML TextField final_y;
    @FXML Button rotate;
    @FXML Button cut;
    @FXML Button invertColors;
    @FXML ImageView imageViewer = new ImageView();
    @FXML Label error;

    //Setters
    public void setImageTransformer(ImageTransformer imgTransformer){
        this.imageTransformer = imgTransformer;
    }

    public void setMainController(MainController father){
        this.father = father;
    }

    //Image display logic
    private Image convertToFxImage(BufferedImage image){
        return SwingFXUtils.toFXImage(image,null);
    }

    private void updateImage(){
        this.imageViewer.setImage(convertToFxImage(this.imageTransformer.getResultImage()));
    }

    //Implementation of required editing functionality
    protected void onRotateButtonClick(){
        try {
            int x1 = Integer.parseInt(this.initial_x.getText());
            int y1 = Integer.parseInt(this.initial_y.getText());
            int x2 = Integer.parseInt(this.final_x.getText());
            int y2 = Integer.parseInt(this.final_y.getText());

            //imageTransformer.rotate(x1, y1, x2, y2, );
            updateImage();
        }catch (NumberFormatException e){
            error.setText("Please input an Integer :)");
        }
    }

    protected void onCutButtonClick(){
        try {
            int x1 = Integer.parseInt(this.initial_x.getText());
            int y1 = Integer.parseInt(this.initial_y.getText());
            int x2 = Integer.parseInt(this.final_x.getText());
            int y2 = Integer.parseInt(this.final_y.getText());

            imageTransformer.cut(x1, y1, x2, y2);

            updateImage();
        }catch (NumberFormatException e){
            error.setText("Please input an Integer :)");
        }
    }

    protected void onInvertButtonClick(){
        imageTransformer.invertColors();
        updateImage();
    }
}
