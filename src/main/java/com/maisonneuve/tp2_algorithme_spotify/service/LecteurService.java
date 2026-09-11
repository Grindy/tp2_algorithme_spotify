package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import com.maisonneuve.tp2_algorithme_spotify.model.Playlist;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.List;
import java.util.function.Consumer;


public class LecteurService {

    private Chanson chansonEnLecture;
    private Playlist contexteEnLecture;
    private int indexEnLecture;
    private int tempsEcouleMs;
    private boolean estEnLecture;

    private final Timeline timeline;
    private Consumer<Integer> onTick;
    private Consumer<Chanson> onChansonChangee;

    public LecteurService() {
        // on crée un timeline d'une seconde, qui après avoir joué crée une autre timeline
        // cette boucle continue jusqu'à ce qu'on l'arrete avec stop() ou pauyse()
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> tick()));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void setOnTick(Consumer<Integer> callback) {
        this.onTick = callback;
    }

    public void setOnChansonChangee(Consumer<Chanson> callback) {
        this.onChansonChangee = callback;
    }

    public void demarrerLecture(Chanson chanson, Playlist contexte) {
        this.contexteEnLecture = contexte;
        this.chansonEnLecture = chanson;
        this.indexEnLecture = contexte.getChansons().indexOf(chanson);
        this.tempsEcouleMs = 0;
        this.estEnLecture = true;

        if (onChansonChangee != null) onChansonChangee.accept(chanson);
        if (onTick != null) onTick.accept(tempsEcouleMs);

        // stop la timeline et la relance du départ quand on démarre la lecture d'une chanson
        timeline.stop();
        timeline.playFromStart();
    }

    public void togglePlayPause() {
        if (chansonEnLecture == null) return;

        if (estEnLecture) {
            timeline.pause();
        } else {
            timeline.play();
        }
        estEnLecture = !estEnLecture;
    }

    public void tick() {
        tempsEcouleMs += 1000;
        if (onTick != null) onTick.accept(tempsEcouleMs);

        if (tempsEcouleMs >= chansonEnLecture.getDuree()) {
            passerSuivante();
        }
    }

    public void passerSuivante() {
        List<Chanson> chansons = contexteEnLecture.getChansons();
        int prochainIndex = (indexEnLecture + 1) % chansons.size();
        demarrerLecture(chansons.get(prochainIndex), contexteEnLecture);
    }

    public boolean estEnLecture() {
        return estEnLecture;
    }

    public Chanson getChansonEnLecture() {
        return chansonEnLecture;
    }
}
