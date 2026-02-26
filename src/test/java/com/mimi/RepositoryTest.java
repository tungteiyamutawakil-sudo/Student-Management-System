package com.mimi;

import com.mimi.domain.Student;
import com.mimi.repository.SQLiteStudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {

    private SQLiteStudentRepository repo;

    @BeforeEach
    void setup() {

        // Use separate test database
        repo = new SQLiteStudentRepository("jdbc:sqlite:data/test_students.db");

        // Clean test database before each test
        new File("data/test_students.db").delete();

        repo = new SQLiteStudentRepository("jdbc:sqlite:data/test_students.db");
    }

    @Test
    void addAndFindStudent() {

        Student s = new Student(
                "TEST123",
                "Repo Test",
                "CS",
                200,
                3.0,
                "repo@email.com",
                "1234567890",
                LocalDate.now(),
                Student.Status.ACTIVE
        );

        repo.add(s);

        Student found = repo.findById("TEST123");

        assertNotNull(found);
        assertEquals("Repo Test", found.getFullName());
    }

    @Test
    void deleteShouldRemoveStudent() {

        Student s = new Student(
                "TEST123",
                "Repo Test",
                "CS",
                200,
                3.0,
                "repo@email.com",
                "1234567890",
                LocalDate.now(),
                Student.Status.ACTIVE
        );

        repo.add(s);
        repo.delete("TEST123");

        Student found = repo.findById("TEST123");

        assertNull(found);
    }

    @Test
    void duplicateIdShouldNotBeAllowed() {

        Student s = new Student(
                "DUP1",
                "Duplicate",
                "CS",
                200,
                3.0,
                "dup@email.com",
                "1234567890",
                LocalDate.now(),
                Student.Status.ACTIVE
        );

        repo.add(s);

        assertThrows(Exception.class, () -> repo.add(s));
    }
}
