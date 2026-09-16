package com.maisonneuve.tp2_algorithme_spotify.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Connexion {
    private static final Properties PROPS = new Properties();

    static {
        try(FileInputStream input =  new FileInputStream("database.properties") ){
            PROPS.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Impossible de charger le fichier database.properties", e);
        }
    }

    public static Connection getConnexion() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }
}
