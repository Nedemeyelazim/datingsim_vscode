package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

public class SixthSceneController {
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private ImageView backgroundImage;
    
    @FXML
    private Button topRightButton;
    
    @FXML
    private Label dialogueLabel;

    @FXML
    private void initialize() {
        System.out.println("SixthScene initializing...");
        
        // Debug: Print scene components
        System.out.println("Components check:");
        System.out.println("- rootPane: " + (rootPane != null ? "found" : "missing"));
        System.out.println("- backgroundImage: " + (backgroundImage != null ? "found" : "missing"));
        System.out.println("- topRightButton: " + (topRightButton != null ? "found" : "missing"));
        System.out.println("- dialogueLabel: " + (dialogueLabel != null ? "found" : "missing"));

        // Set screen size
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        System.out.println("Screen size: " + screenWidth + "x" + screenHeight);
        
        if (backgroundImage != null) {
            backgroundImage.setFitWidth(screenWidth);
            backgroundImage.setFitHeight(screenHeight);
            System.out.println("Background image size set to: " + screenWidth + "x" + screenHeight);
        }

        if (topRightButton != null) {
            topRightButton.setOnAction(event -> {
                try {
                    System.out.println("SixthScene: Back button clicked - switching to FifthScene");
                    App.setRoot("FifthScene");
                    System.out.println("SixthScene: Switch to FifthScene completed");
                } catch (Exception e) {
                    System.out.println("SixthScene: Error switching scene: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            System.out.println("Back button handler initialized");
        }

        System.out.println("Sixth initialization complete");
    }
}