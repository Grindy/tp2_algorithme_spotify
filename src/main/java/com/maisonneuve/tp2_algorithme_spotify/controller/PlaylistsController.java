package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.controller.MainController;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

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
    private MainController mainController;

    public void setBibliotheque(Bibliotheque b) {
        this.biblio = b;
    }

    public void setPlaylistManager(PlaylistManager manager) {
        this.manager = manager;
    }

    public void setToutesLesChansons(Playlist toutesLesChansons) {
        this.toutesLesChansons = toutesLesChansons;
    }

    public TableView<Playlist> getTablePlaylists() {
        return tablePlaylists;
    }

    @FXML
    public void initialize() {
        creerContextMenu();

        // Lier les colonnes de la liste des playlists
        colPlaylists.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().toString()));

        colSupprimerPlaylist.setCellFactory(col -> new TableCell<Playlist, Void>() {
            private final Button btnSupprimerPlaylist = new Button("X");

            {
                btnSupprimerPlaylist.getStyleClass().add("btn-table-view");
                btnSupprimerPlaylist.setOnAction(event -> {
                    Playlist playlist = getTableRow().getItem();
                    if (playlist != null && demanderConfirmationSuppressionPlaylist()) {
                        try {
                            manager.retirerPlaylist(playlist);
                            rafraichirListePlaylist();
                            // On vide la sélection, le MainController s'occupera d'afficher "Toutes les chansons"
                            tablePlaylists.getSelectionModel().clearSelection();
                        } catch (Exception e) {
                            mainController.afficherAlertErreur("Erreur lors de la suppression de la playlist", e);
                        }

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

    public void ouvrirFenetreActionPlaylist(
            String titre,
            String messageLabel,
            String valeurInitiale,
            String texteBtn,
            Consumer<String> fonctionBtn) {
        Stage popupStage = new Stage();

        popupStage.initOwner(btnAjouterPlaylist.getScene().getWindow());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle(titre);


        Label message = new Label(messageLabel);
        TextField nomPlaylist = new TextField(valeurInitiale);
        Button btnAction = new Button(texteBtn);
        nomPlaylist.setMaxWidth(Double.MAX_VALUE);
        btnAction.setMaxWidth(Double.MAX_VALUE);
        HBox hbox = new HBox(8, nomPlaylist, btnAction);
        HBox.setHgrow(nomPlaylist, Priority.ALWAYS);

        btnAction.setOnAction(e -> {
            String saisie = nomPlaylist.getText().trim();
            if (!saisie.isEmpty()) {
                fonctionBtn.accept(saisie);
                rafraichirListePlaylist();
                popupStage.close();
            }
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

        alert.initOwner(btnAjouterPlaylist.getScene().getWindow());

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

    public void creerEtAjouterPlaylist(String nom) {
        try {
            manager.ajouterPlaylist(new Playlist(
                    UUID.randomUUID().toString(),
                    nom,
                    new ArrayList<>()
            ));
            rafraichirListePlaylist();
        } catch (Exception e) {
            mainController.afficherAlertErreur("Erreur lors de la création de la playlist", e);
        }
    }

    public void creerContextMenu() {
        tablePlaylists.setRowFactory(tv -> {
            TableRow<Playlist> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem modifierNomChanson = new MenuItem("Modifier le nom de la playlist");
            modifierNomChanson.setOnAction(e -> {
                Playlist playlist = row.getItem();
                ouvrirFenetreActionPlaylist(
                        "Modifier le nom de la playlist",
                        "Entrez le nouveau nom de votre playlist",
                        playlist.getNom(),
                        "Modifier",
                        playlist::setNom
                        );
            });

            contextMenu.getItems().add(modifierNomChanson);

            // Ne s'affiche pas si la ligne est vide
            row.emptyProperty().addListener((obs, wasEmpty, isEmpty) -> {
                if (isEmpty) {
                    row.setContextMenu(null);
                } else {
                    row.setContextMenu(contextMenu);
                }
            });

            return row;
        });
    }

}