package com.maisonneuve.tp2_algorithme_spotify.Algorithme.tri;
import com.maisonneuve.tp2_algorithme_spotify.Algorithme.Algorithme;
import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;


import java.util.Comparator;
import java.util.List;




public class TriBulle implements Algorithme  {

    private List<Chanson> liste;
    private Comparator<Chanson> comp;

    @Override
    public String nom(){
        return "Tri Bulle";
    }

    @Override
    public String complexiteTheorique(){
        return "O(n^2)";
    }

    @Override
    public void preparer(List<Chanson> liste, Comparator<Chanson> comp){
        this.liste = liste;
        this.comp = comp;
    }


    @Override
    public void executer(){

        int n = liste.size();

        for(int i = 0; i < n - 1; i++){
            for(int j = 0; j < n - 1 - i; j++){
                if (comp.compare(liste.get(j), liste.get(j+1)) > 0) {
                    Chanson temp = liste.get(j);
                    liste.set(j, liste.get(j+1));
                    liste.set(j+1, temp);
                }
            }
        }
    }

}
