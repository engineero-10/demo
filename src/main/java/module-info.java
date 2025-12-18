module org.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    opens org.example.demo to javafx.fxml;
    opens org.example.demo.model to javafx.fxml;
    exports org.example.demo;
    exports org.example.demo.model;
    exports org.example.demo.controller;
    opens org.example.demo.controller to javafx.fxml;
}