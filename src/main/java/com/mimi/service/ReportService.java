package com.mimi.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mimi.domain.Student;

public class ReportService {

    public List<Student> topPerformers(List<Student> students, int limit) {

        return students.stream()
                .sorted((a, b) -> Double.compare(b.getGpa(), a.getGpa()))
                .limit(limit)
                .toList();
    }

    public List<Student> atRiskStudents(List<Student> students, double threshold) {

        return students.stream()
                .filter(s -> s.getGpa() < threshold)
                .sorted(Comparator.comparingDouble(Student::getGpa))
                .toList();
    }

    public Map<String, Long> gpaDistribution(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(s -> {
                    double gpa = s.getGpa();
                    if (gpa >= 3.5) return "3.5 - 4.0";
                    else if (gpa >= 3.0) return "3.0 - 3.49";
                    else if (gpa >= 2.0) return "2.0 - 2.99";
                    else return "Below 2.0";
                }, Collectors.counting()));
    }

    public Map<String, Double> programmeAverageGpa(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getProgramme,
                        Collectors.averagingDouble(Student::getGpa)
                ));
    }

    public Map<String, Long> programmeCount(List<Student> students) {

        return students.stream()
                .collect(Collectors.groupingBy(
                        Student::getProgramme,
                        Collectors.counting()
                ));
    }
}
