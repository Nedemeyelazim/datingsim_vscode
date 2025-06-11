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

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static final DialogueManager dialogueManager = DialogueManager.getInstance();
    private static Label dialogueLabel;

    // Neue Debug-Hilfsmittel
    private static final ArrayList<String> DEBUG_MESSAGES = new ArrayList<>();
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

    private static void printDebugMessages(String context) {
        DEBUG_MESSAGES.sort(DEBUG_MESSAGE_COMPARATOR);
        System.out.println("\n=== Debug Output: " + context + " ===");
        DEBUG_MESSAGES.forEach(System.out::println);
        System.out.println("====================\n");
        DEBUG_MESSAGES.clear();
    }

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
     * Setzt den Root der aktuellen Scene auf die übergebene FXML-Datei.
     * Beispiel: App.setRoot("FirstScene") wechselt vom Menü zur ersten Spielszene.
     */
    public static void setRoot(String fxml) throws IOException {
        Scene oldScene = scene;
        try {
            // Avoid double initialization
            if (scene != null && scene.getRoot().getId().equals(fxml)) {
                DEBUG_MESSAGES.add("WARNING: Attempting to load same scene");
                return;
            }

            scene = new Scene(loadFXML(fxml));
            scene.getStylesheets().add(App.class.getResource("menu.css").toExternalForm());
            primaryStage.setScene(scene);
            
            DEBUG_MESSAGES.add("Previous Scene Resources Cleaned: " + (oldScene != null));
            DEBUG_MESSAGES.add("New Scene Loaded: " + (scene != null));
            DEBUG_MESSAGES.add("New FXML: " + fxml);
            
            primaryStage.setFullScreen(true);
            DEBUG_MESSAGES.add("Fullscreen mode set");
            
            // Only initialize dialogue for game scenes
            if (fxml.equals("FirstScene") || fxml.equals("SecondScene") || fxml.equals("ThirdScene") || fxml.equals("FourthScene")) {
                initializeSceneDialog(fxml);
            }
            
            DEBUG_MESSAGES.add("Scene transition complete");
            printDebugMessages("Scene Transition");
            
        } catch (Exception e) {
            DEBUG_MESSAGES.add("ERROR during scene transition:");
            DEBUG_MESSAGES.add("Target scene: " + fxml);
            DEBUG_MESSAGES.add("Error message: " + e.getMessage());
            printDebugMessages("Scene Transition Error");
            throw new IOException("Failed to set root: " + e.getMessage(), e);
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