package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.model.PlaylistDAO;

import java.util.List;
import java.sql.SQLException;

public class PlaylistManager {

    private final Bibliotheque bibliotheque;
    private final PlaylistDAO playlistDAO;

    public PlaylistManager(Bibliotheque bibliotheque) {
        this.playlistDAO = new PlaylistDAO();
        this.bibliotheque = bibliotheque;
    }

    public void ajouterPlaylist(Playlist playlist) {
        try {
            playlistDAO.ajouter(playlist);
            bibliotheque.getPlaylists().add(playlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void retirerPlaylist(Playlist playlist) {
        try {
            playlistDAO.retirerPlaylist(playlist);
            bibliotheque.getPlaylists().remove(playlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    public void ajouterChanson(Playlist p, Chanson c) {
        try {
            playlistDAO.ajouterChanson(p, c);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void retirerChanson(Playlist p, Chanson c) {
        try {
            playlistDAO.retirerChanson(p, c);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deplacerChanson(Playlist p, Chanson c, String direction) {
        try {
            playlistDAO.deplacerChanson(p, c, direction);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
