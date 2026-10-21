package com.schoollibrary.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.schoollibrary.Borrowing;
import com.schoollibrary.service.BorrowingService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;

    public BorrowingController(BorrowingService borrowingService) {
        this.borrowingService = borrowingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Borrowing borrowBook(@RequestBody Map<String, String> request) {
        try {
            Long studentId = Long.parseLong(request.get("studentId"));
            Long bookId = Long.parseLong(request.get("bookId"));

            LocalDate borrowDate =
                    LocalDate.parse(request.get("borrowDate"));

            LocalDate dueDate =
                    LocalDate.parse(request.get("dueDate"));

            return borrowingService.borrowBook(
                    studentId, bookId, borrowDate, dueDate);

        } catch (IllegalArgumentException | NullPointerException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid borrowing details: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Borrowing> getAllBorrowings() {
        return borrowingService.getAllBorrowings();
    }
    @PutMapping("/{id}/return")
public Borrowing returnBook(@PathVariable Long id) {
    try {
        return borrowingService.returnBook(id);
    } catch (IllegalArgumentException e) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
    }
}
}
