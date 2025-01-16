package com.example.social_media.controller;

import com.example.social_media.User;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    private final UserService userService = new UserService();

    @FXML
    private void handleLoginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Validation Error", "Email or Password cannot be empty.");
            return;
        }

        User user = userService.authenticate(email, password);
        if (user != null) {
            // Switch to profile
            ProfileController profileCtrl = SceneManager.switchRoot("/com/example/social_media/profile.fxml");
            if (profileCtrl != null) {
                profileCtrl.initData(user);
            }
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    @FXML
    private void navigateToRegister() {
        SceneManager.switchRoot("/com/example/social_media/registration.fxml");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
