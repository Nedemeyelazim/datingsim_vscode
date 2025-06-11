package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;

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
import javafx.stage.Stage; 

/**
 * Hauptklasse der Dating Sim Anwendung.
 * Verwaltet Szenen-Navigation, Dialog-System und Fullscreen-Modus.
 */
public class App extends Application {

    // Haupt-UI Komponenten
    private static Scene scene;  // Aktuelle Szene der Anwendung
    private static Stage primaryStage;  // Hauptfenster
    private static final DialogueManager dialogueManager = DialogueManager.getInstance();  // Dialog-System
    private static Label dialogueLabel;  // Textanzeige für Dialoge

    // Debug-System
    private static final ArrayList<String> DEBUG_MESSAGES = new ArrayList<>();  // Sammelt Debug-Nachrichten
    
    /**
     * Sortiert Debug-Nachrichten nach Priorität:
     * 1. Fehlermeldungen
     * 2. Debug-Header
     * 3. Nach Länge
     */
    private static final Comparator<String> DEBUG_MESSAGE_COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(String msg1, String msg2) {
            // Priorisiere Fehlermeldungen
            if (msg1.contains("ERROR") && !msg2.contains("ERROR")) return -1;
            if (!msg1.contains("ERROR") && msg2.contains("ERROR")) return 1;
            // Dann Debug-Header
            if (msg1.contains("===") && !msg2.contains("===")) return -1;
            if (!msg1.contains("===") && msg2.contains("===")) return 1;
            // Zuletzt nach Länge
            return Integer.compare(msg2.length(), msg1.length());
        }
    };

    /**
     * Gibt gesammelte Debug-Nachrichten aus und löscht sie.
     * @param context Beschreibung des Debug-Kontexts
     */
    private static void printDebugMessages(String context) {
        DEBUG_MESSAGES.sort(DEBUG_MESSAGE_COMPARATOR);
        System.out.println("\n=== Debug Output: " + context + " ===");
        DEBUG_MESSAGES.forEach(System.out::println);
        System.out.println("====================\n");
        DEBUG_MESSAGES.clear();
    }

    /**
     * Initialisiert die Anwendung:
     * 1. Lädt Dialogtexte
     * 2. Erstellt Hauptmenü
     * 3. Konfiguriert Vollbildmodus
     */
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

        } catch (IOException e) {
            System.out.println("Fehler beim Start: " + e.getMessage());
        }
    }
    
    /**
     * Wechselt zwischen Szenen mit Fullscreen-Erhaltung.
     * Verhindert Flackern durch atomaren Szenenwechsel.
     * 
     * @param fxml Name der zu ladenden FXML-Datei
     * @throws IOException bei Ladeproblemen
     */
    public static void setRoot(String fxml) throws IOException {
        Scene oldScene = scene;
        try {
            // Vermeidet doppelte Initialisierung
            if (scene != null && scene.getRoot().getId().equals(fxml)) {
                DEBUG_MESSAGES.add("WARNING: Attempting to load same scene");
                return;
            }

            // Lädt neue Szene im Hintergrund
            Parent root = loadFXML(fxml);
            Scene newScene = new Scene(root);
            newScene.getStylesheets().add(App.class.getResource("menu.css").toExternalForm());
            
            // Konfiguriert Stage für nahtlosen Übergang
            Platform.runLater(() -> {
                primaryStage.setScene(newScene);
                scene = newScene;
                
                // Initialisiert Dialog wenn nötig
                if (fxml.equals("FirstScene") || fxml.equals("SecondScene") || 
                    fxml.equals("ThirdScene")) {
                    initializeSceneDialog(fxml);
                }
            });
            
<<<<<<< HEAD
            primaryStage.setFullScreen(true);
            DEBUG_MESSAGES.add("Fullscreen mode set");
            
            // Only initialize dialogue for game scenes
            if (fxml.equals("FirstScene") || fxml.equals("SecondScene") || fxml.equals("ThirdScene") || fxml.equals("FourthScene")) {
                initializeSceneDialog(fxml);
            }
            
            DEBUG_MESSAGES.add("Scene transition complete");
=======
            // Debug-Logging
            DEBUG_MESSAGES.add("Scene transition initiated");
>>>>>>> 50aa091e0089de120ea7ddac6d255191b1c8c15f
            printDebugMessages("Scene Transition");

        } catch (Exception e) {
            DEBUG_MESSAGES.add("ERROR loading FXML: " + e.getMessage());
            printDebugMessages("FXML Load Error");
            throw new IOException("Failed to load root: " + e.getMessage(), e);
        }
    }

    private static void initializeSceneDialog(String fxml) {
        DEBUG_MESSAGES.add("Initializing dialogue for " + fxml);
        
        Platform.runLater(() -> {
            dialogueLabel = (Label) scene.lookup("#dialogueLabel");
            DEBUG_MESSAGES.add("Dialog label lookup: " + (dialogueLabel != null ? "found" : "not found"));
            
            if (dialogueLabel != null) {
                dialogueManager.setCurrentScene(fxml);
                DEBUG_MESSAGES.add("Current scene set in DialogueManager");
                
                dialogueManager.initializeDialog(dialogueLabel);
                DEBUG_MESSAGES.add("Dialog initialized for " + fxml);
                
                KeyEventHandler spaceHandler = new KeyEventHandler(scene, dialogueManager);
                scene.addEventFilter(KeyEvent.KEY_PRESSED, spaceHandler);
                DEBUG_MESSAGES.add("Space key handler added");
                
                printDebugMessages("Dialog Setup");
            } else {
                DEBUG_MESSAGES.add("ERROR: Could not find dialogue label in " + fxml);
                printDebugMessages("Dialog Setup Error");
            }
        });
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