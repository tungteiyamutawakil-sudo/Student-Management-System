package com.mimi.repository;

import com.mimi.domain.Student;
import java.util.List;

public interface StudentRepository {

    List<Student> findAll();

    void add(Student student);

    void update(Student student);

    void delete(String studentId);

    Student findById(String studentId);
}
