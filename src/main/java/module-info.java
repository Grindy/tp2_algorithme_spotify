module com.maisonneuve.tp2_algorithme_spotify {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires java.desktop;
    requires jdk.compiler;

    opens com.maisonneuve.tp2_algorithme_spotify to javafx.fxml;
    exports com.maisonneuve.tp2_algorithme_spotify;

    opens com.maisonneuve.tp2_algorithme_spotify.controller to javafx.fxml;
    exports com.maisonneuve.tp2_algorithme_spotify.controller;

    opens com.maisonneuve.tp2_algorithme_spotify.model to javafx.fxml;
    exports com.maisonneuve.tp2_algorithme_spotify.model;
    exports com.maisonneuve.tp2_algorithme_spotify.DAO;
    opens com.maisonneuve.tp2_algorithme_spotify.DAO to javafx.fxml;
}