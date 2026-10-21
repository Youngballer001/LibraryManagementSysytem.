package com.schoollibrary.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.schoollibrary.Book;
import com.schoollibrary.Borrowing;
import com.schoollibrary.Student;
import com.schoollibrary.repository.BookRepository;
import com.schoollibrary.repository.BorrowingRepository;
import com.schoollibrary.repository.StudentRepository;

@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;

    public BorrowingService(
            BorrowingRepository borrowingRepository,
            StudentRepository studentRepository,
            BookRepository bookRepository) {

        this.borrowingRepository = borrowingRepository;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Borrowing borrowBook(
            Long studentId,
            Long bookId,
            LocalDate borrowDate,
            LocalDate dueDate) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Student not found."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Book not found."));

        if (book.getQuantity() <= 0) {
            throw new IllegalArgumentException(
                    "No copies of this book are available.");
        }

        if (borrowDate == null || dueDate == null) {
            throw new IllegalArgumentException(
                    "Borrowing date and due date are required.");
        }

        if (dueDate.isBefore(borrowDate)) {
            throw new IllegalArgumentException(
                    "Due date cannot be before the borrowing date.");
        }

        book.setQuantity(book.getQuantity() - 1);
        bookRepository.save(book);

        Borrowing borrowing = new Borrowing();
        borrowing.setStudent(student);
        borrowing.setBook(book);
        borrowing.setBorrowDate(borrowDate);
        borrowing.setDueDate(dueDate);
        borrowing.setStatus("BORROWED");

        return borrowingRepository.save(borrowing);
    }

    public List<Borrowing> getAllBorrowings() {
        return borrowingRepository.findAll();
    }
    @Transactional
public Borrowing returnBook(Long borrowingId) {

    Borrowing borrowing = borrowingRepository.findById(borrowingId)
            .orElseThrow(() ->
                    new IllegalArgumentException("Borrowing record not found."));

    if (!"BORROWED".equals(borrowing.getStatus())) {
        throw new IllegalArgumentException(
                "This book has already been returned.");
    }

    // Record the return date and update the borrowing status
    borrowing.setReturnDate(LocalDate.now());
    borrowing.setStatus("RETURNED");

    // Increase the available book quantity
    Book book = borrowing.getBook();
    book.setQuantity(book.getQuantity() + 1);
    bookRepository.save(book);

    // Save the updated borrowing record
    return borrowingRepository.save(borrowing);
}
}
