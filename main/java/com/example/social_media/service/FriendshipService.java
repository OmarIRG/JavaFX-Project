package com.example.social_media.service;

import com.example.social_media.Friendship;
import com.example.social_media.util.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FriendshipService {

    public boolean sendFriendRequest(int userId1, int userId2) {
        String sql = "INSERT INTO friendships (user_id_1, user_id_2, status) VALUES (?, ?, 'pending')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId1);
            pstmt.setInt(2, userId2);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean acceptFriendRequest(int friendshipId) {
        String sql = "UPDATE friendships SET status='accepted' WHERE friendship_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, friendshipId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Remove a friend or cancel a request
    public boolean removeFriend(int friendshipId) {
        String sql = "DELETE FROM friendships WHERE friendship_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, friendshipId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // All accepted friends
    public List<Friendship> getFriends(int userId) {
        List<Friendship> list = new ArrayList<>();
        String sql = "SELECT * FROM friendships "
                + "WHERE (user_id_1=? OR user_id_2=?) AND status='accepted'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Friendship f = new Friendship();
                f.setFriendshipId(rs.getInt("friendship_id"));
                f.setUserId1(rs.getInt("user_id_1"));
                f.setUserId2(rs.getInt("user_id_2"));
                f.setStatus(rs.getString("status"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    f.setCreatedAt(ts.toLocalDateTime());
                }
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Show pending requests
    public List<Friendship> getPending(int userId) {
        List<Friendship> list = new ArrayList<>();
        String sql = "SELECT * FROM friendships WHERE user_id_2=? AND status='pending'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Friendship f = new Friendship();
                f.setFriendshipId(rs.getInt("friendship_id"));
                f.setUserId1(rs.getInt("user_id_1"));
                f.setUserId2(rs.getInt("user_id_2"));
                f.setStatus(rs.getString("status"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) {
                    f.setCreatedAt(ts.toLocalDateTime());
                }
                list.add(f);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
