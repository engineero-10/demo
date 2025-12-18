package org.example.demo.controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.DAO.AdminDao;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    public void handleRegister() {
        String name = fullNameField.getText();
        String email = emailField.getText();
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || user.isEmpty() || pass.isEmpty()) {
            showError("All fields are required!");
            return;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            showError("Invalid Email Format! (ex: user@mail.com)");
            return;
        }

        boolean isSuccess = AdminDao.addAdmin(name, email, user, pass);

        if (isSuccess) {
            statusLabel.setText("Success! Go to Login.");
            statusLabel.setStyle("-fx-text-fill: green;");
            statusLabel.setVisible(true);
            usernameField.setText("");
            fullNameField.setText("");
            passwordField.setText("");
            passwordField.setText("");
        } else {
            showError("Username already exists!");
        }
    }

    @FXML
    public void backToLogin() throws IOException {
        Stage stage = (Stage) fullNameField.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/demo/login.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.setTitle("Login");
    }

    private void showError(String msg) {
        statusLabel.setText(msg);
        statusLabel.setStyle("-fx-text-fill: red;");
        statusLabel.setVisible(true);
    }
}