package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.ChansonDAO;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {
    private List<Chanson> chansons;
    private List<Playlist> playlists;

    public Bibliotheque(String cheminCSV) {
        ChansonDAO dao = new ChansonDAO();
        this.chansons = dao.charger(cheminCSV);
        this.playlists = new ArrayList<>();
    }

    public List<Chanson> getChansons() {
        return new ArrayList<>(chansons);
    }

    public List<Playlist> getPlaylists() {
        return new ArrayList<>(playlists);
    }

    public void ajouterPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
    }

    public void retirerPlaylist(Playlist playlist) {
        this.playlists.remove(playlist);
    }
}
