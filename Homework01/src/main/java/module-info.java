module com.javafxdemo.calculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.javafxdemo.calculator to javafx.fxml;
    exports com.javafxdemo.calculator;
}