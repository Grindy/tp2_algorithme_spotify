package com.maisonneuve.tp2_algorithme_spotify.utils;

import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;
import java.util.List;

public interface SourceDonnees {
    List<Chanson> charger() throws Exception;
}


