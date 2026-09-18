package com.maisonneuve.tp2_algorithme_spotify.DAO;

import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import java.sql.SQLException;
import java.util.List;

public interface PlaylistDAOInterface {

    List<Playlist> trouverTous() throws SQLException;

    Playlist trouverParId(String id) throws SQLException;

    void ajouter(Playlist playlist) throws SQLException;

    void modifier(Playlist playlist) throws SQLException;

    void supprimer(String id) throws SQLException;

}