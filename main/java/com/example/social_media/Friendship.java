package com.example.social_media;

import java.time.LocalDateTime;

public class Friendship {
    private int friendshipId;
    private int userId1;
    private int userId2;
    private String status; // pending, accepted
    private LocalDateTime createdAt;

    public Friendship() {}

    public Friendship(int friendshipId, int userId1, int userId2,
                      String status, LocalDateTime createdAt) {
        this.friendshipId = friendshipId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getFriendshipId() { return friendshipId; }
    public void setFriendshipId(int friendshipId) { this.friendshipId = friendshipId; }

    public int getUserId1() { return userId1; }
    public void setUserId1(int userId1) { this.userId1 = userId1; }

    public int getUserId2() { return userId2; }
    public void setUserId2(int userId2) { this.userId2 = userId2; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
