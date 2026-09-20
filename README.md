
# LABORATOIRE 3 - 420-930-MA - Ete 2026 - gr. 25604

--- 

# TP3 Algorithme Spotify - Migration PostgreSQL & Pattern DAO


![Java 21](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-17+-FF6F00?style=for-the-badge&logo=java&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-14+-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Architecture DAO](https://img.shields.io/badge/Architecture-DAO%20Pattern-2EA44F?style=for-the-badge)

<u>**Cours</u>:** 420-930-MA - Algorithmes et modeles de programmation  
<u>**Session</u>:** Ete 2026, groupe 25604  
<u>**Laboratoire</u>:** 3 (Base de donnees PostgreSQL et pattern DAO)  
<u>**Date de remise</u>:** Dimanche 20 septembre 2026, 23h59  
<u>**Journee de presentation choisie</u>:** Lundi 21 septembre 2026  

---

## Équipe


| Nom complet      | Adresse courriel            | Contribution principale                                                                                                                                                                                         |
|------------------|-----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Francis Boisvert | e2595782@cmaisonneuve.qc.ca | Modèles, Service, Tris, Algo, Graphiques, Separation du main, Conception BDD, DAO (Playlist), interface SourceDonnees, Initialisation et peuplement automatique de la BDD, Gestion des erreurs, Factorisation   |
| Clément Laflamme | e2595952@cmaisonneuve.qc.ca | Fonctions des tableaux, Affichage des playlists/chansons, Pagination, Tri/Filtres en temps réel, Separation du main, Operations CRUD IHM, Validations des entrees, Liaison controleurs/PlaylistManager, Threads |
| Mathieu Gosselin | e2596321@cmaisonneuve.qc.ca | Design, UI FXML, CSS, Lecteur, un peu de Controller, Historique(modèle, DAO, implémentation), DAO de départ, documentation                                                                                      |

---

## Sujet choisi


**Numéro du sujet** : 3  
**Nom du sujet** : Spotify

## 🔗 Lien du depot GitHub PUBLIC


URL : https://github.com/Grindy/tp2_algorithme_spotify

---

## Fonctionnalités implémentées (TP2)

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

## Fonctionnalités implémentées (TP3)

### ✅ Obligatoires

- ✅ Interface DAO + implémentation PostgreSQL (CRUD complet)
- ✅ Schéma SQL
- ✅ Intégration sans régression (TP2 toujours fonctionnel)
- ✅ Opérations CRUD depuis l'interface graphique 
  - ❗ trouverParId programmé dans le DAO mais nous n'avons pas trouvé de cas d'utilisation pertient pour l'ajouter à notre interface
- ✅ PreparedStatement, try-with-resources, gestion des SQLException
- ✅ Classe de connexion isolée avec configuration externalisée hors du dépôt

### 🎁 Bonus

- ✅ Multithreading

### ❌ Non implémenté

Par manque de temps nous n'avons pas réalisé les bonus suivants:
- ❌ Authentification
- ❌ Wishlist / favoris persistants
- ❌ Écran de statistiques avec charts
- ❌ Intégration d'une API externe

---

## Structure du projet

```
tp2_algorithme_spotify/
├── pom.xml
├── README.md
├── database.properties.example
└── src/
    └── main/
        ├── java/
        │   └── com/maisonneuve/tp2_algorithme_spotify/
        │       ├── algorithme/
        │       │   └── tri/
        │       ├── benchmark/
        │       ├── controller/
        │       ├── DAO/
        │       ├── model/
        │       ├── service/
        │       ├── utils/
        │       ├── Launcher.java
        │       └── MainFx.java 
        └── resources/
            ├── data/
            │   └── spotifyData.csv       # Fichier CSV source (TP2)
            ├── image/
            ├── sql/
            │   └── schema.sql            # Script DDL PostgreSQL (TP3)
            └── vues/
```

---

## Instructions pour lancer le projet

### Prerequis :
- Java JDK 21
- PostgreSQL 14+ avec pgAdmin 4
- Maven 3.13
- IntelliJ IDEA


### Etapes (reproductibles sur une base vierge) :

- ### 1.  Creation de la base de donnees dans PostgreSQL :  
   - Ouvrez pgAdmin 4 et créer une nouvelle Database nommée "playlist-manager" 

- ### 2. Installation du projet :
    Cloner le dépôt à partir de Github
```
git clone https://github.com/Grindy/tp2_algorithme_spotify.git
cd tp2_algorithme_spotify
```
ou dans IntelliJ:
```
1. Cloner le projet dans IntelliJ (File > New > Project from Version Control...)
2. Donner l'URL du repo dans le champ URL ( https://github.com/Grindy/tp2_algorithme_spotify.git )
3. Changer le dossier cible au besoin 
```


- ### 3.  Execution du script de structure (schema.sql) :
   - Connectez-vous a la base "playlist-manager" et executez l'integralite du script situe dans : src/main/resources/sql/schema.sql  

   L'application dispose d'un systeme d'amorcage automatique, au premier démarrage si la table chanson est vide elle sera automatiquement peuplée à partir des chansons du CSV.

- ### 4. Configuration des identifiants (database.properties)   
  À la racine du projet, créez un fichier nomme "database.properties" et remplissez-le avec vos informations.

  ##### Un fichier modèle "database.properties.example" est versionné a la racine.  

  Cles attendues dans database.properties :
```
  db.url=jdbc:postgresql://localhost:5432/playlist-manager  
  db.user=postgres  
  db.password=votre_mot_de_passe_local
 ```

- ### 5. Compilation et lancement de l'application

```
En ligne de commande :

1. Compiler le projet :

   mvn clean compile
   
2. Lancer l'application JavaFX :

   mvn javafx:run

Note: Si l'affichage dans le terminal bogue avec les accents, entrez d'abord chcp 65001 avant de lancer l'application. 
 ```

Ou dans IntelliJ:
```
1. Ouvrir `MainFx.java`
2. Cliquer sur le bouton Run
```

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

### TP2:

- Nous avons eu des problèmes avec le MainController qui était devenu trop gros pour être bien géré par github. Des lignes de code ont été écrasées et nous avons du reprendre ou réintégrer celles-ci.
- Ce problème nous a aussi poussé à splitter le MainController en plusieurs petits Controllers ce qui sera utile pour le maintien de l'app dans le futur. [Clément, Francis]
- Figurer comment transposer ce qu'on connaissait du BigOLab pour fonctionner avec des chansons plutôt que de simple Int a été laborieux. [Francis]
- Arriver dans la partie code du projet après avoir passé le début du projet sur le FXML et le CSS était intimidant. [Mathieu]

### TP3:

- Nos choix précédents on fait en sorte qu'il ne faisait pas de sens pour nous de découper nos chansons en tables additionnelles comme prévu dans l'énoncé. 
N'ayant pas plus d'informations que ça à fournir sur l'album ou l'artiste ou autre table 
potentielle en plus d'être toujours utiliséss ensemble avec le titre de la chanson, nous ne trouvions pas justifiable d'implémenter un tel changement de 
modèles et de métier dans toute l'application. Tel que convenu avec le professeur nous avons donc gardé le modèle Chanson tel qu'il était mais avons dû 
ajouter une table AuditJournalier qui enregistre ce qu'on écoute.
- Passer les positions dans les playlists de liste ordonnée (TP2) à une colonne position dans la base de données (TP3) s'est révélé être un plus grand défi qu'imaginé au départ. [Francis]

---

## Répartition du travail (auto-évaluation)

| Nom complet      | Adresse courriel            | Contribution principale                                                                                                                                                                                         |
|------------------|-----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Francis Boisvert | e2595782@cmaisonneuve.qc.ca | Modèles, Service, Tris, Algo, Graphiques, Separation du main, Conception BDD, DAO (Playlist), interface SourceDonnees, Initialisation et peuplement automatique de la BDD, Gestion des erreurs, Factorisation   |
| Clément Laflamme | e2595952@cmaisonneuve.qc.ca | Fonctions des tableaux, Affichage des playlists/chansons, Pagination, Tri/Filtres en temps réel, Separation du main, Operations CRUD IHM, Validations des entrees, Liaison controleurs/PlaylistManager, Threads |
| Mathieu Gosselin | e2596321@cmaisonneuve.qc.ca | Design, UI FXML, CSS, Lecteur, un peu de Controller, Historique(modèle, DAO, implémentation), DAO de départ, documentation                                                                                      |

---

## Notes pour le correcteur

L'application comporte deux écrans principaux, l'appli s'ouvre sur l'écran des playlists et chansons. On peut accéder
à l'écran des tris en appuyant sur le bouton rond 📈.

Certaines fonctionnalités de playlist nécessitent un clic droit dans la liste des chansons ou des playlists pour faire apparaître
leur menu:
- Monter d'une position
- Descendre d'une position
- Vider la playlist
- Supprimer de la playlist
- Renommer une playlist

---

## Captures d'écran

### Écran principal
![EcranPrincipal.png](./screenshots/EcranPrincipal.png)

### Écran de benchmark
![Écran benchmark](./screenshots/EcranBenchmark.png)

---

## Historique Git

**Nombre total de commits** : 215+  
**Date du premier commit** : 2026-09-02  
**Date du dernier commit** : 2026-09-19
