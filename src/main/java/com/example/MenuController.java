package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * Hauptmenü Controller
 * ------------------
 * Steuert die Interaktionen und das Layout des Spielhauptmenüs.
 * 
 * Kernfunktionen:
 * - Responsives Menü-Layout
 * - Spielstart-Steuerung
 * - Programm-Beendigung
 * - Hintergrund-Skalierung
 * 
 * UI-Komponenten:
 * - rootPane: Flex-Container für dynamisches Layout
 * - backgroundImage: Automatisch skalierendes Hintergrundbild
 * - playButton: Startet das Spiel
 * - quitButton: Beendet die Anwendung
 * 
 * Technische Features:
 * - Property Binding für responsive Layouts
 * - Aspekt-Ratio Preservation
 * - Error-Handling bei Szenenübergängen
 * 
 * @author Type Soul Productions
 * @version 1.0
 */
public class MenuController {

    /** 
     * UI-Komponenten (FXML-injiziert)
     * ------------------------------
     */
    @FXML private ImageView backgroundImage;  // Hintergrundbild
    @FXML private AnchorPane rootPane;        // Hauptcontainer

    /**
     * Menü-Initialisierung
     * ------------------
     * FXML-Loader ruft diese Methode automatisch auf.
     * 
     * Layout-Konfiguration:
     * 1. Bindet Bildgröße an Container
     * 2. Behält Seitenverhältnis bei
     * 3. Aktiviert responsives Verhalten
     */
    @FXML
    public void initialize() {
        // Debug-Start
        System.out.println("\n=== Menu Initialization ===");
        
        // Bind image size to container
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        backgroundImage.setPreserveRatio(true);
        
        System.out.println("Menu background configured");
        System.out.println("=========================\n");
    }

    /**
     * Spiel-Start Handler
     * -----------------
     * Initiiert Übergang zur ersten Spielszene.
     * 
     * Prozess:
     * 1. Speichert Menü-Status
     * 2. Lädt erste Spielszene
     * 3. Fehlerbehandlung bei Bedarf
     */
    @FXML
    public void handlePlay() {
        try {
            System.out.println("Starting game...");
            App.setRoot("FirstScene");
        } catch (IOException e) {
            System.err.println("Error starting game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Beenden-Handler
     * -------------
     * Beendet die Anwendung sauber.
     * 
     * Cleanup:
     * 1. Ressourcen freigeben
     * 2. Speicher bereinigen
     * 3. Prozess beenden
     */
    @FXML
    public void handleQuit() {
        System.out.println("Shutting down application...");
        System.exit(0);
    }
}
