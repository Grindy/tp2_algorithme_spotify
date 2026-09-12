package com.maisonneuve.tp2_algorithme_spotify.algorithme.tri;
import com.maisonneuve.tp2_algorithme_spotify.algorithme.Algorithme;
import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;

import java.util.Comparator;
import java.util.List;

public class TriInsertion implements Algorithme {

    private List<Chanson> liste;
    private Comparator<Chanson> comp;

    @Override
    public String nom() {return "Tri Insertion";}

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

        for (int i = 1; i < n; i++){
            Chanson cle = liste.get(i);
            int j = i - 1;

            while(j >= 0 && comp.compare(liste.get(j),  cle) > 0){
                liste.set(j + 1, liste.get(j));
                j--;
            }

            liste.set(j+1, cle);
        }
    }
}
