package com.example.social_media.controller;

import com.example.social_media.Post;
import com.example.social_media.Profile;
import com.example.social_media.User;
import com.example.social_media.service.PostService;
import com.example.social_media.service.ProfileService;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class ProfileController {

    @FXML private Text userHeader;
    @FXML private TextField searchField;

    // Replaces old postField with a TextArea + image preview
    @FXML private TextArea postContentArea;
    @FXML private ImageView postImagePreview;

    // The bottom partial feed
    @FXML private ListView<Post> recentFeedListView;

    @FXML private ImageView topProfileImage; // user’s profile pic on top bar

    private User currentUser;
    private final UserService userService = new UserService();
    private final ProfileService profileService = new ProfileService();
    private final PostService postService = new PostService();

    // Store chosen image bytes temporarily
    private byte[] chosenPostImageData;

    public void initData(User user) {
        this.currentUser = user;
        if (user != null) {
            userHeader.setText("Hello " + user.getUsername());

            // Load the user's profile & picture
            Profile p = profileService.getProfileByUserId(user.getUserId());
            if (p != null && p.getProfilePicture() != null) {
                topProfileImage.setImage(new Image(
                        new ByteArrayInputStream(p.getProfilePicture())
                ));
            }

            // Load a small feed (e.g., user's posts or public feed)
            refreshRecentFeed();
        }
    }

    private void refreshRecentFeed() {
        // Example: let's load user's own posts + show them in the recent feed
        List<Post> userPosts = postService.getPostsByUser(currentUser.getUserId());
        ObservableList<Post> data = FXCollections.observableArrayList(userPosts);
        recentFeedListView.setItems(data);

        // Custom cell to show the post and a "View Details" button
        recentFeedListView.setCellFactory(lv -> new ListCell<>() {
            private final Button detailsBtn = new Button("View Details");

            @Override
            protected void updateItem(Post item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Show text for the post
                    String content = (item.getContent() != null)
                            ? item.getContent()
                            : "[No text]";
                    setText("Post ID: " + item.getPostId()
                            + "\n" + content);

                    // On click, open the postdetail screen
                    detailsBtn.setOnAction(e -> openPostDetails(item));

                    setGraphic(detailsBtn);
                }
            }
        });
    }

    private void openPostDetails(Post post) {
        // Switch scene to postdetail.fxml
        PostDetailController controller =
                SceneManager.switchRoot("/com/example/social_media/postdetail.fxml");
        if (controller != null) {
            controller.initData(currentUser, post);
        }
    }

    @FXML
    private void handleChoosePostImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                chosenPostImageData = Files.readAllBytes(file.toPath());
                postImagePreview.setImage(new Image(new ByteArrayInputStream(chosenPostImageData)));
            } catch (IOException e) {
                showAlert("Error", "Could not read image file: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCreatePost() {
        String content = postContentArea.getText().trim();
        if (content.isEmpty() && chosenPostImageData == null) {
            showAlert("Error", "Please enter some text or choose an image first.");
            return;
        }

        // Create the new Post
        Post post = new Post();
        post.setUserId(currentUser.getUserId());
        post.setContent(content);
        post.setImageData(chosenPostImageData);
        post.setPrivacyLevel("public"); // or you can add a combo box for privacy

        // Save to DB
        postService.createPost(post);

        // Clear fields
        postContentArea.clear();
        chosenPostImageData = null;
        postImagePreview.setImage(null);

        // Refresh feed so we see the newly added post
        refreshRecentFeed();
        showAlert("Success", "Post created successfully!");
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        if (query.isEmpty()) {
            showAlert("Warning", "Please enter something to search.");
            return;
        }
        showAlert("Search", "Searching for: " + query);
    }

    @FXML
    private void showProfile() {
        ProfileManagementController pmc = (ProfileManagementController)
                SceneManager.switchRoot("/com/example/social_media/profilemanagement.fxml");
        if (pmc != null && currentUser != null) {
            pmc.initData(currentUser);
        }
    }

    @FXML
    private void goToNewsFeed() {
        NewsFeedController nfc = (NewsFeedController)
                SceneManager.switchRoot("/com/example/social_media/newsfeed.fxml");
        if (nfc != null && currentUser != null) {
            nfc.initData(currentUser);
        }
    }

    @FXML
    private void goToFriends() {
        FriendsController fc = (FriendsController)
                SceneManager.switchRoot("/com/example/social_media/friends.fxml");
        if (fc != null && currentUser != null) {
            fc.initData(currentUser);
        }
    }

    @FXML
    private void handleLogout() {
        SceneManager.switchRoot("/com/example/social_media/login.fxml");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
