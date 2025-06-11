package com.example;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Erste Spielszene der Dating Sim.
 * Implementiert die Einführungssequenz mit:
 * - Historischer Hintergrund der Han-Dynastie
 * - Setup der geteilten Reiche Wei, Wu und Shu
 * - Einführung der politischen Situation
 * 
 * Diese Szene dient als narrativer Einstieg in die Spielwelt und
 * etabliert den historischen Kontext für die folgende Geschichte.
 */
public class FirstScene extends Scene {

    /**
     * Standardkonstruktor für die erste Szene.
     * Delegiert an den privaten Konstruktor mit geladenem FXML.
     * 
     * @throws IOException Falls die FXML-Datei nicht geladen werden kann
     */
    public FirstScene() throws IOException {
        this(loadFXML());
    }
    
    /**
     * Erweiterter Konstruktor mit Root-Element.
     * Setzt die Szenenauflösung auf 1920x1080 für Full-HD Display.
     * 
     * @param root Das Root-Element der FXML-Hierarchie
     */
    private FirstScene(Parent root) {
        super(root, 1920, 1080);
    }
    
    /**
     * Lädt das FXML-Layout für die erste Szene.
     * Sucht die FXML-Datei im Ressourcenverzeichnis der Anwendung.
     * 
     * @return Parent-Node der geladenen FXML
     * @throws IOException Falls die FXML-Datei nicht gefunden oder geladen werden kann
     */
    private static Parent loadFXML() throws IOException {
        FXMLLoader loader = new FXMLLoader(FirstScene.class.getResource("FirstScene.fxml"));
        return loader.load();
    }
}
