package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

/**
 * Controller für die erste Spielszene.
 * Verwaltet:
 * - Layout und Größenanpassung der Szene
 * - Hintergrundbilder und visuelle Elemente
 * - Navigation und Benutzerinteraktionen
 * - Debug-Logging für Komponenten-Initialisierung
 */
public class FirstSceneController {

    @FXML
    private AnchorPane rootPane;  // Hauptcontainer für alle UI-Elemente

    @FXML
    private ImageView backgroundImage;  // Hintergrundbild der Szene

    @FXML
    private Button topRightButton;  // Navigations-Button (zurück zum Menü)

    /**
     * Initialisiert die Szenenkomponenten beim Laden.
     * Führt folgende Schritte aus:
     * 1. Passt Bildgrößen an den Bildschirm an
     * 2. Richtet Event-Handler für Buttons ein
     * 3. Konfiguriert Layout-Parameter
     * 4. Aktiviert Debug-Logging
     * 
     * Die Methode wird automatisch von FXML-Loader aufgerufen,
     * nachdem alle @FXML-annotierten Felder injiziert wurden.
     */
    @FXML
    public void initialize() {
        try {
            // Debug-Logging Start
            System.out.println("=== Component Initialization Debug ===");
            System.out.println("FirstScene initialized – alles OK!");
            
            // Bildschirmgröße ermitteln und anpassen
            Screen screen = Screen.getPrimary();
            double screenWidth = screen.getBounds().getWidth();
            double screenHeight = screen.getBounds().getHeight();
            
            // Hintergrundbild an Bildschirmgröße anpassen
            backgroundImage.setFitWidth(screenWidth);
            backgroundImage.setFitHeight(screenHeight);
            
            // Event-Handler für Zurück-Button
            topRightButton.setOnAction(event -> {
                try {
                    App.setRoot("menu");
                    System.out.println("Top-right Button clicked.");
                } catch (Exception e) {
                    System.out.println("Navigation error: " + e.getMessage());
                    e.printStackTrace();
                }
            });

        } catch (Exception e) {
            // Fehlerbehandlung mit detailliertem Logging
            System.out.println("=== Initialization Error Debug ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Bereinigt Ressourcen beim Schließen der Szene.
     * Entfernt Event-Handler und gibt Speicher frei.
     * Wird aufgerufen, wenn die Szene geschlossen wird.
     */
    public void cleanup() {
        // Event-Handler entfernen
        if (topRightButton != null) {
            topRightButton.setOnAction(null);
        }
        
        // Referenzen aufräumen
        backgroundImage = null;
        rootPane = null;
    }
}