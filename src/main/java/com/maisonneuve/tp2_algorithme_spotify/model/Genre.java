package com.maisonneuve.tp2_algorithme_spotify.model;

import java.util.Arrays;

public enum Genre {

    COUNTRY_FOLK("Country / Folk"),
    DIVERS_VARIETE("Divers / Variété"),
    HIP_HOP_RAP("Hip-Hop / Rap"),
    JAZZ_BLUES("Jazz / Blues"),
    MUSIQUES_DU_MONDE_REGGAE("Musiques du monde / Reggae"),
    POP("Pop"),
    RB_SOUL("R&B / Soul"),
    ROCK("Rock"),
    ELECTRO_DANCE("Électro / Dance");

    private final String nomBrut;
    private final String nomFormate;

    Genre(String nomBrut) {
        this.nomBrut = nomBrut;
        this.nomFormate = nomBrut;
    }

    public static Genre fromNomBrut(String texte) {
        if (texte == null || texte.isBlank()) {
            return DIVERS_VARIETE;
        }

        return Arrays.stream(values())
                .filter(g -> g.nomBrut.equalsIgnoreCase(texte.trim()))
                .findFirst()
                .orElse(DIVERS_VARIETE);
    }

    public String getNomBrut() {
        return nomBrut;
    }

    public String getNomFormate() {
        return nomFormate;
    }

    @Override
    public String toString() {
        return nomFormate;
    }
}