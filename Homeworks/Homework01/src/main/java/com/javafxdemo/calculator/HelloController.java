package com.javafxdemo.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private ChoiceBox<String> shapeSelector;
    private String[] shapes = {"Square", "Rectangle", "Triangle", "Circle", "Pentagon", "Pentagram", "Semi-Circle"};

    @FXML
    private Label areaText, perimeterText, inputName1, inputName2;

    @FXML
    private TextField input1, input2;

    @FXML
    private HBox firstInputSection, secondInputSection;

    @FXML
    protected void onSolutionButtonClick() {
        String selected = shapeSelector.getValue();
        ShapeController calc = new ShapeController();
        String input1Value = input1.getText();
        String input2Value = input2.getText();

        try {
            double double1 = Double.parseDouble(input1Value);
            double double2;
            switch (selected){
                case "Square":
                    calc.calculateSquare(double1);
                    break;

                case "Rectangle":
                    double2 =Double.parseDouble(input2Value);
                    calc.calculateRect(double1, double2);
                    break;

                case "Triangle":
                    double2 =Double.parseDouble(input2Value);
                    calc.calculateTriangle(double1,double2);
                    break;

                case "Circle":
                    calc.calculateCircle(double1);
                    break;

                case "Pentagon":
                    double2 =Double.parseDouble(input2Value);
                    calc.calculatePentagon(double2, double1);
                    break;

                case "Pentagram":
                    calc.calculatePentagram(double1);
                    break;

                case "Semi-Circle":
                    calc.calculateSemiCircle(double1);
            }

            double area = calc.getArea();
            double perimeter = calc.getPerimeter();

            perimeterText.setVisible(true);

            areaText.setText("Area is: " + area);
            perimeterText.setText("Perimeter is: "+ perimeter);

        } catch (NumberFormatException e) {
            // Handle the exception if the string cannot be parsed
            System.err.println("Invalid input: " + input1Value + input2Value);
            areaText.setText("Try with a real number :)");
            perimeterText.setVisible(false);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1){
      shapeSelector.getItems().addAll(shapes);
      shapeSelector.setOnAction(this::displayOptions);
      firstInputSection.setVisible(false);
      secondInputSection.setVisible(false);
    }

    private void displayOptions(ActionEvent event){
        String selected = shapeSelector.getValue();
        firstInputSection.setVisible(true);

        switch (selected) {
            case "Square":
                secondInputSection.setVisible(false);
                inputName1.setText("Side");
                break;

            case "Rectangle":
                secondInputSection.setVisible(true);
                inputName1.setText("Base");
                inputName2.setText("Height");
                break;

            case "Triangle":
                secondInputSection.setVisible(true);
                inputName1.setText("Base");
                inputName2.setText("Height");
                break;

            case "Circle":
                secondInputSection.setVisible(false);
                inputName1.setText("Radius");
                break;

            case "Pentagon":
                secondInputSection.setVisible(true);
                inputName1.setText("Side");
                inputName2.setText("Apothem");
                break;

            case "Pentagram":
                secondInputSection.setVisible(false);
                inputName1.setText("Side");
                break;

            case "Semi-Circle":
                secondInputSection.setVisible(false);
                inputName1.setText("Radius");
                break;
        }
    }
}