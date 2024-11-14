package com.example.demo;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private boolean available;
    private String borrowerName;

    public Book(String title, String author, String ISBN, boolean available, String borrowerName) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.available = available;
        this.borrowerName = borrowerName;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getISBN() { return ISBN; }
    public boolean isAvailable() { return available; }
    public String getBorrowerName() { return borrowerName; }

    public void borrow(String borrowerName) {
        if (this.available) {
            this.available = false;
            this.borrowerName = borrowerName;
        }
    }

    public void returnBook() {
        this.available = true;
        this.borrowerName = null;
    }
}
