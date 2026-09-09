package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.model.TriMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PlaylistService {

    public List<Chanson> filtrer(
            Playlist playlist,
            String recherche,
            String genre,
            Integer dureeMax,
            Integer nbrEcoute
    ) {
        String recherchePropre = recherche.trim().toLowerCase(Locale.ROOT);
        return playlist.getChansons()
                .stream()
                .filter(c -> (c.getTitre() + " " + c.getArtiste() + " " + c.getAlbum())
                        .toLowerCase(Locale.ROOT)
                        .contains(recherchePropre))
                .filter(c -> genre == null || c.getGenre().equalsIgnoreCase(genre))
                .filter(c -> dureeMax == null || c.getDuree() <= dureeMax)
                .filter(c -> nbrEcoute == null || c.getNbrEcoute() <= nbrEcoute)
                .collect(Collectors.toList());
    }

    public Playlist trierSelon(TriMap critereTri, Playlist playList) {
        List<Chanson> chansonsTriees = new ArrayList<>(playList.getChansons());
        switch (critereTri) {
            case TITRE -> chansonsTriees.sort(Comparator.comparing(Chanson::getTitre, String.CASE_INSENSITIVE_ORDER));
            case ARTISTE ->
                    chansonsTriees.sort(Comparator.comparing(Chanson::getArtiste, String.CASE_INSENSITIVE_ORDER));
            case ANNEE -> chansonsTriees.sort(Comparator.comparingInt(Chanson::getAnneeSortie));
            case NB_ECOUTES -> chansonsTriees.sort(Comparator.comparingInt(Chanson::getNbrEcoute).reversed());
            case null -> System.err.println("Critère invalide");
        }
        return new Playlist(playList.getId(), playList.getNom(), chansonsTriees);
    }
}
