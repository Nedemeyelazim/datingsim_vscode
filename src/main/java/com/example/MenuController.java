package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MenuController {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private AnchorPane rootPane;



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
        } catch (IOException e) {
        }
    }

    @FXML
    public void handleQuit() {
        System.exit(0);
    }
}
