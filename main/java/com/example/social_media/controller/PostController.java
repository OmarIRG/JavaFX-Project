package com.example.social_media.controller;

import com.example.social_media.Post;
import com.example.social_media.User;
import com.example.social_media.service.PostService;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class PostController {

    @FXML private TextArea contentArea;
    @FXML private ComboBox<String> privacyCombo;
    @FXML private ListView<Post> postsListView;
    @FXML private ImageView imagePreview; // for previewing the chosen image

    private final PostService postService = new PostService();
    private final UserService userService = new UserService();

    private User currentUser;
    private byte[] chosenImageData;
    private ObservableList<Post> postObservableList = FXCollections.observableArrayList();

    // Called from somewhere else (e.g. ProfileController) with user data
    public void initData(User user) {
        this.currentUser = user;
        if (privacyCombo != null) {
            privacyCombo.setValue("public"); // default
        }
        refreshPosts();
    }

    private void refreshPosts() {
        // Load current user’s posts from DB
        List<Post> userPosts = postService.getPostsByUser(currentUser.getUserId());
        postObservableList.setAll(userPosts);
        postsListView.setItems(postObservableList);

        // Show basic info in the list
        postsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Post item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // fetch author username
                    var author = userService.getUserById(item.getUserId());
                    String authorName = (author != null) ? author.getUsername() : "[Unknown]";
                    setText("Posted by: " + authorName + "\n"
                            + "Content: " + item.getContent() + "\n"
                            + "Privacy: " + item.getPrivacyLevel());
                }
            }
        });
    }

    @FXML
    private void handleChooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                chosenImageData = Files.readAllBytes(file.toPath());
                // Show a preview
                imagePreview.setImage(new Image(new ByteArrayInputStream(chosenImageData)));
            } catch (IOException e) {
                showAlert("Error", "Could not read image file: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handlePost() {
        String content = contentArea.getText().trim();
        String privacy = privacyCombo.getValue();
        if (content.isEmpty() && chosenImageData == null) {
            showAlert("Error", "Please enter some text or choose an image first.");
            return;
        }

        // Create the post
        Post post = new Post();
        post.setUserId(currentUser.getUserId());
        post.setContent(content);
        post.setImageData(chosenImageData);
        post.setPrivacyLevel(privacy);

        // Save to DB
        postService.createPost(post);

        // Clear UI
        contentArea.clear();
        chosenImageData = null;
        imagePreview.setImage(null);

        // Refresh the list
        refreshPosts();
    }

    @FXML
    private void handleBack() {
        // Go back to profile screen
        var profileCtrl = SceneManager.switchRoot("/com/example/social_media/profile.fxml");
        if (profileCtrl instanceof ProfileController) {
            ((ProfileController) profileCtrl).initData(currentUser);
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
