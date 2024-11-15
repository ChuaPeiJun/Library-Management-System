package com.example.demo;

import java.io.*;
import java.util.ArrayList;

public class FileManager {

    public static void loadData(Library library, ArrayList<User> users) {
        try (BufferedReader br = new BufferedReader(new FileReader("books.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {  // Ensure there are exactly 5 columns
                    library.addBook(new Book(data[0], data[1], data[2], Boolean.parseBoolean(data[3]), data[4]));
                } else {
                    System.out.println("Skipping invalid book entry: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load users from file (implementing CSV loading)
        try (BufferedReader br = new BufferedReader(new FileReader("users.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                users.add(new User(data[0], data[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveBooks(Library library) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("books.csv"))) {
            for (Book book : library.getBooks()) {
                bw.write(book.getTitle() + "," + book.getAuthor() + "," + book.getISBN() + "," + book.isAvailable() + "," + book.getBorrower());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveUsers(ArrayList<User> users) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("users.csv"))) {
            for (User user : users) {
                bw.write(user.getName() + "," + user.getId());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
