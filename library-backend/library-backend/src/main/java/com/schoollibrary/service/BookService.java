package com.schoollibrary.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.schoollibrary.Book;
import com.schoollibrary.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(Book book) {

        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("Book title is required.");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("Book author is required.");
        }

        if (book.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public boolean deleteBook(Long id) {

        if (!bookRepository.existsById(id)) {
            return false;
        }

        bookRepository.deleteById(id);
        return true;
    }
}