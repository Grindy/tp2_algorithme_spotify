package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.ChansonDAO;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.utils.LecteurCSV;
import com.maisonneuve.tp2_algorithme_spotify.utils.SourceDonnees;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {
    private final List<Chanson> chansons;
    private final List<Playlist> playlists;
    private final ChansonDAO chansonDAO;

    public Bibliotheque(String cheminCSV) throws SQLException {
        this.chansonDAO = new ChansonDAO();
        this.chansons = ChansonService.chargerPuisAjouterToutesLesChansons(cheminCSV, chansonDAO);
        this.playlists = new ArrayList<>();
    }

    public ChansonDAO getChansonDAO() {
        return chansonDAO;
    }

    public List<Chanson> getChansons() {
        return chansons;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

}
