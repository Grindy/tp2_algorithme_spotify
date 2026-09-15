package com.maisonneuve.tp2_algorithme_spotify.model;

import com.maisonneuve.tp2_algorithme_spotify.utils.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    // ajouter Playlist
    public void ajouter(Playlist p) throws SQLException {
        String sql =
                "INSERT INTO playlist"
                    + "(nom) "
                    + "VALUES(?)";

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {

            ps.setString(1, p.getNom());

            ps.executeUpdate();

            System.out.println("Playlist " + p.getNom() + " a été créée!");
        }
    }


    // supprimer Playlist
    public void supprimer(Playlist p) throws SQLException {
        String sql =
                "DELETE FROM playlist WHERE id = ?";

        try (Connection co = Connexion.getConnexion();
            PreparedStatement ps = co.prepareStatement(sql)) {

            ps.setString(1, p.getId());

            ps.executeUpdate();

            System.out.println("La playlist a été supprimée!");
        }
    }


    // lister toutes les playlists
    public List<Playlist> listerToutes() throws SQLException {
        String sql = "SELECT * FROM playlist";
        List<Playlist> toutesPlaylists = new ArrayList<>();

        try (Connection co = Connexion.getConnexion();
            Statement st = co.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Playlist p = new Playlist();
                p.setId(rs.getString("id"));
                p.setNom(rs.getString("nom"));
                p.setDateCreation(rs.getDate("date"));
                toutesPlaylists.add(p);
            }
        }
        return toutesPlaylists;
    }


    // trouver une playlist par ID
    public Playlist listerParId(String id) throws SQLException {
        String sql = "SELECT * FROM playlist WHERE id = ?";
        Playlist playlistTrouvee = null;

        try (Connection co = Connexion.getConnexion();
            PreparedStatement ps = co.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    playlistTrouvee = new Playlist();
                    playlistTrouvee.setId(rs.getString("id"));
                    playlistTrouvee.setNom(rs.getString("nom"));
                    playlistTrouvee.setDateCreation(rs.getDate("date"));
                }
            }
        }
        return playlistTrouvee;
    }
}