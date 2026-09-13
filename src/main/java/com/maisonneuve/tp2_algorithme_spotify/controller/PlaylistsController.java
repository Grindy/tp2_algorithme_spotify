package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class PlaylistsController {

    @FXML
    private Button btnVotreBibliotheque;
    @FXML
    private Button btnAjouterPlaylist;
    @FXML
    private TableView<Playlist> tablePlaylists;
    @FXML
    private TableColumn<Playlist, String> colPlaylists;
    @FXML
    private TableColumn<Playlist, Void> colSupprimerPlaylist;





    private Bibliotheque biblio;
    private PlaylistManager manager;
    private Playlist toutesLesChansons;
    public void setBibliotheque (Bibliotheque b) {
        this.biblio = b;
    }
    public void setPlaylistManager(PlaylistManager manager) {this.manager = manager;}
    public void setToutesLesChansons(Playlist toutesLesChansons) {this.toutesLesChansons = toutesLesChansons;}



    @FXML
    public void initialize() {
        // Lier les colonnes de la liste des playlists
        colPlaylists.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().toString()));

        colSupprimerPlaylist.setCellFactory(col -> new TableCell<Playlist, Void>() {
            private final Button btnSupprimerPlaylist = new Button("X");
            {
                btnSupprimerPlaylist.getStyleClass().add("btn-table-view");
                btnSupprimerPlaylist.setOnAction(event -> {
                    Playlist playlist = getTableRow().getItem();
                    if (playlist != null && demanderConfirmationSuppressionPlaylist()) {
                        manager.retirerPlaylist(playlist);
                        rafraichirListePlaylist();
                        // On vide la sélection, le MainController s'occupera d'afficher "Toutes les chansons"
                        tablePlaylists.getSelectionModel().clearSelection();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btnSupprimerPlaylist);
                }
            }
        });

        // Définir les proportions des colonnes de la liste des playlists
        colPlaylists.prefWidthProperty().bind(tablePlaylists.widthProperty().subtract(4).multiply(0.90));
        colSupprimerPlaylist.prefWidthProperty().bind(tablePlaylists.widthProperty().subtract(4).multiply(0.1));

        // Empêcher les comportements par défaut (tri natif, déplacer les colonnes)
        for (TableColumn<?, ?> col : tablePlaylists.getColumns()) {
            col.setSortable(false);
            col.setReorderable(false);
        }
    }

    public void ouvrirFenetreCreerPlaylist() {
        Stage popupStage = new Stage();


        popupStage.initOwner((Stage) btnAjouterPlaylist.getScene().getWindow());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Créer une nouvelle playlist");


        Label message = new Label("Entrez un nom pour votre playlist");
        TextField nomPlaylist = new TextField();
        Button btnAjouter = new Button("Créer");
        nomPlaylist.setMaxWidth(Double.MAX_VALUE);
        btnAjouter.setMaxWidth(Double.MAX_VALUE);
        HBox hbox = new HBox(8,nomPlaylist, btnAjouter);
        HBox.setHgrow(nomPlaylist, Priority.ALWAYS);

        btnAjouter.setOnAction(e -> {
            manager.ajouterPlaylist(new Playlist(java.util.UUID.randomUUID().toString(), nomPlaylist.getText(), new ArrayList<Chanson>()));
            rafraichirListePlaylist();
            popupStage.close();
        });

        VBox layout = new VBox(15, message, hbox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(8));


        Scene scene = new Scene(layout, 300, 200);
        popupStage.setScene(scene);
        popupStage.setResizable(false);

        popupStage.showAndWait();
    }

    public boolean demanderConfirmationSuppressionPlaylist() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer la playlist ?");
        alert.setContentText("Cette action est irréversible. Voulez-vous continuer ?");

        alert.initOwner((Stage) btnAjouterPlaylist.getScene().getWindow());

        Optional<ButtonType> resultat = alert.showAndWait();

        return (resultat.isPresent() && resultat.get() == ButtonType.OK);
    }

    public void rafraichirListePlaylist() {
        // Afficher la liste des playlists dans la TableView à gauche
        tablePlaylists.setItems(FXCollections.observableArrayList(biblio.getPlaylists()));
        tablePlaylists.refresh();
    }

    public javafx.beans.property.ReadOnlyObjectProperty<Playlist> playlistSelectionneeProperty() {
        return tablePlaylists.getSelectionModel().selectedItemProperty();
    }

    public Button getBtnVotreBibliotheque() {
        return btnVotreBibliotheque;
    }

    public Button getBtnAjouterPlaylist() {
        return btnAjouterPlaylist;
    }

    public void deselectionnerPlaylist() {
        tablePlaylists.getSelectionModel().clearSelection();
    }

}