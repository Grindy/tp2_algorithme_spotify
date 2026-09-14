package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistManager;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.util.*;

public class MainController {

    @FXML
    private BorderPane rootPane;

    // Top
    @FXML
    private Button btnAccueil;
    @FXML
    private TextField fieldRecherche;
    @FXML
    private Button btnGraph;

    // Left

    @FXML
    private PlaylistsController playlistsController;

    // Center (Filtres, Liste, Pagination)


    private Bibliotheque biblio;
    private Playlist toutesLesChansons;
    private PlaylistManager playlistManager;
    private PlaylistService playlistService;
    private int nbChansonsParPage = 25;
    private int nbPagesTotales;
    private int pageCourante = 1;
    private Playlist playListSelectionne;
    public static final String IMAGE_PAR_DEFAUT = "https://i.pinimg.com/736x/ba/8e/4d/ba8e4de740a641feb1709ce713889ea5.jpg";
    private Node accueilLeft;
    private Node accueilRight;
    private Node accueilCentre;
    private final BooleanProperty playlistEstFiltreOuTrie = new SimpleBooleanProperty(false);
    private final BooleanProperty toutesLesChansonsEstSelectionne = new SimpleBooleanProperty(true);

    @FXML
    private ChansonController chansonController;

    @FXML
    private TableChansonsController sectionTableChansonsController;


    @FXML
    public void initialize() {
        accueilLeft = rootPane.getLeft();
        accueilCentre = rootPane.getCenter();
        accueilRight = rootPane.getRight();
        creerBibliothequeEtPlaylists();
        afficherLecteur();
        initplaylistsController();
        initTableChansonController();

        definirEcouteursDEvenements();

        // Au démarrage, la liste d'accueil est sélectionnée (Votre Bibliothèque)
        playListSelectionne = toutesLesChansons;
        sectionTableChansonsController.rafraichirListeChansons(playListSelectionne, pageCourante);
    }

    private void initTableChansonController() {
        sectionTableChansonsController.setChansonController(chansonController);
        sectionTableChansonsController.setFieldRecherche(fieldRecherche);
        sectionTableChansonsController.setplaylistsController(playlistsController);
        sectionTableChansonsController.setPlayListSelectionne(playListSelectionne);
        sectionTableChansonsController.setPlaylistService(playlistService);
        sectionTableChansonsController.setTablePlaylists(playlistsController.getTablePlaylists());
        sectionTableChansonsController.setToutesLesChansons(toutesLesChansons);
        sectionTableChansonsController.setToutesLesChansonsEstSelectionne(toutesLesChansonsEstSelectionne);
        sectionTableChansonsController.setBiblio(biblio);
        sectionTableChansonsController.setPageCourante(pageCourante);
    }

    private void initplaylistsController() {
        playlistsController.setBibliotheque(biblio);
        playlistsController.setPlaylistManager(playlistManager);
        playlistsController.setToutesLesChansons(toutesLesChansons);
        playlistsController.rafraichirListePlaylist();
    }

    @FXML
    private void afficherGraphique() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/Graphique.fxml"));
            BorderPane graphique = loader.load();
            GraphiqueController gc = loader.getController();

            gc.setBibliotheque(this.biblio);

            rootPane.setLeft(graphique.getLeft());
            rootPane.setCenter(graphique.getCenter());
            rootPane.setRight(null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void afficherLecteur() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/Lecteur.fxml"));
            BorderPane lecteur = (BorderPane) loader.load();
            rootPane.setBottom(lecteur.getBottom());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        }

    public void definirEcouteursDEvenements() {
        btnGraph.setOnAction(e -> afficherGraphique());
        btnAccueil.setOnAction(e -> {
            afficherAccueil();
            playlistsController.deselectionnerPlaylist();
            sectionTableChansonsController.rafraichirListeChansons(toutesLesChansons, 1);
        });

        playlistsController.getBtnAjouterPlaylist().setOnAction(e -> {
            playlistsController.ouvrirFenetreCreerPlaylist();
            playlistsController.rafraichirListePlaylist();
        });

        playlistsController.playlistSelectionneeProperty().addListener((obs, anciennePlaylist, nouvellePlaylist) -> {
            if (nouvellePlaylist != null) {
                sectionTableChansonsController.rafraichirListeChansons(nouvellePlaylist, 1);
            } else {
                // Si la playlist est supprimée et la sélection devient nulle
                sectionTableChansonsController.rafraichirListeChansons(toutesLesChansons, 1);
            }
        });

        playlistsController.getBtnVotreBibliotheque().setOnAction(e -> {
            sectionTableChansonsController.rafraichirListeChansons(toutesLesChansons, 1);
        });

    };
    private void afficherAccueil() {
        rootPane.setLeft(accueilLeft);
        rootPane.setCenter(accueilCentre);
        rootPane.setRight(accueilRight);
    }

    public void creerBibliothequeEtPlaylists() {
        // Créer la bibliothèque et créer une playlist contenant toutes les chansons
        biblio = new Bibliotheque("src/main/resources/data/spotifyData.csv");
        toutesLesChansons = new Playlist("1", "Toutes les chansons", biblio.getChansons());

        // Créer 3 playlist de 25 chansons (les 75 premières chansons du CSV)
        List<Chanson> chansons = biblio.getChansons();
        Playlist playlist1 = new Playlist("2", "Playlist 1", chansons.subList(0, 25));
        Playlist playlist2 = new Playlist("3", "Playlist 2", chansons.subList(25, 50));
        Playlist playlist3 = new Playlist("4", "Playlist 3", chansons.subList(50, 75));

        // Ajouter les playlists à la bibliothèque
        playlistManager = new PlaylistManager(biblio);
        for (Playlist p : List.of(playlist1, playlist2, playlist3)) {
            playlistManager.ajouterPlaylist(p);
        }

        // Créer un playlist service pour les filtres et tris
        playlistService = new PlaylistService();

    }
}