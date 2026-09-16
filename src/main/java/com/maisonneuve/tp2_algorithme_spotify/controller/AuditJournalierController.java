package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.AuditJournalier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import com.maisonneuve.tp2_algorithme_spotify.model.AuditJournalierDAO;
import javafx.stage.Window;


public class AuditJournalierController {

    @FXML
    private TableView tableHistorique;

    private AuditJournalierDAO auditJournalierDAO = new AuditJournalierDAO();

    public void afficherAuditJournalier(Window ownerWindow) {
        String titre = "Historique";
        Stage popupStage = new Stage();
        popupStage.initOwner(ownerWindow);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle(titre);


        List<AuditJournalier> listeHistorique;
        try {
            listeHistorique = auditJournalierDAO.listerHistorique();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique: " + e.getMessage());
            return;
        }

        ObservableList<AuditJournalier> tableHistorique = FXCollections.observableArrayList(listeHistorique);
        TableView<AuditJournalier> tableView = new TableView<>(tableHistorique);

        TableColumn<AuditJournalier, String> colTitre = new TableColumn<>("Titre");
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));

        TableColumn<AuditJournalier, Timestamp> colTimestamp = new TableColumn<>("Date jouée");
        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("dateLecture"));

        tableView.getColumns().add(colTitre);
        tableView.getColumns().add(colTimestamp);



        VBox layout = new VBox(15, tableView);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(8));

        Scene scene = new Scene(layout, 300, 450);
        popupStage.setScene(scene);
        popupStage.setResizable(false);

        popupStage.showAndWait();

    }

}
