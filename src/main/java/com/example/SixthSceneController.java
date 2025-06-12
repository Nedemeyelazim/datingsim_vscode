package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

/**
 * Controller für die sechste Spielszene
 * ----------------------------------
 * Verwaltet UI und Logik der finalen Story-Szene.
 * 
 * Kernfunktionen:
 * - Skalierbare Bildschirmanpassung
 * - Dialog-System Integration
 * - Navigation zur vorherigen Szene
 * - Detailliertes Debug-Logging
 * 
 * UI-Komponenten:
 * - rootPane: Flex-Container für Layout
 * - backgroundImage: Dynamisch skalierbar
 * - topRightButton: Zurück-Navigation
 * - dialogueLabel: Text-Anzeige
 * 
 * Technische Features:
 * - Responsives Layout-System
 * - Event-basierte Navigation
 * - Fehlertolerante Initialisierung
 * 
 * @author Type Soul Productions
 * @version 1.0
 */
public class SixthSceneController {
    
    /** 
     * UI-Komponenten (FXML-injiziert)
     * ------------------------------
     */
    @FXML private AnchorPane rootPane;        // Layout-Container
    @FXML private ImageView backgroundImage;  // Hintergrundbild
    @FXML private Button topRightButton;      // Navigations-Button
    @FXML private Label dialogueLabel;        // Dialog-Anzeige

    /**
     * Komponenten-Initialisierung
     * -------------------------
     * FXML-Loader ruft diese Methode automatisch auf.
     * 
     * Initialisierungsprozess:
     * 1. Komponenten-Validierung
     * 2. Display-Konfiguration
     * 3. Event-Handler Setup
     * 4. Debug-Logging
     */
    @FXML
    private void initialize() {
        // Debug-Start
        System.out.println("SixthScene initializing...");
        
        // Komponenten-Check
        validateComponents();
        
        // Display-Setup
        configureDisplay();
        
        // Event-Handler
        setupEventHandlers();
        
        System.out.println("Sixth initialization complete");
    }

    /**
     * Komponenten-Validierung
     * ---------------------
     * Prüft Verfügbarkeit aller UI-Elemente
     */
    private void validateComponents() {
        System.out.println("Components check:");
        System.out.println("- rootPane: " + (rootPane != null ? "found" : "missing"));
        System.out.println("- backgroundImage: " + (backgroundImage != null ? "found" : "missing"));
        System.out.println("- topRightButton: " + (topRightButton != null ? "found" : "missing"));
        System.out.println("- dialogueLabel: " + (dialogueLabel != null ? "found" : "missing"));
    }

    /**
     * Display-Konfiguration
     * -------------------
     * Passt UI-Elemente an Bildschirmgröße an
     */
    private void configureDisplay() {
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        System.out.println("Screen size: " + screenWidth + "x" + screenHeight);
        
        if (backgroundImage != null) {
            backgroundImage.setFitWidth(screenWidth);
            backgroundImage.setFitHeight(screenHeight);
            System.out.println("Background image size set to: " + screenWidth + "x" + screenHeight);
        }
    }

    /**
     * Event-Handler Setup
     * -----------------
     * Konfiguriert Button-Aktionen und Navigation
     */
    private void setupEventHandlers() {
        if (topRightButton != null) {
            topRightButton.setOnAction(event -> handleBackNavigation());
            System.out.println("Back button handler initialized");
        }
    }

    /**
     * Navigations-Handler
     * -----------------
     * Verwaltet Rückkehr zur fünften Szene
     */
    private void handleBackNavigation() {
        try {
            System.out.println("SixthScene: Back button clicked - switching to FifthScene");
            App.setRoot("FifthScene");
            System.out.println("SixthScene: Switch to FifthScene completed");
        } catch (Exception e) {
            System.out.println("SixthScene: Error switching scene: " + e.getMessage());
            e.printStackTrace();
        }
    }
}