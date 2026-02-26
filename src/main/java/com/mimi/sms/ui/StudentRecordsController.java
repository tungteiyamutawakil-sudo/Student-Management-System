package com.mimi.sms.ui;


import com.mimi.domain.Student;
import com.mimi.repository.SQLiteStudentRepository;
import com.mimi.service.StudentService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;


public class StudentRecordsController {

    private final StudentService service =
            new StudentService(new SQLiteStudentRepository());

    @FXML
    private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> programmeColumn;
    @FXML private TableColumn<Student, Integer> levelColumn;
    @FXML private TableColumn<Student, Number> gpaColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> phoneColumn;
    @FXML private TableColumn<Student, String> statusColumn;

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStudentId()));

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFullName()));

        programmeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProgramme()));

        levelColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(data.getValue().getLevel()));

        emailColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmail()));

        phoneColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPhone()));

        statusColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStatus().name()));

        // GPA with progress bar
        gpaColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getGpa()));

        gpaColumn.setCellFactory(column -> new TableCell<>() {

            private final ProgressBar bar = new ProgressBar();
            private final Label label = new Label();
            private final HBox box = new HBox(5, bar, label);

            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);

                if (empty || value == null) {
                    setGraphic(null);
                } else {
                    double gpa = value.doubleValue();

                    bar.setProgress(gpa / 5.0); // GPA 0–5
                    bar.setPrefWidth(100);
                    label.setText(String.format("%.2f", gpa));

                    if (gpa < 2.0) {
                        bar.setStyle("-fx-accent: red;");
                    } else if (gpa < 3.5) {
                        bar.setStyle("-fx-accent: orange;");
                    } else {
                        bar.setStyle("-fx-accent: green;");
                    }

                    setGraphic(box);
                }
            }
        });

        studentsTable.setItems(
                FXCollections.observableArrayList(service.getAllStudents()));
    }

    @FXML
    private void openReports() throws Exception {
        Stage stage = new Stage();
        stage.setScene(new Scene(
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                        "/com/mimi/sms/ui/ReportsView.fxml")))));
        stage.setTitle("Reports");
        stage.show();
    }

    @FXML
    private void openDashboard() throws Exception {
        Stage stage = new Stage();
        stage.setScene(new Scene(
                FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                        "/com/mimi/sms/ui/DashboardView.fxml")))));
        stage.setTitle("Dashboard");
        stage.show();
    }

}