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
 * Verwaltet die gesamte Anwendungslogik einschließlich:
 * - Szenen-Navigation und Übergänge
 * - Dialog-System und Text-Steuerung
 * - Fullscreen-Modus und Fenster-Management
 * - Debug-System und Logging
 */
public class App extends Application {

    // Haupt-UI Komponenten mit erweiterten Beschreibungen
    private static Scene scene;  // Aktuelle aktive Szene der Anwendung
    private static Stage primaryStage;  // Hauptfenster der Anwendung
    private static final DialogueManager dialogueManager = DialogueManager.getInstance();  // Zentrale Dialog-Verwaltung
    private static Label dialogueLabel;  // Label zur Anzeige des aktuellen Dialogtextes

    // Debug-System für Entwicklung und Fehlersuche
    private static final ArrayList<String> DEBUG_MESSAGES = new ArrayList<>();  // Temporärer Speicher für Debug-Nachrichten
    
    /**
     * Definiert die Sortierreihenfolge für Debug-Nachrichten.
     * Implementiert eine dreistufige Priorisierung:
     * 1. Fehlermeldungen (enthält "ERROR") haben höchste Priorität
     * 2. Debug-Header (enthält "===") haben mittlere Priorität
     * 3. Alle anderen Nachrichten werden nach Länge sortiert
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
     * Verarbeitet und gibt Debug-Nachrichten aus.
     * Sortiert Nachrichten nach Priorität, gibt sie formatiert aus
     * und löscht den Debug-Nachrichtenspeicher.
     * 
     * @param context Beschreibender Text für den Debug-Block
     */
    private static void printDebugMessages(String context) {
        DEBUG_MESSAGES.sort(DEBUG_MESSAGE_COMPARATOR);
        System.out.println("\n=== Debug Output: " + context + " ===");
        DEBUG_MESSAGES.forEach(System.out::println);
        System.out.println("====================\n");
        DEBUG_MESSAGES.clear();
    }

    /**
     * Initialisiert die Anwendung beim Start.
     * Führt folgende Schritte aus:
     * 1. Lädt Dialogtexte aus externer Datei
     * 2. Erstellt und konfiguriert Hauptmenü
     * 3. Richtet Vollbildmodus und Fenstereinstellungen ein
     * 4. Initialisiert Debug-System
     *
     * @param stage Das Hauptfenster der Anwendung
     * @throws Exception Bei Problemen während der Initialisierung
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
     * Führt einen Szenenwechsel durch.
     * Implementiert einen atomaren Übergang mit Vollbild-Erhaltung:
     * 1. Lädt neue Szene im Hintergrund
     * 2. Speichert Vollbild-Status
     * 3. Führt Szenenwechsel in einem einzelnen UI-Update durch
     * 4. Stellt Vollbild-Modus wieder her
     * 5. Initialisiert Dialog-System wenn nötig
     *
     * @param fxml Name der zu ladenden FXML-Datei (ohne .fxml Endung)
     * @throws IOException Bei Problemen beim Laden der FXML-Datei
     */
    public static void setRoot(String fxml) throws IOException {
        try {
            // Vermeidet doppelte Initialisierung
            if (scene != null && scene.getRoot().getId().equals(fxml)) {
                DEBUG_MESSAGES.add("WARNING: Attempting to load same scene");
                return;
            }

            // Lade neue Szene vor dem UI-Update
            Parent root = loadFXML(fxml);
            Scene newScene = new Scene(root);
            newScene.getStylesheets().add(App.class.getResource("menu.css").toExternalForm());

            // Speichere Vollbild-Status
            boolean wasFullScreen = primaryStage.isFullScreen();
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.setFullScreenExitHint("");

            // Atomarer Szenenwechsel
            Scene oldScene = scene;
            scene = newScene;

            // UI-Update in einem einzigen Platform.runLater
            Platform.runLater(() -> {
                try {
                    // Entferne Event Handler der alten Szene
                    if (oldScene != null) {
                        oldScene.setOnKeyPressed(null);
                        oldScene.setOnKeyReleased(null);
                    }

                    // Setze neue Szene
                    primaryStage.setScene(scene);
                    
                    // Stelle Vollbild wieder her
                    if (wasFullScreen) {
                        primaryStage.setFullScreen(true);
                    }

                    // Debug Logging
                    DEBUG_MESSAGES.add("Scene switch completed without leaving fullscreen");

                    // Initialisiere Dialog für Spielszenen
                    if (fxml.equals("FirstScene") || fxml.equals("SecondScene") || 
                        fxml.equals("ThirdScene") || fxml.equals("FourthScene")) {
                        initializeSceneDialog(fxml);
                    }

                    DEBUG_MESSAGES.add("New Scene Loaded: " + (scene != null));
                    DEBUG_MESSAGES.add("New FXML: " + fxml);
                    printDebugMessages("Scene Transition");

                } catch (Exception e) {
                    DEBUG_MESSAGES.add("ERROR during scene transition: " + e.getMessage());
                    printDebugMessages("Scene Transition Error");
                }
            });

        } catch (IOException e) {
            DEBUG_MESSAGES.add("ERROR loading FXML: " + e.getMessage());
            printDebugMessages("FXML Load Error");
            throw e;
        }
    }

    /**
     * Initialisiert das Dialog-System für eine neue Szene.
     * Führt folgende Schritte aus:
     * 1. Sucht das Dialog-Label in der Szene
     * 2. Konfiguriert DialogueManager für die neue Szene
     * 3. Richtet Event-Handler für Tastatureingaben ein
     * 4. Aktiviert Debug-Logging für Dialog-System
     *
     * @param fxml Name der Szene für die Dialog initialisiert wird
     */
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
    
    /**
     * Event-Handler für Tastatureingaben im Dialog-System.
     * Verarbeitet Leertasten-Eingaben zur Navigation durch Dialoge.
     * Implementiert Debug-Logging für Tastatureingaben.
     */
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

    /**
     * Hilfsmethode zum Laden von FXML-Dateien.
     * Sucht FXML-Dateien im Ressourcen-Verzeichnis und lädt sie.
     *
     * @param fxml Name der zu ladenden FXML-Datei (ohne .fxml Endung)
     * @return Parent-Node der geladenen FXML
     * @throws IOException Bei Problemen beim Laden der FXML-Datei
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Haupteinstiegspunkt der Anwendung.
     * Startet die JavaFX-Anwendung.
     *
     * @param args Kommandozeilenargumente (nicht verwendet)
     */
    public static void main(String[] args) {
        launch();
    }
}