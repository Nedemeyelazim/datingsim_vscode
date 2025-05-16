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
        scene.setRoot(loadFXML(fxml));
        primaryStage.setFullScreen(true);
        
        // Dialog initialisieren wenn wir zur FirstScene wechseln
        if (fxml.equals("FirstScene")) {
            Platform.runLater(() -> {
                dialogueLabel = (Label) scene.lookup("#dialogueLabel");
                if (dialogueLabel != null) {
                    dialogueManager.initializeDialog(dialogueLabel);
                    System.out.println("Dialog in FirstScene initialisiert");
                    
                    // Space-Taste Event Handler mit höherer Priorität
                    KeyEventHandler spaceHandler = new KeyEventHandler();
                    scene.addEventFilter(KeyEvent.KEY_PRESSED, spaceHandler);
                }
            });
        }
    }

    // Neue Klasse für den KeyEvent Handler
    private static class KeyEventHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.SPACE) {
                dialogueManager.showNextLine();
                event.consume(); // Verhindert, dass andere Handler das Event verarbeiten
                System.out.println("Space gedrückt - Nächste Dialogzeile");
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