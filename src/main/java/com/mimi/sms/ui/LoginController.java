package com.mimi.sms.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() throws Exception {

        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = null;

        if (username.equals("admin") && password.equals("1234")) {
            role = "ADMIN";
        }
        else if (username.equals("lecturer") && password.equals("1234")) {
            role = "LECTURER";
        }

        if (role != null) {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/mimi/sms/ui/AdminHomeView.fxml"));

            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            AdminController controller = loader.getController();

            if (role.equals("ADMIN")) {
                stage.setTitle("Admin Panel");
                controller.setLecturer(false);
            }
            else {
                stage.setTitle("Lecturer Panel");
                controller.setLecturer(true);
            }

            stage.show();

            usernameField.getScene().getWindow().hide();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }
}