package com.example.social_media.controller;

import com.example.social_media.Post;
import com.example.social_media.Comment;
import com.example.social_media.User;
import com.example.social_media.service.PostService;
import com.example.social_media.service.CommentService;
import com.example.social_media.service.LikeService;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;

import java.io.ByteArrayInputStream;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class NewsFeedController {

    @FXML private ListView<Post> feedListView;
    @FXML private ListView<Comment> commentListView;
    @FXML private TextField commentField;
    @FXML private Label likesLabel;

    private final PostService postService = new PostService();
    private final CommentService commentService = new CommentService();
    private final LikeService likeService = new LikeService();
    private final UserService userService = new UserService();

    private User currentUser;
    private ObservableList<Post> feedPosts = FXCollections.observableArrayList();
    private ObservableList<Comment> postComments = FXCollections.observableArrayList();

    public void initData(User user) {
        this.currentUser = user;
        refreshFeed();
        setupFeedListView();
        setupCommentListView();
    }

    private void refreshFeed() {
        List<Post> posts = postService.getAllPublicPosts();
        feedPosts.setAll(posts);
        feedListView.setItems(feedPosts);
    }

    private void setupFeedListView() {
        feedListView.setCellFactory(lv -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Post item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    String authorName = "[Unknown]";
                    var author = userService.getUserById(item.getUserId());
                    if (author != null) {
                        authorName = author.getUsername();
                    }
                    String text = "Posted by: " + authorName + "\n"
                            + "Content: " + item.getContent() + "\n"
                            + "Privacy: " + item.getPrivacyLevel();
                    setText(text);

                    if (item.getImageData() != null) {
                        Image img = new Image(new ByteArrayInputStream(item.getImageData()), 50, 0, true, true);
                        imageView.setImage(img);
                        setGraphic(imageView);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        feedListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadComments(newVal);
                int likeCount = likeService.countLikes(newVal.getPostId());
                likesLabel.setText("Likes: " + likeCount);
            }
        });
    }

    private void setupCommentListView() {
        commentListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Comment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    var commenter = userService.getUserById(item.getUserId());
                    String commenterName = (commenter != null) ? commenter.getUsername() : "[Unknown]";
                    setText(commenterName + ": " + item.getContent());
                }
            }
        });
    }

    private void loadComments(Post post) {
        List<Comment> comments = commentService.getCommentsForPost(post.getPostId());
        postComments.setAll(comments);
        commentListView.setItems(postComments);
    }

    @FXML
    private void handleAddComment(ActionEvent event) {
        Post selected = feedListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "No post selected.");
            return;
        }
        String txt = commentField.getText().trim();
        if (txt.isEmpty()) {
            showAlert("Error", "Comment cannot be empty.");
            return;
        }
        Comment c = new Comment();
        c.setPostId(selected.getPostId());
        c.setUserId(currentUser.getUserId());
        c.setContent(txt);
        commentService.addComment(c);
        commentField.clear();
        loadComments(selected);
    }

    @FXML
    private void handleLike(ActionEvent event) {
        Post selected = feedListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "No post selected.");
            return;
        }
        boolean success = likeService.addLike(selected.getPostId(), currentUser.getUserId());
        if (success) {
            int newCount = likeService.countLikes(selected.getPostId());
            likesLabel.setText("Likes: " + newCount);
        } else {
            showAlert("Error", "You may have already liked this post.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        ProfileController pc = SceneManager.switchRoot("/com/example/social_media/profile.fxml");
        if (pc != null) {
            pc.initData(currentUser);
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
