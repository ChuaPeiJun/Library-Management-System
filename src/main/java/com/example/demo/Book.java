package com.example.demo;

import java.time.LocalDate;

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

    // Method to check if the book is overdue
    public boolean isOverdue() {
        return returnDate != null && LocalDate.now().isAfter(returnDate);
    }

    // Method to calculate the fine for overdue books
    public double calculateFine() {
        if (!isOverdue()) {
            return 0.0;
        }
        long daysOverdue = LocalDate.now().toEpochDay() - returnDate.toEpochDay();
        return daysOverdue * 1.0; // $1.00 fine per day
    }

    // Method to renew the book (extends return date by 7 days if not overdue)
    public boolean renew() {
        if (isOverdue()) {
            return false; // Cannot renew overdue books
        }
        if (returnDate != null) {
            returnDate = returnDate.plusDays(7);
            return true;
        }
        return false;
    }
}