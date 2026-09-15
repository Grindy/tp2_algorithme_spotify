package com.maisonneuve.tp2_algorithme_spotify.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {

    // Changez cette valeur pour l'adresse et le nom de votre base de donnée PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/spotify";

    // Changez ces valeurs pour qu'elles correspondent avec vos informations de connexion PostgreSQL
    private static final String USER = "postgres";
    private static final String PASS = "admin";

    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

}
