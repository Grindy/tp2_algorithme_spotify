package com.maisonneuve.tp2_algorithme_spotify.Algorithme;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;

import java.util.Comparator;
import java.util.List;

public interface Algorithme {

    String nom();

    String complexiteTheorique();

    void preparer(List<Chanson> liste, Comparator<Chanson> comp);

    void executer();

}

