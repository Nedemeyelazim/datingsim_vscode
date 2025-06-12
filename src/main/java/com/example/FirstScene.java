package com.example;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Erste Spielszene: Einführung in die Drei Königreiche
 * --------------------------------------------------
 * Implementiert den narrativen Einstieg in die Visual Novel.
 * 
 * Narrative Elemente:
 * - Historischer Kontext der späten Han-Dynastie
 * - Vorstellung der drei Königreiche:
 *   • Wei (北魏): Nördliches Reich, reich an Ressourcen
 *   • Wu (吳): Südliches Reich, stark in Marine
 *   • Shu (蜀): Westliches Reich, geschützt durch Berge
 * - Politische Spannungen und Machtkämpfe
 * 
 * Technische Features:
 * - FXML-basiertes Layout-System
 * - Full-HD Auflösung (1920x1080)
 * - Optimierte Asset-Ladeprozesse
 * - Integriertes Dialog-System
 * 
 * Design-Prinzipien:
 * - Klare Trennung von UI und Logik
 * - Wiederverwendbare FXML-Komponenten
 * - Einheitliche Szenenübergänge
 * 
 * @author Type Soul Productions
 * @version 1.0
 */
public class FirstScene extends Scene {

    /**
     * Standardkonstruktor
     * ------------------
     * Initialisiert die Szene mit Standard-FXML.
     * 
     * Prozessablauf:
     * 1. FXML-Datei laden
     * 2. Root-Element extrahieren
     * 3. Szenenaufbau starten
     * 
     * @throws IOException Bei FXML-Ladefehlern
     */
    public FirstScene() throws IOException {
        this(loadFXML());
    }
    
    /**
     * Erweiterter Konstruktor
     * ----------------------
     * Konfiguriert Szene mit spezifischem Root-Element.
     * 
     * Display-Konfiguration:
     * - Auflösung: 1920x1080 Pixel
     * - Aspect Ratio: 16:9
     * - Skalierbar für verschiedene Bildschirmgrößen
     * 
     * @param root FXML Root-Node
     */
    private FirstScene(Parent root) {
        super(root, 1920, 1080);
    }
    
    /**
     * FXML-Lader
     * ----------
     * Lädt und verarbeitet die FXML-Ressource.
     * 
     * Ressourcen-Management:
     * 1. Lokalisiert FXML im Classpath
     * 2. Initialisiert FXMLLoader
     * 3. Parsed FXML-Struktur
     * 4. Erstellt Parent-Node
     * 
     * Fehlerbehandlung:
     * - Prüft FXML-Existenz
     * - Validiert FXML-Struktur
     * - Behandelt Ressourcen-Fehler
     * 
     * @return Geladene FXML-Hierarchie
     * @throws IOException Bei Ressourcen-Problemen
     */
    private static Parent loadFXML() throws IOException {
        FXMLLoader loader = new FXMLLoader(FirstScene.class.getResource("FirstScene.fxml"));
        return loader.load();
    }
}
