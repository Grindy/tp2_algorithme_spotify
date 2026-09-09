package com.maisonneuve.tp2_algorithme_spotify.model;

public enum TriMap {
    TITRE("Titre"),
    ARTISTE("Artiste"),
    ANNEE("Année"),
    NB_ECOUTES("Nombre d'écoutes");

    final String nomFormatte;

    TriMap(String nomFormatte) {
        this.nomFormatte = nomFormatte;
    }

    public static TriMap fromTexte(String texte) {
        if (texte == null) return null;

        for (TriMap tri : TriMap.values()) {
            if (tri.nomFormatte.equalsIgnoreCase(texte.trim())) {
                return tri;
            }
        }
        return null;
    }
}
