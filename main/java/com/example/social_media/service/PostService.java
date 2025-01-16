package com.example.social_media.service;

import com.example.social_media.Post;
import com.example.social_media.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostService {

    public void createPost(Post post) {
        String sql = "INSERT INTO posts (user_id, content, image_data, privacy_level) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, post.getUserId());
            pstmt.setString(2, post.getContent());
            pstmt.setBytes(3, post.getImageData());
            pstmt.setString(4, post.getPrivacyLevel());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Post> getPostsByUser(int userId) {
        List<Post> list = new ArrayList<>();
        String sql = "SELECT post_id, user_id, content, image_data, privacy_level, created_at "
                + "FROM posts WHERE user_id=? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Post p = new Post();
                p.setPostId(rs.getInt("post_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setContent(rs.getString("content"));
                p.setImageData(rs.getBytes("image_data"));
                p.setPrivacyLevel(rs.getString("privacy_level"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    p.setCreatedAt(ts.toLocalDateTime());
                }
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // For the News Feed (public posts)
    public List<Post> getAllPublicPosts() {
        List<Post> list = new ArrayList<>();
        String sql = "SELECT post_id, user_id, content, image_data, privacy_level, created_at "
                + "FROM posts WHERE privacy_level='public' ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Post p = new Post();
                p.setPostId(rs.getInt("post_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setContent(rs.getString("content"));
                p.setImageData(rs.getBytes("image_data"));
                p.setPrivacyLevel(rs.getString("privacy_level"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    p.setCreatedAt(ts.toLocalDateTime());
                }
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
