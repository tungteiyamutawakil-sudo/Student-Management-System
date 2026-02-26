package com.mimi.sms;

import com.mimi.util.AppLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        AppLogger.log("Application started");

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/mimi/sms/ui/LoginView.fxml"));

        Parent root = loader.load();

        stage.setScene(new Scene(root, 700, 500));
        stage.setTitle("Login");
        stage.show();
    }

}
