package com.example.demo;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UserController {

    @FXML
    private StackPane mainContent;
    @FXML
    private Button viewBorrowedBooksButton;
    @FXML
    private Button borrowBookButton;
    @FXML
    private TextField searchBookInput;

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> availabilityColumn;

    @FXML
    private TableView<Book> borrowedBooksTable;
    @FXML
    private TableColumn<Book, String> borrowedTitleColumn;
    @FXML
    private TableColumn<Book, String> borrowedISBNColumn;
    @FXML
    private TableColumn<Book, String> borrowedStatusColumn;

    @FXML
    private Button borrowSelectedBookButton;
    @FXML
    private Button returnSelectedBookButton;
    @FXML
    private Button renewSelectedBookButton;
    @FXML
    private Button backToHomepageButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private VBox borrowBookView;
    @FXML
    private VBox borrowedBooksView;
    @FXML
    private TableColumn<Book, String> borrowDateColumn;
    @FXML
    private TableColumn<Book, String> returnDateColumn;

    private Library library = new Library();
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser;
    private ObservableList<Book> allBooks;
    private ObservableList<Book> borrowedBooks;

    @FXML
    public void initialize() {
        // Load data
        FileManager.loadData(library);
        FileManager.loadUsers(users);
        // Set up book table columns
        setupBookTableColumns();

        // Set current user
        retrieveCurrentUser();

        // Populate books and borrowed books
        initializeBookTables();

        // Set up search functionality
        setupBookSearch();
        usernameLabel.setText("Welcome, " + currentUser.getName() + "!");
    }

    @FXML
    private void showBorrowBookView() {
        borrowBookView.setVisible(true);
        borrowBookView.setManaged(true);
        borrowedBooksView.setVisible(false);
        borrowedBooksView.setManaged(false);
    }

    @FXML
    private void showBorrowedBooksView() {
        borrowBookView.setVisible(false);
        borrowBookView.setManaged(false);
        borrowedBooksView.setVisible(true);
        borrowedBooksView.setManaged(true);
    }

    @FXML
    private void handleBackToHomepage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Scene homepageScene = new Scene(loader.load(), 1200, 800);
            Stage stage = (Stage) backToHomepageButton.getScene().getWindow();
            stage.setScene(homepageScene);
            stage.setTitle("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void retrieveCurrentUser() {
        // Check the first stored approach of getting current user
        this.currentUser = UserManager.getCurrentUser();

        // If no user found, throw an exception or show an error
        if (this.currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "User Error",
                    "No user logged in. Please log in again.");
            throw new IllegalStateException("No user logged in");
        }

        // Optional: Verify user exists in the loaded users list
        boolean userExists = users.stream()
                .anyMatch(user -> user.getId().equals(currentUser.getId()));

        if (!userExists) {
            showAlert(Alert.AlertType.ERROR, "User Error",
                    "User not found in the system. Please log in again.");
            throw new IllegalStateException("User not found in system");
        }
    }

    private void setupBookTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        availabilityColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().isAvailable() ? "Yes" : "No"));
        borrowedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowedISBNColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        borrowedStatusColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getBorrower() != null ? "Borrowed" : "Available"));
        borrowDateColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getBorrowDate() != null ? data.getValue().getBorrowDate().toString() : "N/A"));
        returnDateColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getReturnDate() != null ? data.getValue().getReturnDate().toString() : "N/A"));
    }

    private void initializeBookTables() {
        allBooks = FXCollections.observableArrayList(library.getBooks());
        bookTable.setItems(allBooks);
        borrowedBooks = FXCollections.observableArrayList(
                library.getBooks().stream()
                        .filter(book -> book.getBorrower() != null && book.getBorrower().equals(currentUser.getName()))
                        .collect(Collectors.toList())
        );
        borrowedBooksTable.setItems(borrowedBooks);
        borrowSelectedBookButton.disableProperty().bind(bookTable.getSelectionModel().selectedItemProperty().isNull());
        returnSelectedBookButton.disableProperty().bind(borrowedBooksTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void setupBookSearch() {
        searchBookInput.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<Book> filteredBooks = new FilteredList<>(allBooks, book ->
                    book.getTitle().toLowerCase().contains(newValue.toLowerCase()) ||
                            book.getAuthor().toLowerCase().contains(newValue.toLowerCase()) ||
                            book.getISBN().toLowerCase().contains(newValue.toLowerCase())
            );
            bookTable.setItems(filteredBooks);
        });
    }

    @FXML
    private void handleBorrowBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null && selectedBook.isAvailable()) {
            LocalDate now = LocalDate.now();
            selectedBook.setAvailable(false);
            selectedBook.setBorrower(currentUser.getName());
            selectedBook.setBorrowDate(now);
            selectedBook.setReturnDate(now.plusDays(7)); // 7-day borrowing period

            FileManager.saveBooks(library);
            initializeBookTables();

            showAlert(Alert.AlertType.INFORMATION, "Book Borrowed",
                    "You have successfully borrowed the book: " + selectedBook.getTitle() +
                            "\nReturn by: " + selectedBook.getReturnDate());
        } else {
            showAlert(Alert.AlertType.WARNING, "Borrow Book",
                    "Please select an available book to borrow.");
        }
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null && selectedBook.getBorrower() != null &&
                selectedBook.getBorrower().equals(currentUser.getName())) { // Match name
            LocalDate now = LocalDate.now();
            LocalDate returnDate = selectedBook.getReturnDate();

            if (now.isAfter(returnDate)) { // Check for overdue
                long daysOverdue = ChronoUnit.DAYS.between(returnDate, now);
                double fine = daysOverdue * 1.0; // $1 per day overdue

                showAlert(Alert.AlertType.WARNING, "Late Return",
                        "The book is overdue by " + daysOverdue + " days. Please settle a fine of $" + fine +
                                " with the library before returning.");
                return; // Prevent returning the book
            }

            // Update book details to mark as returned
            selectedBook.setAvailable(true);
            selectedBook.setBorrower(null);
            selectedBook.setBorrowDate(null);
            selectedBook.setReturnDate(null);

            FileManager.saveBooks(library); // Save changes to the CSV
            initializeBookTables();

            showAlert(Alert.AlertType.INFORMATION, "Book Returned",
                    "You have successfully returned the book: " + selectedBook.getTitle());
        } else {
            showAlert(Alert.AlertType.WARNING, "Return Book",
                    "Please select a borrowed book to return.");
        }
    }

    @FXML
    private void handleRenewBook() {
        Book selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null && selectedBook.getBorrower() != null &&
                selectedBook.getBorrower().equals(currentUser.getName())) { // Match name
            LocalDate now = LocalDate.now();
            LocalDate returnDate = selectedBook.getReturnDate();

            if (now.isAfter(returnDate)) { // Check for overdue
                long daysOverdue = ChronoUnit.DAYS.between(returnDate, now);
                double fine = daysOverdue * 1.0; // $1 per day overdue

                showAlert(Alert.AlertType.WARNING, "Renewal Denied",
                        "The book is overdue by " + daysOverdue + " days. Please settle a fine of $" + fine +
                                " with the library before renewing.");
                return; // Prevent renewing the book
            }

            // Extend return date by 7 days
            selectedBook.setReturnDate(returnDate.plusDays(7));
            FileManager.saveBooks(library); // Save changes to the CSV
            initializeBookTables();

            showAlert(Alert.AlertType.INFORMATION, "Book Renewed",
                    "The return date for '" + selectedBook.getTitle() + "' has been extended to: " + selectedBook.getReturnDate());
        } else {
            showAlert(Alert.AlertType.WARNING, "Renew Book",
                    "Please select a borrowed book to renew.");
        }
    }



    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}