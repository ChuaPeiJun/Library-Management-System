package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Optional;

public class LibraryAppController {

    @FXML
    private Button adminButton;

    @FXML
    private Button userButton;

    private static final String ADMIN_PASSWORD = "123456"; // Define your password here

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

    // Method to handle successful admin access
    private void accessAdminPage() {
        // Add your redirection logic here
        System.out.println("Access granted to admin page.");
    }

    // Handle User button click: Redirect to User Page
    @FXML
    private void handleUserButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserPage.fxml"));
            Scene userScene = new Scene(loader.load(), 1200, 800);
            Stage stage = (Stage) userButton.getScene().getWindow();
            stage.setScene(userScene);
            stage.setTitle("User Management Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
