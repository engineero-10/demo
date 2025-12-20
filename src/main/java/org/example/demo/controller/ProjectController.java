package org.example.demo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.demo.DAO.ProjectDao;
import org.example.demo.model.Project;

import java.net.URL;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {

    @FXML
    private TableView<Project> projectTable;

    @FXML
    private TableColumn<Project, Integer> projectIdCol;

    @FXML
    private TableColumn<Project, String> projectNameCol;

    @FXML
    private TableColumn<Project, String> descriptionCol;

    @FXML
    private TableColumn<Project, String> timeEstimationCol;

    @FXML
    private TableColumn<Project, Void> functionsCol;

    @FXML
    private TextField searchField;

    private final ObservableList<Project> projectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        projectIdCol.setCellValueFactory(new PropertyValueFactory<>("projectId"));
        projectNameCol.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        timeEstimationCol.setCellValueFactory(new PropertyValueFactory<>("timeEstimation"));

        addButtonsToTable();
        loadProjects();

        projectTable.setItems(projectList);
    }

    private void loadProjects() {
        projectList.clear();
        projectList.addAll(ProjectDao.getAllProjects());
    }

    // ================= Buttons Column =================
    private void addButtonsToTable() {
        functionsCol.setCellFactory(param -> new TableCell<>() {

            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox box = new HBox(10, editBtn, deleteBtn);
            {
                editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");
                box.setAlignment(Pos.CENTER);

                editBtn.setOnAction(e -> {
                    Project project = getTableView().getItems().get(getIndex());
                    openEditDialog(project);
                });

                deleteBtn.setOnAction(e -> {
                    Project project = getTableView().getItems().get(getIndex());
                    deleteProject(project);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    // ================= Add Project =================
    @FXML
    private void handleAddProject() {

        Stage dialog = createDialog("Add Project");

        TextField nameField = new TextField();
        TextField descField = new TextField();
        TextField timeField = new TextField();

        nameField.setPromptText("Project Name");
        descField.setPromptText("Description");
        timeField.setPromptText("Time Estimation");

        Button save = new Button("Save");
        Button cancel = new Button("Cancel");

        save.setOnAction(e -> {
            boolean added = ProjectDao.addProject(
                    nameField.getText(),
                    descField.getText(),
                    timeField.getText()
            );

            if (added) {
                loadProjects();
                dialog.close();
            } else {
                showAlert("Error", "Failed to add project");
            }
        });
        save.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
        cancel.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");

        cancel.setOnAction(e -> dialog.close());

        VBox root = createForm(save, cancel, nameField, descField, timeField);
        dialog.setScene(new Scene(root, 400, 300));
        dialog.showAndWait();
    }

    // ================= Edit =================
    private void openEditDialog(Project project) {

        Stage dialog = createDialog("Edit Project");

        TextField nameField = new TextField(project.getProjectName());
        TextField descField = new TextField(project.getDescription());
        TextField timeField = new TextField(project.getTimeEstimation());

        Button save = new Button("Update");
        save.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");

        Button cancel = new Button("Cancel");
        cancel.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");

        save.setOnAction(e -> {
            boolean updated = ProjectDao.updateProject(
                    project.getProjectId(),
                    nameField.getText(),
                    descField.getText(),
                    timeField.getText()
            );

            if (updated) {
                loadProjects();
                dialog.close();
            } else {
                showAlert("Error", "Update failed");
            }
        });

        cancel.setOnAction(e -> dialog.close());

        VBox root = createForm(save, cancel, nameField, descField, timeField);
        dialog.setScene(new Scene(root, 400, 300));
        dialog.showAndWait();
    }

    // ================= Delete =================
    private void deleteProject(Project project) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Delete " + project.getProjectName() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (ProjectDao.deleteProject(project.getProjectId())) {
                loadProjects();
            }
        }
    }

    // ================= Search =================
    @FXML
    private void handleSearch() {
        try {
            int id = Integer.parseInt(searchField.getText());
            Project project = ProjectDao.getProjectById(id);

            projectList.clear();
            if (project != null) {
                projectList.add(project);
            }
        } catch (Exception e) {
            loadProjects();
        }
    }

    // ================= Helpers =================
    private Stage createDialog(String title) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);
        return stage;
    }

    private VBox createForm(Button save, Button cancel, TextField... fields) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-padding:20");

        box.getChildren().addAll(fields);
        box.getChildren().add(new HBox(10, save, cancel));

        return box;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}
