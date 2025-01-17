package com.example.social_media.controller;
import javafx.scene.layout.VBox;

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
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.util.List;

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
            private final Button detailsBtn = new Button("View Details");

            @Override
            protected void updateItem(Post item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    User author = userService.getUserById(item.getUserId());
                    String authorName = (author != null) ? author.getUsername() : "[Unknown]";
                    String text = "Posted by: " + authorName + "\n"
                            + "Content: " + item.getContent() + "\n"
                            + "Privacy: " + item.getPrivacyLevel();
                    setText(text);

                    // Show image (bigger size, e.g., width=200)
                    if (item.getImageData() != null) {
                        Image img = new Image(
                                new ByteArrayInputStream(item.getImageData()),
                                200, // width
                                0,
                                true,
                                true
                        );
                        imageView.setImage(img);
                    } else {
                        imageView.setImage(null);
                    }

                    // “View Details” button to open the post detail scene
                    detailsBtn.setOnAction(e -> openPostDetails(item));

                    // Put both image + button in an HBox or something
                    // but for simplicity we can just setGraphic(imageView) or a small layout.
                    // Example: let's just stack them vertically in a VBox
                    VBox layout = new VBox(5, imageView, detailsBtn);
                    setGraphic(layout);
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

    private void openPostDetails(Post post) {
        PostDetailController controller =
                SceneManager.switchRoot("/com/example/social_media/postdetail.fxml");
        if (controller != null) {
            controller.initData(currentUser, post);
        }
    }

    private void setupCommentListView() {
        commentListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Comment item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    User commenter = userService.getUserById(item.getUserId());
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
