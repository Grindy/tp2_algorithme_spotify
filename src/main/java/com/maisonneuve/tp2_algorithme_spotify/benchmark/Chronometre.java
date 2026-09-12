package com.maisonneuve.tp2_algorithme_spotify.benchmark;
import com.maisonneuve.tp2_algorithme_spotify.algorithme.Algorithme;
import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;

import java.util.Comparator;
import java.util.List;


public class Chronometre {


    public static long mesurer(Algorithme algo, List<Chanson> chansons, Comparator comp, int repetitions){
        algo.preparer(chansons, comp);

        for (int i = 0 ; i < 3 ; i++) algo.executer();

        long debut = System.nanoTime();
        for (int i = 0; i < repetitions; i++){
            algo.preparer(chansons, comp);
            algo.executer();
        }
        long fin = System.nanoTime();

        return (fin - debut) / repetitions;
    }


}

