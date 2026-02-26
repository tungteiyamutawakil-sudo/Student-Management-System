package com.mimi.repository;

import com.mimi.domain.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record SQLiteStudentRepository(String url) implements StudentRepository {

    public SQLiteStudentRepository() {
        this("jdbc:sqlite:data/students.db");
    }

    public SQLiteStudentRepository(String url) {
        this.url = url;
        createTableIfNotExists();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void createTableIfNotExists() {

        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    student_id TEXT PRIMARY KEY,
                    full_name TEXT NOT NULL,
                    programme TEXT NOT NULL,
                    level INTEGER NOT NULL CHECK(level IN (100,200,300,400,500,600,700)),
                    gpa REAL NOT NULL CHECK(gpa >= 0.0 AND gpa <= 5.0),
                    email TEXT,
                    phone TEXT,
                    date_added TEXT NOT NULL,
                    status TEXT NOT NULL CHECK(status IN ('ACTIVE','INACTIVE'))
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> findAll() {

        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Student student = new Student(
                        rs.getString("student_id"),
                        rs.getString("full_name"),
                        rs.getString("programme"),
                        rs.getInt("level"),
                        rs.getDouble("gpa"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        LocalDate.parse(rs.getString("date_added")),
                        Student.Status.valueOf(rs.getString("status"))
                );

                students.add(student);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return students;
    }

    @Override
    public void add(Student student) {

        String sql = """
                INSERT INTO students
                (student_id, full_name, programme, level, gpa, email, phone, date_added, status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getStudentId());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getProgramme());
            stmt.setInt(4, student.getLevel());
            stmt.setDouble(5, student.getGpa());
            stmt.setString(6, student.getEmail());
            stmt.setString(7, student.getPhone());

            // Store date as ISO TEXT
            stmt.setString(8, student.getDateAdded().toString());

            stmt.setString(9, student.getStatus().name());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Student student) {

        String sql = """
                UPDATE students
                SET full_name = ?, programme = ?, level = ?, gpa = ?, email = ?, phone = ?, status = ?
                WHERE student_id = ?
                """;

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getFullName());
            stmt.setString(2, student.getProgramme());
            stmt.setInt(3, student.getLevel());
            stmt.setDouble(4, student.getGpa());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getPhone());
            stmt.setString(7, student.getStatus().name());
            stmt.setString(8, student.getStudentId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String studentId) {

        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public com.mimi.domain.Student findById(String studentId) {

        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Student(
                        rs.getString("student_id"),
                        rs.getString("full_name"),
                        rs.getString("programme"),
                        rs.getInt("level"),
                        rs.getDouble("gpa"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        LocalDate.parse(rs.getString("date_added")),
                        Student.Status.valueOf(rs.getString("status"))
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
