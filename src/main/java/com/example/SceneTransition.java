package com.example;

import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Szenen-Übergangsmanager
 * ----------------------
 * Implementiert flüssige Übergänge zwischen Spielszenen mit
 * Fade-Effekten und Callback-Unterstützung.
 * 
 * Kernfunktionen:
 * - Weiche Überblendungen
 * - Callback-Integration
 * - Thread-sicheres Design
 * - Fehlertolerante Ausführung
 * 
 * Animation Details:
 * - Fade-Out: 500ms, linear
 * - Szenen-Wechsel: atomar
 * - Fade-In: 300ms, linear
 * - Callback: nach Abschluss
 * 
 * Technische Features:
 * - JavaFX Animations-Framework
 * - Non-blocking Transitions
 * - Memory-effizientes Design
 * 
 * @author Type Soul Productions
 * @version 1.0
 */
public class SceneTransition {
    
    /**
     * Szenen-Überblendung
     * ------------------
     * Führt einen animierten Übergang zwischen zwei Szenen durch.
     * 
     * Ablauf:
     * 1. Fade-Out alte Szene (500ms)
     * 2. Atomarer Szenen-Wechsel
     * 3. Fade-In neue Szene (300ms)
     * 4. Optional: Callback ausführen
     * 
     * @param stage        JavaFX Stage für Szenen-Container
     * @param oldScene     Aktuelle Szene die ausgeblendet wird
     * @param newScene     Neue Szene die eingeblendet wird
     * @param onFinished   Optional: Callback nach Transition
     */
    public static void fadeTransition(Stage stage, Scene oldScene, Scene newScene, Runnable onFinished) {
        // Validierung
        if (stage == null || oldScene == null || newScene == null) {
            System.err.println("Error: Invalid transition parameters");
            return;
        }

        try {
            // Erste Fade-Out Animation
            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), oldScene.getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            
            // Zweite Fade-In Animation
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newScene.getRoot());
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            
            // Debug-Logging
            System.out.println("\n=== Transition Debug ===");
            System.out.println("Starting scene transition");
            System.out.println("From: " + oldScene.getRoot().getId());
            System.out.println("To: " + newScene.getRoot().getId());
            
            // Sequenz ausführen
            fadeOut.setOnFinished(e -> {
                stage.setScene(newScene);
                System.out.println("Scene switched");
                fadeIn.play();
            });
            
            fadeIn.setOnFinished(e -> {
                System.out.println("Transition complete");
                if (onFinished != null) {
                    onFinished.run();
                }
                System.out.println("======================\n");
            });
            
            fadeOut.play();
            
        } catch (Exception e) {
            System.err.println("Transition error: " + e.getMessage());
            // Fallback: Direkter Szenenwechsel
            stage.setScene(newScene);
            if (onFinished != null) {
                onFinished.run();
            }
        }
    }
}