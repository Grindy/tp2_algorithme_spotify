module com.maisonneuve.tp2_algorithme_spotify {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.maisonneuve.tp2_algorithme_spotify to javafx.fxml;
    exports com.maisonneuve.tp2_algorithme_spotify;
}