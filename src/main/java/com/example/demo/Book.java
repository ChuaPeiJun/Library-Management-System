package com.example.demo;

import java.time.LocalDate;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private boolean available;
    private String borrower;
    private LocalDate borrowDate; // Date when the book was borrowed
    private LocalDate returnDate; // Due date for returning the book

    public Book(String title, String author, String ISBN, boolean available, String borrower) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.available = available;
        this.borrower = borrower;
        this.borrowDate = null;
        this.returnDate = null;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getISBN() { return ISBN; }
    public boolean isAvailable() { return available; }
    public String getBorrower() { return borrower; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setBorrower(String borrower) { this.borrower = borrower; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    // Check if the book is overdue and calculate the fine
    public boolean isOverdue() {
        if (returnDate == null || LocalDate.now().isBefore(returnDate)) {
            return false;
        }
        return true;
    }

    public double calculateFine() {
        if (!isOverdue()) return 0.0;

        long overdueDays = LocalDate.now().toEpochDay() - returnDate.toEpochDay();
        return overdueDays * 1.0; // Fine: $1.00 per day
    }

    // Renew the book (extend the return date by 7 days)
    public void renew() {
        if (returnDate != null) {
            returnDate = returnDate.plusDays(7);
        }
    }
}
