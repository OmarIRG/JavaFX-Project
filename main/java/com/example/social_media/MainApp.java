package com.example.social_media;

import com.example.social_media.util.SceneManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/social_media/login.fxml"));
        Parent loginRoot = loader.load();

        // Initialize SceneManager with a single scene
        SceneManager.init(primaryStage, loginRoot);

        primaryStage.setTitle("Luxurious Social Media");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
