package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class PlaylistManager {

    private List<Playlist> playlists = new ArrayList<>();

    public void ajouterPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    public void retirerPlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public Playlist trouverParId(String id) {
        return playlists.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


}
