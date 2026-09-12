package com.maisonneuve.tp2_algorithme_spotify.algorithme.tri;
import com.maisonneuve.tp2_algorithme_spotify.algorithme.Algorithme;
import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;

import java.util.Comparator;
import java.util.List;

public class TriSelection implements Algorithme {

    private List<Chanson> liste;
    private Comparator<Chanson> comp;

    @Override
    public String nom(){return "Tri Selection";}

    @Override
    public String complexiteTheorique() {return "O(n^2)";}

    @Override
    public void preparer(List<Chanson> liste, Comparator<Chanson> comp){
        this.liste = liste;
        this.comp = comp;
    }

    @Override
    public void executer(){
        int n = liste.size();

        for( int i = 0 ; i < n - 1 ; i++ ){
            int indexMin = i;
            for (int j = i + 1; j < n ; j++ ){
                if(comp.compare(liste.get(j), liste.get(indexMin)) < 0 ){
                    indexMin = j;
                }
            }

            Chanson temp = liste.get(i);
            liste.set(i, liste.get(indexMin));
            liste.set(indexMin, temp);
        }
    }
}
