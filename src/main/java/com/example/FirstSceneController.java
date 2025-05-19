package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;

public class FirstSceneController {

    @FXML
    private AnchorPane rootPane; // Hauptcontainer
    
    @FXML
    private ImageView backgroundImage;
    
    // Hier verwenden wir den Button oben rechts
    @FXML
    private Button topRightButton;
    
    @FXML
    public void initialize() {
        System.out.println("FirstScene initialized – alles OK!");
        
        // Bildschirmgröße einstellen
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        
        backgroundImage.setFitWidth(screenWidth);
        backgroundImage.setFitHeight(screenHeight);
        
        // Back Button Handler
        topRightButton.setOnAction(event -> {
            try {
                App.setRoot("menu");
                System.out.println("Top-right Button clicked.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}