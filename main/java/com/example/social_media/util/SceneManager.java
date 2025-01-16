package com.example.social_media.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage primaryStage;
    private static Scene mainScene;

    public static void init(Stage stage, Parent initialRoot) {
        primaryStage = stage;
        mainScene = new Scene(initialRoot);

        // Attach a single stylesheet for luxurious styling
        mainScene.getStylesheets().add(
                SceneManager.class.getResource("/com/example/social_media/styles.css").toExternalForm()
        );

        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static <T> T switchRoot(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent newRoot = loader.load();
            mainScene.setRoot(newRoot);
            primaryStage.setMaximized(true);
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
