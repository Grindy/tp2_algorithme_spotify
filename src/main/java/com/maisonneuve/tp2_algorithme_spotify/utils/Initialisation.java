package com.maisonneuve.tp2_algorithme_spotify.utils;

import com.maisonneuve.tp2_algorithme_spotify.DAO.ChansonDAO;
import com.maisonneuve.tp2_algorithme_spotify.DAO.PlaylistDAO;
import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.service.Bibliotheque;

import java.sql.SQLException;
import java.util.List;

public class Initialisation {


    //Remplir chansons si la table est vite
    public static void peuplerChansonsSiVide(String cheminCSV, ChansonDAO chansonDAO) throws Exception {
        if (chansonDAO.estVide()) {
            System.out.println("Base de données vide : importation initiale du CSV...");
            LecteurCSV lecteur = new LecteurCSV(cheminCSV);
            List<Chanson> chansonsCSV = lecteur.charger();

            for (Chanson c : chansonsCSV) {
                chansonDAO.ajouter(c);
            }
            System.out.println("Importation des chansons terminée avec succès.");
        }
    }

    //Remplir la playlist_chanson si elle ne contient pas de cle 11111111-1111-1111-1111-111111111111
    public static void peuplerPlaylistBiblioSiVide(Bibliotheque biblio, Playlist toutesLesChansons, PlaylistDAO playlistDAO) throws SQLException {
        if (!playlistDAO.isCreePlaylistInitiale()) {
            System.out.println("Peuplement initial de la playlist bibliothèque en base...");
            for (Chanson c : biblio.getChansons()) {
                playlistDAO.ajouterChanson(toutesLesChansons, c);
            }
            System.out.println("Chansons associées à la playlist bibliothèque avec succès.");
        }
    }


}