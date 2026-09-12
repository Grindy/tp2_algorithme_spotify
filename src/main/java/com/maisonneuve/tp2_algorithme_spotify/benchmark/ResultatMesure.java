package com.maisonneuve.tp2_algorithme_spotify.benchmark;

public class ResultatMesure {

    public final String nomAlgo;
    public final int taille;
    public final long tempsNanoSec;

    public ResultatMesure(String nomAlgo, int taille, long tempsNanoSec) {
        this.nomAlgo = nomAlgo;
        this.taille = taille;
        this.tempsNanoSec = tempsNanoSec;
    }
}


