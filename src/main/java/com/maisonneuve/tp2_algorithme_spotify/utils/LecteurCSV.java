package com.maisonneuve.tp2_algorithme_spotify.utils;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LecteurCSV implements SourceDonnees {
    private final String chemin;

    public LecteurCSV(String chemin) {
        this.chemin = chemin;
    }

    @Override
    public List<Chanson> charger() {
        List<Chanson> chansons = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.chemin))) {
            String ligne;
            br.readLine(); // saute l'en-tête

            while ((ligne = br.readLine()) != null) {
                String[] donnees = ligne.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for (int i = 0; i < donnees.length; i++) {
                    donnees[i] = donnees[i].replace("\"", "").trim();
                }

                Chanson c = new Chanson(
                        donnees[0],
                        donnees[1],
                        donnees[2],
                        donnees[3],
                        donnees[8],
                        donnees[10],
                        Integer.parseInt(donnees[4]),
                        Integer.parseInt(donnees[6]),
                        Integer.parseInt(donnees[7]),
                        Float.parseFloat(donnees[9]),
                        donnees[5]
                );
                chansons.add(c);
            }
        } catch (Exception e) {
           throw new RuntimeException("Erreur lors de la lecture du CSV !", e);
        }
        return chansons;
    }
}