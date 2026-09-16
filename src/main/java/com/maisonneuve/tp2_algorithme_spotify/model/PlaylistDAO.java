package com.maisonneuve.tp2_algorithme_spotify.model;
import java.util.UUID;

import com.maisonneuve.tp2_algorithme_spotify.utils.Connexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
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
    public void retirerPlaylist(Playlist p) throws SQLException {
        String sql =
                "DELETE FROM playlist WHERE id = ?";

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {
            ps.setObject(1, UUID.fromString(p.getId()));
            ps.executeUpdate();
            System.out.println("La playlist a été supprimée!");
        }
    }

    // ajouter chanson dans une Playlist
    public void ajouterChanson(Playlist p, Chanson c) throws SQLException {
        int nouvellePosition = p.getChansons().size() + 1;

        String sql = "INSERT INTO playlist_chanson (id_playlist, id_chanson, position) VALUES (?, ?, ?)";

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {

            ps.setObject(1, UUID.fromString(p.getId()));
            ps.setObject(2, c.getId());
            ps.setInt(3, nouvellePosition);

            ps.executeUpdate();

            p.getChansons().add(c);
            System.out.println("Chanson " + c.getTitre() + " ajoutée à la playlist " + p.getNom() + " en position " + nouvellePosition);
        }
    }



    //Je comprend parfaitement cette fonction mais, de la a la pondre moi meme, on repassera
    //J'ai utilisé gemini car mes pistes de solution s'en allaient un peu n'importe ou
    public void retirerChanson(Playlist p, Chanson c) throws SQLException {
        // 1. Supprime la ligne et renvoie directement la position supprimée
        String sqlDelete =
                "DELETE FROM playlist_chanson " +
                        "WHERE id_playlist = ? AND id_chanson = ? " +
                        "RETURNING position";

        // 2. Décale d'un bloc toutes les chansons qui avaient une position supérieure
        String sqlShift =
                "UPDATE playlist_chanson " +
                        "SET position = position - 1 " +
                        "WHERE id_playlist = ? AND position > ?";

        try (Connection co = Connexion.getConnexion()) {
            co.setAutoCommit(false); // Transaction : tout passe ou tout s'annule

            try {
                int positionSupprimee = -1;

                // Exécution du DELETE avec récupération de la position
                try (PreparedStatement psDelete = co.prepareStatement(sqlDelete)) {
                    psDelete.setObject(1, UUID.fromString(p.getId()));
                    psDelete.setString(2, c.getId());

                    try (ResultSet rs = psDelete.executeQuery()) {
                        if (rs.next()) {
                            positionSupprimee = rs.getInt("position");
                        }
                    }
                }
                // Si la chanson existait bien, on comble le trou
                if (positionSupprimee != -1) {
                    try (PreparedStatement psShift = co.prepareStatement(sqlShift)) {
                        psShift.setObject(1, p.getId());
                        psShift.setInt(2, positionSupprimee);
                        psShift.executeUpdate();
                    }
                }
                p.getChansons().remove(c);
                co.commit(); // Validation en base
                System.out.println("Chanson retirée et positions réajustées.");

            } catch (SQLException e) {
                co.rollback(); // En cas de problème, rien n'est modifié
                throw e;
            }
        }
    }

    public void deplacerChanson(Playlist p, Chanson chanson, String direction) throws SQLException {
        List<Chanson> chansons = p.getChansons();
        int i = chansons.indexOf(chanson);
        if (i < 0) return;

        if (direction.equals("up") && i > 0) {
            echanger(p, i + 1, i);
            Collections.swap(chansons, i - 1, i);
        }

        if (direction.equals("down") && i < chansons.size() - 1) {
            echanger(p, i + 1, i + 2);
            Collections.swap(chansons, i, i + 1);
        }
    }


    private void echanger(Playlist p, int posX, int posY) throws SQLException {
        String sql = "UPDATE playlist_chanson " +
                //comme un if
                "SET position = CASE " +
                // si la position = x alors y
                "WHEN position = ? THEN ? " +
                //si la position = y alors x
                "WHEN position = ? THEN ? " +
                "END " +
                //on cherche dans quelle playlist et dans les 2 position donnees
                "WHERE id_playlist = ? AND position IN (?,?)";
        try (Connection co = Connexion.getConnexion();
        PreparedStatement ps = co.prepareStatement(sql)) {
            ps.setInt(1, posX);
            ps.setInt(2, posY);
            ps.setInt(3, posY);
            ps.setInt(4, posX);
            ps.setObject(5, UUID.fromString(p.getId()));
            ps.setInt(6, posX);
            ps.setInt(7, posY);

            ps.executeUpdate();

        }
    }


    // Recuperer les chansons d'une playlist
    public List<Chanson> getChansons(Playlist p) throws SQLException {
        List<Chanson> liste = new ArrayList<>();

        String sql =
                "SELECT c.* " +
                        "FROM chanson c " +
                        "JOIN playlist_chanson pc ON c.id = pc.id_chanson " +
                        "WHERE pc.id_playlist = ? " +
                        "ORDER BY pc.position ASC";

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {

            // Si id_playlist est un UUID en base PostgreSQL :
            ps.setObject(1, UUID.fromString(p.getId()));

            try (ResultSet rs = ps.executeQuery()) {
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
                    c.setImageUrl(rs.getString("imageurl"));

                    liste.add(c);
                }
            }
        }

        // Met à jour la liste dans l'instance reçue pour que tout concorde en mémoire
        if (p.getChansons() != null) {
            p.getChansons().clear();
            p.getChansons().addAll(liste);
        }

        return liste;
    }
}