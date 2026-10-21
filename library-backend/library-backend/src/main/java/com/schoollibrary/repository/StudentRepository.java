package com.schoollibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoollibrary.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
