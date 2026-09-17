package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.AuditJournalier;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import com.maisonneuve.tp2_algorithme_spotify.model.AuditJournalierDAO;
import javafx.stage.Window;


public class AuditJournalierController {

    @FXML
    private TableView<AuditJournalier> tableView;
    @FXML
    private TableColumn<AuditJournalier, String> colTitre;
    @FXML
    private TableColumn<AuditJournalier, Timestamp> colTimestamp;

    private AuditJournalierDAO auditJournalierDAO = new AuditJournalierDAO();

    @FXML
    public void initialize() {
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colTimestamp.setCellValueFactory(new PropertyValueFactory<>("dateLecture"));

        colTitre.prefWidthProperty().bind(tableView.widthProperty().multiply(0.6));
        colTimestamp.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
    }

    public void afficherAuditJournalier(Window ownerWindow) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/AuditJournalier.fxml"));
            Parent root = loader.load();
            AuditJournalierController controller = loader.getController();

            List<AuditJournalier> listeHistorique = auditJournalierDAO.listerHistorique();
            controller.tableView.setItems(FXCollections.observableArrayList(listeHistorique));

            String titre = "Historique";
            Stage popupStage = new Stage();
            popupStage.initOwner(ownerWindow);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.setTitle(titre);

            Scene scene = new Scene(root, 488, 600);
            scene.getStylesheets().add(getClass().getResource("/vues/style.css").toExternalForm());

            popupStage.setScene(scene);
            popupStage.setResizable(false);

            popupStage.showAndWait();


        } catch (IOException | SQLException e) {
            System.err.println("Erreur lors de l'affichage de l'historique: " + e.getMessage());
            e.printStackTrace();
        }
    }
}