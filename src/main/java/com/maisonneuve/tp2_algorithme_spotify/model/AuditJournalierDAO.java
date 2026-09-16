package com.maisonneuve.tp2_algorithme_spotify.model;

import com.maisonneuve.tp2_algorithme_spotify.utils.Connexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditJournalierDAO {

    // ajouter une entrée lors de la lecture
    public void ajouter(String idChanson) throws SQLException {
        String sqlInsert =
                "INSERT INTO auditjournalier"
                        + "(idChanson)"
                        + "VALUES(?)";

        String sqlTitre = "SELECT titre FROM chanson WHERE id = ?";

        try (Connection co = Connexion.getConnexion()) {
            // création de l'entrée
            try (PreparedStatement ps = co.prepareStatement(sqlInsert)) {

                ps.setString(1, idChanson);
                ps.executeUpdate();
            }

            // récupération du titre
            String titre = null;
            try (PreparedStatement ps = co.prepareStatement(sqlTitre)) {

                ps.setString(1, idChanson);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        titre = rs.getString("titre");
                    }
                }
            }

                System.out.println("Chanson " + titre + " ajoutée aux chansons jouées!");
        }
    }

    // supprimer l'historique
    public void vider() throws SQLException {
        String sql =
                "DELETE FROM auditjournalier";

        try (Connection co = Connexion.getConnexion();
             PreparedStatement ps = co.prepareStatement(sql)) {

            ps.executeUpdate();

            System.out.println("L'historique a été vidé!");
        }
    }

    // get l'historique complet
    public List<AuditJournalier> listerHistorique() throws SQLException {
        String sql =
                "SELECT * FROM auditjournalier";

        List<AuditJournalier> toutHistorique = new ArrayList<>();

        try (Connection co = Connexion.getConnexion();
            PreparedStatement ps = co.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String idChanson = rs.getString("idChanson");
                    AuditJournalier historique = new AuditJournalier(idChanson);
                    historique.setDateJouee(rs.getDate("dateJouee"));

                    toutHistorique.add(historique);
                }
            }
        }
        return toutHistorique;
    }

}
