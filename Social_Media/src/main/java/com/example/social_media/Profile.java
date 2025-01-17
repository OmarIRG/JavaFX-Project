package com.example.social_media;

import java.time.LocalDateTime;

public class Profile {
    private int profileId;
    private int userId;
    private String fullName;
    private String bio;
    private byte[] profilePicture;
    private LocalDateTime updatedAt;

    public Profile() {}

    public Profile(int profileId, int userId, String fullName,
                   String bio, byte[] profilePicture, LocalDateTime updatedAt) {
        this.profileId = profileId;
        this.userId = userId;
        this.fullName = fullName;
        this.bio = bio;
        this.profilePicture = profilePicture;
        this.updatedAt = updatedAt;
    }

    public int getProfileId() { return profileId; }
    public void setProfileId(int profileId) { this.profileId = profileId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public byte[] getProfilePicture() { return profilePicture; }
    public void setProfilePicture(byte[] profilePicture) { this.profilePicture = profilePicture; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
