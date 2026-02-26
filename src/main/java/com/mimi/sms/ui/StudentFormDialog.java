package com.mimi.sms.ui;

import com.mimi.domain.Student;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

public class StudentFormDialog extends Dialog<Student> {

    private final TextField idField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField programmeField = new TextField();
    private final ComboBox<Integer> levelBox = new ComboBox<>();
    private final TextField gpaField = new TextField();
    private final TextField emailField = new TextField();
    private final TextField phoneField = new TextField();
    private final ComboBox<Student.Status> statusBox = new ComboBox<>();

    public StudentFormDialog(Student existing) {

        setTitle(existing == null ? "Add Student" : "Edit Student");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        levelBox.getItems().addAll(100,200,300,400,500,600,700);
        statusBox.getItems().addAll(Student.Status.ACTIVE, Student.Status.INACTIVE);

        grid.add(new Label("Student ID:"), 0, 0);
        grid.add(idField, 1, 0);

        grid.add(new Label("Full Name:"), 0, 1);
        grid.add(nameField, 1, 1);

        grid.add(new Label("Programme:"), 0, 2);
        grid.add(programmeField, 1, 2);

        grid.add(new Label("Level:"), 0, 3);
        grid.add(levelBox, 1, 3);

        grid.add(new Label("GPA:"), 0, 4);
        grid.add(gpaField, 1, 4);

        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);

        grid.add(new Label("Phone:"), 0, 6);
        grid.add(phoneField, 1, 6);

        grid.add(new Label("Status:"), 0, 7);
        grid.add(statusBox, 1, 7);

        getDialogPane().setContent(grid);

        // If editing, pre-fill fields
        if (existing != null) {
            idField.setText(existing.getStudentId());
            idField.setDisable(true);
            nameField.setText(existing.getFullName());
            programmeField.setText(existing.getProgramme());
            levelBox.setValue(existing.getLevel());
            gpaField.setText(String.valueOf(existing.getGpa()));
            emailField.setText(existing.getEmail());
            phoneField.setText(existing.getPhone());
            statusBox.setValue(existing.getStatus());
        }

        setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Student(
                        idField.getText(),
                        nameField.getText(),
                        programmeField.getText(),
                        levelBox.getValue(),
                        Double.parseDouble(gpaField.getText()),
                        emailField.getText(),
                        phoneField.getText(),
                        LocalDate.now(),
                        statusBox.getValue()
                );
            }
            return null;
        });
    }
}
