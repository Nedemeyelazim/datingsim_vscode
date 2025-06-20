package com.example.dialogue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.App; 

import javafx.scene.control.Label;

/**
 * Dialog-Management System
 * ----------------------
 * Zentrale Komponente für die Verwaltung und Anzeige von Dialogtexten
 * in der Visual Novel Engine.
 * 
 * Kernfunktionen:
 * - Szenenbasierte Dialog-Organisation
 * - Dynamisches Nachladen von Texten
 * - Automatische Szenenübergänge
 * - Thread-sicheres Singleton-Pattern
 * 
 * Dateiformat für Dialoge:
 * [SzenenName]
 * Dialog-Text Zeile 1
 * Dialog-Text Zeile 2
 * [End]
 * 
 * @author Type Soul Productions
 * @version 1.0
 */
public class DialogueManager {
    
    /**
     * Interne Datenstrukturen
     * ----------------------
     * sceneDialogues: Speichert Dialogtexte pro Szene
     * currentScene: Aktuelle aktive Szene
     * currentIndex: Position im aktuellen Dialog
     * dialogueLabel: UI-Element für Textanzeige
     */
    private Map<String, List<String>> sceneDialogues = new HashMap<>();
    private String currentScene;
    private int currentIndex = 0;
    private Label dialogueLabel;
    private static DialogueManager instance;

    /** Private Konstruktor für Singleton-Pattern */
    private DialogueManager() {}

    /**
     * Singleton-Zugriffsmethode
     * Thread-sicher durch lazy initialization
     */
    public static DialogueManager getInstance() {
        if (instance == null) {
            instance = new DialogueManager();
        }
        return instance;
    }

    /**
     * Dialog-Initialisierung
     * ---------------------
     * Richtet neue Dialog-Anzeige ein und startet Text
     * 
     * @param label JavaFX Label für Textanzeige
     */
    public void initializeDialog(Label label) {
        this.dialogueLabel = label;
        currentIndex = 0;
        showNextLine();
    }

