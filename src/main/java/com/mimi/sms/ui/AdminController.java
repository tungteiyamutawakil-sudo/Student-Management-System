package com.mimi.sms.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class AdminController {

    @FXML
    public Button systemSettingsButton;

    @FXML
    @SuppressWarnings("unused")
    private StackPane rootPane;

    private boolean isLecturer = false;

    public void setLecturer(boolean lecturer) {
        this.isLecturer = lecturer;

        if (lecturer && systemSettingsButton != null) {
            systemSettingsButton.setVisible(false);
            systemSettingsButton.setManaged(false);
        }
    }

    @FXML
    private void loadStudentRecords() throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/mimi/sms/ui/StudentRecordsView.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Student Records");
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void loadSystemSettings() throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/mimi/sms/ui/StudentsView.fxml"));

        Stage stage = new Stage();
        stage.setTitle("System Settings");
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    private void handleLogout(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mimi/sms/ui/LoginView.fxml"));

        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(loader.load()));
        loginStage.setTitle("Login");
        loginStage.show();

        Stage currentStage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        currentStage.close();
    }

    public boolean isLecturer() {
        return isLecturer;
    }
}