package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Class representing a User
public class User {
    private String name; // User's name
    private String id; // User's unique identifier
    private ObservableList<Book> borrowedBooks; // ObservableList of books borrowed by the user

    // Constructor to initialize a User with a name and id
    public User(String name, String id) {
        this.name = name;
        this.id = id;
        this.borrowedBooks = FXCollections.observableArrayList(); // Initialize with ObservableList
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for the list of borrowed books
    public ObservableList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

}
