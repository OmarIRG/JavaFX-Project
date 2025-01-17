# Social Media JavaFX Project

A **desktop-based** social media application built with **JavaFX** and **MySQL**. It supports user registration, login, profile management (including profile pictures and bio), posting (with images), a news feed, likes/comments, and basic friend management.

---

## Features

1. **User Registration & Login**  
   - Secure password hashing with BCrypt (`jbcrypt`).  
   - MySQL-based user storage (username, email, password).

2. **Profile Management**  
   - Edit full name, bio, and profile picture.  
   - Updates stored in the `profiles` table.

3. **Posting**  
   - Create text-based or image-based posts.  
   - Posts saved in MySQL with optional image attachments.

4. **News Feed**  
   - Shows all **public** posts.  
   - Allows commenting and liking.

5. **Likes & Comments**  
   - Users can like others’ posts (stored in `likes` table).  
   - Users can add comments (stored in `comments` table).

6. **Friend System**  
   - Send friend requests (pending/accept).  
   - View friends’ profiles and their public posts.

7. **Post Details**  
   - A dedicated view for each post, showing who liked it, comments, and a larger post image.

---

## Tech Stack

- **Java 17+** (works on JDK 17, tested up to JDK 23).
- **JavaFX 17** for the UI.
- **MySQL** 8.x for data storage.
- **Maven** for build & dependency management (`pom.xml`).

---

## Project Structure

