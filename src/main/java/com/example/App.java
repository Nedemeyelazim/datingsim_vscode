package com.example;

import java.io.IOException;
import java.io.InputStream;

import com.example.dialogue.DialogueManager;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static final DialogueManager dialogueManager = DialogueManager.getInstance();
    private static Label dialogueLabel;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        try {
            // Load dialogue first
            try (InputStream dialogueStream = getClass().getResourceAsStream("/com/example/dialogue.txt")) {
                if (dialogueStream == null) {
                    System.out.println("FEHLER: dialogue.txt nicht gefunden!");
                    return;
                }
                dialogueManager.loadDialogueFromStream(dialogueStream);
                System.out.println("Dialog erfolgreich geladen!");
            }

            // Load menu scene
            Parent menuRoot = loadFXML("menu");
            scene = new Scene(menuRoot);
            scene.getStylesheets().add(getClass().getResource("menu.css").toExternalForm());
            
            // Configure stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Dating Sim - Menu");
            primaryStage.setMaximized(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreen(true);
            primaryStage.show();

        } catch (Exception e) {
            System.out.println("Fehler beim Start: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void maintainAspectRatio() {
        if (scene == null) return;
        
        AnchorPane gameContainer = (AnchorPane) scene.lookup("#gameContainer");
        if (gameContainer == null) return;
        
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();
        
        // Berechne Skalierungsfaktor für 16:9
        double scale = Math.min(sceneWidth / 1920.0, sceneHeight / 1080.0);
        
        gameContainer.setScaleX(scale);
        gameContainer.setScaleY(scale);
    }

    /**
     * Setzt den Root der aktuellen Scene auf die übergebene FXML-Datei.
     * Beispiel: App.setRoot("FirstScene") wechselt vom Menü zur ersten Spielszene.
     */
    public static void setRoot(String fxml) throws IOException {
        System.out.println("\n=== Scene Transition Debug ===");
        System.out.println("Attempting to switch to: " + fxml);
        
        try {
            Parent newRoot = loadFXML(fxml);
            System.out.println("FXML loaded successfully");
            
            scene.setRoot(newRoot);
            System.out.println("New root set to scene");
            
            primaryStage.setFullScreen(true);
            System.out.println("Fullscreen mode set");
            
            // Dialog für alle Szenen initialisieren
            if (fxml.equals("FirstScene") || fxml.equals("SecondScene") || fxml.equals("ThirdScene")) {
                System.out.println("Initializing dialogue for " + fxml);
                
                Platform.runLater(() -> {
                    System.out.println("\n=== Dialog Setup Debug ===");
                    dialogueLabel = (Label) scene.lookup("#dialogueLabel");
                    System.out.println("Dialog label lookup: " + (dialogueLabel != null ? "found" : "not found"));
                    
                    if (dialogueLabel != null) {
                        dialogueManager.setCurrentScene(fxml);
                        System.out.println("Current scene set in DialogueManager");
                        
                        dialogueManager.initializeDialog(dialogueLabel);
                        System.out.println("Dialog initialized for " + fxml);
                        
                        // Create handler with dependencies
                        KeyEventHandler spaceHandler = new KeyEventHandler(scene, dialogueManager);
                        scene.addEventFilter(KeyEvent.KEY_PRESSED, spaceHandler);
                        System.out.println("Space key handler added with scene and DialogueManager");
                    } else {
                        System.out.println("ERROR: Could not find dialogue label in " + fxml);
                    }
                });
            }
            
            System.out.println("Scene transition complete");
            System.out.println("========================\n");
            
        } catch (Exception e) {
            System.out.println("ERROR during scene transition:");
            System.out.println("Target scene: " + fxml);
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Neue Klasse für den KeyEvent Handler
    private static class KeyEventHandler implements EventHandler<KeyEvent> {
        private final Scene scene;
        private final DialogueManager dialogueManager;

        public KeyEventHandler(Scene scene, DialogueManager dialogueManager) {
            this.scene = scene;
            this.dialogueManager = dialogueManager;
        }

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.SPACE) {
                System.out.println("\n=== KeyEvent Debug ===");
                System.out.println("Space pressed in scene: " + scene.getRoot().getId());
                System.out.println("Current DialogueManager state: " + dialogueManager);
                
                dialogueManager.showNextLine();
                event.consume();
                
                System.out.println("Space key event handled");
                System.out.println("====================\n");
            }
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}