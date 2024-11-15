package com.example.demo;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class AdminController {

    @FXML
    private Button manageBooksButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private VBox bookManagementView;
    @FXML
    private VBox userManagementView;
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

    @FXML
    public void initialize() {
        // Load data from files
        FileManager.loadData(library, users);

        // Debug prints
        System.out.println("Books loaded: " + library.getBooks().size());
        System.out.println("Users loaded: " + users.size());

        // Initialize book table columns
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

        // Add row selection listener for user table to display borrowed books
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayBorrowedBooks(newSelection);
            }
        });

        System.out.println("Books in table: " + bookTable.getItems().size());
        System.out.println("Users in table: " + userTable.getItems().size());
    }

    @FXML
    private void handleManageBooks() {
        bookManagementView.setVisible(true);
        userManagementView.setVisible(false);
        System.out.println("Switched to Book Management View");
    }

    @FXML
    private void handleManageUsers() {
        userManagementView.setVisible(true);
        bookManagementView.setVisible(false);
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

        Book newBook = new Book(title, author, isbn, true, null);
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
        ObservableList<String> borrowedBooks = FXCollections.observableArrayList();
        for (Book book : user.getBorrowedBooks()) {
            borrowedBooks.add(book.getTitle());
        }
        borrowedBooksListView.setItems(borrowedBooks);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}