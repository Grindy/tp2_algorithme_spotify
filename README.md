# 🎧 Spoutify — Client Spotify Maison en JavaFX

> Application desktop qui reproduit l'interface et les fonctionnalités clés de Spotify : navigation par playlists, lecteur intégré, recherche/filtres en temps réel et visualisation d'algorithmes de tri.

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?logo=java)
![Maven](https://img.shields.io/badge/Maven-3.13-C71A36?logo=apachemaven&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

<br>

## 📸 Aperçu

[![Écran principal](./screenshots/EcranPrincipal.png)](./screenshots/EcranPrincipal.png)

_Écran principal — navigation dans les playlists et chansons, recherche et filtres en temps réel._

<br>

## ✨ Fonctionnalités

- **Navigation & lecture** — playlists et chansons affichées avec pagination, panneau de détail pour l'élément sélectionné, lecteur intégré avec formatage des durées (`0:00`) et mode _shuffle_.
- **Recherche et filtres en temps réel** — recherche par texte (titre + artiste + album), 4 filtres combinables.
- **Gestion de playlists** — ajout/retrait de chansons sans doublons, réorganisation, suppression via menu contextuel (clic droit).
- **Visualisation d'algorithmes de tri** — comparateur/benchmark de 3 algorithmes de tri (bulles, sélection, insertion) avec mesure du temps d'exécution en direct.
- **Intégration Spotify** — lors de la lecture d'une chanson, l'app dirige automatiquement Spotify vers le morceau en cours (le _streaming_ natif dans l'app est bloqué par le DRM de Spotify).

<br>

## 🖼️ Fonctionnalités en images

[![Écran de benchmark](./screenshots/EcranBenchmark.png)](./screenshots/EcranBenchmark.png)

_Comparateur de tris — visualisation du temps d'exécution de chaque algorithme sur le jeu de données._

<br>

## 🛠️ Stack technique

| Couche       | Technologie                                            |
| ------------ | ------------------------------------------------------ |
| Interface    | JavaFX 21 (FXML + CSS)                                 |
| Langage      | Java 21                                                |
| Build        | Maven                                                  |
| Données      | CSV (500+ entrées, UTF-8)                              |
| Architecture | MVC (model / service / algorithme / controller / util) |

<br>

## 🚀 Installation et lancement

**Prérequis**

- JDK 21
- Maven 3.13

```bash
# 1. Cloner le dépôt
git clone https://github.com/clementlaflamme/Spoutify.git
cd Spoutify

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

**Avec IntelliJ IDEA**

1. `File > New > Project from Version Control...`
2. Coller l'URL du dépôt
3. Ouvrir `MainFx.java`
4. Cliquer sur _Run_

<br>

## 🗂️ Structure du projet

```
Spoutify/
├── pom.xml
├── README.md
└── src/main/
    ├── java/com/maisonneuve/tp2_algorithme_spotify/
    │   ├── algorithme/tri/
    │   ├── benchmark/
    │   ├── controller/
    │   ├── model/
    │   ├── service/
    │   ├── utils/
    │   ├── Launcher.java
    │   └── MainFx.java
    └── resources/
        ├── data/spotifyData.csv
        └── vues/
            ├── Chanson.fxml
            ├── Graphique.fxml
            ├── Lecteur.fxml
            ├── Main.fxml
            ├── NouvellePlaylist.fxml
            └── style.css
```

<br>

## 📚 Contexte du projet

Réalisé dans le cadre du cours _Algorithmes et modèles de programmation_ (420-930-MA), Collège de Maisonneuve — projet portant sur l'implémentation et la comparaison d'algorithmes de tri appliqués à un jeu de données réel.

<br>

## 👥 Auteurs

- Francis Boisvert
- Clément Laflamme
- Mathieu Gosselin
