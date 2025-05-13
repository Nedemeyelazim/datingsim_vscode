package com.example;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class FirstScene extends Scene {

    public FirstScene() throws IOException {
        this(loadFXML());
    }
    
    private FirstScene(Parent root) {
        super(root, 1920, 1080);
    }
    
    private static Parent loadFXML() throws IOException {
        FXMLLoader loader = new FXMLLoader(FirstScene.class.getResource("FirstScene.fxml"));
        return loader.load();
    }
}
