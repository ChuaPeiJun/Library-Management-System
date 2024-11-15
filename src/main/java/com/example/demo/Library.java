package com.example.demo;

import java.util.ArrayList;

public class Library {
    private ArrayList<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }
}
