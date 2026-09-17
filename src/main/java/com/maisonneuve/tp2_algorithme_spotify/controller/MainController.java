package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.DAO.ChansonDAO;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.DAO.PlaylistDAO;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistManager;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistService;
import com.maisonneuve.tp2_algorithme_spotify.utils.Initialisation;
import com.maisonneuve.tp2_algorithme_spotify.utils.SourceDonnees;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.sql.SQLException;
import java.util.ArrayList;

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
    @FXML
    private Button btnAuditJournalier;

    // Left

    @FXML
    private PlaylistsController playlistsController;

    // Center (Filtres, Liste, Pagination)


    private Bibliotheque biblio;
    private Playlist toutesLesChansons;
    private PlaylistManager playlistManager;
    private PlaylistService playlistService = new PlaylistService();
    private final int pageCourante = 1;
    private Playlist playListSelectionne;
    public static final String IMAGE_PAR_DEFAUT = "https://i.pinimg.com/736x/ba/8e/4d/ba8e4de740a641feb1709ce713889ea5.jpg";
    private Node accueilLeft;
    private Node accueilRight;
    private Node accueilCentre;
    private final BooleanProperty toutesLesChansonsEstSelectionne = new SimpleBooleanProperty(true);
    private final PlaylistDAO playlistDao = new PlaylistDAO();
    private final ChansonDAO chansonDAO = new ChansonDAO();
    private AuditJournalierController auditJournalierController = new AuditJournalierController();

    @FXML
    private ChansonController chansonController;
    @FXML
    private TableChansonsController sectionTableChansonsController;
    @FXML
    private LecteurController lecteurController = new LecteurController();

    @FXML
    public void initialize() {
        accueilLeft = rootPane.getLeft();
        accueilCentre = rootPane.getCenter();
        accueilRight = rootPane.getRight();

        creerBibliotheque();
        creerPlaylistBilio();

        try {
            this.playlistManager = new PlaylistManager(biblio);
            Initialisation.peuplerPlaylistBiblioSiVide(this.biblio, this.toutesLesChansons, this.playlistDao);
        } catch (SQLException e) {
            afficherAlertErreur("Erreur lors de l'initialisation des playlists", e);
        }

        afficherLecteur();
        initplaylistsController();
        initTableChansonController();
        auditJournalierController.setMainController(this);
        lecteurController.setMainController(this);

        definirEcouteursDEvenements();


        playListSelectionne = toutesLesChansons;
        sectionTableChansonsController.rafraichirListeChansons(playListSelectionne, pageCourante);
    }

    private void initTableChansonController() {
        sectionTableChansonsController.setPlaylistManager(this.playlistManager);
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
        sectionTableChansonsController.setMainController(this);
        sectionTableChansonsController.setPlaylistManager(playlistManager);
    }

    private void initplaylistsController() {
        playlistsController.setBibliotheque(biblio);
        playlistsController.setPlaylistManager(playlistManager);
        playlistsController.setToutesLesChansons(toutesLesChansons);
        playlistsController.rafraichirListePlaylist();
        playlistsController.setPlaylistManager(playlistManager);
        playlistsController.setMainController(this);
        playlistsController.setTableChansonsController(sectionTableChansonsController);
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

        btnAuditJournalier.setOnAction( e -> auditJournalierController.afficherAuditJournalier(btnAuditJournalier.getScene().getWindow()));
    }

    private void afficherAccueil() {
        rootPane.setLeft(accueilLeft);
        rootPane.setCenter(accueilCentre);
        rootPane.setRight(accueilRight);
    }

    public void creerBibliotheque() {
        try {
            // Mode BDD :
            Initialisation.peuplerChansonsSiVide("src/main/resources/data/spotifyData.csv", this.chansonDAO);
            SourceDonnees source = this.chansonDAO;

            // Mode CSV :
            // SourceDonnees source = new LecteurCSV("src/main/resources/data/spotifyData.csv");

            this.biblio = new Bibliotheque(source);
        } catch (Exception e) {
            afficherAlertErreur("Erreur lors de l'initialisation de la bibliothèque", e);
        }
    }

    public void creerPlaylistBilio() {
        try {
            toutesLesChansons = new Playlist("11111111-1111-1111-1111-111111111111", "Toutes les chansons", new ArrayList<>());
            playlistDao.ajouter(toutesLesChansons);
        } catch (SQLException e) {
            afficherAlertErreur("Erreur lors de la création de la playlist bibliothèque !", e);
        }
    }

    public void afficherAlertErreur(String titre, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText("Erreur !");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }
}