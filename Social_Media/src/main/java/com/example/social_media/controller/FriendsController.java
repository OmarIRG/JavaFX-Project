package com.example.social_media.controller;

import com.example.social_media.Friendship;
import com.example.social_media.User;
import com.example.social_media.service.FriendshipService;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class FriendsController {

    @FXML private TextField friendIdField; // now actually a username
    @FXML private ListView<Friendship> pendingListView;
    @FXML private ListView<Friendship> friendsListView;

    private final FriendshipService friendshipService = new FriendshipService();
    private final UserService userService = new UserService();

    private User currentUser;
    private ObservableList<Friendship> pendingRequests = FXCollections.observableArrayList();
    private ObservableList<Friendship> acceptedFriends = FXCollections.observableArrayList();

    public void initData(User user) {
        this.currentUser = user;
        refreshPending();
        refreshFriends();
    }

    private void refreshPending() {
        List<Friendship> list = friendshipService.getPending(currentUser.getUserId());
        pendingRequests.setAll(list);
        pendingListView.setItems(pendingRequests);
        pendingListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Friendship item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    var sender = userService.getUserById(item.getUserId1());
                    String senderName = (sender != null) ? sender.getUsername() : "[Unknown]";
                    setText("Request #" + item.getFriendshipId() + " from " + senderName);
                }
            }
        });
    }

    private void refreshFriends() {
        List<Friendship> accepted = friendshipService.getFriends(currentUser.getUserId());
        acceptedFriends.setAll(accepted);
        friendsListView.setItems(acceptedFriends);
        friendsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Friendship item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int friendId = (item.getUserId1() == currentUser.getUserId())
                            ? item.getUserId2() : item.getUserId1();
                    var friendUser = userService.getUserById(friendId);
                    String friendName = (friendUser != null) ? friendUser.getUsername() : "[Unknown]";
                    setText("Friendship #" + item.getFriendshipId() + " with: " + friendName);
                }
            }
        });
    }

    @FXML
    private void handleSendRequest(ActionEvent event) {
        String friendUsername = friendIdField.getText().trim();
        if (friendUsername.isEmpty()) {
            showAlert("Error", "Please enter friend's username.");
            return;
        }

        // look up that user
        User friendUser = userService.getUserByUsername(friendUsername);
        if (friendUser == null) {
            showAlert("Error", "No user found with username: " + friendUsername);
            return;
        }

        boolean ok = friendshipService.sendFriendRequest(currentUser.getUserId(), friendUser.getUserId());
        if (ok) {
            showAlert("Success", "Request sent!");
            refreshPending();
        } else {
            showAlert("Error", "Failed or duplicate request.");
        }
    }

    @FXML
    private void handleAcceptRequest(ActionEvent event) {
        Friendship selected = pendingListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a pending request first.");
            return;
        }
        boolean ok = friendshipService.acceptFriendRequest(selected.getFriendshipId());
        if (ok) {
            showAlert("Success", "Request accepted!");
            refreshPending();
            refreshFriends();
        } else {
            showAlert("Error", "Could not accept request.");
        }
    }

    @FXML
    private void handleViewFriendProfile(ActionEvent event) {
        Friendship selected = friendsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a friend to view their profile.");
            return;
        }
        int friendId = (selected.getUserId1() == currentUser.getUserId())
                ? selected.getUserId2() : selected.getUserId1();

        var friendProfileCtrl = SceneManager.switchRoot("/com/example/social_media/friendprofile.fxml");
        if (friendProfileCtrl instanceof FriendProfileController) {
            ((FriendProfileController) friendProfileCtrl).initData(currentUser, friendId);
        }
    }

    @FXML
    private void handleRemoveFriend(ActionEvent event) {
        Friendship selected = friendsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a friend to remove.");
            return;
        }
        boolean ok = friendshipService.removeFriend(selected.getFriendshipId());
        if (ok) {
            showAlert("Removed", "You are no longer friends with that user.");
            refreshFriends();
        } else {
            showAlert("Error", "Could not remove friend.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        ProfileController pc = (ProfileController) SceneManager.switchRoot("/com/example/social_media/profile.fxml");
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
