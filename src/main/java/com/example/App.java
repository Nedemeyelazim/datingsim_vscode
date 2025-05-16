package com.example;

import java.io.IOException;

import com.example.dialogue.DialogueManager;

import javafx.application.Application;
import javafx.application.Platform;
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
    private static final DialogueManager dialogueManager = new DialogueManager();
    private static Label dialogueLabel; // Moved to class level

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Menü-Szene laden
        try {
            Parent menuRoot = loadFXML("menu");
            scene = new Scene(menuRoot);
            scene.getStylesheets().add(App.class.getResource("menu.css").toExternalForm());
            
            scene.setOnMouseClicked(event -> {
                System.out.println("Mausposition - X: " + event.getX() + ", Y: " + event.getY());
            });

            primaryStage.setScene(scene);
            primaryStage.setTitle("Dating Sim - Menu");
            primaryStage.setMaximized(true);
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.setFullScreenExitHint("");
            primaryStage.setFullScreen(true);
            primaryStage.show();

            // Debug: Prüfe ob Dialogdatei existiert
            var dialogueResource = App.class.getResource("/dialogue.txt");
            if (dialogueResource == null) {
                System.out.println("FEHLER: dialogue.txt nicht gefunden!");
                return;
            }

            // Dialogdatei laden
            dialogueManager.loadDialogue(dialogueResource.getPath());
            System.out.println("Dialog erfolgreich geladen");

            // Wechsel zur FirstScene
            try {
                Parent firstSceneRoot = loadFXML("FirstScene");
                scene.setRoot(firstSceneRoot);
                
                // Dialog Label nach dem Scene-Aufbau platzieren
                Platform.runLater(() -> {
                    dialogueLabel = (Label) firstSceneRoot.lookup("#dialogueLabel");
                    if (dialogueLabel == null) {
                        System.out.println("FEHLER: dialogueLabel nicht gefunden!");
                        return;
                    }
                    
                    // Neue Position setzen
                    dialogueLabel.setLayoutX(225.0);
                    dialogueLabel.setLayoutY(629.0);
                    
                    // Weitere Eigenschaften
                    dialogueLabel.setWrapText(true);
                    dialogueLabel.setPrefWidth(1120);
                    dialogueLabel.setPrefHeight(207);
                    
                    // Debug
                    System.out.println("Label neu positioniert: X=" + dialogueLabel.getLayoutX() 
                                      + ", Y=" + dialogueLabel.getLayoutY());
                });

                // Key-Listener
                scene.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPress);

            } catch (Exception e) {
                System.out.println("Fehler beim Laden der FirstScene: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("Allgemeiner Fehler: " + e.getMessage());
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

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.SPACE && dialogueLabel != null) {
            if (dialogueManager.hasMoreDialogue()) {
                DialogueManager.DialogueLine currentLine = dialogueManager.getCurrentLine();
                dialogueLabel.setText(currentLine.toString());
                dialogueManager.advanceDialogue();
            } else {
                dialogueLabel.setText("Ende des Dialogs.");
            }
        }
    }

    /**
     * Setzt den Root der aktuellen Scene auf die übergebene FXML-Datei.
     * Beispiel: App.setRoot("FirstScene") wechselt vom Menü zur ersten Spielszene.
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        primaryStage.setFullScreen(true);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}