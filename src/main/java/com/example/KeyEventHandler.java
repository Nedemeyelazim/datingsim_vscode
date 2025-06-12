package com.example;

import com.example.dialogue.DialogueManager;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Tastatur-Event Handler
 * --------------------
 * Verarbeitet Tastatureingaben für das Dialog-System der Visual Novel.
 * 
 * Kernfunktionen:
 * - Erkennung von Tastatureingaben (primär Leertaste)
 * - Steuerung der Dialog-Progression
 * - Detailliertes Debug-Logging
 * - Event-Isolation durch Consumption
 * 
 * Technische Features:
 * - Thread-sicheres Event-Handling
 * - Null-Safety für DialogManager
 * - Szenen-spezifische Verarbeitung
 * 
 * @author Type Soul Productions
 * @version 1.0
 */
public class KeyEventHandler implements EventHandler<KeyEvent> {
    
    /**
     * Handler-Komponenten
     * -----------------
     * scene: Aktuelle JavaFX-Szene für Kontext
     * dialogueManager: Referenz zum Dialog-System
     */
    private final Scene scene;
    private final DialogueManager dialogueManager;

    /**
     * Handler-Initialisierung
     * ---------------------
     * Richtet Event-Verarbeitung für spezifische Szene ein.
     * 
     * @param scene Aktive JavaFX-Szene
     * @param dialogueManager Dialog-System Referenz
     */
    public KeyEventHandler(Scene scene, DialogueManager dialogueManager) {
        this.scene = scene;
        this.dialogueManager = dialogueManager;
        System.out.println("KeyEventHandler initialized with scene: " + scene);
    }

    /**
     * Event-Verarbeitung
     * ----------------
     * Prozess:
     * 1. Event-Identifikation
     * 2. Debug-Logging
     * 3. Leertasten-Verarbeitung
     * 4. Dialog-Progression
     * 5. Event-Consumption
     * 
     * @param event JavaFX KeyEvent
     */
    @Override
    public void handle(KeyEvent event) {
        // Debug-Header
        System.out.println("\n=== Key Event Debug ===");
        System.out.println("Key Pressed: " + event.getCode());
        System.out.println("Scene: " + scene);
        System.out.println("DialogueManager: " + dialogueManager);
        
        // Leertasten-Verarbeitung
        if (event.getCode() == KeyCode.SPACE) {
            System.out.println("Space key detected!");
            
            // Dialog-Progression mit Null-Check
            if (dialogueManager != null) {
                System.out.println("Calling showNextLine()...");
                dialogueManager.showNextLine();
                System.out.println("showNextLine() completed");
                
                event.consume();
                System.out.println("Event consumed");
            } else {
                System.out.println("ERROR: DialogueManager is null!");
            }
        }
        
        // Debug-Footer
        System.out.println("=== Event Handling Complete ===\n");
    }
}