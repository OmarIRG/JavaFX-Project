package com.example.social_media.controller;

import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {

    @FXML private TextField txtUser;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPass;
    @FXML private PasswordField txtPassConfirm;

    private final UserService userService = new UserService();

    @FXML
    private void handleRegister() {
        if (txtUser.getText().isEmpty() ||
                txtEmail.getText().isEmpty() ||
                txtPass.getText().isEmpty() ||
                txtPassConfirm.getText().isEmpty()) {

            new Alert(Alert.AlertType.ERROR, "Please fill all fields").showAndWait();
            return;
        }
        if (!txtPass.getText().equals(txtPassConfirm.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match").showAndWait();
            return;
        }

        boolean success = userService.registerUser(txtUser.getText(), txtEmail.getText(), txtPass.getText());
        if (success) {
            new Alert(Alert.AlertType.INFORMATION, "Registration Successful").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "Username or Email already exists!").showAndWait();
        }
    }

    @FXML
    private void handleBack() {
        SceneManager.switchRoot("/com/example/social_media/login.fxml");
    }
}
