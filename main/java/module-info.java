module com.example.social_media {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;  // for password hashing
    requires javafx.graphics;
    requires javafx.base;
    // etc. if you need other modules (javafx.web, etc.)

    // Open your controller packages to FXML
    opens com.example.social_media.controller to javafx.fxml;
    opens com.example.social_media to javafx.fxml;
    exports com.example.social_media;
}
