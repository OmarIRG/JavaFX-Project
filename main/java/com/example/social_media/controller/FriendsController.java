package com.example.social_media.controller;

import com.example.social_media.Friendship;
import com.example.social_media.User;
import com.example.social_media.service.FriendshipService;
import com.example.social_media.service.UserService;
import com.example.social_media.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FriendsController {

    @FXML private TextField friendIdField;
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
        String friendIdStr = friendIdField.getText().trim();
        if (friendIdStr.isEmpty()) {
            showAlert("Error", "Please enter user_id.");
            return;
        }
        try {
            int friendId = Integer.parseInt(friendIdStr);
            boolean ok = friendshipService.sendFriendRequest(currentUser.getUserId(), friendId);
            if (ok) {
                showAlert("Success", "Request sent!");
                refreshPending();
            } else {
                showAlert("Error", "Failed or duplicate request.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "User_id must be integer.");
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

    // 1) View a friend's profile & posts
    @FXML
    private void handleViewFriendProfile(ActionEvent event) {
        Friendship selected = friendsListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a friend to view their profile.");
            return;
        }
        int friendId = (selected.getUserId1() == currentUser.getUserId())
                ? selected.getUserId2() : selected.getUserId1();

        // Switch to friendprofile.fxml
        var friendProfileCtrl = SceneManager.switchRoot("/com/example/social_media/friendprofile.fxml");
        if (friendProfileCtrl instanceof FriendProfileController) {
            ((FriendProfileController) friendProfileCtrl).initData(currentUser, friendId);
        }
    }

    // 2) Remove friend from DB
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
        var pc = SceneManager.switchRoot("/com/example/social_media/profile.fxml");
        if (pc instanceof ProfileController profileController) {
            profileController.initData(currentUser);
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
