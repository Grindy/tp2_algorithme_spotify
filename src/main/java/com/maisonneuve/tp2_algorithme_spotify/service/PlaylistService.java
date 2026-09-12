package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.algorithme.tri.TriInsertion;
import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.model.TriMap;
import com.maisonneuve.tp2_algorithme_spotify.utils.TimeUtils;

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
            String dureeMaxString,
            String nbEcoutes
    ) {
        final String recherchePropre = (recherche != null) ? recherche.trim().toLowerCase(Locale.ROOT) : "";
        final int dureeMaxInt;

        if (TimeUtils.estFormatTempsValide(dureeMaxString)) {
            try {
                dureeMaxInt = TimeUtils.MinToMs(dureeMaxString);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                return playlist;
            }
        } else return playlist;

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
                .filter(c -> genre == null || genre.isBlank() ||
                        (c.getGenre() != null && c.getGenre().equalsIgnoreCase(genre.trim())))
                .filter(c -> dureeMaxInt <= 0 || c.getDuree() <= dureeMaxInt)
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

        Comparator<Chanson> comp = TriComparateurService.COMPARATEURS.get(critereTri);

        List<Chanson> chansonsTriees = new ArrayList<>(playList.getChansons());

        if (comp != null) {
            TriInsertion tri = new TriInsertion();
            tri.preparer(chansonsTriees, comp);
            tri.executer();
        }

        return new Playlist(playList.getId(), playList.getNom(), chansonsTriees);
    }

}
