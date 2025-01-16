package com.example.social_media.service;

import com.example.social_media.Profile;
import com.example.social_media.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;

public class ProfileService {

    public Profile getProfileByUserId(int userId) {
        String sql = "SELECT profile_id, user_id, full_name, bio, profile_picture, updated_at "
                + "FROM profiles WHERE user_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Profile p = new Profile();
                p.setProfileId(rs.getInt("profile_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setFullName(rs.getString("full_name"));
                p.setBio(rs.getString("bio"));
                p.setProfilePicture(rs.getBytes("profile_picture"));
                Timestamp ts = rs.getTimestamp("updated_at");
                if (ts != null) {
                    p.setUpdatedAt(ts.toLocalDateTime());
                }
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void createProfile(Profile profile) {
        String sql = "INSERT INTO profiles (user_id, full_name, bio, profile_picture) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, profile.getUserId());
            pstmt.setString(2, profile.getFullName());
            pstmt.setString(3, profile.getBio());
            pstmt.setBytes(4, profile.getProfilePicture());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProfile(Profile profile) {
        String sql = "UPDATE profiles SET full_name=?, bio=?, profile_picture=?, "
                + "updated_at=CURRENT_TIMESTAMP WHERE profile_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, profile.getFullName());
            pstmt.setString(2, profile.getBio());
            pstmt.setBytes(3, profile.getProfilePicture());
            pstmt.setInt(4, profile.getProfileId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveProfile(Profile profile) {
        if (profile.getProfileId() > 0) {
            updateProfile(profile);
        } else {
            createProfile(profile);
        }
    }
}
