
package com.schoollibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.schoollibrary.Borrowing;

public interface BorrowingRepository
        extends JpaRepository<Borrowing, Long> {
}