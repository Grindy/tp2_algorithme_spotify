package com.maisonneuve.tp2_algorithme_spotify.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ExampleConnexion {
    private static final String URL = "jdbc:postgresql://localhost:5432/NOM_DE_VOTRE_BDD";
    private static final String USER = "VOTRE_NOM_UTILISATEUR";
    private static final String PASS = "VOTRE_MOT_DE_PASSE";

    private ExampleConnexion(){};

    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(URL,USER,PASS);
    }
}
