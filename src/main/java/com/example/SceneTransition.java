package com.example;

import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneTransition {
    
    public static void fadeTransition(Stage stage, Scene oldScene, Scene newScene, Runnable onFinished) {
        // Erste Fade-Out Animation
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), oldScene.getRoot());
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        
        // Zweite Fade-In Animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newScene.getRoot());
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        
        // Sequenz ausführen
        fadeOut.setOnFinished(e -> {
            stage.setScene(newScene);
            fadeIn.play();
        });
        
        fadeIn.setOnFinished(e -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });
        
        fadeOut.play();
    }
}