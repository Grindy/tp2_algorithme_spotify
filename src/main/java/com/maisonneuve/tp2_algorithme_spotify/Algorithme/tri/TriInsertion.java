package com.maisonneuve.tp2_algorithme_spotify.Algorithme.tri;
import com.maisonneuve.tp2_algorithme_spotify.Algorithme.Algorithme;

public class TriInsertion implements Algorithme {

    private int[] tabChansons;

    @Override
    public String nom() {return "Tri Insertion";}

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

        for (int i = 1; i < n; i++){
            int cle = tab[i];
            int j = i - 1;

            while(j >= 0 && tab[j] > cle){
                tab[j + 1] = tab[j];
                j--;
            }

            tab[j+1] = cle;
        }
    }
}
