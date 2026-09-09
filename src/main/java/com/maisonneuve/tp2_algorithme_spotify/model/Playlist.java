package com.maisonneuve.tp2_algorithme_spotify.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Playlist {
    private String id;
    private String nom;
    private List<Chanson> chansons;
    private Date dateCreation;

    public Playlist(String id, String nom, List<Chanson> chansons) {
        this.id = id;
        this.nom = nom;
        this.chansons = chansons;
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

    public void viderPlaylist() {
        chansons.clear();
    }

    public void deplacerChanson(Chanson chanson, String direction) {
        int i = chansons.indexOf(chanson);
        if (i < 0 ) return;
        if (direction.equals("up") && i > 0) {
            Collections.swap(chansons, i-1, i);
        }
        if (direction.equals("down") && i < chansons.size() - 1){
            Collections.swap(chansons, i, i +1);
        }
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    @Override
    public String toString() {
        return nom;
    }
}
