package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import com.maisonneuve.tp2_algorithme_spotify.DAO.PlaylistDAO;

import java.util.List;
import java.sql.SQLException;

public class PlaylistManager {

    private final Bibliotheque bibliotheque;
    private final PlaylistDAO playlistDAO;

    public PlaylistManager(Bibliotheque bibliotheque) throws SQLException {
        this.playlistDAO = new PlaylistDAO();
        this.bibliotheque = bibliotheque;
        chargerPlaylistsDepuisBD();
    }

    private void chargerPlaylistsDepuisBD() throws SQLException {

            List<Playlist> playlistsBD = playlistDAO.trouverTous();
            bibliotheque.getPlaylists().clear();
            bibliotheque.getPlaylists().addAll(playlistsBD);

    }

    public void ajouterPlaylist(Playlist playlist) throws SQLException {
            playlistDAO.ajouter(playlist);
            bibliotheque.getPlaylists().add(playlist);
    }

    public void retirerPlaylist(Playlist playlist) throws SQLException {
            playlistDAO.supprimer(playlist.getId());
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

    public void ajouterChanson(Playlist p, Chanson c) throws SQLException {
        if (!p.getChansons().contains(c)) {
            playlistDAO.ajouterChanson(p, c);
        }
    }

    public void retirerChanson(Playlist p, Chanson c) throws SQLException {
            playlistDAO.retirerChanson(p, c);
    }

    public void deplacerChanson(Playlist p, Chanson c, String direction) throws SQLException {
            playlistDAO.deplacerChanson(p, c, direction);
    }

    public void viderPlaylist(Playlist p) throws SQLException {
        playlistDAO.viderPlaylist(p);
        p.viderPlaylist();
    }

}
