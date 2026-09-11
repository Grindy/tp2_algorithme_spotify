package com.maisonneuve.tp2_algorithme_spotify.Algorithme.tri;
import com.maisonneuve.tp2_algorithme_spotify.Algorithme.Algorithme;

public class TriSelection implements Algorithme {

    private int[] tabChansons;

    @Override
    public String nom(){return "Tri Selection";}

    @Override
    public String complexiteTheorique() {return "O(n^2)";}

//    @Override
//    public void preparer(int n){
//        this.tabChansons = GenerateurDonnees.tableauMelanger(n);
//    }

    @Override
    public void executer(){
        int[] tab = tabChansons.clone();
        int n = tab.length;

        for( int i = 0 ; i < n - 1 ; i++ ){
            int indexMin = i;
            for (int j = i + 1; j < n ; j++ ){
                if(tab[j] < tab[indexMin]){
                    indexMin = j;
                }
            }

            int temp = tab[i];
            tab[i] = tab[indexMin];
            tab[indexMin] = temp;
        }
    }
}
