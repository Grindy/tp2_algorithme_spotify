package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.utils.TimeUtils;
import com.maisonneuve.tp2_algorithme_spotify.model.AuditJournalierDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import com.maisonneuve.tp2_algorithme_spotify.service.LecteurService;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;

public class LecteurController {

    @FXML private Button btnLecteurJouer;
    @FXML private Button btnLecteurPrecedente;
    @FXML private Button btnLecteurSuivante;
    @FXML private Button btnLecteurShuffle;
    @FXML private Region regionShuffle;
    @FXML private Region regionLecteurJouer;
    @FXML private Slider sliderTemps;
    @FXML private Label labelTempsActuel;
    @FXML private Label labelTempsTotal;
    @FXML private ImageView imgLecteurAlbum;
    @FXML private Label labelLecteurTitre;
    @FXML private Label labelLecteurArtiste;

    private final LecteurService lecteurService = LecteurService.getInstance();
    private AuditJournalierDAO auditJournalierDAO = new AuditJournalierDAO();

    @FXML
    public void initialize() {

        imgLecteurAlbum.setImage(new Image(MainController.IMAGE_PAR_DEFAUT));


        btnLecteurJouer.setOnAction(e -> lecteurService.togglePlayPause());
        btnLecteurPrecedente.setOnAction(e -> lecteurService.passerPrecedente());
        btnLecteurSuivante.setOnAction(e -> lecteurService.passerSuivante());

        btnLecteurShuffle.setOnAction(e -> {
            lecteurService.toggleAleatoire();
            if (lecteurService.getEstEnAleatoire()) {
                regionShuffle.getStyleClass().add("icone-active");
            } else {
                regionShuffle.getStyleClass().remove("icone-active");
            }
        });


        lecteurService.setOnChansonChangee(chanson -> {
            imgLecteurAlbum.setImage(new Image(chanson.getImageUrl()));
            labelLecteurTitre.setText(chanson.getTitre());
            labelLecteurArtiste.setText(chanson.getArtiste());
            labelTempsTotal.setText(TimeUtils.msToMinutes(chanson.getDuree()));
            sliderTemps.setMax(chanson.getDuree());
            sliderTemps.setValue(0);
            labelTempsActuel.setText("0:00");

            try {
                auditJournalierDAO.ajouter(chanson.getId());
            } catch (SQLException e) {
                System.err.println("Erreur lors de l'ajout à l'historique: " + e.getMessage());
            }


        });

        lecteurService.setOnEtatLectureChangee(enLecture -> {
            regionLecteurJouer.setId(enLecture ? "icone-pause" : "icone-play");
        });

        lecteurService.setOnTick(ms -> {
            sliderTemps.setValue(ms);
            labelTempsActuel.setText(TimeUtils.msToMinutes(ms));
        });

        clipperImageChanson();
    }

    private void clipperImageChanson() {
        double width = imgLecteurAlbum.getFitWidth();
        double height = imgLecteurAlbum.getFitHeight();

        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(22);
        clip.setArcHeight(22);

        imgLecteurAlbum.setClip(clip);
    }
}


