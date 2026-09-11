package com.maisonneuve.tp2_algorithme_spotify.Algorithme.tri;
import com.maisonneuve.tp2_algorithme_spotify.Algorithme.Algorithme;
import com.maisonneuve.tp2_algorithme_spotify.model.Chanson;


public class TriBulle implements Algorithme  {

    private int[] tabChansons;

    @Override
    public String nom(){
        return "Tri Bulle";
    }

    @Override
    public String complexiteTheorique(){
        return "O(n^2)";
    }

//    @Override
//    public void preparer(int n){
//        this.tabChansons = GenerateurDonnees.tableauMelanger(n);
//    }

    @Override
    public void executer(){
        int[] tab = tabChansons.clone();
        int n = tab.length;

        for(int i = 0; i < n - 1; i++){
            for(int j = 0; j < n - 1 - i; j++){
                if(tab[j] > tab[j+1]){
                    int temp = tab[j];
                    tab[j] = tab[j+1];
                    tab[j+1] = temp;
                }
                //System.out.println("> "+ Arrays.toString(tab));
            }
        }
    }

}
