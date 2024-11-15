package com.example.demo;

public class Book {
    private String title;
    private String author;
    private String ISBN;
    private boolean available;
    private String borrower;

    public Book(String title, String author, String ISBN, boolean available, String borrower) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.available = available;
        this.borrower = borrower;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getISBN() { return ISBN; }
    public boolean isAvailable() { return available; }
    public String getBorrower() { return borrower; }

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }
    public void setAvailable(boolean available) { this.available = available; }
    public void setBorrower(String borrower) { this.borrower = borrower; }
}