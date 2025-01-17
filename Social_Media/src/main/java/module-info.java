module com.example.social_media {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;
    requires javafx.graphics;
    requires javafx.base;

    opens com.example.social_media to javafx.fxml;
    opens com.example.social_media.controller to javafx.fxml;
    exports com.example.social_media;
}
