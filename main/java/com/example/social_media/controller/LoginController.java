package com.example.social_media.controller;

import com.example.social_media.User;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField txtUser;  // now holds the email
    @FXML private PasswordField txtPass;

    private final UserService userService = new UserService();

    @FXML
    private void handleLoginAction() {
        if (txtUser.getText().isEmpty() || txtPass.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in all fields").showAndWait();
            return;
        }
        // We authenticate by email now
        User user = userService.authenticate(txtUser.getText(), txtPass.getText());
        if (user != null) {
            ProfileController pc = (ProfileController)
                    SceneManager.switchRoot("/com/example/social_media/profile.fxml");
            if (pc != null) {
                pc.initData(user);
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Invalid email or password.").showAndWait();
        }
    }

    @FXML
    private void navigateToRegister() {
        SceneManager.switchRoot("/com/example/social_media/registration.fxml");
    }
}
