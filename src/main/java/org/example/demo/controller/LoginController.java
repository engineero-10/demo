package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.demo.DAO.AdminDao;
import org.example.demo.util.ErrorHandler;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private AnchorPane container;

    public void handleLogin() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Please enter fields.");
            errorLabel.setVisible(true);
            return;
        }

        try {
            if (AdminDao.checkAdmin(user, pass)) {
                goToDashboard();
            } else {
                errorLabel.setText("Invalid Username or Password.");
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setVisible(true);
            }
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Login Error",
                    "An error occurred during login",
                    e.getMessage()
            );
        }
    }

    public void goToDashboard() {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource("/org/example/demo/main-view.fxml")
            );

            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.setScene(new Scene(root));
            newStage.centerOnScreen();
            newStage.show();

            Stage oldStage = (Stage) loginButton.getScene().getWindow();
            oldStage.close();

        } catch (IOException e) {
            ErrorHandler.showErrorWithException(
                    "Navigation Error",
                    "Failed to open dashboard",
                    e
            );
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred",
                    e.getMessage()
            );
        }
    }

    public void openRegisterPage() {
        try {
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/register.fxml"));
            stage.setScene(new Scene(root));
            stage.setTitle("Register New Account");
        } catch (IOException e) {
            ErrorHandler.showErrorWithException(
                    "Navigation Error",
                    "Failed to open registration page",
                    e
            );
        } catch (Exception e) {
            ErrorHandler.showError(
                    "Unexpected Error",
                    "An unexpected error occurred",
                    e.getMessage()
            );
        }
    }
}