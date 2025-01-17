package com.example.social_media.controller;

import com.example.social_media.Post;
import com.example.social_media.Comment;
import com.example.social_media.User;
import com.example.social_media.service.CommentService;
import com.example.social_media.service.LikeService;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.List;

public class PostDetailController {

    @FXML private ImageView postImageView;
    @FXML private TextArea postTextArea;
    @FXML private Label likesCountLabel;
    @FXML private ListView<User> likesListView;
    @FXML private ListView<Comment> commentsListView;
    @FXML private TextField commentField;

    private User currentUser;
    private Post currentPost;

    private final UserService userService = new UserService();
    private final CommentService commentService = new CommentService();
    private final LikeService likeService = new LikeService();

    // Initialize with user + post
    public void initData(User user, Post post) {
        this.currentUser = user;
        this.currentPost = post;

        // Show post content
        postTextArea.setText(post.getContent());
        if (post.getImageData() != null) {
            postImageView.setImage(new Image(
                    new ByteArrayInputStream(post.getImageData())
            ));
        }

        // Show like count
        refreshLikes();
        // Show comments
        refreshComments();
    }

    private void refreshLikes() {
        int likeCount = likeService.countLikes(currentPost.getPostId());
        likesCountLabel.setText(String.valueOf(likeCount));

        // Retrieve the users who liked the post
        List<User> likers = likeService.getUsersWhoLikedPost(currentPost.getPostId());
        likesListView.getItems().setAll(likers);
        // Show each user by their username
        likesListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getUsername());
                }
            }
        });
    }

    private void refreshComments() {
        List<Comment> comments = commentService.getCommentsForPost(currentPost.getPostId());
        commentsListView.getItems().setAll(comments);

        commentsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Comment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    User commenter = userService.getUserById(item.getUserId());
                    String commenterName = (commenter != null)
                            ? commenter.getUsername() : "[Unknown]";
                    setText(commenterName + ": " + item.getContent());
                }
            }
        });
    }

    @FXML
    private void handleLikePost() {
        boolean success = likeService.addLike(currentPost.getPostId(), currentUser.getUserId());
        if (!success) {
            showAlert("Error", "You may have already liked this post!");
            return;
        }
        refreshLikes();
    }

    @FXML
    private void handleAddComment() {
        String text = commentField.getText().trim();
        if (text.isEmpty()) {
            showAlert("Error", "Comment cannot be empty.");
            return;
        }
        Comment c = new Comment();
        c.setPostId(currentPost.getPostId());
        c.setUserId(currentUser.getUserId());
        c.setContent(text);
        commentService.addComment(c);

        commentField.clear();
        refreshComments();
    }

    @FXML
    private void handleBack() {
        // Return to the newsfeed or profile, whichever you prefer:
        // Here we'll go back to profile.
        SceneManager.switchRoot("/com/example/social_media/profile.fxml");
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
