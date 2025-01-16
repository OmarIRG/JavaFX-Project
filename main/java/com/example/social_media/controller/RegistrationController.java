package com.example.social_media.controller;

import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegistrationController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final UserService userService = new UserService();

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!");
            return;
        }

        boolean success = userService.registerUser(username, email, password);
        if (success) {
            showAlert("Success", "Registered successfully!");
            // Optionally go back to login
            SceneManager.switchRoot("/com/example/social_media/login.fxml");
        } else {
            showAlert("Error", "Registration failed. Possibly duplicate email/username.");
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchRoot("/com/example/social_media/login.fxml");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
