package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Genre;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.model.TriMap;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistManager;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistService;
import com.maisonneuve.tp2_algorithme_spotify.service.LecteurService;
import com.maisonneuve.tp2_algorithme_spotify.utils.TimeUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class MainController {

    // Top
    @FXML
    private Button btnAccueil;
    @FXML
    private TextField fieldRecherche;
    @FXML
    private Button btnGraph;

    // Bottom (Lecteur)
    @FXML
    private ImageView imgLecteurAlbum;
    @FXML
    private Label labelLecteurTitre;
    @FXML
    private Label labelLecteurArtiste;
    @FXML
    private Button btnLecteurPrecedente;
    @FXML
    private Button btnLecteurJouer;
    @FXML
    private Button btnLecteurSuivante;
    @FXML
    private Label labelTempsActuel;
    @FXML
    private Slider sliderTemps;
    @FXML
    private Label labelTempsTotal;

    // Left
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

    // Right (Détails)
    @FXML
    private ImageView imgAlbum;
    @FXML
    private Label labelTitre;
    @FXML
    private Label labelAlbum;
    @FXML
    private Label labelArtiste;
    @FXML
    private Label labelAnnee;
    @FXML
    private Label labelGenre;
    @FXML
    private Label labelMaisonDisques;
    @FXML
    private Label labelDuree;
    @FXML
    private Label labelDansabilite;
    @FXML
    private Label labelNbEcoutes;
    @FXML
    private Button btnResetFiltres;
    @FXML
    private Button btnAppliquerFiltres;

    // Center (Filtres, Liste, Pagination)
    @FXML
    private ComboBox<String> comboGenre;
    @FXML
    private TextField fieldDureeMax;
    @FXML
    private TextField fieldNombreEcoutes;
    @FXML
    private MenuButton dropTri;
    @FXML
    private TableView<Chanson> tableChansons;
    @FXML
    private TableColumn<Chanson, String> colTitre;
    @FXML
    private TableColumn<Chanson, String> colArtiste;
    @FXML
    private TableColumn<Chanson, String> colAlbum;
    @FXML
    private TableColumn<Chanson, Integer> colAnnee;
    @FXML
    private TableColumn<Chanson, Void> colActions;
    @FXML
    private Button btnPagePrecedente;
    @FXML
    private Label labelPages;
    @FXML
    private Button btnPageSuivante;

    private Bibliotheque biblio;
    private Playlist toutesLesChansons;
    private PlaylistManager playlistManager;
    private PlaylistService playlistService;
    private LecteurService lecteurService;
    private int NB_CHANSONS_PAR_PAGE = 10;
    private int nbPagesTotales;
    private int pageCourante = 1;
    private Playlist playListSelectionne;
    private Map<String, String> dataFiltreTri = new HashMap<>() {
        {
            put("filtreRecherche", "");
            put("filtreGenre", "");
            put("filtreDureeMax", "");
            put("filtreNbEcoutes", "");
            put("critereTri", "");
        }
    };
    private static final String IMAGE_PAR_DEFAUT = "https://i.pinimg.com/736x/ba/8e/4d/ba8e4de740a641feb1709ce713889ea5.jpg";

    @FXML
    public void initialize() {
        creerBibliothequeEtPlaylists();
        formaterFieldDureeMax();
        configurerColonnesTables();
        rafraichirListePlaylist();
        definirEcouteursDEvenements();
        chargerChoixGenres();

        imgLecteurAlbum.setImage(new Image(IMAGE_PAR_DEFAUT));
        imgAlbum.setImage(new Image(IMAGE_PAR_DEFAUT));

        // Au démarrage, la liste d'accueil est sélectionnée (Votre Bibliothèque)
        playListSelectionne = toutesLesChansons;
        rafraichirListeChansons(playListSelectionne, pageCourante);
    }



    // Fonction générée par Gemini
    private void formaterFieldDureeMax() {
        // Regex pour le format mm:ss
        Pattern pattern = Pattern.compile("^$|^[0-9]{1,2}$|^[0-9]{1,2}:$|^[0-9]{1,2}:[0-5]$|^[0-9]{1,2}:[0-5][0-9]$");

        UnaryOperator<TextFormatter.Change> filter = change -> {
            // Autorise la réinitialisation directe via setText("00:00") ou toute valeur valide complète
            if (change.getText().matches("^[0-9]{2}:[0-5][0-9]$")) {
                return change;
            }
            // Récupère le texte actuel ou "00:00" s'il est vide/incomplet
            String currentText = change.getControlText();
            if (currentText.length() != 5) {
                currentText = "00:00";
            }

            // 1. Touche Backspace / Delete
            if (change.getText().isEmpty()) {
                int start = change.getRangeStart();
                int end = change.getRangeEnd();

                if (start != end) {
                    char[] chars = currentText.toCharArray();
                    for (int i = start; i < end; i++) {
                        if (i != 2) {
                            chars[i] = '0';
                        }
                    }
                    change.setRange(0, change.getControlText().length());
                    change.setText(new String(chars));
                    change.setCaretPosition(start);
                    change.setAnchor(start);
                    return change;
                }
                return change;
            }

            // 2. Frappe d'un chiffre
            if (change.getText().matches("[0-9]")) {
                int pos = change.getRangeStart();

                // Si le curseur est sur le ':', on passe directement au chiffre des secondes
                if (pos == 2) {
                    pos = 3;
                }

                // Bloque si le curseur est au-delà du 5e caractère
                if (pos >= 5) {
                    return null;
                }

                char digit = change.getText().charAt(0);

                // Validation des dizaines de secondes (index 3 : max 59 secondes)
                if (pos == 3 && digit > '5') {
                    return null;
                }

                char[] chars = currentText.toCharArray();
                chars[pos] = digit;

                int nextCaret = (pos + 1 == 2) ? 3 : pos + 1;

                change.setRange(0, change.getControlText().length());
                change.setText(new String(chars));
                change.setCaretPosition(nextCaret);
                change.setAnchor(nextCaret);
                return change;
            }

            // Rejette toute autre touche non numérique
            return null;
        };

// Initialise le TextFormatter avec "00:00" par défaut
        fieldDureeMax.setTextFormatter(new TextFormatter<>(filter));
        fieldDureeMax.setText("00:00");
    }

    private void configurerColonnesTables() {

        // Lier les colonnes de la liste des playlists
        colPlaylists.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colSupprimerPlaylist.setCellFactory(col -> new TableCell<Playlist, Void>() {
            // Ajouter un bouton "supprimerPlaylist" à chaque playlist
            private final Button btnSupprimerPlaylist = new Button("🗑");

            {
                btnSupprimerPlaylist.setOnAction(event -> {
                    Playlist playlist = getTableRow().getItem();
                    if (playlist != null) {
                        // Logique de suppression
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

        // Définir les proportions des colonnes de la liste des chansons
        colPlaylists.prefWidthProperty().bind(tablePlaylists.widthProperty().subtract(4).multiply(0.90));
        colSupprimerPlaylist.prefWidthProperty().bind(tablePlaylists.widthProperty().subtract(4).multiply(0.1));


        // Lier les colonnes de la liste des chansons
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colArtiste.setCellValueFactory(new PropertyValueFactory<>("artiste"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneeSortie"));

        // Définir les proportions des colonnes de la liste des chansons
        colTitre.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.25));
        colArtiste.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.25));
        colAlbum.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.25));
        colAnnee.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.1));
        colActions.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.15));
        colActions.setCellFactory(col -> new TableCell<Chanson, Void>() {

            // Ajout des boutons d'actions pour chaque chanson (play, ajouter, supprimer)
            private final Button btnLire = new Button("▶");
            private final Button btnAjouterAPlaylist = new Button("+");
            private final Button btnSupprimer = new Button("🗑");
            private final HBox conteneurBoutons = new HBox(8, btnLire, btnAjouterAPlaylist, btnSupprimer);

            {
                conteneurBoutons.setAlignment(Pos.CENTER);

                // Actions des boutons
                btnLire.setOnAction(event -> {
                    Chanson chanson = getTableRow().getItem();
                    if (chanson != null) {
                        Playlist contexte = tablePlaylists.getSelectionModel().getSelectedItem() != null
                                ? tablePlaylists.getSelectionModel().getSelectedItem()
                                : toutesLesChansons;
                        lecteurService.demarrerLecture(chanson, contexte);

                        // implementer la mise a jour des labels et slider
                    }
                });

                btnSupprimer.setOnAction(event -> {
                    Chanson chanson = getTableRow().getItem();
                    if (chanson != null) {
                        // Logique de suppression
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    setGraphic(conteneurBoutons);
                }
            }
        });

        // Empêcher les comportements par défaut des TableViews (tri natif, déplacer les colonnes)
        for (TableColumn<?, ?> col : tableChansons.getColumns()) {
            col.setSortable(false);
            col.setReorderable(false);
        }
        for (TableColumn<?, ?> col : tablePlaylists.getColumns()) {
            col.setSortable(false);
            col.setReorderable(false);
        }
    }


    public void definirEcouteursDEvenements() {

        // Écouteur sur la sélection de la playlist
        tablePlaylists.getSelectionModel().selectedItemProperty().addListener((obs, anciennePlaylist, nouvellePlaylist) -> {
            if (nouvellePlaylist != null) {
                rafraichirListeChansons(nouvellePlaylist, 1);
            }
        });

        // Écouteur sur le bouton "Votre Bibliothèqeue"
        btnVotreBibliotheque.setOnAction(e -> {
            tablePlaylists.getSelectionModel().clearSelection();
            rafraichirListeChansons(toutesLesChansons, 1);
        });

        // Écouteur sur le bouton page précédente
        btnPagePrecedente.setOnAction(e -> {
            if (estPageValide(pageCourante - 1)) {
                rafraichirListeChansons(playListSelectionne, pageCourante - 1);
            }
        });

        // Écouteur sur le bouton page suivante
        btnPageSuivante.setOnAction(e -> {
            if (estPageValide(pageCourante + 1)) {
                rafraichirListeChansons(playListSelectionne, pageCourante + 1);
            }
        });

        tableChansons.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                afficherChansonSelectionnee(newVal);
            }
        });

        // Écouteur sur le dropdown de tri
        for (MenuItem i : dropTri.getItems()) {
            i.setOnAction(e -> {
                dropTri.setText(i.getText());
            });
        }

        // Écouteur sur le bouton pour appliques les filtres/tri
        btnAppliquerFiltres.setOnAction(e -> {
            rafraichirListeChansons(playListSelectionne, 1);
        });

        // Écouteur sur le bouton pour rénitialiser les filtres/tri
        btnResetFiltres.setOnAction(e -> {
            renitialiserFiltresEtTri();
            rafraichirListeChansons(playListSelectionne, 1);
        });

    }

    public void creerBibliothequeEtPlaylists() {
        // Créer la bibliothèque et créer une playlist contenant toutes les chansons
        biblio = new Bibliotheque("src/main/resources/data/spotifyData-v2.csv");
        toutesLesChansons = new Playlist("1", "Toutes les chansons", biblio.getChansons());

        // Créer 3 playlist de 10 chansons (les 30 premières chansons du CSV)
        List<Chanson> chansons = biblio.getChansons();
        Playlist playlist1 = new Playlist("2", "Playlist 1", chansons.subList(0, 10));
        Playlist playlist2 = new Playlist("3", "Playlist 2", chansons.subList(10, 20));
        Playlist playlist3 = new Playlist("4", "Playlist 3", chansons.subList(20, 30));

        // Ajouter les playlists à la bibliothèque
        playlistManager = new PlaylistManager(biblio);
        for (Playlist p : List.of(playlist1, playlist2, playlist3)) {
            playlistManager.ajouterPlaylist(p);
        }

        // Créer un playlist service pour les filtres et tris
        playlistService = new PlaylistService();

        // Créer le service de lecture
        lecteurService = new LecteurService();

        lecteurService.setOnChansonChangee(chanson -> {
            String imageUrl = (chanson.getImageUrl() != null && !chanson.getImageUrl().isBlank())
                    ? chanson.getImageUrl()
                    : IMAGE_PAR_DEFAUT;
            labelLecteurTitre.setText(chanson.getTitre());
            labelLecteurArtiste.setText(chanson.getArtiste());
            imgLecteurAlbum.setImage(new Image(imageUrl));

            //la fonctionnalité du slider utilise des ms, l'affichage les min:sec
            labelTempsTotal.setText(TimeUtils.msToMinutes(chanson.getDuree()));
            sliderTemps.setMax(chanson.getDuree());
            sliderTemps.setValue(0);
            labelTempsActuel.setText("0:00");
        });

        lecteurService.setOnTick(tempsMs -> {
            sliderTemps.setValue(tempsMs);
            labelTempsActuel.setText(TimeUtils.msToMinutes(tempsMs));
        });
    }

    public void rafraichirListePlaylist() {
        // Afficher la liste des playlists dans la TableView à gauche
        tablePlaylists.setItems(FXCollections.observableArrayList(biblio.getPlaylists()));
    }

    public void rafraichirListeChansons(Playlist playlist, int page) {
        playListSelectionne = playlist;
        recupererFiltresEtTri();

        Playlist playlistTrie = playlistService.filtrer(playlist,
                dataFiltreTri.get("filtreRecherche"),
                dataFiltreTri.get("filtreGenre"),
                dataFiltreTri.get("filtreDureeMax"),
                dataFiltreTri.get("filtreNbEcoutes")
        );

        Playlist playlistTrieEtFiltre = playlistService.trierSelon(dataFiltreTri.get("critereTri"), playlistTrie);
        List<Chanson> chansons = playlistTrieEtFiltre.getChansons();

        pageCourante = page;
        nbPagesTotales = (int) Math.ceil((double) chansons.size() / NB_CHANSONS_PAR_PAGE);
        labelPages.setText(pageCourante + " / " + nbPagesTotales);

        int indexDebut = (page - 1) * NB_CHANSONS_PAR_PAGE;
        int indexFin = Math.min(indexDebut + NB_CHANSONS_PAR_PAGE, chansons.size());

        if (indexDebut > indexFin) {
            indexDebut = indexFin;
        }

        tableChansons.setItems(FXCollections.observableArrayList(chansons.subList(indexDebut, indexFin)));
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

    public void recupererFiltresEtTri() {
        dataFiltreTri.put("filtreRecherche", fieldRecherche.getText());
        dataFiltreTri.put("filtreGenre", comboGenre.getValue());
        dataFiltreTri.put("filtreDureeMax", fieldDureeMax.getText());
        dataFiltreTri.put("filtreNbEcoutes", fieldNombreEcoutes.getText());
        dataFiltreTri.put("critereTri", dropTri.getText());
    }

    public void renitialiserFiltresEtTri() {
        fieldRecherche.setText("");
        comboGenre.getSelectionModel().clearSelection();
        fieldDureeMax.setText("00:00");
        fieldNombreEcoutes.setText("");
        dropTri.setText("");

        dataFiltreTri.clear();
    }

    public void chargerChoixGenres() {
        for (Genre g : Genre.values()) {
            comboGenre.getItems().add(g.getNomFormate());
        }
    }
}
