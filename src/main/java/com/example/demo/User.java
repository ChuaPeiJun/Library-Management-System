package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String id;
    private List<Book> borrowedBooks;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
        this.borrowedBooks = new ArrayList<Book>(); // Initialize the list
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public java.util.List<Book> getBorrowedBooks() { return borrowedBooks; }
    public void setBorrowedBooks(java.util.List<Book> borrowedBooks) { this.borrowedBooks = borrowedBooks; }
}