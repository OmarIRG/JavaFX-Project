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

        // Attach your global stylesheet
        mainScene.getStylesheets().add(
                SceneManager.class.getResource("/com/example/social_media/styles.css").toExternalForm()
        );

        primaryStage.setScene(mainScene);

        // Removed setMaximized(true) and set the window to a fixed size:
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    public static <T> T switchRoot(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent newRoot = loader.load();
            mainScene.setRoot(newRoot);

            // Also remove setMaximized(true) here:
            primaryStage.setWidth(1000);
            primaryStage.setHeight(700);
            primaryStage.centerOnScreen();

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
