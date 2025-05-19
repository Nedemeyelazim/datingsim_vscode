package com.example;

import com.example.dialogue.DialogueManager;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyEventHandler implements EventHandler<KeyEvent> {
    private final Scene scene;
    private final DialogueManager dialogueManager;

    public KeyEventHandler(Scene scene, DialogueManager dialogueManager) {
        this.scene = scene;
        this.dialogueManager = dialogueManager;
        System.out.println("KeyEventHandler initialized with scene: " + scene);
    }

    @Override
    public void handle(KeyEvent event) {
        System.out.println("\n=== Key Event Debug ===");
        System.out.println("Key Pressed: " + event.getCode());
        System.out.println("Scene: " + scene);
        System.out.println("DialogueManager: " + dialogueManager);
        
        if (event.getCode() == KeyCode.SPACE) {
            System.out.println("Space key detected!");
            
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
        
        System.out.println("=== Event Handling Complete ===\n");
    }
}