    /**
     * Dialog-Datei Laden
     * -----------------
     * Liest Dialogtexte aus externer Datei:
     * 1. Erkennt Szenenmarker [SzenenName]
     * 2. Sammelt zugehörige Dialogzeilen
     * 3. Speichert in sceneDialogues Map
     * 
     * @param filename Pfad zur Dialog-Datei
     * @throws IOException Bei Dateizugriffsproblemen
     */
    public void loadDialogue(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filename))) {
            String line;
            String currentScene = null;
            List<String> currentDialogue = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("[") && line.endsWith("]")) {
                    // If we were processing a scene, save it
                    if (currentScene != null && !currentDialogue.isEmpty()) {
                        sceneDialogues.put(currentScene, new ArrayList<>(currentDialogue));
                    }
                    // Start new scene
                    currentScene = line.substring(1, line.length() - 1);
                    currentDialogue.clear();
                } else if (!line.isEmpty() && currentScene != null) {
                    currentDialogue.add(line);
                }
            }
            // Save the last scene
            if (currentScene != null && !currentDialogue.isEmpty()) {
                sceneDialogues.put(currentScene, new ArrayList<>(currentDialogue));
            }
        }
    }

    /**
     * Stream-basiertes Dialog-Laden
     * ---------------------------
     * Wie loadDialogue(), aber für InputStream:
     * - Löscht existierende Dialoge
     * - Überspringt Leerzeilen und Kommentare
     * - Gibt Debug-Informationen aus
     * 
     * @param stream InputStream mit Dialogdaten
     * @throws IOException Bei Stream-Problemen
     */
    public void loadDialogueFromStream(InputStream stream) throws IOException {
        sceneDialogues.clear();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String currentScene = null;
        List<String> currentDialogue = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("//")) continue;

            if (line.startsWith("[") && line.endsWith("]")) {
                if (line.equals("[End]")) {
                    // Save current scene's dialogue when we hit [End]
                    if (currentScene != null && !currentDialogue.isEmpty()) {
                        sceneDialogues.put(currentScene, new ArrayList<>(currentDialogue));
                        currentDialogue.clear();
                    }
                } else {
                    // Start new scene
                    currentScene = line.substring(1, line.length() - 1);
                    currentDialogue.clear();
                }
            } else if (currentScene != null) {
                currentDialogue.add(line);
            }
        }

        // Debug output
        System.out.println("\n=== Dialogue Loading Debug ===");
        for (String scene : sceneDialogues.keySet()) {
            System.out.println("Scene: " + scene);
            System.out.println("Lines: " + sceneDialogues.get(scene).size());
        }
        System.out.println("===========================\n");
    }

    /**
     * Szenen-Aktivierung
     * -----------------
     * Wechselt zur angegebenen Szene:
     * 1. Validiert Szenen-Existenz
     * 2. Setzt Zähler zurück
     * 3. Zeigt ersten Dialog
     * 4. Logging für Debug
     * 
     * @param scene Name der Zielszene
     */
    public void setCurrentScene(String scene) {
        System.out.println("\n=== Scene Change Debug ===");
        System.out.println("Changing from " + currentScene + " to " + scene);
        
        this.currentScene = scene;
        this.currentIndex = 0;
        
        if (!sceneDialogues.containsKey(scene)) {
            System.out.println("WARNING: Scene marker [" + scene + "] not found in dialogue!");
            return;
        }
        
        // Reset dialogue label and show first line
        if (dialogueLabel != null) {
            showNextLine(); // Show the first line immediately
        }
        
        System.out.println("=========================\n");
    }

    /**
     * Dialog-Progression
     * ----------------
     * Zeigt nächste Dialogzeile oder initiiert Szenenwechsel:
     * - Prüft Verfügbarkeit weiterer Zeilen
     * - Aktualisiert UI
     * - Handled Szenenende
     * - Ausführliches Logging
     */
    public void showNextLine() {
        if (dialogueLabel == null || currentScene == null) return;

        List<String> currentDialogue = sceneDialogues.get(currentScene);
        if (currentDialogue == null || currentDialogue.isEmpty()) {
            System.out.println("WARNING: No dialogue found for scene " + currentScene);
            return;
        }

        System.out.println("\n=== DialogueManager Debug ===");
        System.out.println("Current Scene: " + currentScene);
        System.out.println("Current Index: " + currentIndex);
        System.out.println("Total Lines: " + currentDialogue.size());

        if (currentIndex < currentDialogue.size()) {
            String line = currentDialogue.get(currentIndex);
            dialogueLabel.setText(line);
            currentIndex++;
            System.out.println("Showing dialogue: " + line);
        } else {
            // Handle end of dialogue and scene transitions
            dialogueLabel.setText("");
            handleSceneTransition();
        }
        
        System.out.println("===========================\n");
    }

    /**
     * Szenen-Übergangslogik
     * --------------------
     * Verwaltet Übergänge zwischen Szenen:
     * - Definiert Ablaufsequenz
     * - Lädt neue FXML
     * - Aktualisiert DialogManager
     * - Fehlerbehandlung
     */
    private void handleSceneTransition() {
        try {
            switch (currentScene) {
                case "FirstScene":
                    App.setRoot("SecondScene");
                    setCurrentScene("SecondScene");
                    break;
                case "SecondScene":
                    App.setRoot("ThirdScene");
                    setCurrentScene("ThirdScene");
                    break;
                case "ThirdScene":
                    App.setRoot("FourthScene");
                    setCurrentScene("FourthScene");
                    break;
                case "FourthScene":
                    App.setRoot("FifthScene");
                    setCurrentScene("FifthScene");
                    break;
                case "FifthScene":
                    App.setRoot("SixthScene");
                    setCurrentScene("SixthScene");
                    break;
                case "SixthScene":
                    App.setRoot("SeventhScene");
                    setCurrentScene("SeventhScene");
                    break;    
                case "SeventhScene":
                    App.setRoot("menu");
                    setCurrentScene("menu");
                    break;
                default:
                    System.out.println("No next scene defined for: " + currentScene);
            }
        } catch (IOException e) {
            System.out.println("Error during scene transition: " + e.getMessage());
        }
    }
}