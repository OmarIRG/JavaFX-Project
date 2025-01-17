
---

## Database Setup

1. **Create a MySQL database** (e.g., `social_media_app`).
2. Run or ensure the following tables exist (example schema):
   ```sql
   CREATE TABLE users (
       user_id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(255) NOT NULL UNIQUE,
       email VARCHAR(255) NOT NULL UNIQUE,
       password_hash VARCHAR(255) NOT NULL
   );

   CREATE TABLE profiles (
       profile_id INT AUTO_INCREMENT PRIMARY KEY,
       user_id INT NOT NULL,
       full_name VARCHAR(255),
       bio TEXT,
       profile_picture LONGBLOB,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );

   CREATE TABLE posts (
       post_id INT AUTO_INCREMENT PRIMARY KEY,
       user_id INT NOT NULL,
       content TEXT,
       image_data LONGBLOB,
       privacy_level VARCHAR(50),
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );

   CREATE TABLE comments (
       comment_id INT AUTO_INCREMENT PRIMARY KEY,
       post_id INT NOT NULL,
       user_id INT NOT NULL,
       content TEXT,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (post_id) REFERENCES posts(post_id),
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );

   CREATE TABLE likes (
       like_id INT AUTO_INCREMENT PRIMARY KEY,
       post_id INT NOT NULL,
       user_id INT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       UNIQUE KEY unique_like (post_id, user_id),
       FOREIGN KEY (post_id) REFERENCES posts(post_id),
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );

   CREATE TABLE friendships (
       friendship_id INT AUTO_INCREMENT PRIMARY KEY,
       user_id_1 INT NOT NULL,
       user_id_2 INT NOT NULL,
       status VARCHAR(20) DEFAULT 'pending',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id_1) REFERENCES users(user_id),
       FOREIGN KEY (user_id_2) REFERENCES users(user_id)
   );
