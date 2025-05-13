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
        
        // Hole die Bildschirmgröße
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        
        // Setze die Bildgröße auf die Bildschirmgröße
        backgroundImage.setFitWidth(screenWidth);
        backgroundImage.setFitHeight(screenHeight);
        
        // Aktion für den Button oben rechts:
        topRightButton.setOnAction(event -> {
            System.out.println("Top-right Button clicked.");
            try {
                // Wechsel zur Menu
                App.setRoot("menu");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Weitere Aktionen können hier eingebaut werden.
        });
        
        // Hinweis: Entfernt wurde der BackButton-Code, da in der FXML kein Element "backButton" definiert ist.
    }

    
}