package com.mimi.domain;

import java.time.LocalDate;

public class Student {
    private String studentId;
    private String fullName;
    private String programme;
    private int level;
    private double gpa;
    private String email;
    private String phone;
    private final LocalDate dateAdded;
    private final Status status;

    public enum Status {
        ACTIVE, INACTIVE
    }

    public Student() {
        this.dateAdded = LocalDate.now();
        this.status = Status.ACTIVE;
    }

    public Student(String studentId, String fullName, String programme,
                   int level, double gpa, String email, String phone,
                   LocalDate dateAdded, Status status) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.programme = programme;
        this.level = level;
        this.gpa = gpa;
        this.email = email;
        this.phone = phone;
        this.dateAdded = dateAdded;
        this.status = status;
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }

    public String getProgramme() { return programme; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public LocalDate getDateAdded() { return dateAdded; }

    public Status getStatus() { return status; }
}
