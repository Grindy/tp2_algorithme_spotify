package com.maisonneuve.tp2_algorithme_spotify.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Playlist {
    private String id;
    private String nom;
    private ArrayList<Chanson> chansons;
    private Date dateCreation;

    public Playlist(String id, String nom) {
        this.id = id;
        this.nom = nom;
        this.chansons = new ArrayList<>();
        this.dateCreation = new Date();
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Chanson> getChansons() {
        return new ArrayList<>(chansons);
    }

    public void ajouterChanson(Chanson chanson) {
        chansons.add(chanson);
    }

    public void retirerChanson(Chanson chanson) {
        chansons.remove(chanson);
    }

    public int getDureeTotale() {
        return chansons.stream().mapToInt(Chanson::getDuree).sum();
    }

    public Date getDateCreation() {
        return dateCreation;
    }


}
