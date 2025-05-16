package com.example.dialogue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Label;

public class DialogueManager {
    private static DialogueManager instance;
    private final List<DialogueLine> dialogueLines;
    private int currentIndex;
    private Label dialogueLabel;

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
        if (dialogueLabel == null) {
            System.out.println("FEHLER: DialogueLabel ist null!");
            return;
        }

        if (hasMoreDialogue()) {
            DialogueLine line = getCurrentLine();
            dialogueLabel.setText(line.getDialogue()); // Nur den Dialog-Text anzeigen
            System.out.println("Zeige Dialog: " + line.getDialogue());
            advanceDialogue();
        } else {
            dialogueLabel.setText("Ende des Dialogs.");
        }
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

                if (line.startsWith("[") && line.endsWith("]")) {
                    currentCharacter = line.substring(1, line.length() - 1);
                } else if (!line.isEmpty()) {
                    dialogueLines.add(new DialogueLine(currentCharacter, line));
                }
            }
        }
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