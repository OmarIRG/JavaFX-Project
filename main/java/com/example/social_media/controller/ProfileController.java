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
import javafx.event.ActionEvent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ProfileController {

    @FXML private TextField fullNameField;
    @FXML private TextArea bioArea;
    @FXML private ImageView profileImageView;

    private final ProfileService profileService = new ProfileService();
    private Profile currentProfile;
    private User currentUser;
    private byte[] chosenPictureData = null;

    // Called after switching root from SceneManager
    public void initData(User user) {
        this.currentUser = user;
        currentProfile = profileService.getProfileByUserId(user.getUserId());
        if (currentProfile == null) {
            currentProfile = new Profile();
            currentProfile.setUserId(user.getUserId());
        } else {
            fullNameField.setText(currentProfile.getFullName());
            bioArea.setText(currentProfile.getBio());
            if (currentProfile.getProfilePicture() != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(currentProfile.getProfilePicture());
                profileImageView.setImage(new Image(bis));
            }
        }
    }

    @FXML
    private void handleChooseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null); // or get window if needed
        if (file != null) {
            try {
                chosenPictureData = Files.readAllBytes(file.toPath());
                profileImageView.setImage(new Image(new ByteArrayInputStream(chosenPictureData)));
            } catch (IOException e) {
                showAlert("Error", "Cannot read file.");
            }
        }
    }

    @FXML
    private void handleSaveProfile(ActionEvent event) {
        currentProfile.setFullName(fullNameField.getText());
        currentProfile.setBio(bioArea.getText());
        if (chosenPictureData != null) {
            currentProfile.setProfilePicture(chosenPictureData);
        }
        profileService.saveProfile(currentProfile);
        showAlert("Success", "Profile saved!");
    }

    @FXML
    private void handleGoToPosts(ActionEvent event) {
        PostController postCtrl = SceneManager.switchRoot("/com/example/social_media/post.fxml");
        if (postCtrl != null) {
            postCtrl.initData(currentUser);
        }
    }

    @FXML
    private void handleGoToNewsFeed(ActionEvent event) {
        NewsFeedController nfc = SceneManager.switchRoot("/com/example/social_media/newsfeed.fxml");
        if (nfc != null) {
            nfc.initData(currentUser);
        }
    }

    @FXML
    private void handleGoToFriends(ActionEvent event) {
        FriendsController fc = SceneManager.switchRoot("/com/example/social_media/friends.fxml");
        if (fc != null) {
            fc.initData(currentUser);
        }
    }

    @FXML
    private void handleSignOut(ActionEvent event) {
        SceneManager.switchRoot("/com/example/social_media/login.fxml");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
