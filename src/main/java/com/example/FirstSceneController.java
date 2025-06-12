package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

/**
 * Controller für die erste Spielszene
 * ---------------------------------
 * Verwaltet UI-Logik und Benutzerinteraktionen der Einführungsszene.
 * 
 * Kernfunktionen:
 * - Responsive Layout-Anpassung
 * - Event-Handling für Navigation
 * - Ressourcen-Management
 * - Debug-System Integration
 * 
 * UI-Komponenten:
 * - rootPane: Flex-Container für dynamisches Layout
 * - backgroundImage: Skalierbare Hintergrundgrafik
 * - topRightButton: Kontext-sensitiver Navigationsbutton
 * 
 * Technische Features:
 * - Multi-Display Support
 * - Automatische Bildanpassung
 * - Memory Management
 * - Error Handling
 * 
 * @author Type Soul Productions
 * @version 1.0
 */
public class FirstSceneController {

    /** 
     * UI-Komponenten (FXML-injiziert)
     * ------------------------------
     */
    @FXML private AnchorPane rootPane;        // Flex-Container
    @FXML private ImageView backgroundImage;  // Hintergrund
    @FXML private Button topRightButton;      // Navigation

    /**
     * Komponenten-Initialisierung
     * -------------------------
     * FXML-Loader ruft diese Methode automatisch auf.
     * 
     * Prozessablauf:
     * 1. Display-Konfiguration
     *    - Ermittelt Bildschirmgröße
     *    - Passt Hintergrundbild an
     *    - Konfiguriert Skalierung
     * 
     * 2. Event-System
     *    - Registriert Button-Handler
     *    - Konfiguriert Navigation
     *    - Setzt Error-Callbacks
     * 
     * 3. Debug-System
     *    - Initialisiert Logging
     *    - Validiert Komponenten
     *    - Protokolliert Status
     */
    @FXML
    public void initialize() {
        try {
            // Debug-Start
            System.out.println("=== Component Initialization Debug ===");
            validateComponents();
            
            // Display-Setup
            configureDisplay();
            
            // Event-Handler
            setupEventHandlers();
            
            // Debug-Ende
            System.out.println("FirstScene initialization complete");
            
        } catch (Exception e) {
            handleInitializationError(e);
        }
    }

    /**
     * Ressourcen-Bereinigung
     * --------------------
     * Wird beim Szenen-Wechsel aufgerufen.
     * 
     * Cleanup-Prozess:
     * 1. Event-Handler entfernen
     * 2. UI-Referenzen nullen
     * 3. Speicher freigeben
     */
    public void cleanup() {
        if (topRightButton != null) {
            topRightButton.setOnAction(null);
        }
        
        // Referenzen bereinigen
        backgroundImage = null;
        rootPane = null;
        
        System.out.println("FirstScene cleanup complete");
    }

    /**
     * Hilfsmethoden
     * ------------
     */
    private void validateComponents() {
        System.out.println("Components check:");
        System.out.println("- rootPane: " + (rootPane != null ? "found" : "missing"));
        System.out.println("- backgroundImage: " + (backgroundImage != null ? "found" : "missing"));
        System.out.println("- topRightButton: " + (topRightButton != null ? "found" : "missing"));
    }

    private void configureDisplay() {
        Screen screen = Screen.getPrimary();
        double width = screen.getBounds().getWidth();
        double height = screen.getBounds().getHeight();
        
        System.out.println("Screen size: " + width + "x" + height);
        
        backgroundImage.setFitWidth(width);
        backgroundImage.setFitHeight(height);
        
        System.out.println("Background image size set to: " + width + "x" + height);
    }

    private void setupEventHandlers() {
        topRightButton.setOnAction(event -> {
            try {
                App.setRoot("menu");
                System.out.println("Top-right Button clicked.");
            } catch (Exception e) {
                System.out.println("Navigation error: " + e.getMessage());
                e.printStackTrace();
            }
        });
        System.out.println("Back button handler initialized");
    }

    private void handleInitializationError(Exception e) {
        System.out.println("=== Initialization Error Debug ===");
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
    }
}