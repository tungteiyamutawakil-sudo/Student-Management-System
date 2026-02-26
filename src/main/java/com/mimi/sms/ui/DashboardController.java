package com.mimi.sms.ui;

import com.mimi.domain.Student;
import com.mimi.repository.SQLiteStudentRepository;
import com.mimi.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DashboardController {

    private final StudentService service =
            new StudentService(new SQLiteStudentRepository());

    @FXML
    private Label totalLabel;

    @FXML
    private Label averageGpaLabel;

    @FXML
    private Label activeLabel;

    @FXML
    private Label inactiveLabel;

    @FXML
    private ListView<String> topStudentsList;

    @FXML
    private BarChart<String, Number> gpaChart;

    @FXML
    private PieChart programmeChart;

    @FXML
    public void initialize() {

        totalLabel.setText(String.valueOf(service.getTotalStudents()));
        averageGpaLabel.setText(String.format("%.2f", service.getAverageGpa()));
        activeLabel.setText(String.valueOf(service.getActiveStudents()));
        inactiveLabel.setText(String.valueOf(service.getInactiveStudents()));

        loadTopStudents();
        loadGpaChart();
        loadProgrammeChart();
    }

    @FXML
    private void handleRefresh() {
        totalLabel.setText(String.valueOf(service.getTotalStudents()));
        averageGpaLabel.setText(String.format("%.2f", service.getAverageGpa()));
        activeLabel.setText(String.valueOf(service.getActiveStudents()));
        inactiveLabel.setText(String.valueOf(service.getInactiveStudents()));

        topStudentsList.getItems().clear();
        gpaChart.getData().clear();

        loadTopStudents();
        loadGpaChart();
        loadProgrammeChart();
    }

    private void loadTopStudents() {

        for (Student s : service.getTopStudents(5)) {
            topStudentsList.getItems()
                    .add(s.getFullName() + " (" + s.getGpa() + ")");
        }
    }

    private void loadGpaChart() {

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("GPA");

        for (Student s : service.getTopStudents(5)) {
            series.getData().add(
                    new XYChart.Data<>(s.getFullName(), s.getGpa()));
        }

        gpaChart.getData().add(series);
    }

    private void loadProgrammeChart() {

        programmeChart.getData().clear();

        var distribution = service.getProgrammeDistribution();

        for (var entry : distribution.entrySet()) {
            programmeChart.getData().add(
                    new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }


}
