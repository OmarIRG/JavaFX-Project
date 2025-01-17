# Social Media Application

## Overview

This project is a **JavaFX-based social media application** designed to allow users to interact in a minimalistic and engaging way. It features user registration, profile management, the ability to post updates (with text and images), a news feed, and a friend system. The application leverages **MySQL** for data persistence and follows object-oriented programming principles.

---

## Features

### Core Functionalities
1. **User Registration and Login**
   - Secure password hashing using BCrypt.
   - MySQL database for storing user credentials.

2. **Profile Management**
   - Update full name, bio, and profile picture.
   - Real-time data synchronization with the database.

3. **Posting Updates**
   - Users can post text and images.
   - Privacy settings for posts (e.g., Public, Friends-only).

4. **News Feed**
   - View posts from other users.
   - Support for lazy loading/pagination.

5. **Likes and Comments**
   - Like and comment on posts.
   - See who liked a specific post.

6. **Friend System**
   - Add friends, view their profiles, and see their posts.

---

## Prerequisites

### Software Requirements
- **Java Development Kit (JDK)**: Version 17 or later.
- **JavaFX**: Version 17.
- **MySQL**: Version 8.0 or later.
- **Maven**: For dependency management and building the project.

### Database Setup
1. Create a MySQL database called `social_media_app`.
2. Run the following script to set up the required tables:
   ```sql
   CREATE TABLE users (
       user_id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) UNIQUE NOT NULL,
       email VARCHAR(100) UNIQUE NOT NULL,
       password_hash VARCHAR(255) NOT NULL
   );

   CREATE TABLE profiles (
       profile_id INT AUTO_INCREMENT PRIMARY KEY,
       user_id INT UNIQUE NOT NULL,
       full_name VARCHAR(255),
       bio TEXT,
       profile_picture BLOB,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );

   CREATE TABLE posts (
       post_id INT AUTO_INCREMENT PRIMARY KEY,
       user_id INT NOT NULL,
       content TEXT,
       image_data BLOB,
       privacy_level ENUM('public', 'friends-only', 'private') DEFAULT 'public',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );

   CREATE TABLE likes (
       like_id INT AUTO_INCREMENT PRIMARY KEY,
       post_id INT NOT NULL,
       user_id INT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       UNIQUE(post_id, user_id),
       FOREIGN KEY (post_id) REFERENCES posts(post_id),
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );

   CREATE TABLE comments (
       comment_id INT AUTO_INCREMENT PRIMARY KEY,
       post_id INT NOT NULL,
       user_id INT NOT NULL,
       content TEXT NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (post_id) REFERENCES posts(post_id),
       FOREIGN KEY (user_id) REFERENCES users(user_id)
   );

   CREATE TABLE friendships (
       friendship_id INT AUTO_INCREMENT PRIMARY KEY,
       user_id_1 INT NOT NULL,
       user_id_2 INT NOT NULL,
       status ENUM('pending', 'accepted') DEFAULT 'pending',
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       UNIQUE(user_id_1, user_id_2),
       FOREIGN KEY (user_id_1) REFERENCES users(user_id),
       FOREIGN KEY (user_id_2) REFERENCES users(user_id)
   );
   ```

### Maven Dependencies
Add the following dependencies to your `pom.xml` file:
```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.6</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.6</version>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>
</dependencies>
```

---

## Setup and Run

### Clone the Repository
```bash
git clone https://github.com/your-repo/social-media-app.git
cd social-media-app
```

### Configure the Database Connection
Edit the `DBConnection.java` file:
```java
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/social_media_app";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

### Build and Run
1. Build the project using Maven:
   ```bash
   mvn clean install
   ```
2. Run the application:
   ```bash
   mvn javafx:run
   ```

---

## Usage

### Registering a New User
1. On the login screen, click "Register" to create a new account.
2. Enter a username, email, and password.

### Managing Your Profile
1. Log in and navigate to "My Profile."
2. Update your **Full Name**, **Bio**, and **Profile Picture**.
3. Click "Save Profile" to persist changes.

### Posting
1. On your profile page, create a new post by entering text and/or attaching an image.
2. Posts appear in your profile and the news feed (based on privacy settings).

### Viewing Posts
1. Navigate to the "News Feed" or a friend's profile to see posts.
2. Click "View Details" on any post to like, comment, or view who liked it.

---

## Project Structure
```
src
├── main
│   ├── java
│   │   ├── com.example.social_media
│   │   │   ├── controller  # JavaFX controllers
│   │   │   ├── service     # Business logic and database operations
│   │   │   ├── util        # Utility classes (e.g., DBConnection)
│   │   │   ├── models      # POJOs (User, Profile, Post, etc.)
│   ├── resources
│   │   ├── com.example.social_media
│   │   │   ├── fxml        # FXML files for UI
│   │   │   ├── css         # Stylesheets
├── pom.xml                  # Maven build file
```

---

## Contributing
Feel free to fork this project and submit pull requests. Contributions are welcome!

---

## License
This project is licensed under the [MIT License](LICENSE).

