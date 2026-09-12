package com.maisonneuve.tp2_algorithme_spotify.service;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;

import java.util.Comparator;
import java.util.Map;

public class TriComparateurService {
    public static final Map<String, Comparator<Chanson>> COMPARATEURS = Map.of(
            "Année", Chanson.COMP_ANNEE,
            "Artiste", Chanson.COMP_ARTISTE,
            "Durée", Chanson.COMP_DUREE,
            "Nombre d'écoutes", Chanson.COMP_NB_ECOUTE,
            "Titre", Chanson.COMP_TITRE
    );
}
