package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Genre;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;
import com.maisonneuve.tp2_algorithme_spotify.service.LecteurService;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistManager;
import com.maisonneuve.tp2_algorithme_spotify.service.PlaylistService;
import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class TableChansonsController {
    @FXML
    public Button btnResetFiltres;
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
    private ComboBox<Integer> comboNbPages;
    @FXML
    private Button btnPageSuivante;

    private BooleanProperty toutesLesChansonsEstSelectionne;
    private Playlist playListSelectionne;
    private PlaylistsController playlistsController;
    private final BooleanProperty playlistEstFiltreOuTrie = new SimpleBooleanProperty(false);
    private final Map<String, String> templateDataFiltreTri = new HashMap<>() {{
        put("filtreRecherche", "");
        put("filtreGenre", null);
        put("filtreDureeMax", "00:00");
        put("filtreNbEcoutes", "");
        put("critereTri", "---");
    }};
    private final Map<String, String> dataFiltreTri = new HashMap<>(templateDataFiltreTri);
    private int nbChansonsParPage = 25;
    private int nbPagesTotales;
    private int pageCourante;
    private TextField fieldRecherche;
    private TableView<Playlist> tablePlaylists;
    private Playlist toutesLesChansons;
    private ChansonController chansonController;
    private MainController mainController;
    private PlaylistService playlistService;
    private Bibliotheque biblio;
    private PlaylistManager playlistManager;

    public void setPlaylistManager(PlaylistManager playlistManager) {
        this.playlistManager = playlistManager;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setPageCourante(int pageCourante) {
        this.pageCourante = pageCourante;
    }

    public void setBiblio(Bibliotheque biblio) {
        this.biblio = biblio;
    }

    public void setPlaylistService(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    public void setChansonController(ChansonController chansonController) {
        this.chansonController = chansonController;
    }

    public void setToutesLesChansons(Playlist toutesLesChansons) {
        this.toutesLesChansons = toutesLesChansons;
    }

    public void setTablePlaylists(TableView<Playlist> tablePlaylists) {
        this.tablePlaylists = tablePlaylists;
    }

    public void setToutesLesChansonsEstSelectionne(BooleanProperty toutesLesChansonsEstSelectionne) {
        this.toutesLesChansonsEstSelectionne = toutesLesChansonsEstSelectionne;
    }

    public void setPlayListSelectionne(Playlist playListSelectionne) {
        this.playListSelectionne = playListSelectionne;
    }

    public void setplaylistsController(PlaylistsController playlistsController) {
        this.playlistsController = playlistsController;
    }

    public void setFieldRecherche(TextField fieldRecherche) {
        this.fieldRecherche = fieldRecherche;
        this.fieldRecherche.textProperty().addListener((obs, oldValue, newValue) -> {
            rafraichirListeChansons(playListSelectionne, 1);
        });
    }

    @FXML
    public void initialize() {
        initComboPages();
        formaterFieldDureeMax();
        configurerColonnesTable();
        definirEcouteursDEvenements();
        chargerChoixGenres();
        creerContextMenu();
    }

    private void definirEcouteursDEvenements() {
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
                chansonController.afficherChansonSelectionnee(newVal);
            }
        });

        // Écouteur sur les input de filtre pour rafraichir la liste des chansons en temps réel
        List<ObservableValue<?>> inputsFiltre = List.of(comboGenre.valueProperty(), fieldDureeMax.textProperty(), fieldNombreEcoutes.textProperty());
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

        // Écouteur sur le combo nombre de pages
        comboNbPages.setOnAction(e -> {
            nbChansonsParPage = comboNbPages.getValue();
            configurerColonnesTable();
            rafraichirListeChansons(playListSelectionne, 1);
        });

        // Mettre le curseur au début du TextField pour le temps max
        fieldDureeMax.focusedProperty().addListener((obs, oldVal, hasFocus) -> {
            if (hasFocus) {
                Platform.runLater(() -> fieldDureeMax.positionCaret(0));
            }
        });
    }

    private void configurerColonnesTable() {
        // Lier les colonnes de la liste des chansons
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colArtiste.setCellValueFactory(new PropertyValueFactory<>("artiste"));
        colAlbum.setCellValueFactory(new PropertyValueFactory<>("album"));
        colAnnee.setCellValueFactory(new PropertyValueFactory<>("anneeSortie"));

        // Définir les proportions des colonnes de la liste des chansons
        colTitre.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.25));
        colArtiste.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.25));
        colAlbum.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.25));
        colAnnee.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.075));
        colActions.prefWidthProperty().bind(tableChansons.widthProperty().subtract(4).multiply(0.175));

        colActions.setCellFactory(col -> new TableCell<Chanson, Void>() {

            private final Button btnLire = new Button("▶");
            private final Button btnAjouterAPlaylist = new Button("+");
            private final Button btnSupprimer = new Button("X");
            private final HBox conteneurBoutons = new HBox(8, btnLire, btnAjouterAPlaylist);

            {
                if (!toutesLesChansonsEstSelectionne.getValue()) conteneurBoutons.getChildren().add(btnSupprimer);

                conteneurBoutons.setAlignment(Pos.CENTER);

                btnAjouterAPlaylist.getStyleClass().add("btn-table-view");
                btnLire.getStyleClass().add("btn-table-view");
                btnSupprimer.getStyleClass().add("btn-table-view");

                btnLire.setOnAction(event -> {
                    Chanson chanson = recupererChansonCourante();
                    if (chanson != null) {
                        try {
                            Playlist contexte = (tablePlaylists != null && tablePlaylists.getSelectionModel().getSelectedItem() != null) ? tablePlaylists.getSelectionModel().getSelectedItem() : toutesLesChansons;
                            LecteurService.getInstance().demarrerLecture(chanson, contexte);
                        } catch (Exception e) {
                            mainController.afficherAlertErreur("Erreur lors du lancement de Spotify", e);
                        }
                    }
                });

                btnAjouterAPlaylist.setOnAction(event -> {
                    Chanson chanson = recupererChansonCourante();
                    if (chanson != null) {
                        ouvrirFenetreAjouterAPlaylist(chanson);
                    }
                });

                btnSupprimer.setOnAction(event -> {
                    Chanson chanson = recupererChansonCourante();
                    if (chanson != null && toutesLesChansonsEstSelectionne != null && !toutesLesChansonsEstSelectionne.get()) {
                        new Thread(() -> {
                            try {
                                playlistManager.retirerChanson(playListSelectionne, chanson);
                                Platform.runLater(() -> {
                                    if (playlistsController != null) {
                                        playlistsController.rafraichirListePlaylist();
                                    }
                                    rafraichirListeChansons(playListSelectionne, pageCourante);
                                });
                            } catch (Exception e) {
                                Platform.runLater(() -> mainController.afficherAlertErreur("Erreur lors de la suppression de la chanson !", e));
                            }
                        }).start();
                    }
                });
            }

            private Chanson recupererChansonCourante() {
                int index = getIndex();
                if (index >= 0 && index < getTableView().getItems().size()) {
                    return getTableView().getItems().get(index);
                }
                return null;
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    setGraphic(conteneurBoutons);
                }
            }
        });

        // Empêcher les comportements par défaut des TableViews
        for (TableColumn<?, ?> col : tableChansons.getColumns()) {
            col.setSortable(false);
            col.setReorderable(false);
        }
    }

    private void initComboPages() {
        comboNbPages.getItems().addAll(10, 25, 50, 100);
        comboNbPages.setValue(25);
    }

    // Fonction générée par Gemini
    private void formaterFieldDureeMax() {

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

    public void rafraichirListeChansons(Playlist playlist, int page) {
        playListSelectionne = playlist;
        recupererFiltresEtTri();

        // Variable pour savoir si la playlist sélectionnée est filtrée/triée ou non
        playlistEstFiltreOuTrie.set(!(dataFiltreTri.equals(templateDataFiltreTri)));

        // Si "toutes les chansons" sont sélectionné, on disable le bouton pour supprimer une chanson
        toutesLesChansonsEstSelectionne.set(playListSelectionne == toutesLesChansons);

        Playlist playlistTrie = playlistService.filtrer(playlist, dataFiltreTri.get("filtreRecherche"), dataFiltreTri.get("filtreGenre"), dataFiltreTri.get("filtreDureeMax"), dataFiltreTri.get("filtreNbEcoutes"));

        Playlist playlistTrieEtFiltre = playlistService.trierSelon(dataFiltreTri.get("critereTri"), playlistTrie);
        List<Chanson> chansons = playlistTrieEtFiltre.getChansons();

        pageCourante = page;
        nbPagesTotales = (int) Math.ceil((double) chansons.size() / nbChansonsParPage);
        if (nbPagesTotales == 0) nbPagesTotales = 1;
        labelPages.setText(pageCourante + " / " + nbPagesTotales);

        int indexDebut = (page - 1) * nbChansonsParPage;
        int indexFin = Math.min(indexDebut + nbChansonsParPage, chansons.size());

        if (indexDebut > indexFin) {
            indexDebut = indexFin;
        }

        tableChansons.setItems(FXCollections.observableArrayList(chansons.subList(indexDebut, indexFin)));
        configurerColonnesTable();
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
                if (chanson != null) {
                    new Thread(() -> {
                        try {
                            playlistManager.deplacerChanson(playListSelectionne, chanson, "up");
                            Platform.runLater(() -> rafraichirListeChansons(playListSelectionne, pageCourante));
                        } catch (SQLException ex) {
                            Platform.runLater(() -> mainController.afficherAlertErreur("Erreur lors du déplacement", ex));
                        }
                    }).start();
                }
            });

            MenuItem descendreChanson = new MenuItem("Descendre d'une position");
            descendreChanson.setOnAction(e -> {
                Chanson chanson = row.getItem();
                if (chanson != null) {
                    new Thread(() -> {
                        try {
                            playlistManager.deplacerChanson(playListSelectionne, chanson, "down");
                            Platform.runLater(() -> rafraichirListeChansons(playListSelectionne, pageCourante));
                        } catch (SQLException ex) {
                            Platform.runLater(() -> mainController.afficherAlertErreur("Erreur lors du déplacement", ex));
                        }
                    }).start();
                }
            });

            // Désactiver les boutons de réordonnage si la liste est filtrée ou triée
            monterChanson.disableProperty().bind(playlistEstFiltreOuTrie);
            descendreChanson.disableProperty().bind(playlistEstFiltreOuTrie);

            MenuItem viderPlaylist = new MenuItem("Vider la playlist");
            viderPlaylist.setOnAction(e -> {
                new Thread(() -> {
                    try {
                        playlistManager.viderPlaylist(playListSelectionne);
                        Platform.runLater(() -> {
                            playlistsController.rafraichirListePlaylist();
                            rafraichirListeChansons(playListSelectionne, pageCourante);
                        });
                    } catch (SQLException ex) {
                        Platform.runLater(() -> mainController.afficherAlertErreur("Erreur lors du videment de la playlist !", ex));
                    }
                }).start();
            });

            MenuItem supprimerChanson = new MenuItem("Supprimer de la playlist");
            supprimerChanson.setOnAction(e -> {
                Chanson chanson = row.getItem();
                if (chanson != null) {
                    new Thread(() -> {
                        try {
                            playlistManager.retirerChanson(playListSelectionne, chanson);
                            Platform.runLater(() -> {
                                if (playlistsController != null) {
                                    playlistsController.rafraichirListePlaylist();
                                }
                                rafraichirListeChansons(playListSelectionne, pageCourante);
                            });
                        } catch (SQLException ex) {
                            Platform.runLater(() -> mainController.afficherAlertErreur("Erreur lors de la suppression de la chanson", ex));
                        }
                    }).start();
                }
            });

            // Désactiver le bouton de supression et vider la playlist si on est dans la bibliothèque
            supprimerChanson.disableProperty().bind(toutesLesChansonsEstSelectionne);
            viderPlaylist.disableProperty().bind(toutesLesChansonsEstSelectionne);

            contextMenu.getItems().addAll(monterChanson, descendreChanson, viderPlaylist, supprimerChanson);

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

    public boolean estPageValide(int page) {
        return page >= 1 && page <= nbPagesTotales;
    }

    public void ouvrirFenetreAjouterAPlaylist(Chanson chanson) {
        Stage popupStage = new Stage();

        // Définir le propriétaire et bloquer la fenêtre principale
        popupStage.initOwner(tableChansons.getScene().getWindow());
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.setTitle("Détails");

        // Construire le contenu
        Label message = new Label("À quelle playlist voulez-vous ajouter cette chanson ?");
        ComboBox<Playlist> playlists = new ComboBox<>();
        playlists.getItems().addAll(playlistManager.getPlaylists());
        playlists.setPromptText("Sélectionnez une playlist");
        Button btnAjouter = new Button("Ajouter");
        playlists.setMaxWidth(Double.MAX_VALUE);
        btnAjouter.setMaxWidth(Double.MAX_VALUE);
        HBox hbox = new HBox(8, playlists, btnAjouter);
        HBox.setHgrow(playlists, Priority.ALWAYS);

        btnAjouter.setOnAction(e -> {
            Playlist playlistSelectionne = playlists.getSelectionModel().getSelectedItem();

            if (playlistSelectionne == null) {
                mainController.afficherAlertErreur("Sélection requise", new Exception("Veuillez sélectionner une playlist."));
                return;
            }
            if (playlistSelectionne.getChansons().contains(chanson)) {
                mainController.afficherAlertErreur("Cette chanson figure déjà dans cette playlist !", new Exception("Une chanson ne peut pas figurer plusieurs fois dans la même playlist."));
                return;
            }

            new Thread(() -> {
                try {
                    playlistManager.ajouterChanson(playlistSelectionne, chanson);
                    Platform.runLater(() -> {
                        playlistsController.rafraichirListePlaylist();
                        popupStage.close();
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> mainController.afficherAlertErreur("Erreur lors de l'ajout de la chanson à la playlist !", ex));
                }
            }).start();
        });

        VBox layout = new VBox(15, message, hbox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(8));

        // Afficher la fenêtre avec des dimensions fixes
        Scene scene = new Scene(layout, 300, 120);
        scene.getStylesheets().add(getClass().getResource("/vues/style.css").toExternalForm());
        layout.getStyleClass().add("nouv-playlist");
        popupStage.setScene(scene);
        popupStage.setResizable(false);

        popupStage.showAndWait();
    }
}
