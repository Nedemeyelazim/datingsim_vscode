package com.example.dialogue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialogueManager {
    private final List<DialogueLine> dialogueLines;
    private int currentIndex;

    public DialogueManager() {
        this.dialogueLines = new ArrayList<>();
        this.currentIndex = 0;
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