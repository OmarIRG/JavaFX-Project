package com.example.social_media.controller;

import com.example.social_media.Profile;
import com.example.social_media.Post;
import com.example.social_media.User;
import com.example.social_media.service.ProfileService;
import com.example.social_media.service.PostService;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.stream.Collectors;

public class FriendProfileController {

    @FXML private ImageView friendImageView;
    @FXML private Label fullNameLabel;
    @FXML private Label bioLabel;
    @FXML private ListView<Post> postsListView;

    private final ProfileService profileService = new ProfileService();
    private final PostService postService = new PostService();
    private final UserService userService = new UserService();

    private User currentUser;
    private int friendUserId;
    private ObservableList<Post> friendPosts = FXCollections.observableArrayList();

    public void initData(User current, int friendId) {
        this.currentUser = current;
        this.friendUserId = friendId;

        // Load friend's profile
        Profile friendProfile = profileService.getProfileByUserId(friendUserId);
        if (friendProfile != null) {
            if (friendProfile.getProfilePicture() != null) {
                friendImageView.setImage(new Image(new ByteArrayInputStream(friendProfile.getProfilePicture())));
            }
            fullNameLabel.setText(friendProfile.getFullName() != null
                    ? friendProfile.getFullName() : "[No Name]");
            bioLabel.setText(friendProfile.getBio() != null
                    ? friendProfile.getBio() : "[No bio]");
        } else {
            fullNameLabel.setText("[No profile]");
            bioLabel.setText("");
        }

        // Load friend's posts, skipping "private"
        List<Post> rawPosts = postService.getPostsByUser(friendUserId);
        List<Post> visiblePosts = rawPosts.stream()
                .filter(p -> !"private".equals(p.getPrivacyLevel()))
                .collect(Collectors.toList());

        friendPosts.setAll(visiblePosts);
        postsListView.setItems(friendPosts);

        // Show each post + "View Details" button
        postsListView.setCellFactory(lv -> new ListCell<>() {
            private final Button detailsBtn = new Button("View Details");

            @Override
            protected void updateItem(Post item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    User friendUser = userService.getUserById(item.getUserId());
                    String friendName = (friendUser != null) ? friendUser.getUsername() : "[Unknown]";
                    setText("Posted by: " + friendName
                            + "\nContent: " + item.getContent()
                            + "\nPrivacy: " + item.getPrivacyLevel());

                    detailsBtn.setOnAction(e -> openPostDetails(item));
                    setGraphic(detailsBtn);
                }
            }
        });
    }

    private void openPostDetails(Post post) {
        PostDetailController controller =
                SceneManager.switchRoot("/com/example/social_media/postdetail.fxml");
        if (controller != null) {
            controller.initData(currentUser, post);
        }
    }

    @FXML
    private void handleBackToFriends() {
        var fc = SceneManager.switchRoot("/com/example/social_media/friends.fxml");
        if (fc instanceof FriendsController) {
            ((FriendsController) fc).initData(currentUser);
        }
    }
}
