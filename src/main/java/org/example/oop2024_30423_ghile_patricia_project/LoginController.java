package org.example.oop2024_30423_ghile_patricia_project;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import org.example.oop2024_30423_ghile_patricia_project.library.LibraryInventory;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;

    private LibraryInventory libraryInventory;

    public LoginController() {
        this.libraryInventory = new LibraryInventory();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (libraryInventory.authenticateUser(username, password)) {

            showAlert("Login successful", "Welcome! ");

            loadLibraryWindow();
        } else {

            showAlert("Login failed", "Invalid username or password.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadLibraryWindow() {
        try {
            //load the inventory FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/oop2024_30423_ghile_patricia_project/gui.fxml"));
            Scene libraryScene = new Scene(loader.load());

            //get the current stage and set the inventory scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(libraryScene);
            stage.setTitle("Library Management");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the inventory screen.");
        }
    }
}
