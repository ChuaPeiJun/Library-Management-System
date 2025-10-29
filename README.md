# Library Management System (JavaFX)

**📖 Project Overview**

The Library Management System is a desktop application built using Java and JavaFX that provides an intuitive interface for managing books and users in a library environment.
The system includes separate functionalities for Administrators and Users, enabling efficient management of books, borrowing, and penalties.

This project demonstrates strong understanding and application of:

- Object-Oriented Programming (OOP) principles

- Encapsulation and modular class design

- File handling using CSV for data persistence

- JavaFX UI design and event-driven programming

## 👨‍💼 Admin Features

**Book Management:**

Add, delete, search, and edit book details

**User Management:**

Add, delete, search, and edit user information

Display the list of books borrowed by each user

## 👩‍🎓 User Features

Borrow and return books through an interactive interface

Renew borrowed books with ease

Automatic late penalty calculation for overdue books

View personal borrowing records and due dates

## ⚙️ Technologies and Concepts Used

Programming Language: Java

Framework: JavaFX

File Handling: CSV-based data storage for books and users

OOP Concepts: Classes, Objects, Encapsulation, and Inheritance

Architecture: MVC pattern (LibraryApp, Controllers, Managers, Models)

## 🧩 Core Classes Implemented

| Class               | Description                                                                         |
| ------------------- | ----------------------------------------------------------------------------------- |
| **Book**            | Encapsulates all book-related data and behavior (title, author, availability, etc.) |
| **User**            | Represents a library user and manages borrowed books and penalties                  |
| **Library**         | Maintains the collection of books and supports add, delete, and search operations   |
| **FileManager**     | Handles file input/output for reading and writing data to CSV files                 |
| **UserManager**     | Manages user accounts and their interactions with the system                        |
| **LibraryApp**      | The main entry point for the JavaFX application                                     |
| **AdminController** | Provides administrative control for managing books and users                        |
| **UserController**  | Handles user interactions and borrowing operations                                  |
