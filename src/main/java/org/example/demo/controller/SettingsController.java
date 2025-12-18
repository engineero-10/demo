package org.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.demo.util.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class SettingsController {

    @FXML
    private Slider fontSlider;
    @FXML
    private Button btnLogout;
    @FXML
    private Label dbStatusLabel;

    // تغيير حجم الخط في المشروع كله
    public void changeFontSize() {
        double size = fontSlider.getValue();
        Stage stage = (Stage) fontSlider.getScene().getWindow();
        stage.getScene().getRoot().setStyle(
            "-fx-font-size: " + size + "px;"
        );
    }

    // فحص اتصال الداتابيز
    public void checkDatabase() {
        try {
            Connection conn = DBConnection.getConnection();

            if (conn != null) {
                dbStatusLabel.setText("Database Connected ✅");
                dbStatusLabel.setStyle("-fx-text-fill: lightgreen;");
                conn.close();
            }

        } catch (Exception e) {
            dbStatusLabel.setText("Database Not Connected ❌");
            dbStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    public void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/org/example/demo/login.fxml")
            );

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.centerOnScreen();
            newStage.show();
            Stage oldStage = (Stage) btnLogout.getScene().getWindow();
            oldStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}