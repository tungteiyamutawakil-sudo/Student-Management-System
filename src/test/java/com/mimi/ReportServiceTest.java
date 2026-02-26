package com.mimi;

import com.mimi.domain.Student;
import com.mimi.service.ReportService;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {

    private Student create(String id, double gpa) {
        return new Student(
                id,
                "Test Student",
                "CS",
                200,
                gpa,
                "test@email.com",
                "1234567890",
                LocalDate.now(),
                Student.Status.ACTIVE
        );
    }

    @Test
    void topPerformersShouldBeSorted() {

        ReportService service = new ReportService();

        List<Student> list = List.of(
                create("1", 2.0),
                create("2", 3.5),
                create("3", 1.0)
        );

        List<Student> result = service.topPerformers(list, 2);

        assertEquals("2", result.get(0).getStudentId());
        assertEquals("1", result.get(1).getStudentId());
    }

    @Test
    void atRiskShouldFilterCorrectly() {

        ReportService service = new ReportService();

        List<Student> list = List.of(
                create("1", 1.5),
                create("2", 3.0)
        );

        List<Student> result = service.atRiskStudents(list, 2.0);

        assertEquals(1, result.size());
        assertEquals("1", result.getFirst().getStudentId());
    }

    @Test
    void gpaDistributionShouldGroup() {

        ReportService service = new ReportService();

        List<Student> list = List.of(
                create("1", 3.6),
                create("2", 3.1),
                create("3", 2.5),
                create("4", 1.0)
        );

        var result = service.gpaDistribution(list);

        assertTrue(result.containsKey("3.5 - 4.0"));
        assertTrue(result.containsKey("Below 2.0"));
    }

    @Test
    void programmeAverageShouldBeCorrect() {

        ReportService service = new ReportService();

        List<Student> list = List.of(
                create("1", 3.0),
                create("2", 4.0)
        );

        var avg = service.programmeAverageGpa(list);

        assertEquals(3.5, avg.get("CS"));
    }
}
