package com.example.dialogue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.example.App;

import javafx.scene.control.Label;

public class DialogueManager {
    private static DialogueManager instance;
    private final List<DialogueLine> dialogueLines;
    private int currentIndex;
    private Label dialogueLabel;
    private String currentScene = "FirstScene";

    private DialogueManager() {
        this.dialogueLines = new ArrayList<>();
        this.currentIndex = 0;
    }

    public static DialogueManager getInstance() {
        if (instance == null) {
            instance = new DialogueManager();
        }
        return instance;
    }

    public void initializeDialog(Label label) {
        this.dialogueLabel = label;
        if (!dialogueLines.isEmpty()) {
            showNextLine(); // Zeige erste Zeile, wenn Dialog geladen ist
        }
    }

    private void loadDefaultDialogue() {
        URL dialogueResource = getClass().getResource("/com/example/dialogue.txt");
        System.out.println("Suche Dialog in: " + dialogueResource);
        
        if (dialogueResource == null) {
            System.out.println("FEHLER: dialogue.txt nicht gefunden!");
            return;
        }

        try {
            loadDialogue(dialogueResource.getPath());
            System.out.println("Dialog geladen, zeige erste Zeile...");
            if (hasMoreDialogue()) {
                showNextLine();
                System.out.println("Erste Zeile angezeigt!");
            }
        } catch (Exception e) {
            System.out.println("Fehler beim Laden des Dialogs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showNextLine() {
        System.out.println("\n=== DialogueManager Debug ===");
        System.out.println("Current Scene: " + currentScene);
        System.out.println("Current Index: " + currentIndex);
        System.out.println("Total Lines: " + dialogueLines.size());

        if (dialogueLabel == null) {
            System.out.println("ERROR: DialogueLabel ist null!");
            return;
        }

        if (hasMoreDialogue()) {
            DialogueLine line = getCurrentLine();
            
            // Check for End marker
            if (line.getCharacter().equals("[End]")) {
                System.out.println("Found [End] marker - Transitioning to next scene");
                if (currentScene.equals("FirstScene")) {
                    try {
                        System.out.println("Transitioning from FirstScene to SecondScene");
                        App.setRoot("SecondScene");
                        currentScene = "SecondScene";
                    } catch (IOException e) {
                        System.out.println("ERROR transitioning to SecondScene: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else if (currentScene.equals("SecondScene")) {
                    try {
                        System.out.println("Transitioning from SecondScene to ThirdScene");
                        App.setRoot("ThirdScene");
                        currentScene = "ThirdScene";
                    } catch (IOException e) {
                        System.out.println("ERROR transitioning to ThirdScene: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                advanceDialogue();
            } else {
                dialogueLabel.setText(line.getDialogue());
                System.out.println("Showing dialogue: " + line.getDialogue());
                advanceDialogue();
            }
        } else {
            System.out.println("No more dialogue available");
            dialogueLabel.setText("Ende des Dialogs.");
        }
        System.out.println("===========================\n");
    }

    public void setCurrentScene(String scene) {
        System.out.println("\n=== Scene Change Debug ===");
        System.out.println("Changing from " + currentScene + " to " + scene);
        
        this.currentScene = scene;
        
        // Reset index for new scene
        currentIndex = 0;
        boolean foundScene = false;
        
        for (int i = 0; i < dialogueLines.size(); i++) {
            DialogueLine line = dialogueLines.get(i);
            if (line.getCharacter().equals(scene)) {
                currentIndex = i + 1;
                foundScene = true;
                System.out.println("Found scene marker at index " + i);
                break;
            }
        }
        
        if (!foundScene) {
            System.out.println("WARNING: Scene marker [" + scene + "] not found in dialogue!");
        } else {
            System.out.println("Scene changed successfully. New index: " + currentIndex);
        }
        System.out.println("=========================\n");
    }

    public void loadDialogue(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            String currentCharacter = "";

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("[") && line.endsWith("]")) {
                    currentCharacter = line.substring(1, line.length() - 1);
                } else if (!line.isEmpty()) {
                    dialogueLines.add(new DialogueLine(currentCharacter, line));
                }
            }
        }
    }

    public void loadDialogueFromStream(InputStream input) throws IOException {
        dialogueLines.clear();
        currentIndex = 0;
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            String line;
            String currentCharacter = "";
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                
                if (line.equals("[End]")) {
                    dialogueLines.add(new DialogueLine("[End]", ""));
                    System.out.println("Added End marker to dialogue");
                }
                else if (line.startsWith("[") && line.endsWith("]")) {
                    currentCharacter = line;
                    dialogueLines.add(new DialogueLine(currentCharacter, ""));
                    System.out.println("Added scene marker: " + currentCharacter);
                }
                else {
                    dialogueLines.add(new DialogueLine(currentCharacter, line));
                    System.out.println("Added dialogue line: " + line);
                }
            }
        }
        System.out.println("Loaded " + dialogueLines.size() + " dialogue lines");
    }

    public DialogueLine getCurrentLine() {
        if (currentIndex < dialogueLines.size()) {
            return dialogueLines.get(currentIndex);
        }
        return null;
    }

    public boolean advanceDialogue() {
        if (currentIndex < dialogueLines.size() - 1) {
            currentIndex++;
            return true;
        }
        return false;
    }

    public boolean hasMoreDialogue() {
        return currentIndex < dialogueLines.size();
    }

    public void resetDialogue() {
        currentIndex = 0;
    }

    public static class DialogueLine {
        private final String character;
        private final String dialogue;

        public DialogueLine(String character, String dialogue) {
            this.character = character;
            this.dialogue = dialogue;
        }

        public String getCharacter() {
            return character;
        }

        public String getDialogue() {
            return dialogue;
        }

        @Override
        public String toString() {
            return character + ": " + dialogue;
        }
    }
}