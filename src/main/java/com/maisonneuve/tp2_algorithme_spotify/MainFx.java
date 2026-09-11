package com.maisonneuve.tp2_algorithme_spotify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class MainFx extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/Main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        // pour le rechargement a chaud, effacer avant la remise
        // apres changement dans le CSS, CTRL + MAJ + B dans IntelliJ puis F5 dans l'app
        // -----------------------------------------------------
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F5) {
                System.out.println("F5 détecté"); // debug temporaire
                try {
                    java.io.File source = new java.io.File(getClass().getResource("/vues/style.css").toURI());
                    java.io.File temp = java.io.File.createTempFile("style-", ".css");
                    temp.deleteOnExit();
                    java.nio.file.Files.copy(source.toPath(), temp.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(temp.toURI().toString());
                    System.out.println("CSS rechargé : " + temp.toURI()); // debug temporaire
                    scene.getRoot().applyCss();
                    scene.getRoot().layout();
                } catch (Exception ex) {
                    ex.printStackTrace(); // s'assurer que rien n'est avalé silencieusement
                }
            }
        });
        //-----------------------------------------------------^

        stage.setTitle("TP2 Algorithme Spotify");
        stage.setScene(scene);
        stage.setMinWidth(600);
        stage.setMinHeight(400);

        stage.show();
    }
}
