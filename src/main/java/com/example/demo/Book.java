package com.example.demo;

import java.time.LocalDate;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private boolean available;
    private String borrower;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public Book(String title, String author, String ISBN, boolean available, String borrower, LocalDate borrowDate, LocalDate returnDate) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.available = available;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getISBN() { return ISBN; }
    public boolean isAvailable() { return available; }
    public String getBorrower() { return borrower; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setAvailable(boolean available) { this.available = available; }
    public void setBorrower(String borrower) { this.borrower = borrower; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    // Check if the book is overdue
    public boolean isOverdue() {
        return returnDate != null && LocalDate.now().isAfter(returnDate);
    }

    // Calculate fine for overdue books
    public double calculateFine() {
        if (!isOverdue()) return 0.0;
        long daysOverdue = LocalDate.now().toEpochDay() - returnDate.toEpochDay();
        return daysOverdue * 1.0; // $1.00 fine per day
    }

    // Renew the book (extend return date by 7 days if not overdue)
    public boolean renew() {
        if (isOverdue()) return false; // Cannot renew overdue books
        if (returnDate != null) {
            returnDate = returnDate.plusDays(7);
            return true;
        }
        return false;
    }
}
