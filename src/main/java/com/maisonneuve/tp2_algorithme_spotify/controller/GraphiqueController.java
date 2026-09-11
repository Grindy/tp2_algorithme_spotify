package com.maisonneuve.tp2_algorithme_spotify.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;

public class GraphiqueController {

    public void initialize(){
        definirEcouteursDEvenements();
    }

    public Bibliotheque biblio;

    public void setBibliotheque (Bibliotheque b) {
        this.biblio = b;
    }

    public void definirEcouteursDEvenements() {

    }

}
