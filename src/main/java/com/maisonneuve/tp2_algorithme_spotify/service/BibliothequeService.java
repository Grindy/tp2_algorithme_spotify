package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;

import java.util.List;
import java.util.stream.Collectors;

public class BibliothequeService {
    private final Bibliotheque bibliotheque;

    public BibliothequeService(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    public List<Chanson> filtrer(
            String genre,
            Integer dureeMax,
            Integer nbrEcoute,
            Playlist playlist
    ) {
        return bibliotheque.getChansons()
                .stream()
                .filter(c -> genre == null || c.getGenre().equalsIgnoreCase(genre))
                .filter(c -> dureeMax == null || c.getDuree() <= dureeMax)
                .filter(c -> nbrEcoute == null || c.getNbrEcoute() <= nbrEcoute)
                .collect(Collectors.toList());
    }

}
