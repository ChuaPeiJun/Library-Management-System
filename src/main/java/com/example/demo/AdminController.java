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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminController {

    @FXML
    private Button backButton;
    @FXML
    private Button manageBooksButton;
    @FXML
    private Button manageUsersButton;
    @FXML
    private Button manageLateReturnBooksButton;
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
    private TableColumn<Book, String> availabilityColumn;
    @FXML
    private TableColumn<Book, String> borrowerColumn;
    @FXML
    private TableColumn<Book, String> borrowDateColumn1;
    @FXML
    private TableColumn<Book, String> returnDateColumn1;

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
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> nameColumn;
    @FXML
    private TableColumn<User, String> idColumn;

    @FXML
    private TextField searchBookInput;
    @FXML
    private TextField searchUserInput;
    @FXML
    private TableView<Book> borrowedBooksTableView;
    @FXML
    private TableColumn<Book, String> bookTitleColumn;
    @FXML
    private TableColumn<Book, String> bookAuthorColumn;
    @FXML
    private TableColumn<Book, String> bookIsbnColumn;
    @FXML
    private TableColumn<Book, String> bookBorrowDateColumn;
    @FXML
    private TableColumn<Book, String> bookReturnDateColumn;


    @FXML
    private ListView<String> borrowedBooksListView;

    private ObservableList<Book> allBooks;
    private Library library = new Library();
    private ArrayList<User> users = new ArrayList<>();

    @FXML
    public void initialize() {
        // Handle the initial view state and management setup
        handleManageBooks();

        // Enable editing for the book and user tables
        bookTable.setEditable(true);
        userTable.setEditable(true);

        // Enable editing for individual columns in both tables
        titleColumn.setEditable(true);
        authorColumn.setEditable(true);
        isbnColumn.setEditable(true);

        nameColumn.setEditable(true);
        idColumn.setEditable(true);

        // Load data for books and users from files
        FileManager.loadData(library);
        FileManager.loadUsers(users);

        // Initialize the default view visibility
        userManagementView.setVisible(false);
        bookManagementView.setVisible(true);
        lateReturnBooksView.setVisible(false);

        // Set up columns and populate data for the book table
        initializeBookTable();

        // Set up columns and populate data for the user table
        initializeUserTable();

        // Setup search functionality for books and users
        setupSearchFunctionality();

        // Setup tables and UI components for late return books and borrowed books
        setupLateReturnBooksTable();
        initializeBorrowedBooksTable();

        // Make book table columns editable and save changes on edit
        setupEditableBookColumns();

        // Make user table columns editable and save changes on edit
        setupEditableUserColumns();

        // Initialize borrowed books logic
        initializeBorrowedBooks();
    }

    // Method to initialize the book table and its columns
    private void initializeBookTable() {
        // Set cell value factories for book table columns
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        // Availability column logic for displaying "Yes" or "No"
        availabilityColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().isAvailable() ? "Yes" : "No")
        );

        // Borrower column logic to display borrower name or blank
        borrowerColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getBorrower() == null ? "" : data.getValue().getBorrower())
        );

        // Borrow Date column logic to display the borrow date or blank
        borrowDateColumn1.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getBorrowDate() != null ? data.getValue().getBorrowDate().toString() : "")
        );

        // Return Date column logic to display the return date or blank
        returnDateColumn1.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getReturnDate() != null ? data.getValue().getReturnDate().toString() : "")
        );

        // Populate book table with data from the library
        allBooks = FXCollections.observableArrayList(library.getBooks());
        bookTable.setItems(allBooks);
    }

    // Method to initialize the user table and its columns
    private void initializeUserTable() {
        // Set cell value factories for user table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Populate user table with data
        userTable.setItems(FXCollections.observableArrayList(users));

        // Add a selection listener to display borrowed books for the selected user
        userTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayBorrowedBooks(newValue);
            }
        });
    }

    // Method to setup search functionality for books and users
    private void setupSearchFunctionality() {
        handleSearchBooks(); // Search logic for books
        handleSearchUsers(); // Search logic for users
    }

    // Method to setup editable columns in the book table
    private void setupEditableBookColumns() {
        // Editable title column
        titleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        titleColumn.setOnEditCommit(event -> {
            Book selectedBook = event.getRowValue();
            String oldTitle = selectedBook.getTitle();
            String newTitle = event.getNewValue();

            // Update the book's title and reflect changes for users who borrowed it
            selectedBook.setTitle(newTitle);
            updateBorrowedBookDetailsForUsers(oldTitle, selectedBook);

            // Save updated data
            saveUpdatedData();
        });

        // Editable author column
        authorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        authorColumn.setOnEditCommit(event -> {
            Book selectedBook = event.getRowValue();
            String oldAuthor = selectedBook.getAuthor();
            String newAuthor = event.getNewValue();

            // Update the book's author and reflect changes for users who borrowed it
            selectedBook.setAuthor(newAuthor);
            updateBorrowedBookDetailsForUsers(oldAuthor, selectedBook);

            // Save updated data
            saveUpdatedData();
        });

        // Editable ISBN column
        isbnColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        isbnColumn.setOnEditCommit(event -> {
            Book selectedBook = event.getRowValue();
            String oldIsbn = selectedBook.getISBN();
            String newIsbn = event.getNewValue();

            // Update the book's ISBN and reflect changes for users who borrowed it
            selectedBook.setISBN(newIsbn);
            updateBorrowedBookDetailsForUsers(oldIsbn, selectedBook);

            // Save updated data
            saveUpdatedData();
        });
    }

    // Method to update borrowed book details for users
    private void updateBorrowedBookDetailsForUsers(String oldDetail, Book updatedBook) {
        for (User user : users) {
            if (user.getBorrowedBooks() != null && user.getBorrowedBooks().contains(updatedBook)) {
                user.getBorrowedBooks().removeIf(book -> book.getTitle().equals(oldDetail) ||
                        book.getAuthor().equals(oldDetail) ||
                        book.getISBN().equals(oldDetail));
                user.getBorrowedBooks().add(updatedBook);
            }
        }
    }

    // Method to save updated book and user data
    private void saveUpdatedData() {
        FileManager.saveBooks(library); // Save updated book list
        FileManager.saveUsers(users);  // Save updated user list
    }

    // Method to setup editable columns in the user table
    private void setupEditableUserColumns() {
        // Editable name column
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> {
            User selectedUser = event.getRowValue();
            String oldName = selectedUser.getName();
            String newName = event.getNewValue();

            // Update the user's name and reflect changes in borrowed book details
            selectedUser.setName(newName);
            updateBorrowedBookBorrowerNames(oldName, newName);

            // Refresh the book table and save updated data
            bookTable.refresh();
            borrowedBooksTable.refresh();
            saveUpdatedData();
        });

        // Editable ID column
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        idColumn.setOnEditCommit(event -> {
            User selectedUser = event.getRowValue();
            selectedUser.setId(event.getNewValue());
            FileManager.saveUsers(users); // Save updated user list
        });
    }

    // Method to update borrower names in borrowed book details
    private void updateBorrowedBookBorrowerNames(String oldName, String newName) {
        for (Book book : library.getBooks()) {
            if (book.getBorrower() != null && book.getBorrower().equals(oldName)) {
                book.setBorrower(newName);
            }
        }
    }

    // Handle Back to Homepage button click
    @FXML
    private void handleBackButton() {
        // Reset all buttons to default, keeping font size at 16px
        backButton.setStyle("-fx-background-color: #3b5998; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageBooksButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold;-fx-font-size: 16px;");
        manageUsersButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageLateReturnBooksButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

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

    // Handle Manage Books button click
    @FXML
    private void handleManageBooks() {
        // Update button styles and display the Book Management view
        backButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageBooksButton.setStyle("-fx-background-color: #3b5998; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageUsersButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageLateReturnBooksButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

        bookManagementView.setVisible(true);
        userManagementView.setVisible(false);
        lateReturnBooksView.setVisible(false);
    }

    // Handle Manage Users button click
    @FXML
    private void handleManageUsers() {
        // Update button styles and display the User Management view
        backButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageBooksButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageUsersButton.setStyle("-fx-background-color: #3b5998; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageLateReturnBooksButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

        userManagementView.setVisible(true);
        bookManagementView.setVisible(false);
        lateReturnBooksView.setVisible(false);
    }

    // Method to handle switching to the Late Return Books management view
    @FXML
    private void handleManageLateReturnBooks() {
        // Update button styles and font size
        backButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageBooksButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageUsersButton.setStyle("-fx-background-color: #8b9dc3; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        manageLateReturnBooksButton.setStyle("-fx-background-color: #3b5998; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");

        // Update visible views
        bookManagementView.setVisible(false);
        userManagementView.setVisible(false);
        lateReturnBooksView.setVisible(true);

        // Ensure the table is up-to-date
        initializeBorrowedBooksTable();
    }

    // Handle Add New Book button click
    @FXML
    private void handleAddBook() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Book");
        dialog.setHeaderText("Enter the details of the new book:");

        // Create dialog content
        VBox dialogContent = new VBox(10);
        TextField titleField = new TextField();
        titleField.setPromptText("Book Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Book Author");
        TextField isbnField = new TextField();
        isbnField.setPromptText("Book ISBN");
        dialogContent.getChildren().addAll(new Label("Title:"), titleField, new Label("Author:"), authorField, new Label("ISBN:"), isbnField);

        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show dialog and process result
        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String isbn = isbnField.getText().trim();

                if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Error", "All fields are required.");
                    return;
                }

                // Add new book to library and table
                Book newBook = new Book(title, author, isbn, true, null, null, null);
                library.addBook(newBook);
                allBooks.add(newBook); // Update the ObservableList
                FileManager.saveBooks(library);
                // Update the TableView
                bookTable.setItems(FXCollections.observableArrayList(library.getBooks()));
            }
        });
    }

    // Handle Delete Book button click
    @FXML
    private void handleDeleteBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            if (!selectedBook.isAvailable()) {
                showAlert(Alert.AlertType.WARNING, "Delete Error", "This book cannot be deleted as it is currently borrowed by a user.");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Book");
            confirmationAlert.setHeaderText("Are you sure you want to delete this book?");
            confirmationAlert.setContentText("Title: " + selectedBook.getTitle() + "\nAuthor: " + selectedBook.getAuthor());

            // Wait for confirmation
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Use the deleteBook method in Library to remove the book
                    boolean isDeleted = library.deleteBook(selectedBook);
                    if (isDeleted) {
                        allBooks.remove(selectedBook); // Remove from the ObservableList
                        FileManager.saveBooks(library); // Save the updated library to the file

                        // Update the TableView
                        bookTable.setItems(FXCollections.observableArrayList(library.getBooks()));
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete the book.");
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a book to delete.");
        }
    }

    @FXML
    private void handleAddUser() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New User");
        dialog.setHeaderText("Enter the details of the new user:");

        // Create dialog content
        VBox dialogContent = new VBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("User Name");
        TextField idField = new TextField();
        idField.setPromptText("User ID");
        dialogContent.getChildren().addAll(new Label("Name:"), nameField, new Label("ID:"), idField);

        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show dialog and process result
        dialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String name = nameField.getText().trim();
                String id = idField.getText().trim();

                // Validate input fields
                if (name.isEmpty() || id.isEmpty()) {
                    showAlert(Alert.AlertType.WARNING, "Input Error", "All fields are required.");
                    return;
                }

                // Ensure user ID is unique
                boolean isIdUnique = users.stream().noneMatch(user -> user.getId().equals(id));
                if (!isIdUnique) {
                    showAlert(Alert.AlertType.WARNING, "ID Error", "The user ID is already taken. Please enter a unique ID.");
                    return;
                }

                // Add new user to the list
                User newUser = new User(name, id);
                users.add(newUser);

                // Save the updated list of users
                FileManager.saveUsers(users);

                // Reload the TableView
                userTable.setItems(FXCollections.observableArrayList(users));
            }
        });
    }

    @FXML
    private void handleDeleteUser() {
        // Get the selected user from the TableView
        User selectedUser = userTable.getSelectionModel().getSelectedItem();

        if (selectedUser != null) {
            // Check if the selected user has borrowed books
            boolean hasBorrowedBooks = library.getBooks().stream()
                    .anyMatch(book -> selectedUser.getName().equals(book.getBorrower()));

            if (hasBorrowedBooks) {
                showAlert(Alert.AlertType.WARNING, "Delete Error", "This user cannot be deleted as they have borrowed books.");
                return;
            }

            // Confirm deletion
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete User");
            confirmationAlert.setHeaderText("Are you sure you want to delete this user?");
            confirmationAlert.setContentText("Name: " + selectedUser.getName() + "\nID: " + selectedUser.getId());

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Remove user from list and TableView
                    users.remove(selectedUser);
                    userTable.getItems().remove(selectedUser);
                    FileManager.saveUsers(users); // Save changes
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "Please select a user to delete.");
        }
    }

    @FXML
    private void handleSearchBooks() {
        // Add listener to filter book list based on search input
        searchBookInput.textProperty().addListener((observable, oldValue, newValue) -> {
            // Use Library's searchBooks method
            List<Book> filteredBooks = library.searchBooks(newValue);
            bookTable.setItems(FXCollections.observableArrayList(filteredBooks)); // Update TableView with filtered list
        });
    }

    @FXML
    private void handleSearchUsers() {
        // Add listener to filter user list based on search input
        searchUserInput.textProperty().addListener((observable, oldValue, newValue) -> {
            FilteredList<User> filteredUsers = new FilteredList<>(FXCollections.observableArrayList(users), user ->
                    user.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                            user.getId().toLowerCase().contains(newValue.toLowerCase())
            );
            userTable.setItems(filteredUsers); // Update TableView with filtered list
        });
    }

    @FXML
    private void initializeBorrowedBooks() {
        // Initialize borrowed books TableView columns
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookIsbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        bookBorrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        bookReturnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    }

    @FXML
    private void displayBorrowedBooks(User user) {
        // Clear existing items in the borrowed books TableView
        borrowedBooksTableView.getItems().clear();

        // Filter books borrowed by the selected user
        List<Book> borrowedBooks = library.getBooks().stream()
                .filter(book -> book.getBorrower() != null && book.getBorrower().equals(user.getName()))
                .collect(Collectors.toList());

        if (borrowedBooks.isEmpty()) {
            // Display placeholder if no books are borrowed
            borrowedBooksTableView.setPlaceholder(new Label("No books borrowed"));
        } else {
            // Update TableView with borrowed books
            borrowedBooksTableView.setItems(FXCollections.observableArrayList(borrowedBooks));
        }
    }


    // Method to setup the late return books table
    private void setupLateReturnBooksTable() {
        borrowedTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowerNameColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getBorrower()));
        borrowDateColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getBorrowDate() != null
                        ? data.getValue().getBorrowDate().toString() : ""));
        returnDateColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getReturnDate() != null
                        ? data.getValue().getReturnDate().toString() : ""));
        fineColumn.setCellValueFactory(data -> {
            LocalDate returnDate = data.getValue().getReturnDate();
            if (returnDate != null && LocalDate.now().isAfter(returnDate)) {
                long daysOverdue = ChronoUnit.DAYS.between(returnDate, LocalDate.now());
                double fine = daysOverdue * 1.0; // Calculate fine at RM1 per overdue day
                // Format the fine to 2 decimal places
                String fineFormatted = String.format("%.2f", fine);
                return new ReadOnlyStringWrapper("RM" + fineFormatted);
            }
            return new ReadOnlyStringWrapper("RM0");
        });
    }

    // Method to initialize the borrowed books table for overdue books
    private void initializeBorrowedBooksTable() {
        ObservableList<Book> overdueBooks = FXCollections.observableArrayList(
                library.getBooks().stream()
                        .filter(book -> book.getReturnDate() != null && LocalDate.now().isAfter(book.getReturnDate()))
                        .collect(Collectors.toList())
        );
        borrowedBooksTable.setItems(overdueBooks);
    }

    // Method to handle renewing a book's return date
    @FXML
    private void handleRenewBook() {
        Book selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            LocalDate now = LocalDate.now();
            selectedBook.setReturnDate(now.plusDays(7)); // Extend return date by 7 days
            FileManager.saveBooks(library); // Save updates to the CSV
            initializeBorrowedBooksTable(); // Refresh table
            showAlert(Alert.AlertType.INFORMATION, "Book Renewed",
                    "The book '" + selectedBook.getTitle() + "' has been renewed for 7 days starting today.");
            bookTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Renew Book", "Please select a book to renew.");
        }
    }

    // Method to handle returning a book
    @FXML
    private void handleReturnBook() {
        Book selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            selectedBook.setAvailable(true);
            selectedBook.setBorrower(null);
            selectedBook.setBorrowDate(null);
            selectedBook.setReturnDate(null); // Reset return date
            FileManager.saveBooks(library); // Save updates to the CSV
            initializeBorrowedBooksTable(); // Refresh table
            showAlert(Alert.AlertType.INFORMATION, "Book Returned",
                    "The book '" + selectedBook.getTitle() + "' has been returned.");
            bookTable.refresh();
        } else {
            showAlert(Alert.AlertType.WARNING, "Return Book", "Please select a book to return.");
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}