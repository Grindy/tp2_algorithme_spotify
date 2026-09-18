
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

## Identification de l'equipe et sujet


<u>Numero du sujet</u> : 3  
<u>Nom du sujet</u> : Spotify  

<u>Equipe et repartition du travail</u> :

Nom complet      | Adresse courriel            | Contribution principale                                                                                                                                               | % estime
-----------------|-----------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|---------
Francis Boisvert | e2595782@cmaisonneuve.qc.ca | Conception DDL SQL, couche DAO (Playlist), interface SourceDonnees, Initialisation et peuplement automatique de la bdd, Gestion des erreurs, README.md, Factorisation | 33%
Clement Laflamme | e2595952@cmaisonneuve.qc.ca | Operations CRUD IHM, validations des entrees, liaison controleurs/PlaylistManager, Threads                                                                            | 33%
Mathieu Gosselin | e2596321@cmaisonneuve.qc.ca | Vues FXML (Audit, CRUD), gestion visuelle des alertes JavaFX, documentation                                                                                           | 33%

---

## 🔗 Lien du depot GitHub PUBLIC


URL : https://github.com/Grindy/tp2_algorithme_spotify

---

## Procedure d'installation et de reconstruction de la base


### Prerequis :
- Java JDK 21
- PostgreSQL 14+ avec pgAdmin 4 (ou psql CLI)
- Maven 3.9+

### Etapes a suivre (reproductibles sur une base vierge) :

- ### 1.  Creation de la base de donnees dans PostgreSQL :  
   - Ouvrez psql ou pgAdmin 4 et executez la requete suivante :  
   - CREATE DATABASE "playlist-manager";  

- ### 2.  Execution du script de structure (schema.sql) :
   - Connectez-vous a la base "playlist-manager" et executez l'integralite du script situe dans : src/main/resources/sql/schema.sql  


- ### 3. Peuplement des donnees :
   L'application dispose d'un systeme d'amorcage automatique :
    - Au premier demarrage, la methode Initialisation.peuplerChansonsSiVide(...) detecte si la table chanson est vide. Si oui, elle injecte automatiquement les morceaux a partir de src/main/resources/data/spotifyData.csv.
    - La playlist initiale "Toutes les chansons" (UUID 11111111-1111-1111-1111-111111111111) est egalement generee et associee en base.
    - Optionnel (via psql CLI) : Pour importer manuellement en ligne de commande :  
        - \copy chanson(id, titre, artiste, album, genre, label, annee_sortie, duree, nbr_ecoute, dansabilitee, image_url) FROM 'src/main/resources/data/spotifyData.csv' WITH (FORMAT csv, HEADER true, DELIMITER ',');


- ### 4. Configuration des identifiants (database.properties)  
  Les identifiants ne sont jamais ecrits en dur dans le code source Java.  
  A la racine du projet, creez un fichier nomme "database.properties"  
  (ce fichier est exclu par le .gitignore).

  ##### Un fichier modele "database.properties.example" est versionne a la racine.  

  Cles attendues dans database.properties :  
  db.url=jdbc:postgresql://localhost:5432/playlist-manager  
  db.user=postgres  
  db.password=votre_mot_de_passe_local  

---

## Compilation et lancement de l'application


En ligne de commande :
1. Compiler le projet :
   mvn clean compile
2. Lancer l'application JavaFX :
   mvn javafx:run

Depuis IntelliJ IDEA :
1. Ouvrir le projet Maven.
2. S'assurer que le fichier database.properties est renseigne a la racine.
3. Executer le script schema.sql dans votre base locale.
4. Lancer la classe MainFx.java (ou Launcher.java).

--- 

## Fonctionnalites implementees

✅ <u>LABORATOIRE 2</u> (Conserve & Actif)

- Algorithmes de tri personnalises (sans Collections.sort) :
    * Tri par selection (Selection Sort).
    * Tri a bulles (Bubble Sort).
    * Tri par insertion (Insertion Sort).
    * Comparateur multi-criteres dynamique (titre, artiste, annee, popularite).
