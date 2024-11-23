package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class LibraryAppController {

    @FXML
    private Button adminButton;

    @FXML
    private Button userButton;

    private static final String ADMIN_PASSWORD = "123456"; // Define your password here

    private Library library = new Library();
    private ArrayList<User> users = new ArrayList<>();

    // Initialize method to load users from the file
    public void initialize() {
        // Assuming FileManager.loadData is the method that loads users from a file
        FileManager.loadData(library);
        FileManager.loadUsers(users);
    }

    // Handle Admin button click: Redirect to Admin Page
    @FXML
    private void handleAdminButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Admin Authentication");
        dialog.setHeaderText("Admin Access Required");
        dialog.setContentText("Please enter the admin password:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            // Check if the provided password matches the defined admin password
            if (result.get().equals(ADMIN_PASSWORD)) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPage.fxml"));
                    Scene adminScene = new Scene(loader.load(), 1200, 800);
                    Stage stage = (Stage) adminButton.getScene().getWindow();
                    stage.setScene(adminScene);
                    stage.setTitle("Admin Management Page");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Show an alert if the password is incorrect
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Authentication Failed");
                alert.setHeaderText("Invalid Password");
                alert.setContentText("The password you entered is incorrect. Please try again.");
                alert.showAndWait();
            }
        }
    }

    // Handle User button click: Redirect to User Page
    @FXML
    private void handleUserButton() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("User Login");
        dialog.setHeaderText("Please enter your User ID");
        dialog.setContentText("User ID:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String userId = result.get().trim();

            if (!userId.isEmpty()) {
                // Find the user by ID
                User foundUser = users.stream()
                        .filter(user -> user.getId().equals(userId))
                        .findFirst()
                        .orElse(null);

                if (foundUser != null) {
                    try {
                        // Set the current user
                        UserManager.setCurrentUser(foundUser);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserPage.fxml"));
                        Scene userScene = new Scene(loader.load(), 1200, 800);

                        Stage stage = (Stage) userButton.getScene().getWindow();
                        stage.setScene(userScene);
                        stage.setTitle("User Management Page");
                    } catch (IOException e) {
                        System.err.println("Error loading UserPage.fxml: " + e.getMessage());
                        e.printStackTrace();

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Page Load Failed");
                        alert.setContentText("There was an issue loading the User Management Page. Please try again.");
                        alert.showAndWait();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Authentication Failed");
                    alert.setHeaderText("User Not Found");
                    alert.setContentText("The User ID you entered is not found. Please try again.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Input");
                alert.setHeaderText("User ID Cannot Be Empty");
                alert.setContentText("Please enter a valid User ID.");
                alert.showAndWait();
            }
        }
    }
}
