package com.mimi.util;

import com.mimi.domain.Student;

public class ValidationUtil {

    public static void validateStudent(Student s) {

        if (s.getStudentId() == null ||
                !s.getStudentId().matches("[A-Za-z0-9]{4,20}"))
            throw new IllegalArgumentException("Invalid Student ID");

        if (s.getFullName() == null ||
                !s.getFullName().matches("[\\p{L} .'-]{2,60}"))
            throw new IllegalArgumentException("Invalid full name");

        if (s.getProgramme() == null || s.getProgramme().isBlank())
            throw new IllegalArgumentException("Programme required");

        if (s.getLevel() < 100 || s.getLevel() > 700)
            throw new IllegalArgumentException("Invalid level");

        if (s.getGpa() < 0.0 || s.getGpa() > 5.0)
            throw new IllegalArgumentException("Invalid GPA");

        if (s.getEmail() != null &&
                !s.getEmail().matches(".+@.+\\..+"))
            throw new IllegalArgumentException("Invalid email");

        if (s.getPhone() != null &&
                !s.getPhone().matches("\\d{10,15}"))
            throw new IllegalArgumentException("Invalid phone");
    }
}
