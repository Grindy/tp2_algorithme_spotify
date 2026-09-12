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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;
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
    private final Map<String, String> templateDataFiltreTri = new HashMap<>() {{
        put("filtreRecherche", "");
        put("filtreGenre", null);
        put("filtreDureeMax", "00:00");
        put("filtreNbEcoutes", "");
        put("critereTri", "---");
    }};
    private final Map<String, String> dataFiltreTri = new HashMap<>(templateDataFiltreTri);
    private static final String IMAGE_PAR_DEFAUT = "https://i.pinimg.com/736x/ba/8e/4d/ba8e4de740a641feb1709ce713889ea5.jpg";
    private final BooleanProperty playlistEstFiltreOuTrie = new SimpleBooleanProperty(false);
    private final BooleanProperty toutesLesChansonsEstSelectionne = new SimpleBooleanProperty(true);
    private String prochainIdPlaylist = "5";


    @FXML
    public void initialize() {
        creerBibliothequeEtPlaylists();
        formaterFieldDureeMax();
        configurerColonnesTables();
        rafraichirListePlaylist();
        definirEcouteursDEvenements();
        chargerChoixGenres();
        creerContextMenu();

        imgLecteurAlbum.setImage(new Image(IMAGE_PAR_DEFAUT));
        imgAlbum.setImage(new Image(IMAGE_PAR_DEFAUT));

        // Au démarrage, la liste d'accueil est sélectionnée (Votre Bibliothèque)
        playListSelectionne = toutesLesChansons;
        rafraichirListeChansons(playListSelectionne, pageCourante);
    }

    private Stage getFenetrePrincipale() {
        return (Stage) fieldRecherche.getScene().getWindow();
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
                    if (playlist != null && demanderConfirmationSuppressionPlaylist()) {
                        playlistManager.retirerPlaylist(playlist);
                        rafraichirListePlaylist();
                        rafraichirListeChansons(toutesLesChansons, 1);
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
        colTitre.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.30));
        colArtiste.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.25));
        colAlbum.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.25));
        colAnnee.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.1));
        colActions.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.1));
        colActions.setCellFactory(col -> new TableCell<Chanson, Void>() {

            // Ajout des boutons d'actions pour chaque chanson (play, ajouter, supprimer)
            private final Button btnLire = new Button("▶");
            private final Button btnAjouterAPlaylist = new Button("+");
            private final HBox conteneurBoutons = new HBox(8, btnLire, btnAjouterAPlaylist);

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

                btnAjouterAPlaylist.setOnAction(event -> {
                    Chanson chanson = getTableRow().getItem();
                    if (chanson != null) {
                       // Ouvrir page pour sélectionner playlist
                        ouvrirFenetreAjouterAPlaylist(chanson);
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

        // Écouteur sur la table des chansons pour afficher la chanson dans la carte à droite
        tableChansons.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                afficherChansonSelectionnee(newVal);
            }
        });

        // Écouteur sur les input de filtre pour rafraichir la liste des chansons en temps réel
        List<ObservableValue<?>> inputsFiltre = List.of(
                comboGenre.valueProperty(),
                fieldDureeMax.textProperty(),
                fieldNombreEcoutes.textProperty(),
                fieldRecherche.textProperty()
        );
        for (ObservableValue<?> property : inputsFiltre) {
            property.addListener((obs, oldValue, newValue) -> {
                rafraichirListeChansons(playListSelectionne, 1);
            });
        }

        for (MenuItem i : dropTri.getItems()) {
            i.setOnAction(e -> {
                dropTri.setText(i.getText());
                rafraichirListeChansons(playListSelectionne, 1);
            });
        }

        // Écouteur sur le bouton pour rénitialiser les filtres/tri
        btnResetFiltres.setOnAction(e -> {
            renitialiserFiltresEtTri();
            rafraichirListeChansons(playListSelectionne, 1);
        });
    };

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

        // Écouteur pour créer une playlist
        btnAjouterPlaylist.setOnAction(e -> {
            ouvrirFenetreCreerPlaylist();
            rafraichirListePlaylist();
        });
    }

    public void rafraichirListePlaylist() {
        // Afficher la liste des playlists dans la TableView à gauche
        tablePlaylists.setItems(FXCollections.observableArrayList(biblio.getPlaylists()));
    }

    public void rafraichirListeChansons(Playlist playlist, int page) {
        playListSelectionne = playlist;
        recupererFiltresEtTri();

        // Variable pour savoir si la playlist sélectionnée est filtrée/triée ou non
        playlistEstFiltreOuTrie.set(!(dataFiltreTri.equals(templateDataFiltreTri)) );

        // Si "toutes les chansons" sont sélectionné, on disable le bouton pour supprimer une chanson
        toutesLesChansonsEstSelectionne.set(playListSelectionne == toutesLesChansons);

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
        if (nbPagesTotales == 0) nbPagesTotales = 1;
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
        dropTri.setText("---");
        dataFiltreTri.clear();
    }

    public void chargerChoixGenres() {
        for (Genre g : Genre.values()) {
            comboGenre.getItems().add(g.getNomFormate());
        }
    }

    public void creerContextMenu() {
        tableChansons.setRowFactory(tv -> {
            TableRow<Chanson> row = new TableRow<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem monterChanson = new MenuItem("Monter d'une position");
            monterChanson.setOnAction(e -> {
                Chanson chanson = row.getItem();
                playListSelectionne.deplacerChanson(chanson, "up");
                rafraichirListeChansons(playListSelectionne, pageCourante);
            });

            MenuItem descendreChanson = new MenuItem("Descendre d'une position");
            descendreChanson.setOnAction(e -> {
                Chanson chanson = row.getItem();
                playListSelectionne.deplacerChanson(chanson, "down");
                rafraichirListeChansons(playListSelectionne, pageCourante);
            });

            // Désactiver les boutons de réordonnage si la liste est filtrée ou triée
            monterChanson.disableProperty().bind(playlistEstFiltreOuTrie);
            descendreChanson.disableProperty().bind(playlistEstFiltreOuTrie);


            MenuItem supprimerChanson = new MenuItem("Supprimer de la playlist");
            supprimerChanson.setOnAction(e -> {
                Chanson chanson = row.getItem();
                playListSelectionne.retirerChanson(chanson);
                rafraichirListeChansons(playListSelectionne, pageCourante);
            });

            // Désactiver le bouton de supression si on est dans la bibliothèque
            supprimerChanson.disableProperty().bind(toutesLesChansonsEstSelectionne);

            contextMenu.getItems().addAll(monterChanson, descendreChanson, supprimerChanson);

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

    public void ouvrirFenetreAjouterAPlaylist(Chanson chanson) {
        Stage popupStage = new Stage();

        // Définir le propriétaire et bloquer la fenêtre principale
        popupStage.initOwner(getFenetrePrincipale());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Détails");

        // Construire le contenu
        Label message = new Label("À quelle playlist voulez-vous ajouter cette chanson ?");
        ComboBox<Playlist> playlists = new ComboBox<>();
        playlists.getItems().addAll(biblio.getPlaylists());
        playlists.setPromptText("Sélectionnez une playlist");
        Button btnAjouter = new Button("Ajouter");
        playlists.setMaxWidth(Double.MAX_VALUE);
        btnAjouter.setMaxWidth(Double.MAX_VALUE);
        HBox hbox = new HBox(8,playlists, btnAjouter);
        HBox.setHgrow(playlists, Priority.ALWAYS);

        btnAjouter.setOnAction(e -> {
            Playlist playlistSelectionne = playlists.getSelectionModel().getSelectedItem();
            playlistSelectionne.ajouterChanson(chanson);
            popupStage.close();
        });

        VBox layout = new VBox(15, message, hbox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(8));

        // Afficher la fenêtre avec des dimensions fixes
        Scene scene = new Scene(layout, 300, 200);
        popupStage.setScene(scene);
        popupStage.setResizable(false);

        popupStage.showAndWait();
    }

    public void ouvrirFenetreCreerPlaylist() {
        Stage popupStage = new Stage();

        // Définir le propriétaire et bloquer la fenêtre principale
        popupStage.initOwner(getFenetrePrincipale());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Créer une nouvelle playlist");

        // Construire le contenu
        Label message = new Label("Entrez un nom pour votre playlist");
        TextField nomPlaylist = new TextField();
        Button btnAjouter = new Button("Créer");
        nomPlaylist.setMaxWidth(Double.MAX_VALUE);
        btnAjouter.setMaxWidth(Double.MAX_VALUE);
        HBox hbox = new HBox(8,nomPlaylist, btnAjouter);
        HBox.setHgrow(nomPlaylist, Priority.ALWAYS);

        btnAjouter.setOnAction(e -> {
              playlistManager.ajouterPlaylist(new Playlist(prochainIdPlaylist, nomPlaylist.getText(), new ArrayList<Chanson>()));
            popupStage.close();
        });

        VBox layout = new VBox(15, message, hbox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(8));

        // Afficher la fenêtre avec des dimensions fixes
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

        // Attache la popup à la fenêtre principale
        alert.initOwner(getFenetrePrincipale());

        // showAndWait() bloque jusqu'au clic de l'utilisateur
        Optional<ButtonType> resultat = alert.showAndWait();

        return (resultat.isPresent() && resultat.get() == ButtonType.OK);
    }
}
