CREATE DATABASE social_media_app;
USE social_media_app;

-- Users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Profiles table
CREATE TABLE profiles (
    profile_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE,
    full_name VARCHAR(100),
    bio TEXT,
    profile_picture LONGBLOB,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Posts table
CREATE TABLE posts (
    post_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    content TEXT,
    image_data LONGBLOB,
    privacy_level ENUM('public','friends-only','private') DEFAULT 'public',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Comments table
CREATE TABLE comments (
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT,
    user_id INT,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Likes table
CREATE TABLE likes (
    like_id INT PRIMARY KEY AUTO_INCREMENT,
    post_id INT,
    user_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_like (post_id, user_id)
);

-- Friendships table
CREATE TABLE friendships (
    friendship_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id_1 INT,
    user_id_2 INT,
    status ENUM('pending','accepted') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id_1) REFERENCES users(user_id),
    FOREIGN KEY (user_id_2) REFERENCES users(user_id),
    UNIQUE KEY unique_friendship (user_id_1, user_id_2)
);
