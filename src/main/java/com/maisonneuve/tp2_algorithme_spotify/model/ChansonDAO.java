package com.maisonneuve.tp2_algorithme_spotify.model;

import com.maisonneuve.tp2_algorithme_spotify.utils.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChansonDAO {

    // ajouter Chanson
    public void ajouter(Chanson c) throws SQLException {
        String sql =
                "INSERT INTO chanson"
                        + "(titre, artiste, album, genre, label, anneeSortie, duree, nbrEcoute, dansabilitee, imageUrl) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {

            ps.setString(1, c.getTitre());
            ps.setString(2, c.getArtiste());
            ps.setString(3, c.getAlbum());
            ps.setString(4, c.getGenre());
            ps.setString(5, c.getAlbum());
            ps.setInt(6, c.getAnneeSortie());
            ps.setInt(7, c.getDuree());
            ps.setInt(8, c.getNbrEcoute());
            ps.setFloat(9, c.getDansabilitee());
            ps.setString(9, c.getImageUrl());

            ps.executeUpdate();

            System.out.println("Chanson " + c.getTitre() + " a été ajoutée!");
        }
    }


    // supprimer Chanson
    public void supprimer(Chanson p) throws SQLException {
        String sql =
                "DELETE FROM chanson WHERE id = ?";

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {

            ps.setString(1, p.getId());

            ps.executeUpdate();

            System.out.println("La chanson a été supprimée!");
        }
    }


    // lister toutes les chansons
    public List<Chanson> listerToutes() throws SQLException {
        String sql = "SELECT * FROM chanson";
        List<Chanson> toutesChansons = new ArrayList<>();

        try (Connection co = Connexion.getConnexion();
             Statement st = co.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Chanson c = new Chanson();
                c.setId(rs.getString("id"));
                c.setTitre(rs.getString("titre"));
                c.setArtiste(rs.getString("artiste"));
                c.setAlbum(rs.getString("album"));
                c.setGenre(rs.getString("genre"));
                c.setLabel(rs.getString("label"));
                c.setAnneeSortie(rs.getInt("anneeSortie"));
                c.setDuree(rs.getInt("duree"));
                c.setNbrEcoute(rs.getInt("nbrEcoute"));
                c.setDansabilitee(rs.getFloat("dansabilitee"));
                c.setImageUrl(rs.getString("imageUrl"));
                toutesChansons.add(c);
            }
        }
        return toutesChansons;
    }


    // trouver une chanson par ID
    public Chanson listerParId(String id) throws SQLException {
        String sql = "SELECT * FROM chanson WHERE id = ?";
        Chanson chansonTrouvee = null;

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    chansonTrouvee.id = rs.getString("id");
                    chansonTrouvee.titre = rs.getString("titre");
                    chansonTrouvee.artiste = rs.getString("artiste");
                    chansonTrouvee.album = rs.getString("album");
                    chansonTrouvee.genre = rs.getString("genre");
                    chansonTrouvee.label = rs.getString("label");
                    chansonTrouvee.anneeSortie = rs.getInt("anneeSortie");
                    chansonTrouvee.duree = rs.getInt("duree");
                    chansonTrouvee.nbrEcoute = rs.getInt("nbrEcoute");
                    chansonTrouvee.dansabilitee = rs.getFloat("dansabilitee");
                    chansonTrouvee.imageUrl = rs.getString("imageUrl");
                }
            }
        }
        return chansonTrouvee;
    }
}