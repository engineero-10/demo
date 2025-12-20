package org.example.demo.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    private ImageView close;
    @FXML
    private StackPane contentArea;
     @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        close.setOnMouseClicked(e->System.exit(0));
        try{
            Parent fxml = FXMLLoader.load(getClass().getResource("/org/example/demo/employee-view.fxml"));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().addAll(fxml);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
     }
    public void employee(ActionEvent event){
        Parent fxml = null;
        try {
            fxml = FXMLLoader.load(getClass().getResource("/org/example/demo/employee-view.fxml"));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().addAll(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void project(ActionEvent event){
        Parent fxml = null;
        try {
            fxml = FXMLLoader.load(getClass().getResource("/org/example/demo/project-view.fxml"));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().addAll(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void about(ActionEvent event){
        Parent fxml = null;
        try {
            fxml = FXMLLoader.load(getClass().getResource("/org/example/demo/about-view.fxml"));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().addAll(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   public void setting(ActionEvent event){
        Parent fxml = null;
        try {
            fxml = FXMLLoader.load(getClass().getResource("/org/example/demo/setting-view.fxml"));
            contentArea.getChildren().removeAll();
            contentArea.getChildren().addAll(fxml);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
