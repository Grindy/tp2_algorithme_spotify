package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.DAO.PlaylistDAO;
import com.maisonneuve.tp2_algorithme_spotify.utils.SourceDonnees;

import java.util.List;

public class Bibliotheque {
    private final List<Chanson> chansons;
    private final List<Playlist> playlists;
    private final SourceDonnees sourceDonnees;
    private final PlaylistDAO playlistDAO;

    public Bibliotheque(SourceDonnees sourceDonnees) throws Exception {
        this.sourceDonnees = sourceDonnees;
        this.playlistDAO = new PlaylistDAO();

        // On charge via l'interface : pas d'addition, juste la source sélectionnée !
        this.chansons = sourceDonnees.charger();
        this.playlists = playlistDAO.trouverTous();
    }

    public SourceDonnees getSourceDonnees() {
        return sourceDonnees;
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }
}