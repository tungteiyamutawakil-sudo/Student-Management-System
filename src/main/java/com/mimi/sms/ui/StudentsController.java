package com.mimi.sms.ui;

import com.mimi.domain.Student;
import com.mimi.repository.SQLiteStudentRepository;
import com.mimi.service.StudentService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class StudentsController {

    private final StudentService service =
            new StudentService(new SQLiteStudentRepository());

    private String userRole;

    @FXML
    private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    public void setUserRole(String role) {
        this.userRole = role;

        if ("LECTURER".equals(role)) {
            disableEditingFeatures();
        }
    }

    private void disableEditingFeatures() {
        addButton.setDisable(true);
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    // ================= TABLE =================
    @FXML private TableView<Student> studentsTable;
    @FXML private TableColumn<Student, String> idColumn;
    @FXML private TableColumn<Student, String> nameColumn;
    @FXML private TableColumn<Student, String> programmeColumn;
    @FXML private TableColumn<Student, Integer> levelColumn;
    @FXML private TableColumn<Student, Number> gpaColumn;
    @FXML private TableColumn<Student, String> emailColumn;
    @FXML private TableColumn<Student, String> phoneColumn;
    @FXML private TableColumn<Student, String> statusColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> programmeFilter;
    @FXML private ComboBox<Integer> levelFilter;
    @FXML private ComboBox<String> statusFilter;

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentId()));

        nameColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));

        programmeColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getProgramme()));

        levelColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getLevel()));

        gpaColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getGpa()));

        gpaColumn.setCellFactory(column -> new TableCell<>() {

            private final ProgressBar bar = new ProgressBar();
            private final Label label = new Label();
            private final HBox container = new HBox(5, bar, label);

            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);

                if (empty || value == null) {
                    setGraphic(null);
                } else {

                    double gpa = value.doubleValue();

                    bar.setProgress(gpa / 5.0); // 0–5 scale
                    bar.setPrefWidth(100);

                    label.setText(String.format("%.2f", gpa));

                    if (gpa < 2.0) {
                        bar.setStyle("-fx-accent: red;");
                    } else if (gpa < 3.5) {
                        bar.setStyle("-fx-accent: orange;");
                    } else {
                        bar.setStyle("-fx-accent: green;");
                    }

                    setGraphic(container);
                }
            }
        });

        studentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        emailColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));

        phoneColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getPhone()));

        statusColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus().name()));

        programmeFilter.setItems(FXCollections.observableArrayList("CS", "IT", "Engineering"));
        levelFilter.setItems(FXCollections.observableArrayList(100,200,300,400,500,600,700));
        statusFilter.setItems(FXCollections.observableArrayList("ACTIVE", "INACTIVE"));

        searchField.textProperty().addListener((obs, oldVal, newVal) -> handleSearch());
        programmeFilter.valueProperty().addListener((obs, oldVal, newVal) -> handleSearch());
        levelFilter.valueProperty().addListener((obs, oldVal, newVal) -> handleSearch());
        statusFilter.valueProperty().addListener((obs, oldVal, newVal) -> handleSearch());

        loadStudents();
    }

    private void loadStudents() {
        studentsTable.setItems(
                FXCollections.observableArrayList(service.getAllStudents()));
    }

    @FXML
    private void handleSearch() {

        String keyword = searchField.getText();
        String programme = programmeFilter.getValue();
        Integer level = levelFilter.getValue();
        String status = statusFilter.getValue();

        List<Student> filtered = service.searchStudents(keyword, programme, level, status);

        studentsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void handleReset() {

        searchField.clear();

        programmeFilter.getSelectionModel().clearSelection();
        levelFilter.getSelectionModel().clearSelection();
        statusFilter.getSelectionModel().clearSelection();

        programmeFilter.setButtonCell(new ListCell<>());
        levelFilter.setButtonCell(new ListCell<>());
        statusFilter.setButtonCell(new ListCell<>());

        studentsTable.getSortOrder().clear();

        loadStudents();
    }


    @FXML
    private void handleSortGpa() {
        var sorted = service.getAllStudents().stream()
                .sorted((a,b) -> Double.compare(b.getGpa(), a.getGpa()))
                .toList();

        studentsTable.setItems(FXCollections.observableArrayList(sorted));
    }

    @FXML
    private void handleSortName() {
        var sorted = service.getAllStudents().stream()
                .sorted((a,b) -> a.getFullName().compareToIgnoreCase(b.getFullName()))
                .toList();

        studentsTable.setItems(FXCollections.observableArrayList(sorted));
    }

    @FXML
    private void handleDelete() {

        Student selected = studentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a student to delete.");
            return;
        }

        service.deleteStudent(selected.getStudentId());
        loadStudents();
    }

    @FXML
    private void handleAdd() {

        StudentFormDialog dialog = new StudentFormDialog(null);

        dialog.showAndWait().ifPresent(student -> {
            try {
                service.addStudent(student);
                loadStudents();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    @FXML
    private void handleEdit() {

        Student selected = studentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("No Selection", "Please select a student to edit.");
            return;
        }

        StudentFormDialog dialog = new StudentFormDialog(selected);

        dialog.showAndWait().ifPresent(updated -> {
            try {
                service.updateStudent(updated);
                loadStudents();
            } catch (Exception e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    @FXML
    private void openReports() throws Exception {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/mimi/sms/ui/ReportsView.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Reports");
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void openDashboard() throws Exception {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/com/mimi/sms/ui/DashboardView.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Dashboard");
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getUserRole() {
        return userRole;
    }
}
