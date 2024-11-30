package com.example.demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

// Class representing a Book
public class Book {
    private String title; // Title of the book
    private String author; // Author of the book
    private String ISBN; // ISBN number of the book
    private boolean available; // Availability status of the book
    private String borrower; // Name of the person who borrowed the book
    private LocalDate borrowDate; // Date when the book was borrowed
    private LocalDate returnDate; // Date when the book is due to be returned

    // Constructor to initialize a book with all details
    public Book(String title, String author, String ISBN, boolean available, String borrower,
                LocalDate borrowDate, LocalDate returnDate) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.available = available;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters and setters for book attributes
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    // Method to borrow a book
    public boolean borrow(String borrowerName) {
        if (this.isAvailable()) {
            LocalDate now = LocalDate.now();
            this.available = false;
            this.borrower = borrowerName;
            this.borrowDate = now;
            this.returnDate = now.plusDays(7); // 7-day borrowing period
            return true;
        }
        return false; // Book is not available
    }

    // Method to return a book
    public String returnBook(String returningUser) {
        if (this.borrower == null || !this.borrower.equals(returningUser)) {
            return "You are not the borrower of this book.";
        }

        LocalDate now = LocalDate.now();
        if (this.returnDate != null && now.isAfter(this.returnDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(this.returnDate, now);
            double fine = daysOverdue * 1.00; // RM1 per day overdue
            String fineFormatted = String.format("%.2f", fine);
            return "The book is overdue by " + daysOverdue + " days. Please settle a fine of RM" + fineFormatted +
                    " with the library before returning.";
        }

        // Reset book details to mark it as available
        this.available = true;
        this.borrower = null;
        this.borrowDate = null;
        this.returnDate = null;

        return "success"; // Indicates a successful return
    }

    // Method to renew a book
    public String renewBook(String borrowerName) {
        if (this.borrower == null || !this.borrower.equals(borrowerName)) {
            return "You are not the borrower of this book.";
        }

        LocalDate now = LocalDate.now();
        if (this.returnDate != null && now.isAfter(this.returnDate)) {
            long daysOverdue = ChronoUnit.DAYS.between(this.returnDate, now);
            double fine = daysOverdue * 1.00; // RM1 per day overdue
            String fineFormatted = String.format("%.2f", fine);
            return "The book is overdue by " + daysOverdue + " days. Please settle a fine of RM" + fineFormatted +
                    " with the library before renewing.";
        }

        if (ChronoUnit.DAYS.between(now, this.returnDate) >= 7) {
            return "You have already renewed this book. You cannot renew it again.";
        }

        this.returnDate = this.returnDate.plusDays(7); // Extend return date by 7 days
        return "success"; // Indicates a successful renewal
    }

    public String displayDetails() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return String.format(
                "%s\n %s\n %s\n %s\n %s\n %s",
                title,
                author,
                ISBN,
                borrower,
                borrowDate.format(formatter),
                returnDate.format(formatter)
        );
    }
}