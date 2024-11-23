package com.example.demo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final String BOOKS_FILE = "books.csv";

    // Load books from the CSV file into the library
    public static void loadData(Library library) {
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line = reader.readLine(); // Skip header row
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                String title = fields[0];
                String author = fields[1];
                String isbn = fields[2];
                boolean available = Boolean.parseBoolean(fields[3]);
                String borrower = fields[4];
                LocalDate borrowDate = fields[5].isEmpty() ? null : LocalDate.parse(fields[5]);
                LocalDate returnDate = fields[6].isEmpty() ? null : LocalDate.parse(fields[6]);

                Book book = new Book(title, author, isbn, available, borrower, borrowDate, returnDate);
                library.addBook(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading data from " + BOOKS_FILE);
        }
    }
    public static void loadUsers(ArrayList<User> users) {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",", -1);
                if (fields.length >= 2) {
                    String name = fields[0];
                    String id = fields[1];
                    User user = new User(name, id);
                    users.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading users from users.csv");
        }
    }


    // Save the updated list of books back to the CSV file
    public static void saveBooks(Library library) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKS_FILE))) {
            writer.write("Title,Author,ISBN,Available,Borrower,BorrowDate,ReturnDate");
            writer.newLine();
            for (Book book : library.getBooks()) {
                writer.write(String.format("%s,%s,%s,%b,%s,%s,%s",
                        book.getTitle(),
                        book.getAuthor(),
                        book.getISBN(),
                        book.isAvailable(),
                        book.getBorrower() != null ? book.getBorrower() : "",
                        book.getBorrowDate() != null ? book.getBorrowDate().toString() : "",
                        book.getReturnDate() != null ? book.getReturnDate().toString() : ""));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error saving data to " + BOOKS_FILE);
        }
    }

    public static void saveUsers(ArrayList<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.csv"))) {
            for (User user : users) {
                bw.write(user.getName() + "," + user.getId());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
}
