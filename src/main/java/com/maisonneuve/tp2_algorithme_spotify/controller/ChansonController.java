package com.maisonneuve.tp2_algorithme_spotify.controller;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.utils.TimeUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import static com.maisonneuve.tp2_algorithme_spotify.controller.MainController.IMAGE_PAR_DEFAUT;

public class ChansonController {
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


    public void initialize(){
        imgAlbum.setImage(new Image(IMAGE_PAR_DEFAUT));
        clipperImageChanson();
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
        clipperImageChanson();
    }

    private void clipperImageChanson() {
        double width = imgAlbum.getFitWidth();
        double height = imgAlbum.getFitHeight();

        Rectangle clip = new Rectangle(width, height);
        clip.setArcWidth(22);
        clip.setArcHeight(22);

        imgAlbum.setClip(clip);
    }
}
