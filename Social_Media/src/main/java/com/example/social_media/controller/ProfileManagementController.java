package com.example.social_media.controller;

import com.example.social_media.Profile;
import com.example.social_media.User;
import com.example.social_media.service.ProfileService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ProfileManagementController {

    @FXML private Label usernameLabel;
    @FXML private TextField fullNameField;     // ADDED for full name
    @FXML private TextArea bioField;
    @FXML private ImageView profileImageView;

    private final ProfileService profileService = new ProfileService();
    private User currentUser;
    private Profile currentProfile;
    private byte[] chosenPictureData;

    public void initData(User user) {
        this.currentUser = user;
        if (user != null) {
            // Display the user's username
            usernameLabel.setText(user.getUsername());
            // Load the existing profile or create a new one
            loadProfile(user.getUserId());
        }
    }

    private void loadProfile(int userId) {
        currentProfile = profileService.getProfileByUserId(userId);
        if (currentProfile == null) {
            currentProfile = new Profile();
            currentProfile.setUserId(userId);
        } else {
            // Fill UI fields with existing data
            if (currentProfile.getFullName() != null) {
                fullNameField.setText(currentProfile.getFullName());
            }
            if (currentProfile.getBio() != null) {
                bioField.setText(currentProfile.getBio());
            }
            if (currentProfile.getProfilePicture() != null) {
                profileImageView.setImage(new Image(
                        new ByteArrayInputStream(currentProfile.getProfilePicture())
                ));
            }
        }
    }

    @FXML
    private void handleChooseProfilePic() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                chosenPictureData = Files.readAllBytes(file.toPath());
                profileImageView.setImage(new Image(new ByteArrayInputStream(chosenPictureData)));
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Could not read image file: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    private void handleSaveProfile() {
        // Just to confirm it's triggered
        System.out.println("DEBUG: handleSaveProfile triggered!");

        if (currentProfile != null) {
            // Update the profile object with the new data
            currentProfile.setFullName(fullNameField.getText()); // set the new full name
            currentProfile.setBio(bioField.getText());
            if (chosenPictureData != null) {
                currentProfile.setProfilePicture(chosenPictureData);
            }

            // Persist to DB (create or update)
            profileService.saveProfile(currentProfile);

            // Reload the updated profile from DB to confirm changes
            currentProfile = profileService.getProfileByUserId(currentProfile.getUserId());
            if (currentProfile != null) {
                // Overwrite fields in case the DB changed them
                fullNameField.setText(currentProfile.getFullName());
                bioField.setText(currentProfile.getBio());
                if (currentProfile.getProfilePicture() != null) {
                    profileImageView.setImage(new Image(
                            new ByteArrayInputStream(currentProfile.getProfilePicture())
                    ));
                }
            }

            new Alert(Alert.AlertType.INFORMATION, "Profile saved successfully!").showAndWait();
        } else {
            new Alert(Alert.AlertType.ERROR, "No profile object to save.").showAndWait();
        }
    }

    @FXML
    private void handleBack() {
        var pc = SceneManager.switchRoot("/com/example/social_media/profile.fxml");
        if (pc instanceof ProfileController && currentUser != null) {
            ((ProfileController) pc).initData(currentUser);
        }
    }
}
