package com.schoollibrary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.schoollibrary.Student;
import com.schoollibrary.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        if (student.getRegNo() == null || student.getRegNo().isBlank()) {
            throw new IllegalArgumentException("Registration number is required");
        }

        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Student name is required");
        }

        if (student.getCourse() == null || student.getCourse().isBlank()) {
            throw new IllegalArgumentException("Course is required");
        }

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public boolean deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }

        studentRepository.deleteById(id);
        return true;
    }
}
