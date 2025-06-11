package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

public class FourthSceneController {
    
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
        System.out.println("FourthScene initializing...");
        
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
                    System.out.println("FourthScene: Back button clicked - switching to ThirdScene");
                    App.setRoot("ThirdScene");
                    System.out.println("FourthScene: Switch to ThirdScene completed");
                } catch (Exception e) {
                    System.out.println("FourthScene: Error switching scene: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            System.out.println("Back button handler initialized");
        }

        System.out.println("Fourth initialization complete");
    }
}