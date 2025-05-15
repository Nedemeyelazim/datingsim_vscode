package com.example;

import java.io.IOException;

import com.example.dialogue.DialogueManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    // Instanz des DialogueManagers
    private static final DialogueManager dialogueManager = new DialogueManager();

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Beim Booten wird zuerst das Menü geladen
        Parent menuRoot = loadFXML("menu");
        scene = new Scene(menuRoot);
        scene.getStylesheets().add(App.class.getResource("menu.css").toExternalForm());

        // Optional: Maus-Klick-Listener (nur zur Debug-Ausgabe)
        scene.setOnMouseClicked(event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            System.out.println("Mouse clicked at: x = " + x + ", y = " + y);
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("Dating Sim - Menu");

        // Stage-Einstellungen: Maximiert und Vollbild
        primaryStage.setMaximized(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);

        primaryStage.show();

        // Beispiel: Dialogdatei laden (Dateipfad ggf. anpassen)
        try {
            dialogueManager.loadDialogue("dialogue.txt");
        } catch (IOException e) {
            System.out.println("Fehler beim Laden der Dialogdatei: " + e.getMessage());
        }

        // Key-Listener: Bei SPACEtaste wird die aktuelle Dialogzeile ausgegeben und weitergeschaltet
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (dialogueManager.hasMoreDialogue()) {
                    DialogueManager.DialogueLine currentLine = dialogueManager.getCurrentLine();
                    System.out.println(currentLine);
                    dialogueManager.advanceDialogue();
                } else {
                    System.out.println("Ende des Dialogs.");
                }
            }
        });
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