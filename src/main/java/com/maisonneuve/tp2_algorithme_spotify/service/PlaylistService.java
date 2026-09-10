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

    public Playlist filtrer(
            Playlist playlist,
            String recherche,
            String genre,
            String dureeMax,
            String nbEcoutes
    ) {
        final String recherchePropre = (recherche != null) ? recherche.trim().toLowerCase(Locale.ROOT) : "";
        final Integer dureeMaxInt = parserEntier(dureeMax);
        final Integer nbEcoutesInt = parserEntier(nbEcoutes);

        return new Playlist(playlist.getId(), playlist.getNom(), playlist.getChansons()
                .stream()
                .filter(c -> {
                    if (recherchePropre.isEmpty()) return true;
                    String texte = String.join(" ",
                            c.getTitre() != null ? c.getTitre() : "",
                            c.getArtiste() != null ? c.getArtiste() : "",
                            c.getAlbum() != null ? c.getAlbum() : ""
                    ).toLowerCase(Locale.ROOT);
                    return texte.contains(recherchePropre);
                })
                .filter(c -> genre == null || genre.trim().isEmpty() ||
                        (c.getGenre() != null && c.getGenre().equalsIgnoreCase(genre.trim())))
                .filter(c -> dureeMaxInt == null || c.getDuree() <= dureeMaxInt)
                .filter(c -> nbEcoutesInt == null || c.getNbrEcoute() >= nbEcoutesInt)
                .collect(Collectors.toList()));
    }

    private Integer parserEntier(String texte) {
        if (texte == null || texte.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(texte.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Playlist trierSelon(String critereTri, Playlist playList) {
        TriMap critereTriEnum = TriMap.fromTexte(critereTri);
        List<Chanson> chansonsTriees = new ArrayList<>(playList.getChansons());
        switch (critereTriEnum) {
            case TITRE -> chansonsTriees.sort(Comparator.comparing(Chanson::getTitre, String.CASE_INSENSITIVE_ORDER));
            case ARTISTE ->
                    chansonsTriees.sort(Comparator.comparing(Chanson::getArtiste, String.CASE_INSENSITIVE_ORDER));
            case ANNEE -> chansonsTriees.sort(Comparator.comparingInt(Chanson::getAnneeSortie));
            case NB_ECOUTES -> chansonsTriees.sort(Comparator.comparingInt(Chanson::getNbrEcoute).reversed());
            case null -> {}
        }
        return new Playlist(playList.getId(), playList.getNom(), chansonsTriees);
    }
}
