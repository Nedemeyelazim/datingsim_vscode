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
 * Dating Simulation Hauptanwendung
 * -------------------------------
 * Eine Visual Novel Engine für interaktive Geschichte im Setting
 * der Drei Königreiche Chinas.
 * 
 * Kernfunktionen:
 * - Multi-Szenen Management mit flüssigen Übergängen
 * - Fortgeschrittenes Dialog-System mit Text-Progression
 * - Persistenter Fullscreen-Modus
 * - Umfangreiches Debug-System
 * 
 * Technische Features:
 * - JavaFX UI-Framework
 * - FXML-basierte Szenen-Layouts
 * - CSS-Styling für konsistentes Design
 * - Event-basierte Benutzerinteraktion
 * 
 * @author Type Soul Productions
 * @version 1.0
 */
public class App extends Application {

    /** 
     * Zentrale UI-Komponenten
     * ----------------------
     * scene: Aktuelle aktive Szene, verwaltet UI-Hierarchie
     * primaryStage: Hauptfenster der Anwendung, Container für Szenen
     * dialogueManager: Singleton für zentrales Dialog-Management
     * dialogueLabel: UI-Element zur Textanzeige in Spielszenen
     */
    private static Scene scene;
    private static Stage primaryStage;
    private static final DialogueManager dialogueManager = DialogueManager.getInstance();
    private static Label dialogueLabel;

    /** 
     * Debug-System
     * -----------
     * Sammelt und organisiert Debug-Nachrichten für strukturierte Ausgabe.
     * Nachrichten werden nach Priorität sortiert und in Blöcken ausgegeben.
     */
    private static final ArrayList<String> DEBUG_MESSAGES = new ArrayList<>();

    /**
     * Debug-Nachricht Prioritäts-Sortierung
     * -----------------------------------
     * Implementiert 3-stufige Priorisierung für Debug-Ausgaben:
     * 1. ERROR-Nachrichten (höchste Priorität)
     *    - Kritische Fehler und Exceptions
     *    - Sofortige Aufmerksamkeit erforderlich
     * 
     * 2. Debug-Header (mittlere Priorität)
     *    - Markiert durch === Symbole
     *    - Strukturiert Debug-Ausgaben in Blöcke
     * 
     * 3. Standard-Nachrichten (normale Priorität)
     *    - Nach Länge sortiert für bessere Lesbarkeit
     *    - Längere Nachrichten erscheinen zuerst
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
     * Debug-Ausgabe Prozessor
     * ----------------------
     * Verarbeitet gesammelte Debug-Nachrichten:
     * 1. Sortiert nach definierter Priorität
     * 2. Formatiert mit Kontext-Header
     * 3. Gibt als Block aus
     * 4. Bereinigt Buffer nach Ausgabe
     * 
     * @param context Identifiziert den Debug-Block (z.B. "Scene Transition")
     */
    private static void printDebugMessages(String context) {
        DEBUG_MESSAGES.sort(DEBUG_MESSAGE_COMPARATOR);
        System.out.println("\n=== Debug Output: " + context + " ===");
        DEBUG_MESSAGES.forEach(System.out::println);
        System.out.println("====================\n");
        DEBUG_MESSAGES.clear();
    }

    /**
     * Anwendungsstart und Initialisierung
     * ---------------------------------
     * Mehrstufiger Startprozess:
     * 1. Dialog-System
     *    - Lädt Dialogtexte aus externer Datei
     *    - Initialisiert DialogueManager
     * 
     * 2. UI-Setup
     *    - Lädt Hauptmenü-FXML
     *    - Erstellt erste Szene
     *    - Lädt CSS-Styling
     * 
     * 3. Fenster-Konfiguration
     *    - Setzt Titel und Größe
     *    - Aktiviert Vollbild
     *    - Konfiguriert Vollbild-Verhalten
     * 
     * @param stage JavaFX Hauptfenster
     * @throws Exception Bei Initialisierungsfehlern
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
            primaryStage.setTitle("Dating Sim");
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
            // Lade neue Szene
            Parent root = loadFXML(fxml);
            Scene newScene = new Scene(root);
            newScene.getStylesheets().add(App.class.getResource("menu.css").toExternalForm());

            // Speichere Vollbild-Status
            boolean wasFullScreen = primaryStage.isFullScreen();
            primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            primaryStage.setFullScreenExitHint("");

            // Führe Transition durch
            Platform.runLater(() -> {
                try {
                    Scene oldScene = scene;
                    scene = newScene;

                    SceneTransition.fadeTransition(primaryStage, oldScene, newScene, () -> {
                        // Nach der Transition
                        if (wasFullScreen) {
                            primaryStage.setFullScreen(true);
                        }
                        
                        // Initialisiere Dialog für Spielszenen
                        if (fxml.equals("FirstScene") || fxml.equals("SecondScene") || 
                            fxml.equals("ThirdScene") || fxml.equals("FourthScene") || 
                            fxml.equals("FifthScene") || fxml.equals("SixthScene") || fxml.equals("SeventhScene")) {
                            initializeSceneDialog(fxml);
                        }
                    });

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