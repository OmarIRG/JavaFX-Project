package com.example.social_media;

import java.time.LocalDateTime;

public class Post {
    private int postId;
    private int userId;
    private String content;
    private byte[] imageData;
    private String privacyLevel;
    private LocalDateTime createdAt;

    public Post() {}

    public Post(int postId, int userId, String content, byte[] imageData,
                String privacyLevel, LocalDateTime createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.imageData = imageData;
        this.privacyLevel = privacyLevel;
        this.createdAt = createdAt;
    }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    public String getPrivacyLevel() { return privacyLevel; }
    public void setPrivacyLevel(String privacyLevel) { this.privacyLevel = privacyLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
