package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;

import java.util.List;
import java.util.stream.Collectors;

public class PlaylistService {

    public void ajouterChansons(Playlist playlist, Chanson chanson) {
        playlist.ajouterChanson(chanson);
    }

    public void retirerChansons(Playlist playlist, Chanson chanson) {
        playlist.retirerChanson(chanson);
    }

    public int calculerDuree(Playlist playlist) {
        return playlist.getDureeTotale();
    }

    public List<Chanson> filtrer(
            Playlist playlist,
            String genre,
            Integer dureeMax,
            Integer nbrEcoute
    ) {
        return playlist.getChansons()
                .stream()
                .filter(c -> genre == null || c.getGenre().equalsIgnoreCase(genre))
                .filter(c -> dureeMax == null || c.getDuree() <= dureeMax)
                .filter(c -> nbrEcoute == null || c.getNbrEcoute() <= nbrEcoute)
                .collect(Collectors.toList());
    }
}