- Navigation et affichage des donnees :
    * Pagination dynamique (10, 25, 50, 100 elements par page).
    * Recherche textuelle en temps reel sur le titre et l'artiste.
    * Filtres composes simultanes (genre musical, duree maximale formatee "mm:ss", nombre d'ecoutes).
- Benchmark et analyse :
    * Module d'analyse comparative mesurant le temps d'execution de chaque algorithme de tri sur la collection de donnees.
- Lecteur et IHM :
    * Lecteur multimedial visuel (LecteurService / Lecteur.fxml) avec integration Spotify.
    * Lecture initiale des morceaux basee sur fichier CSV (LecteurCSV).


✅ <u>LABORATOIRE 3</u>

- Migration et persistance PostgreSQL :
    * Schema relationnel normalise (3NF) : chanson, playlist, playlist_chanson, audit_journalier.
    * Remplacement du stockage CSV par PostgreSQL au demarrage de l'application.
- Architecture DAO & Separation stricte :
    * Couche d'acces aux donnees isolee dans le package DAO (ChansonDAO, PlaylistDAO).
    * Centralisation de la connexion dans Connexion.java via database.properties.
    * Aucun code SQL ni ressource JDBC manipulee dans les controleurs ou modeles (seules les SQLException sont remontees pour affichage).
- Interface SourceDonnees & Couplage faible :
    * Abstraction complete : bascule instantanee entre LecteurCSV et ChansonDAO en modifiant une seule ligne dans MainController.
- Operations CRUD completes depuis l'interface JavaFX :
    * Create : Creation de nouvelles playlists et ajout de chansons via fenetre modale avec verification anti-doublon (equals/hashCode sur ID).
    * Read : Chargement des listes et synchronisation bidirectionnelle memoire/BDD par PlaylistManager.
    * Update : Reorganisation de l'ordre des chansons (monter / descendre) directement persistee en base de donnees via menu contextuel.
    * Delete : Retrait d'une chanson, vidage d'une playlist et suppression entiere de la playlist avec dialogue de confirmation.
- Robustesse et securite :
    * Requetes 100% parametrees via PreparedStatement (protection contre les injections SQL).
    * Gestion propre des ressources JDBC via try-with-resources.
    * Interception des erreurs de connexion/base sans plantage, traduites en boites de dialogue utilisateur (Alert).


🎁 <u>BONUS IMPLEMENTES</u> (Lab 3)

1. Transactions SQL & Contrainte differee :
    - Encadrement transactionnel (setAutoCommit(false), commit(), rollback()) des operations multi-etapes dans PlaylistDAO.
    - Utilisation de la contrainte UNIQUE (id_playlist, position) DEFERRABLE INITIALLY DEFERRED : permet d'inverser deux positions en une requete CASE sans declencher de collision d'unicite prematuree.
    - Suppression propre avec RETURNING position suivi d'un reajustement automatique (UPDATE ... position = position - 1) pour eviter les trous d'index.
2. Audit Journalier (Historique des ecoutes) :
    - Table audit_journalier tracant chaque piste lue avec horodatage.
    - Interface modale permettant de consulter l'historique complet de la session.


## Structure des packages

```text
tp2_algorithme_spotify/
|-- pom.xml
|-- README.md
|-- database.properties.example
`-- src/
    `-- main/
        |-- java/
        |   `-- com/maisonneuve/tp2_algorithme_spotify/
        |       |-- algorithme/
        |       |   `-- tri/             # Algorithmes de tri personnalises (Lab 2)
        |       |-- benchmark/           # Mesures de performance de tri (Lab 2)
        |       |-- controller/          # Controleurs JavaFX (Main, TableChansons, Playlists, etc.)
        |       |-- DAO/                 # Classes d'acces aux donnees JDBC (Lab 3)
        |       |-- model/               # Entites metier (Chanson, Playlist, Genre)
        |       |-- service/             # Bibliotheque, PlaylistManager, PlaylistService, LecteurService, TriComparateurService
        |       |-- utils/               # Connexion, SourceDonnees, Initialisation, LecteurCSV, FormatterFieldDureeMax, TimeUtils
        |       |-- Launcher.java
        |       `-- MainFx.java          # Point d'entree JavaFX
        `-- resources/
            |-- data/
            |   `-- spotifyData.csv       # Fichier CSV source (Lab 2)
            |-- sql/
            |   `-- schema.sql           # Script DDL PostgreSQL (Lab 3)
            `-- vues/
                |-- AuditJournalier.fxml
                |-- Chanson.fxml
                |-- Graphique.fxml
                |-- Lecteur.fxml
                |-- Main.fxml
                |-- Playlist.fxml
                |-- TableChansons.fxml
                `-- style.css
```


## Justifications architecturales


- Decoupage relationnel :  
  &emsp;Une table chanson separee de playlist_chanson evite la duplication
  d'informations des morceaux (artiste, titre, duree) lors de la creation de playlists multiples.  
- Choix de suppression (ON DELETE) :
    * playlist_chanson.id_playlist possede ON DELETE CASCADE : la suppression d'une playlist
      nettoie automatiquement les lignes de liaison correspondantes.
    * chanson(id) ne possede PAS de cascade : les morceaux de la bibliotheque sont
      sanctuarises et proteges contre toute suppression accidentelle.  
- Protection contre les injections SQL :  
  &emsp;L'utilisation systematique de PreparedStatement separe l'analyse syntaxique SQL
  des donnees saisies, neutralisant totalement les tentatives d'injections.  

- Role de l'interface SourceDonnees :  
  &emsp;Permet a la classe Bibliotheque d'appliquer le principe d'inversion des dependances (DIP) :
  le metier depend d'une abstraction et non d'une source physique.   
  &emsp;Le passage de LecteurCSV a ChansonDAO s'effectue en une seule ligne sans reecrire les controleurs ou tris.