package com.maisonneuve.tp2_algorithme_spotify.DAO;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Genre;
import com.maisonneuve.tp2_algorithme_spotify.utils.Connexion;
import com.maisonneuve.tp2_algorithme_spotify.utils.SourceDonnees;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChansonDAO implements SourceDonnees {

    // ajouter Chanson
    public void ajouter(Chanson c) throws SQLException {
        String sql =
                "INSERT INTO chanson"
                        + "(id, titre, artiste, album, genre, label, anneeSortie, duree, nbrEcoute, dansabilitee, imageUrl) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                        + "ON CONFLICT (id) DO NOTHING";

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {

            ps.setString(1, c.getId());
            ps.setString(2, c.getTitre());
            ps.setString(3, c.getArtiste());
            ps.setString(4, c.getAlbum());
            ps.setObject(5, Genre.fromNomBrut(c.getGenre()).name(), Types.OTHER);
            ps.setString(6, c.getLabel());
            ps.setInt(7, c.getAnneeSortie());
            ps.setInt(8, c.getDuree());
            ps.setInt(9, c.getNbrEcoute());
            ps.setFloat(10, c.getDansabilitee());
            ps.setString(11, c.getImageUrl());

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
    @Override
    public List<Chanson> charger() throws SQLException {
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
                    id = rs.getString("id");
                    String titre = rs.getString("titre");
                    String artiste = rs.getString("artiste");
                    String album = rs.getString("album");
                    String genre = rs.getString("genre");
                    String label = rs.getString("label");
                    int anneeSortie = rs.getInt("anneeSortie");
                    int duree = rs.getInt("duree");
                    int nbrEcoute = rs.getInt("nbrEcoute");
                    float dansabilitee = rs.getFloat("dansabilitee");
                    String imageUrl = rs.getString("imageUrl");
                    chansonTrouvee = new Chanson( id,  titre,  artiste,  album,  genre,  label,  anneeSortie,  duree,  nbrEcoute,  dansabilitee,  imageUrl);
                }
            }
        }
        return chansonTrouvee;
    }

    public boolean estVide() throws SQLException {
        String sql = "SELECT COUNT(*) FROM chanson";
        try (Connection co = Connexion.getConnexion();
             Statement st = co.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return true;
    }
}