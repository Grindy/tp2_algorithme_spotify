package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;

import java.util.List;

public class PlaylistManager {

    private final Bibliotheque bibliotheque;

    public PlaylistManager(Bibliotheque bibliotheque) {
        this.bibliotheque = bibliotheque;
    }

    public void ajouterPlaylist(Playlist playlist) {
        bibliotheque.getPlaylists().add(playlist);
    }

    public void retirerPlaylist(Playlist playlist) {
        bibliotheque.getPlaylists().remove(playlist);
    }

    public List<Playlist> getPlaylists() {
        return bibliotheque.getPlaylists();
    }

    public Playlist trouverParId(String id) {
        return bibliotheque.getPlaylists().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


}
