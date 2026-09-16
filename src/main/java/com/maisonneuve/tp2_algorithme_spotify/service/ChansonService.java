package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.ChansonDAO;
import com.maisonneuve.tp2_algorithme_spotify.utils.LecteurCSV;
import com.maisonneuve.tp2_algorithme_spotify.utils.SourceDonnees;

import java.sql.SQLException;
import java.util.List;

public class ChansonService {

    public static List<Chanson> chargerPuisAjouterToutesLesChansons(String cheminCSV, ChansonDAO chansonDao) throws SQLException {
        SourceDonnees csv = new LecteurCSV(cheminCSV);
        List<Chanson> toutesLesChanson = csv.charger();

        for (Chanson c : toutesLesChanson) {
            chansonDao.ajouter(c);
        }
        return toutesLesChanson;
    }
}
