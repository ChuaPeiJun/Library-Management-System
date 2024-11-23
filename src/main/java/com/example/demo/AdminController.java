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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AdminController {

    @FXML
    private Button backButton;
    @FXML
    private Button manageBooksButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private VBox bookManagementView;
    @FXML
    private VBox userManagementView;
    @FXML
    private VBox lateReturnBooksView;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField authorInput;
    @FXML
    private TextField isbnInput;
    @FXML
    private TableView<Book> borrowedBooksTable;
    @FXML
    private TableColumn<Book, String> borrowedTitleColumn;
    @FXML
    private TableColumn<Book, String> borrowDateColumn;
    @FXML
    private TableColumn<Book, String> returnDateColumn;
    @FXML
    private TableColumn<Book, String> borrowerNameColumn;
    @FXML
    private TableColumn<Book, String> fineColumn;

    @FXML
    private Button returnSelectedBookButton;
    @FXML
    private Button renewSelectedBookButton;
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> idColumn;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField idInput;
    @FXML
    private TextField searchBookInput;
    @FXML
    private TextField searchUserInput;
    @FXML
    private TableColumn<Book, String> availabilityColumn;
    @FXML
    private TableColumn<Book, String> borrowerColumn;
    @FXML
    private ListView<String> borrowedBooksListView;

    private Library library = new Library();
    private ArrayList<User> users = new ArrayList<>();
    private ObservableList<Book> borrowedBooks;

    @FXML
    public void initialize() {
        // Load data from files
        FileManager.loadData(library);
        FileManager.loadUsers(users);

        // Debug prints
        System.out.println("Books loaded: " + library.getBooks().size());
        System.out.println("Users loaded: " + users.size());

        // Initialize book table columns
        userManagementView.setVisible(true);
        bookManagementView.setVisible(false);
        lateReturnBooksView.setVisible(false);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        availabilityColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().isAvailable() ? "Yes" : "No"));
        borrowerColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getBorrower() == null ? "" : data.getValue().getBorrower()));
        bookTable.setItems(FXCollections.observableArrayList(library.getBooks()));

        // Initialize user table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userTable.setItems(FXCollections.observableArrayList(users));

        // Add selection listener to userTable
        userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayBorrowedBooks(newValue);
            }
        });

        System.out.println("Books in table: " + bookTable.getItems().size());
        System.out.println("Users in table: " + userTable.getItems().size());
        setupLateReturnBooksTable();
        initializeBorrowedBooksTable();
    }

    // Handle Back to Homepage button click
    @FXML
    private void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("homepage.fxml"));
            Scene homepageScene = new Scene(loader.load(), 1200, 800);
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(homepageScene);
            stage.setTitle("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageBooks() {
        bookManagementView.setVisible(true);
        userManagementView.setVisible(false);
        lateReturnBooksView.setVisible(false);
        System.out.println("Switched to Book Management View");
    }

    @FXML
    private void handleManageUsers() {
        userManagementView.setVisible(true);
        bookManagementView.setVisible(false);
        lateReturnBooksView.setVisible(false);
        System.out.println("Switched to User Management View");
    }

    @FXML
    private void handleAddBook() {
        String title = titleInput.getText();
        String author = authorInput.getText();
        String isbn = isbnInput.getText();

        if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "All fields are required.");
            return;
        }

        Book newBook = new Book(title, author, isbn, true, null,null,null);
        library.addBook(newBook);
        bookTable.getItems().add(newBook);

        titleInput.clear();
        authorInput.clear();
        isbnInput.clear();
        FileManager.saveBooks(library);
    }

    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            library.getBooks().remove(selectedBook);
            bookTable.getItems().remove(selectedBook);
            FileManager.saveBooks(library);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a book to delete.");
        }
    }

    @FXML
    private void handleAddUser() {
        String name = nameInput.getText();
        String id = idInput.getText();

        if (name.isEmpty() || id.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "All fields are required.");
            return;
        }

        User newUser = new User(name, id);
        users.add(newUser);
        userTable.getItems().add(newUser);

        nameInput.clear();
        idInput.clear();
        FileManager.saveUsers(users);
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            users.remove(selectedUser);
            userTable.getItems().remove(selectedUser);
            FileManager.saveUsers(users);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a user to delete.");
        }
    }

    @FXML
    private void handleSearchBooks() {
        String searchQuery = searchBookInput.getText().toLowerCase();
        ObservableList<Book> filteredBooks = FXCollections.observableArrayList(library.getBooks());
        FilteredList<Book> filteredList = new FilteredList<>(filteredBooks, book ->
                book.getTitle().toLowerCase().contains(searchQuery) ||
                        book.getAuthor().toLowerCase().contains(searchQuery) ||
                        book.getISBN().toLowerCase().contains(searchQuery) ||
                        (book.getBorrower() != null && book.getBorrower().toLowerCase().contains(searchQuery))
        );
        bookTable.setItems(filteredList);
        System.out.println("Books after search: " + filteredList.size());
    }

    @FXML
    private void handleSearchUsers() {
        String searchQuery = searchUserInput.getText().toLowerCase();
        ObservableList<User> filteredUsers = FXCollections.observableArrayList(users);
        FilteredList<User> filteredList = new FilteredList<>(filteredUsers, user ->
                user.getName().toLowerCase().contains(searchQuery) ||
                        user.getId().toLowerCase().contains(searchQuery)
        );
        userTable.setItems(filteredList);
        System.out.println("Users after search: " + filteredList.size());
    }

    @FXML
    private void displayBorrowedBooks(User user) {
        borrowedBooksListView.getItems().clear(); // Clear existing items
        boolean hasBorrowedBooks = false;
        for (Book book : library.getBooks()) {
            if (user.getName().equals(book.getBorrower())) {
                String bookDetails = String.format("Title: %s, Author: %s, ISBN: %s", book.getTitle(), book.getAuthor(), book.getISBN());
                borrowedBooksListView.getItems().add(bookDetails);
                hasBorrowedBooks = true;
            }
        }
        if (!hasBorrowedBooks) {
            borrowedBooksListView.getItems().add("No books borrowed");
        }
    }

    private void setupLateReturnBooksTable() {
        borrowedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowerNameColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getBorrower()));
        borrowDateColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getBorrowDate() != null
                        ? data.getValue().getBorrowDate().toString()
                        : ""));
        returnDateColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getReturnDate() != null
                        ? data.getValue().getReturnDate().toString()
                        : ""));
        fineColumn.setCellValueFactory(data -> {
            LocalDate returnDate = data.getValue().getReturnDate();
            if (returnDate != null) {
                long daysOverdue = ChronoUnit.DAYS.between(returnDate, LocalDate.now());
                double fine = daysOverdue > 0 ? daysOverdue * 1.0 : 0;
                return new ReadOnlyStringWrapper("$" + fine);
            }
            return new ReadOnlyStringWrapper("$0");
        });
    }

    private void initializeBorrowedBooksTable() {
        ObservableList<Book> borrowedBooks = FXCollections.observableArrayList(
                library.getBooks().stream()
                        .filter(book -> book.getBorrower() != null)
                        .collect(Collectors.toList())
        );
        borrowedBooksTable.setItems(borrowedBooks);
    }

    @FXML
    private void handleManageLateReturnBooks() {
        bookManagementView.setVisible(false);
        userManagementView.setVisible(false);
        lateReturnBooksView.setVisible(true);
        initializeBorrowedBooksTable(); // Ensure the table is up-to-date
        System.out.println("Switched to Late Return Books View");
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            LocalDate now = LocalDate.now();
            LocalDate returnDate = selectedBook.getReturnDate();

            long daysOverdue = ChronoUnit.DAYS.between(returnDate, now);
            double fine = daysOverdue > 0 ? daysOverdue * 1.0 : 0;

            selectedBook.setAvailable(true);
            selectedBook.setBorrower(null);
            selectedBook.setBorrowDate(null);
            selectedBook.setReturnDate(null);

            FileManager.saveBooks(library);
            initializeBorrowedBooksTable();

            showAlert(Alert.AlertType.INFORMATION, "Book Returned",
                    "The book '" + selectedBook.getTitle() + "' has been returned." +
                            "\nFine: $" + fine);
        } else {
            showAlert(Alert.AlertType.WARNING, "Return Book", "Please select a book to return.");
        }
    }

    @FXML
    private void handleRenewBook() {
        Book selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            LocalDate now = LocalDate.now();

            selectedBook.setReturnDate(now.plusDays(7));

            FileManager.saveBooks(library);
            initializeBorrowedBooksTable();

            showAlert(Alert.AlertType.INFORMATION, "Book Renewed",
                    "The book '" + selectedBook.getTitle() + "' has been renewed for 7 days starting today.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Renew Book", "Please select a book to renew.");
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