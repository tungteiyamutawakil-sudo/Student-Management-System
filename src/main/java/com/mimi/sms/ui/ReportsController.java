package com.mimi.sms.ui;

import com.mimi.domain.Student;
import com.mimi.repository.SQLiteStudentRepository;
import com.mimi.service.ReportService;
import com.mimi.service.StudentService;
import com.mimi.util.AppLogger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Map;

public class ReportsController {

    private final StudentService studentService =
            new StudentService(new SQLiteStudentRepository());

    private final ReportService reportService = new ReportService();

    @FXML
    private ComboBox<String> reportTypeBox;

    @FXML
    private TextField thresholdField;

    @FXML
    private TableView<Object[]> reportTable;

    @FXML
    private TableColumn<Object[], String> col1;

    @FXML
    private TableColumn<Object[], String> col2;

    @FXML
    private TableColumn<Object[], String> col3;

    private List<Object[]> currentReportData;

    @FXML
    public void initialize() {

        reportTypeBox.setItems(FXCollections.observableArrayList(
                "Top Performers",
                "At Risk Students",
                "GPA Distribution",
                "Programme Summary"
        ));

        col1.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue()[0].toString()));

        col2.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue()[1].toString()));

        col3.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().length > 2 ? data.getValue()[2].toString() : ""));
    }

    @FXML
    private void handleGenerate() {

        String selected = reportTypeBox.getValue();

        if (selected == null) {
            showAlert("Select a report type.");
            return;
        }

        List<Student> students = studentService.getAllStudents();

        switch (selected) {

            case "Top Performers" -> generateTopPerformers(students);

            case "At Risk Students" -> generateAtRisk(students);

            case "GPA Distribution" -> generateGpaDistribution(students);

            case "Programme Summary" -> generateProgrammeSummary(students);
        }
    }

    private void generateTopPerformers(List<Student> students) {

        List<Student> top = reportService.topPerformers(students, 10);

        col1.setText("Student ID");
        col2.setText("Full Name");
        col3.setText("GPA");

        currentReportData = top.stream()
                .map(s -> new Object[]{
                        s.getStudentId(),
                        s.getFullName(),
                        s.getGpa()
                })
                .toList();

        reportTable.setItems(FXCollections.observableArrayList(currentReportData));
    }


    private void generateAtRisk(List<Student> students) {

        double threshold = 2.0;

        try {
            if (!thresholdField.getText().isBlank()) {
                threshold = Double.parseDouble(thresholdField.getText());
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid GPA threshold. Using default 2.0");
        }

        List<Student> atRisk = reportService.atRiskStudents(students, threshold);

        col1.setText("Student ID");
        col2.setText("Full Name");
        col3.setText("GPA");

        currentReportData = atRisk.stream()
                .map(s -> new Object[]{
                        s.getStudentId(),
                        s.getFullName(),
                        s.getGpa()
                })
                .toList();

        reportTable.setItems(FXCollections.observableArrayList(currentReportData));
    }


    private void generateGpaDistribution(List<Student> students) {

        Map<String, Long> distribution =
                reportService.gpaDistribution(students);

        col1.setText("GPA Band");
        col2.setText("Number of Students");
        col3.setText("");

        currentReportData = distribution.entrySet().stream()
                .map(e -> new Object[]{
                        e.getKey(),
                        e.getValue()
                })
                .toList();

        reportTable.setItems(FXCollections.observableArrayList(currentReportData));
    }


    private void generateProgrammeSummary(List<Student> students) {

        Map<String, Long> count =
                reportService.programmeCount(students);

        Map<String, Double> avg =
                reportService.programmeAverageGpa(students);

        col1.setText("Programme");
        col2.setText("Total Students");
        col3.setText("Average GPA");

        currentReportData = count.entrySet().stream()
                .map(e -> new Object[]{
                        e.getKey(),
                        e.getValue(),
                        String.format("%.2f", avg.get(e.getKey()))
                })
                .toList();

        reportTable.setItems(FXCollections.observableArrayList(currentReportData));
    }


    @FXML
    private void handleExport() {

        if (currentReportData == null || currentReportData.isEmpty()) {
            showAlert("Generate a report first.");
            return;
        }

        try (java.io.BufferedWriter writer =
                     new java.io.BufferedWriter(
                             new java.io.FileWriter("data/report_export.csv"))) {

            writer.write(col1.getText() + "," + col2.getText() + "," + col3.getText());
            writer.newLine();

            for (Object[] row : currentReportData) {

                String line = java.util.Arrays.stream(row)
                        .map(Object::toString)
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");

                writer.write(line);
                writer.newLine();
            }

            showAlert("Report exported successfully.");

            AppLogger.log("Report exported to data/report_export.csv");

        } catch (Exception e) {
            showAlert("Export failed: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
