package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MenuController {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button playButton;

    @FXML
    private Button quitButton;

    @FXML
    public void initialize() {
        // Bind the ImageView's width and height to the AnchorPane's size
        backgroundImage.fitWidthProperty().bind(rootPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(rootPane.heightProperty());
        backgroundImage.setPreserveRatio(true); // Maintain the aspect ratio
    }

    @FXML
    public void handlePlay() {
        try {
            // Wechsel zur FirstScene
            App.setRoot("FirstScene");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleQuit() {
        System.exit(0);
    }
}
