====================================================================

  LABORATOIRE 2 - 420-930-MA - Ete 2026 - gr. 25604

====================================================================


# TP2 Algorithme Spotify

**Cours** : 420-930-MA — Algorithmes et modèles de programmation
**Session** : Été 2026, groupe 25604
**Laboratoire** : 2 (Application JavaFX v1)
**Date de remise** : 13 septembre 2026, 23h59

---

## Équipe

| Nom complet      | Adresse courriel            | Contribution principale                                                                                             |
|------------------|-----------------------------|---------------------------------------------------------------------------------------------------------------------|
| Francis Boisvert | e2595782@cmaisonneuve.qc.ca | Modèles, Service, Tris, Algo, Graphiques, Separation du main                                                        |
| Clément Laflamme | e2595952@cmaisonneuve.qc.ca | Fonctions des tableaux, Affichage des playlists/chansons, Pagination, Tri/Filtres en temps réel, Separation du main |
| Mathieu Gosselin | e2596321@cmaisonneuve.qc.ca | Design, UI FXML, CSS, Lecteur, un peu de Controller                                                                 |

---

## Sujet choisi

**Numéro du sujet** : 3  
**Nom du sujet** : Spotify  

---

## 🔗 Lien du dépôt GitHub PUBLIC

**URL**: https://github.com/Grindy/tp2_algorithme_spotify

---

## Fonctionnalités implémentées

### ✅ Obligatoires

- ✅ Architecture MVC avec packages séparés (model / service / algorithmes / controller / util)
- ✅ Chargement des données depuis fichier CSV (nombre de lignes : 500
- ✅ Interface JavaFX principale avec liste/tableau
- ✅ Panneau détail affichant l'élément sélectionné
- ✅ Pagination fonctionnelle (taille de page : 25)
- ✅ Filtres multi-critères combinables (nombre implémentés : 4 / 4)
- ✅ Recherche par texte en temps réel
- ✅ Interface Algorithme définie
- ✅ Tri #1 implémenté : Tri par bulles
- ✅ Tri #2 implémenté : Tri par sélection
- ✅ Tri #3 implémenté : Tri par insertion
- ✅ Comparateur/benchmark des tris avec mesure du temps
- ✅ Wishlist / Favoris (ajout, retrait, pas de doublons)
- ✅ CSS appliqué (thème visuel du projet)

### 🎁 Bonus

- ✅ Recherche par texte avec l'album
- ✅ Mode shuffle
- ✅ Formattage des durées au format 0:00
- ✅ Lorsqu'une chanson joue, l'app dirrige spotify vers la chanson en cours 
- (Pour des raisons de DRM, il est impossible de faire jouer une chanson streamée de Spotify dans notre app)

### ❌ Non implémenté

Par manque de temps nous n'avons pas réalisé les bonus suivants:
- ❌ Statistiques d'écoute
- ❌ Import / Export de playlist
- ❌ Playlist "Mix quotidien" auto-créée
- ❌ Thèmes visuels supplémentaires
- ❌ Recherche insensible aux accents

---

## Structure du projet

```
tp2_algorithme_spotify/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/maisonneuve/tp2_algorithme_spotify/
        │       ├── algorithme/
        │       │   └── tri/
        │       ├── benchmark/
        │       ├── controller/
        │       ├── model/
        │       ├── service/
        │       ├── utils/
        │       ├── Launcher.java
        │       └── MainFx.java 
        └── resources/
            ├── data/
            │   └── spotifyData.csv
            └── vues/
                ├── Chanson.fxml
                ├── Graphique.fxml
                ├── Lecteur.fxml
                ├── Main.fxml
                ├── NouvellePlaylist.fxml
                └── style.css
```

---

## Instructions pour lancer le projet

### Prérequis

- JDK 21
- Maven 3.13
- IntelliJ IDEA

### Étapes 

```bash
# 1. Cloner le dépôt
git clone https://github.com/Grindy/tp2_algorithme_spotify.git
cd tp2_algorithme_spotify

# 2. Compiler
mvn clean compile

# 3. Lancer l'application
mvn javafx:run
```

### Étapes avec IntelliJ

1. Cloner le projet dans IntelliJ (File > New > Project from Version Control...)
2. Donner l'URL du repo dans le champ URL ( https://github.com/Grindy/tp2_algorithme_spotify.git )
3. Changer le dossier cible au besoin
4. Ouvrir `MainFx.java`
5. Cliquer sur le bouton Run

---

## Choix techniques

### Version Java utilisée
Java 21 avec JavaFX 21

### Format des données
CSV: séparateur "," - encodage: UTF-8 - nombre de lignes: 501

### Algorithmes de tri implémentés
Tri par bulles - O(n²)
Tri par sélection - O(n²)
Tri par insertion - O(n²)

### Bibliothèques externes utilisées
Aucune

---

## Difficultés rencontrées

- Nous avons eu des problèmes avec le MainController qui était devenu trop gros pour être bien géré par github. Des lignes de code ont été écrasées et nous avons du reprendre ou réintégrer celles-ci.
- Ce problème nous a aussi poussé à splitter le MainController en plusieurs petits Controllers ce qui sera utile pour le maintien de l'app dans le futur. [Clément, Francis]
- Figurer comment transposer ce qu'on connaissait du BigOLab pour fonctionner avec des chansons plutôt que de simple Int a été laborieux. [Francis]
- Arriver dans la partie code du projet après avoir passé le début du projet sur le FXML et le CSS était intimidant. [Mathieu]

---

## Répartition du travail (auto-évaluation)

| Membre           | % contribution estimée | Ce sur quoi j'ai travaillé                                                                    |
|------------------|------------------------|-----------------------------------------------------------------------------------------------|
| Francis Boisvert | 33%                    | Modeles, Algorithme, Tri, Graphique, Services, Utils, Separation du main en modules |
| Clément Laflamme | 33%                    | Fonctions des tableaux, Affichage des playlists/chansons, Pagination, Tri/Filtres en temps réel, Separation du main                                                                                             |
| Mathieu Gosselin | 33%                    | Design, UI FXML, CSS, Lecteur, un peu de Controller                                           |

---

## Notes pour le correcteur

L'application comporte deux écrans principaux, l'appli s'ouvre sur l'écran des playlists et chansons. On peut accéder
à l'écran des tris en appuyant sur le bouton rond 📈.

Certaines fonctionnalités de playlist nécessitent un clic droit dans la liste des chansons pour faire apparaître
leur menu:
- Monter d'une position
- Descendre d'une position
- Vider la playlist
- Supprimer de la playlist

---

## Captures d'écran

### Écran principal
![EcranPrincipal.png](./screenshots/EcranPrincipal.png)

### Écran de benchmark
![Écran benchmark](./screenshots/EcranBenchmark.png)

---

## Historique Git

**Nombre total de commits** : 116+  
**Date du premier commit** : 2026-09-02  
**Date du dernier commit** : 2026-09-13  