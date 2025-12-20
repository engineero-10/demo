package org.example.demo.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import org.example.demo.DAO.AdminDao;
import org.example.demo.DAO.EmployeeDao;
import org.example.demo.model.EmployeeWithProjects;
import javafx.geometry.Insets;

public class EmployeeController {

    @FXML private TableView<EmployeeWithProjects> table;
    @FXML private TableColumn<EmployeeWithProjects, Integer> colId;
    @FXML private TableColumn<EmployeeWithProjects, String> colName;
    @FXML private TableColumn<EmployeeWithProjects, String> colEmail;
    @FXML private TableColumn<EmployeeWithProjects, String> colPosition;
    @FXML private TableColumn<EmployeeWithProjects, Integer> colProId;
    @FXML private TableColumn<EmployeeWithProjects, String> colProject;
    @FXML private TableColumn<EmployeeWithProjects, String> colStartDate;
    @FXML private TableColumn<EmployeeWithProjects, Void> colActions;
    @FXML private TextField searchIdField;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadData();
    }

    private boolean isInputValid(String name, String email, String position) {
        String errorMessage = "";

        if (name == null || name.trim().isEmpty()) {
            errorMessage += "Name cannot be empty!\n";
        } else if (!name.matches("^[a-zA-Z\\s]+$")) {
            errorMessage += "Name must contain letters only (no numbers)!\n";
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (email == null || email.trim().isEmpty()) {
            errorMessage += "Email cannot be empty!\n";
        } else if (!email.matches(emailRegex)) {
            errorMessage += "Invalid Email format (e.g., name@example.com)!\n";
        }

        if (position == null || position.trim().isEmpty()) {
            errorMessage += "Position cannot be empty!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    @FXML
    void addEmployee(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Employee");
        dialog.setHeaderText("Add a New Employee to the System");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField emailField = new TextField();
        emailField.setPromptText("example@mail.com");
        TextField positionField = new TextField();
        positionField.setPromptText("Position");

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Position:"), 0, 2);
        grid.add(positionField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        final Button btSave = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btSave.addEventFilter(ActionEvent.ACTION, ae -> {
            if (!isInputValid(nameField.getText(), emailField.getText(), positionField.getText())) {
                ae.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButtonType) {
                boolean success = EmployeeDao.addEmployee(
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        positionField.getText().trim()
                );

                if (success) {
                    loadData();
                }
            }
        });
    }

    // Method جديد لتعيين موظف في مشروع
    @FXML
    void assignEmployeeToProject(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Assign Employee to Project");
        dialog.setHeaderText("Enter Employee ID and Project ID");

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField empIdField = new TextField();
        empIdField.setPromptText("Employee ID");
        TextField proIdField = new TextField();
        proIdField.setPromptText("Project ID");

        grid.add(new Label("Employee ID:"), 0, 0);
        grid.add(empIdField, 1, 0);
        grid.add(new Label("Project ID:"), 0, 1);
        grid.add(proIdField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        final Button btAssign = (Button) dialog.getDialogPane().lookupButton(assignButtonType);
        btAssign.addEventFilter(ActionEvent.ACTION, ae -> {
            String empIdText = empIdField.getText().trim();
            String proIdText = proIdField.getText().trim();

            if (empIdText.isEmpty() || proIdText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("Both fields are required!");
                alert.showAndWait();
                ae.consume();
                return;
            }

            try {
                Integer.parseInt(empIdText);
                Integer.parseInt(proIdText);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("IDs must be numbers!");
                alert.showAndWait();
                ae.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == assignButtonType) {
                try {
                    int empId = Integer.parseInt(empIdField.getText().trim());
                    int proId = Integer.parseInt(proIdField.getText().trim());

                    boolean success = AdminDao.assignEmployeeInProject(empId, proId);

                    if (success) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Employee assigned to project successfully!");
                        alert.showAndWait();
                        loadData();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Failed to assign employee to project!");
                        alert.setContentText("Please check if the Employee ID and Project ID are valid.");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("An error occurred!");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
        });
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        colProId.setCellValueFactory(new PropertyValueFactory<>("projectId"));
        colProject.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(editBtn, deleteBtn);

            {
                pane.setSpacing(10);
                editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");

                deleteBtn.setOnAction(e -> {
                    EmployeeWithProjects emp = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete: " + emp.getName() + "?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            if (EmployeeDao.deleteEmployee(emp.getEmployeeId())) {
                                loadData();
                            }
                        }
                    });
                });

                editBtn.setOnAction(e -> {
                    EmployeeWithProjects emp = getTableView().getItems().get(getIndex());
                    showEditDialog(emp);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void showEditDialog(EmployeeWithProjects emp) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Employee");
        dialog.setHeaderText("Update details for: " + emp.getName());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(emp.getName());
        TextField emailField = new TextField(emp.getEmail());
        TextField positionField = new TextField(emp.getPosition());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Position:"), 0, 2);
        grid.add(positionField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        final Button btOk = (Button) dialog.getDialogPane().lookupButton(saveButtonType);
        btOk.addEventFilter(ActionEvent.ACTION, event -> {
            if (!isInputValid(nameField.getText(), emailField.getText(), positionField.getText())) {
                event.consume();
            }
        });

        dialog.showAndWait().ifPresent(response -> {
            if (response == saveButtonType) {
                boolean success = EmployeeDao.updateEmployee(
                        emp.getEmployeeId(),
                        nameField.getText().trim(),
                        emailField.getText().trim(),
                        positionField.getText().trim()
                );
                if (success) loadData();
            }
        });
    }

    private void loadData() {
        ObservableList<EmployeeWithProjects> masterData = FXCollections.observableArrayList(EmployeeDao.getEmployeeWithProjects());
        FilteredList<EmployeeWithProjects> filteredData = new FilteredList<>(masterData, p -> true);

        searchIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(emp -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String employeeId = String.valueOf(emp.getEmployeeId());
                return employeeId.contains(newVal);
            });
        });

        SortedList<EmployeeWithProjects> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }
}