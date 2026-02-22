module com.javafxdemo.first_midterm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    //requires org.kordamp.bootstrapfx.core;

    opens com.javafxdemo.first_midterm to javafx.fxml;
    exports com.javafxdemo.first_midterm;
    exports com.javafxdemo.first_midterm.controllers;
    opens com.javafxdemo.first_midterm.controllers to javafx.fxml;
}