package com.mimi.service;

import com.mimi.domain.Student;
import com.mimi.repository.StudentRepository;
import com.mimi.util.AppLogger;
import com.mimi.util.ValidationUtil;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.List;

public record StudentService(StudentRepository repository) {

    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    public void addStudent(Student student) {
        ValidationUtil.validateStudent(student);
        repository.add(student);
        AppLogger.log("Added student ID: " + student.getStudentId());
    }

    public void updateStudent(Student student) {
        ValidationUtil.validateStudent(student);
        repository.update(student);
        AppLogger.log("Updated student ID: " + student.getStudentId());
    }

    public void deleteStudent(String studentId) {
        repository.delete(studentId);
        AppLogger.log("Deleted student ID: " + studentId);
    }

    public List<Student> searchStudents(String keyword,
                                        String programme,
                                        Integer level,
                                        String status) {

        return repository.findAll().stream()

                .filter(s -> keyword == null || keyword.isBlank()
                        || s.getStudentId().toLowerCase().contains(keyword.toLowerCase())
                        || s.getFullName().toLowerCase().contains(keyword.toLowerCase()))

                .filter(s -> programme == null || programme.isBlank()
                        || s.getProgramme().equalsIgnoreCase(programme))

                .filter(s -> level == null || s.getLevel() == level)

                .filter(s -> status == null || status.isBlank()
                        || s.getStatus().name().equalsIgnoreCase(status))

                .toList();
    }


    public long getTotalStudents() {
        return repository.findAll().size();
    }

    public long getActiveStudents() {
        return repository.findAll().stream()
                .filter(s -> s.getStatus() == Student.Status.ACTIVE)
                .count();
    }

    public long getInactiveStudents() {
        return repository.findAll().stream()
                .filter(s -> s.getStatus() == Student.Status.INACTIVE)
                .count();
    }

    public double getAverageGpa() {
        return repository.findAll().stream()
                .mapToDouble(Student::getGpa)
                .average()
                .orElse(0.0);
    }

    public List<Student> getTopStudents(int limit) {
        return repository.findAll().stream()
                .sorted((a, b) -> Double.compare(b.getGpa(), a.getGpa()))
                .limit(limit)
                .toList();
    }

    public Map<String, Long> getProgrammeDistribution() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Student::getProgramme,
                        Collectors.counting()
                ));
    }

}
