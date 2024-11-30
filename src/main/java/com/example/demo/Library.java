package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public boolean deleteBook(Book book) {
        return books.remove(book);
    }

    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return books; // Return all books if keyword is empty
        }
        String lowerKeyword = keyword.toLowerCase();
        return books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(lowerKeyword) ||
                        book.getAuthor().toLowerCase().contains(lowerKeyword) ||
                        book.getISBN().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
}
