package com.example.social_media.service;

import com.example.social_media.User;
import com.example.social_media.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikeService {

    public boolean addLike(int postId, int userId) {
        String sql = "INSERT INTO likes (post_id, user_id) VALUES (?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException dup) {
            // user already liked this post
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int countLikes(int postId) {
        String sql = "SELECT COUNT(*) AS total FROM likes WHERE post_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // NEW: get a list of User objects who liked this post
    public List<User> getUsersWhoLikedPost(int postId) {
        List<User> likers = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.email, u.password_hash "
                + "FROM likes l "
                + "JOIN users u ON l.user_id = u.user_id "
                + "WHERE l.post_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                likers.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likers;
    }
}
