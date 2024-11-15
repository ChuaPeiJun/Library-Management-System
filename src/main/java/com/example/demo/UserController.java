package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class UserController {

    @FXML
    private Button borrowBookButton;
    @FXML
    private Button returnBookButton;
    @FXML
    private Button searchBookButton;
    @FXML
    private Button backButton;

    // Handle Borrow Book button click
    @FXML
    private void handleBorrowBook() {
        showAlert(Alert.AlertType.INFORMATION, "Borrow Book", "Borrow book functionality goes here.");
    }

    // Handle Return Book button click
    @FXML
    private void handleReturnBook() {
        showAlert(Alert.AlertType.INFORMATION, "Return Book", "Return book functionality goes here.");
    }

    // Handle Search Book button click
    @FXML
    private void handleSearchBook() {
        showAlert(Alert.AlertType.INFORMATION, "Search Book", "Search book functionality goes here.");
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
