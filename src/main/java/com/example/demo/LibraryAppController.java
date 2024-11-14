package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class LibraryAppController {

    @FXML
    private Button adminButton;

    @FXML
    private Button userButton;

    // Handle Admin button click: Redirect to Admin Page
    @FXML
    private void handleAdminButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminPage.fxml"));
            Scene adminScene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) adminButton.getScene().getWindow();
            stage.setScene(adminScene);
            stage.setTitle("Admin Management Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle User button click: Redirect to User Page
    @FXML
    private void handleUserButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserPage.fxml"));
            Scene userScene = new Scene(loader.load(), 600, 400);
            Stage stage = (Stage) userButton.getScene().getWindow();
            stage.setScene(userScene);
            stage.setTitle("User Management Page");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
