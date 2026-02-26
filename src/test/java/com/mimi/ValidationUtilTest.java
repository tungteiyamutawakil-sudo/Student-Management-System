package com.mimi;

import com.mimi.domain.Student;
import com.mimi.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    private Student validStudent() {
        return new Student(
                "1001",
                "John Doe",
                "CS",
                200,
                3.5,
                "john@email.com",
                "1234567890",
                LocalDate.now(),
                Student.Status.ACTIVE
        );
    }

    @Test
    void validStudentShouldPass() {
        assertDoesNotThrow(() ->
                ValidationUtil.validateStudent(validStudent()));
    }

    @Test
    void invalidGpaShouldFail() {
        Student s = validStudent();
        s.setGpa(5.0);
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateStudent(s));
    }

    @Test
    void invalidEmailShouldFail() {
        Student s = validStudent();
        s.setEmail("invalid email");
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateStudent(s));
    }

    @Test
    void invalidLevelShouldFail() {
        Student s = validStudent();
        s.setLevel(900);
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateStudent(s));
    }

    @Test
    void invalidStudentIdShouldFail() {
        Student s = validStudent();
        s.setStudentId("!");
        assertThrows(IllegalArgumentException.class,
                () -> ValidationUtil.validateStudent(s));
    }
}
