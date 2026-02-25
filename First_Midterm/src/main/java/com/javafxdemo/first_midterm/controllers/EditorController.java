package com.javafxdemo.first_midterm.controllers;

import com.javafxdemo.first_midterm.utils.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;

public class EditorController {
    private ImageTransformer imageTransformer;
    private MainController father;

    @FXML TextField initial_x;
    @FXML TextField initial_y;
    @FXML TextField final_x;
    @FXML TextField final_y;
    @FXML TextField angle;
    @FXML Button rotate;
    @FXML Button cut;
    @FXML Button invertColors;
    @FXML Button saveImage;
    @FXML Button backToMenu;
    @FXML ImageView imageViewer;
    @FXML StackPane imageContainer;
    @FXML Label error;
    @FXML Label imageSize;

    //Setters
    public void setImageTransformer(ImageTransformer imgTransformer){
        this.imageTransformer = imgTransformer;

        //Assigns the iamge to the viewport after initialization
        if (this.imageTransformer != null) {
            updateImage();
        }
    }

    public void setMainController(MainController father){
        this.father = father;
    }

    //Image display logic
    private Image convertToFxImage(BufferedImage image){
        return SwingFXUtils.toFXImage(image,null);
    }

    private void updateImage(){
        int height = this.imageTransformer.getResultImage().getHeight();
        int width = this.imageTransformer.getResultImage().getWidth();
        String size = "Image resolution: "+width + "px, " + height +"px";

        this.imageViewer.setImage(convertToFxImage(this.imageTransformer.getResultImage()));
        imageSize.setText(size);
        error.setText("");
    }

    //Implementation of required editing functionality
    @FXML
    protected void onRotateButtonClick(){
        try {
            //Tries to parse the input into
            int x1 = Integer.parseInt(this.initial_x.getText());
            int y1 = Integer.parseInt(this.initial_y.getText());
            int x2 = Integer.parseInt(this.final_x.getText());
            int y2 = Integer.parseInt(this.final_y.getText());
            double angleInput = Double.parseDouble(this.angle.getText());

            //Executes the function
            if(angleInput % 90 == 0.0) {
                imageTransformer.rotate(x1, y1, x2, y2, angleInput);
                updateImage();
            } else {
                error.setText("Input a valid angle");
            }

        }catch (NumberFormatException e){
            error.setText("Please input a number :)");
        } catch (InvalidImageDimensionsException e){
            error.setText("Input valid image coordinates");
        }
    }

    @FXML
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
        }catch (InvalidImageDimensionsException e){
            error.setText("Input valid image coordinates");
        }
    }

    @FXML
    protected void onInvertButtonClick(){
        imageTransformer.invertColors();
        updateImage();
    }

    @FXML
    protected void onSaveButtonClick(ActionEvent event){
        Node source = (Node) event.getSource();
        Stage currentStage = (Stage) source.getScene().getWindow();
        FileHandler.saveImage(this.imageTransformer.getResultImage(), currentStage);
    }

    @FXML
    protected void onBackToMenuButtonClick(){
        father.navigateToView(0);
    }
}
