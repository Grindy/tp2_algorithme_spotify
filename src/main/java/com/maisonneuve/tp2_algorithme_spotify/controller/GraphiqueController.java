package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;
import com.maisonneuve.tp2_algorithme_spotify.algorithme.tri.*;
import com.maisonneuve.tp2_algorithme_spotify.benchmark.Chronometre;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.maisonneuve.tp2_algorithme_spotify.algorithme.Algorithme;


public class GraphiqueController {

    @FXML
    private CheckBox checkTriBulles, checkTriSelection, checkTriInsertion;

    @FXML
    private Spinner<Integer> spinnerRepetitions;

    @FXML
    private Button btnGraphiqueLancer, btnGraphiqueReinitialiser;

    @FXML
    private NumberAxis AxeX, AxeY;

    @FXML
    private LineChart<Number,Number> chartLine;


    private static final int[] TAILLES = {50,100,250,500,750,1000,1500};



    public void initialize(){
        spinnerRepetitions.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,10));
        chartLine.setAnimated(false);

        btnGraphiqueLancer.setOnAction(e -> lancerBenchmark());

        btnGraphiqueReinitialiser.setOnAction(e -> reset());
        definirEcouteursDEvenements();
    }

    public Bibliotheque biblio;


    public void setBibliotheque (Bibliotheque b) {
        this.biblio = b;
    }


    public void definirEcouteursDEvenements() {

    }

    private void lancerBenchmark(){
        List<Algorithme> algos = collecterAlgorithmes();
        if(algos.isEmpty()){
            afficherAlert("Selectionnez au moins un algorithme");
            return;
        }
        List<Chanson> chansons = biblio.getChansons();

        chartLine.getData().clear();
        int repetitions = spinnerRepetitions.getValue();

        new Thread(() -> {
            for(Algorithme algo: algos){
                XYChart.Series<Number,Number> serie = new XYChart.Series<>();

                serie.setName(algo.nom());

                for(int n : TAILLES){
                    //Sinon ca modifie la liste elle meme
                    List<Chanson> listeChansons = new ArrayList<>(chansons.subList(0, n));
                    Comparator<Chanson> comp = Chanson.COMP_DUREE;

                    long tempsNs = Chronometre.mesurer(algo, listeChansons, comp, repetitions);
                    double tempsMilli = tempsNs / 1_000_000.0;
                    Platform.runLater(() ->
                            serie.getData().add(new XYChart.Data<>(n,tempsMilli)));
                }
                Platform.runLater(() -> chartLine.getData().add(serie));
            }
        }).start();
    }

    private void reset(){
        chartLine.getData().clear();
    }

    private List<Algorithme> collecterAlgorithmes(){
        List<Algorithme> algos = new ArrayList<>();

        if(checkTriBulles.isSelected()) algos.add(new TriBulle());
        if(checkTriSelection.isSelected()) algos.add(new TriSelection());
        if(checkTriInsertion.isSelected()) algos.add(new TriInsertion());

        return algos;
    }

    private void afficherAlert(String msg){
        new Alert(Alert.AlertType.WARNING, msg).showAndWait();
    }
}
