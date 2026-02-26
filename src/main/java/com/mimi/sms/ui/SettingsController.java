package com.mimi.sms.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    public void initialize() {
        System.out.println("SettingsController loaded");
    }

    @FXML
    private void handleBack(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mimi/sms/ui/AdminHomeView.fxml"));

        Stage adminStage = new Stage();
        adminStage.setScene(new Scene(loader.load()));
        adminStage.setTitle("Admin Panel");
        adminStage.show();

        // Close current window
        Stage currentStage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        currentStage.close();
    }
}