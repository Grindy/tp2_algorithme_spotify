package com.maisonneuve.tp2_algorithme_spotify.model;

import java.util.ArrayList;
import java.util.Date;

public class Chanson {
    private String id;
    private String titre;
    private String artiste;
    private String album;
    private String genre;
    private String label;
    private int anneeSortie;
    private int duree;
    private int nbrEcoute;
    private float dansabilitee;
    private String imageUrl;;

    // Object Anemique
    public Chanson(){}

    public Chanson(String id, String titre, String artiste, String album, String genre, String label, int anneeSortie, int duree, int nbrEcoute, float dansabilitee, String imageUrl) {
        this.id = id;
        this.titre = titre;
        this.artiste = artiste;
        this.album = album;
        this.genre = genre;
        this.label = label;
        this.anneeSortie = anneeSortie;
        this.duree = duree;
        this.nbrEcoute = nbrEcoute;
        this.dansabilitee = dansabilitee;
        this.imageUrl = imageUrl;
    }

    public String getLabel(){
        return label;
    }

    public String getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public String getArtiste() {
        return artiste;
    }

    public String getAlbum() {
        return album;
    }

    public String getGenre() {
        return genre;
    }

    public int getAnneeSortie() {
        return anneeSortie;
    }

    public int getDuree() {
        return duree;
    }

    public int getNbrEcoute() {
        return nbrEcoute;
    }

    public float getDansabilitee() {
        return dansabilitee;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return titre + " - " + artiste + " - " + album;
    }
}
