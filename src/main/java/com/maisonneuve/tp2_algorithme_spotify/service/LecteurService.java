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
    private boolean estEnAleatoire = false;

    private final Timeline timeline;
    private Consumer<Integer> onTick;
    private Consumer<Chanson> onChansonChangee;
    private Consumer<Boolean> onEtatLectureChangee;

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

    public void setOnEtatLectureChangee(Consumer<Boolean> callback) { this.onEtatLectureChangee = callback; }

    public void demarrerLecture(Chanson chanson, Playlist contexte) {
        this.contexteEnLecture = contexte;
        this.chansonEnLecture = chanson;
        this.indexEnLecture = contexte.getChansons().indexOf(chanson);
        this.tempsEcouleMs = 0;
        this.estEnLecture = true;

        if (onChansonChangee != null) onChansonChangee.accept(chanson);
        if (onTick != null) onTick.accept(tempsEcouleMs);
        if (onEtatLectureChangee != null) onEtatLectureChangee.accept(estEnLecture);

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

        if (onEtatLectureChangee != null) onEtatLectureChangee.accept(estEnLecture);
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
        int prochainIndex;

        if(estEnAleatoire) {
            prochainIndex = (int) (Math.random() * chansons.size());
        } else {
            prochainIndex = (indexEnLecture + 1) % chansons.size();
        }

        demarrerLecture(chansons.get(prochainIndex), contexteEnLecture);
    }

    public void passerPrecedente() {
        List<Chanson> chansons = contexteEnLecture.getChansons();
        int precedentIndex = indexEnLecture - 1;
        if(precedentIndex < 0) { precedentIndex = (chansons.toArray().length - 1);
        }
        demarrerLecture(chansons.get(precedentIndex), contexteEnLecture);
    }

    public void toggleAleatoire() {
        estEnAleatoire = !estEnAleatoire;
    }

    public boolean getEstEnAleatoire() {
        return estEnAleatoire;
    }

    public boolean estEnLecture() {
        return estEnLecture;
    }

    public Chanson getChansonEnLecture() {
        return chansonEnLecture;
    }
}
