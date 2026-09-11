package com.maisonneuve.tp2_algorithme_spotify.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class GraphiqueController {
    @FXML
    private Button btnAccueil;



    public void initialize(){
        definirEcouteursDEvenements();
    }













    public void definirEcouteursDEvenements() {

        btnAccueil.setOnAction(e -> {
            try {
                Parent newRoot = FXMLLoader.load(getClass().getResource("/vues/Main.fxml"));
                btnAccueil.getScene().setRoot(newRoot);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });
    }




















}
