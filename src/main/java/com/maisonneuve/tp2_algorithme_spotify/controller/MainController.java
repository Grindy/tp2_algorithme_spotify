package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistManager;
import com.maisonneuve.tp2_algorithme_spotify.utils.TimeUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class MainController {

    // Top
    @FXML private Button btnAccueil;
    @FXML private TextField fieldRecherche;
    @FXML private Button btnGraph;

    // Bottom (Lecteur)
    @FXML private ImageView imgLecteurAlbum;
    @FXML private Label labelLecteurTitre;
    @FXML private Label labelLecteurArtiste;
    @FXML private Button btnLecteurPrecedente;
    @FXML private Button btnLecteurJouer;
    @FXML private Button btnLecteurSuivante;
    @FXML private Label labelTempsActuel;
    @FXML private Slider sliderTemps;
    @FXML private Label labelTempsTotal;

    // Left
    @FXML private Button btnVotreBibliotheque;
    @FXML private Button btnAjouterPlaylist;
    @FXML private ListView<Playlist> listPlaylists;

    // Right (Détails)
    @FXML private ImageView imgAlbum;
    @FXML private Label labelTitre;
    @FXML private Label labelAlbum;
    @FXML private Label labelArtiste;
    @FXML private Label labelAnnee;
    @FXML private Label labelGenre;
    @FXML private Label labelMaisonDisques;
    @FXML private Label labelDuree;
    @FXML private Label labelDansabilite;
    @FXML private Label labelNbEcoutes;

    // Center (Filtres, Liste, Pagination)
    @FXML private ComboBox<?> comboGenre;
    @FXML private TextField fieldDureeMax;
    @FXML private TextField fieldNombreEcoutes;
    @FXML private MenuButton dropTri;
    @FXML private ListView<Chanson> listChansons;
    @FXML private Button btnPagePrecedente;
    @FXML private Label labelPages;
    @FXML private Button btnPageSuivante;

    private Bibliotheque biblio;
    private PlaylistManager playlistManager;
    private int NB_CHANSONS_PAR_PAGE = 10;
    private int nbPagesTotales;
    private int pageCourante = 1;
    private List<Chanson> listeChansonsCourante;


    @FXML
    public void initialize() {
        creerBibliothequeEtPlaylists();
        rafraichirListePlaylist();
        definirEcouteursDEvenements();

        // Au démarrage, la liste d'accueil est sélectionnée (Votre Bibliothèque)
        listeChansonsCourante = biblio.getChansons();
        rafraichirListeChansons(listeChansonsCourante, pageCourante);
    }

    public void definirEcouteursDEvenements() {

        // Écouteur sur la sélection de la playlist
        listPlaylists.getSelectionModel().selectedItemProperty().addListener((obs, anciennePlaylist, nouvellePlaylist) -> {
            if (nouvellePlaylist != null) {
                pageCourante = 1;
                rafraichirListeChansons(nouvellePlaylist.getChansons(), pageCourante);
            }
        });

        // Écouteur sur le bouton "Votre Bibliothèqeue"
        btnVotreBibliotheque.setOnAction(e -> {
            listPlaylists.getSelectionModel().clearSelection();
            rafraichirListeChansons(biblio.getChansons(), 1);
            pageCourante = 1;
        });

        // Écouteur sur le bouton page précédente
        btnPagePrecedente.setOnAction(e -> {
            if (estPageValide(pageCourante - 1)) {
                pageCourante -= 1;
                rafraichirListeChansons(biblio.getChansons(), pageCourante);
            }
        });

        // Écouteur sur le bouton page suivante
        btnPageSuivante.setOnAction(e -> {
            if (estPageValide(pageCourante + 1)) {
                pageCourante += 1;
                rafraichirListeChansons(biblio.getChansons(), pageCourante);
            }
        });

        listChansons.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                afficherChansonSelectionnee(newVal);
            }
        });


    }

    public void creerBibliothequeEtPlaylists() {
        // Créer la bibliothèque
        biblio = new Bibliotheque("src/main/resources/data/spotifyData-v2.csv");

        // Créer 3 playlist de 10 chansons (les 30 premières chansons du CSV)
        List<Chanson> chansons = biblio.getChansons();
        Playlist playlist1 = new Playlist("1", "Playlist 1", chansons.subList(0, 10));
        Playlist playlist2 = new Playlist("2", "Playlist 2", chansons.subList(10, 20));
        Playlist playlist3 = new Playlist("3", "Playlist 3", chansons.subList(20, 30));

        // Ajouter les playlists à la bibliothèque
        playlistManager = new PlaylistManager(biblio);
        for (Playlist p : List.of(playlist1, playlist2, playlist3)) {
            playlistManager.ajouterPlaylist(p);
        }
    }

    public void rafraichirListePlaylist() {
        // Afficher la liste des playlists dans la ListView à gauche
        listPlaylists.setItems(FXCollections.observableArrayList(biblio.getPlaylists()));
    }

    public void rafraichirListeChansons(List<Chanson> chansons, int page) {
        listeChansonsCourante = chansons;
        nbPagesTotales = (int) Math.ceil((double) chansons.size() / NB_CHANSONS_PAR_PAGE);
        labelPages.setText(pageCourante + " / " + nbPagesTotales);
        int indexFin = Math.min(page * NB_CHANSONS_PAR_PAGE, chansons.size());
        int indexDebut = page * NB_CHANSONS_PAR_PAGE - 10;

        listChansons.setItems(FXCollections.observableArrayList(chansons.subList(indexDebut, indexFin)));
    }

    public void afficherChansonSelectionnee(Chanson chanson){
        labelTitre.setText(chanson.getTitre());
        labelAlbum.setText(chanson.getAlbum());
        labelArtiste.setText(chanson.getArtiste());
        imgAlbum.setImage(new Image (chanson.getImageUrl()));
        labelGenre.setText(chanson.getGenre());
        labelAnnee.setText(Integer.toString(chanson.getAnneeSortie()));
        labelMaisonDisques.setText(chanson.getLabel());
        labelDuree.setText(TimeUtils.msToMinutes(chanson.getDuree()));
        labelDansabilite.setText(Float.toString(chanson.getDansabilitee()));
        labelNbEcoutes.setText(Integer.toString(chanson.getNbrEcoute()));
    }

    public boolean estPageValide(int page) {
        return page >= 1 && page <= nbPagesTotales;
    }

}
