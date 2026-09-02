package com.maisonneuve.tp2_algorithme_spotify.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ChansonDAO {
    public List<Chanson> charger(String chemin)  {
        List<Chanson> chansons = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(chemin))) {
            String ligne;

            br.readLine();

            while ((ligne = br.readLine()) != null) {
                String[] donnees = ligne.split(",");

                Chanson c = new Chanson(
                        donnees[0],                      // Track URI → id
                        donnees[1],                      // Track Name → titre
                        donnees[2],                      // Artist Name(s) → artiste
                        donnees[3],                      // Album Name → album
                        donnees[8],                      // Artist Genres → genre
                        donnees[10],                    // Label
                        Integer.parseInt(donnees[4]), // Album Release Date → anneeSortie
                        Integer.parseInt(donnees[6]),    // Track Duration (ms) → duree
                        Integer.parseInt(donnees[7]),    // Popularity → nbrEcoute
                        Float.parseFloat(donnees[9]),    // Danceability → dansabilitee
                        donnees[5]                       // Album Image URL → imageUrl
                );
                chansons.add(c);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return chansons;
    }
}
