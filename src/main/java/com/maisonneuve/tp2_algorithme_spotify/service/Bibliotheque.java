package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.utils.LecteurCSV;

import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {
    private final List<Chanson> chansons;
    private final List<Playlist> playlists;

    public Bibliotheque(String cheminCSV) {
        LecteurCSV lecteur = new LecteurCSV();
        this.chansons = lecteur.charger(cheminCSV);
        this.playlists = new ArrayList<>();
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

}
