package com.schoollibrary.repository;

import com.schoollibrary.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
