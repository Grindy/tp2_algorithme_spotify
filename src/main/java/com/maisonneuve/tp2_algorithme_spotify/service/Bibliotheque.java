package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.ChansonDAO;

import java.util.List;

public class Bibliotheque {
    private List<Chanson> chansons;

    public Bibliotheque(String cheminCSV) {
        ChansonDAO dao = new ChansonDAO();
        this.chansons = dao.charger(cheminCSV);
    }

    public List<Chanson> getChansons() {
        return chansons;
    }
}
