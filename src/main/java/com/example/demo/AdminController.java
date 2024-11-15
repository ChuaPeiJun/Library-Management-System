package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AdminController {

    @FXML
    private Button addBookButton;
    @FXML
    private Button searchBookButton;
    @FXML
    private Button viewBookListButton;
    @FXML
    private Button addUserButton;
    @FXML
    private Button searchUserButton;
    @FXML
    private Button viewUserListButton;
    @FXML
    private Button backButton;

    // Handle Add Book button click
    @FXML
    private void handleAddBook() {
        showAlert(Alert.AlertType.INFORMATION, "Add Book", "Add book functionality goes here.");
    }

    // Handle Search Book button click
    @FXML
    private void handleSearchBook() {
        showAlert(Alert.AlertType.INFORMATION, "Search Book", "Search book functionality goes here.");
    }

    // Handle View Book List button click
    @FXML
    private void handleViewBookList() {
        showAlert(Alert.AlertType.INFORMATION, "View Book List", "View book list functionality goes here.");
    }

    // Handle Add User button click
    @FXML
    private void handleAddUser() {
        showAlert(Alert.AlertType.INFORMATION, "Add User", "Add user functionality goes here.");
    }

    // Handle Search User button click
    @FXML
    private void handleSearchUser() {
        showAlert(Alert.AlertType.INFORMATION, "Search User", "Search user functionality goes here.");
    }

    // Handle View User List button click
    @FXML
    private void handleViewUserList() {
        showAlert(Alert.AlertType.INFORMATION, "View User List", "View user list functionality goes here.");
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

    // Utility method to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
