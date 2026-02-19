module com.javafxdemo.first_midterm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.javafxdemo.first_midterm to javafx.fxml;
    exports com.javafxdemo.first_midterm;
}