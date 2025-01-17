Social Media JavaFX Project
A desktop-based social media application built with JavaFX and MySQL. It supports user registration, login, profile management (including profile pictures and bio), posting (with images), a news feed, likes/comments, and basic friend management.

Features
User Registration & Login

Secure password hashing with BCrypt (jbcrypt).
MySQL-based user storage (username, email, password).
Profile Management

Edit full name, bio, and profile picture.
Updates stored in the profiles table.
Posting

Create text-based or image-based posts.
Posts saved in MySQL with optional image attachments.
News Feed

Shows all public posts.
Allows commenting and liking.
Likes & Comments

Users can like others’ posts (stored in likes table).
Users can add comments (stored in comments table).
Friend System

Send friend requests (pending/accept).
View friends’ profiles and their public posts.
Post Details

A detailed view for each post, showing who liked it, comments, and the post image in a larger format.
Tech Stack
Java 17+ (works on JDK 17, tested up to JDK 23).
JavaFX 17 for the UI.
MySQL 8.x for data storage.
Maven for build & dependency management (pom.xml).
Project Structure
bash
Copy
Social_Media/
├── pom.xml                         # Maven config
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/social_media/
│   │   │       ├── MainApp.java
│   │   │       ├── User.java, Profile.java, Post.java, etc.
│   │   │       ├── service/        # DB services for User, Profile, Post, etc.
│   │   │       ├── controller/     # JavaFX controllers
│   │   │       └── util/           # Utility classes (DBConnection, SceneManager, etc.)
│   │   └── resources/
│   │       └── com/example/social_media/
│   │           ├── *.fxml          # JavaFX layouts
│   │           └── styles.css      # Global stylesheet
└── README.md
Database Setup
Create a MySQL database (e.g., social_media_app).
Run or ensure the following tables exist (an example schema might be):
sql
Copy
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
Update DBConnection.java with your MySQL connection details:
java
Copy
private static final String URL = "jdbc:mysql://yourhost:3306/social_media_app";
private static final String USER = "db_user";
private static final String PASSWORD = "db_pass";
How to Build
Clone this repository.
Import it into your IDE (IntelliJ or Eclipse) as a Maven project, or run from command line:
bash
Copy
mvn clean install
How to Run
From IntelliJ (Recommended)
Set your Run Configuration:
Main class: com.example.social_media.MainApp
Add JavaFX VM options (if necessary) or let Maven/IDE handle JavaFX modules.
Press Run.
From Command Line
Build: mvn clean package
Then run (Java 17 example):
bash
Copy
java --module-path "path\to\javafx-sdk-17\lib" \
     --add-modules javafx.controls,javafx.fxml \
     -cp target/Social_Media-1.0-SNAPSHOT.jar \
     com.example.social_media.MainApp
Usage & Navigation
Login / Register
Launch the app, create a new user or log in with existing credentials.
Profile Screen
Create a post (text + optional image).
View partial feed of your own posts.
Access “My Profile” to update full name, bio, and profile picture.
News Feed
See public posts from all users, like/comment them.
Click “View Details” on any post to see bigger image, who liked it, and more comments.
Friends
Send friend requests, accept pending requests, remove friends, or view friend profiles.
Known Issues or Tips
Make sure you don’t run JDK 23 without matching JavaFX 23. JavaFX 17 can work but might cause weird issues on some systems.
If you see ClassNotFoundException for JavaFX classes, check your module path and ensure JavaFX is properly installed.
The profile_id may be 0 if a user never saved their profile. The code handles creating vs. updating accordingly.
Large images might cause performance issues if not scaled or stored properly. For production, consider storing image files on disk or a CDN.
License
This project is free to use and modify. If you share it publicly, please give credit to the original authors as a courtesy.

Contributing
Fork the repository.
Create a new branch (git checkout -b feature-xyz).
Make changes and commit.
Push to your branch (git push origin feature-xyz).
Create a Pull Request describing your changes.
Enjoy your JavaFX Social Media App!
