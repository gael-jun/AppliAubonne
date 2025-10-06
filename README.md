# Appli Pré-Études PV 

Cette application est conçue pour standardiser les calculs lors des pré-études 
photovoltaïques. Elle intègre les données relatives aux toitures, modules, onduleurs, 
câbles et systèmes de protection, permettant aux utilisateurs d'obtenir des résultats 
précis et utiles.
Aussi, elle estime la production photovoltaïque (PVGIS), elle visualise des graphes financiers et elle exporte les rapports en PDF/CSV.


## 1) Prérequis et installation (Windows)

- Java 21 (JRE/JDK)
  - Recommandé: Microsoft Build of OpenJDK 21 ou Eclipse Temurin 21
  - Téléchargement:
    - Microsoft OpenJDK: https://learn.microsoft.com/java/openjdk/download
    - Eclipse Temurin: https://adoptium.net/

  - Après installation, vérifiez dans l’invite de commandes (cmd):

```bat
java -version
```

La version doit indiquer 21.x.
  - Si la version n'est pas 21, passez au point 8 du README

- (Optionnel pour compilation locale) Apache Maven 3.9+
  - https://maven.apache.org/download.cgi
  - Vérifier:

```bat
mvn -version
```


## 2) Lancer l’application à partir du JAR

Le build Maven produit un JAR "avec dépendances" prêt à l’emploi: `appli_pre_etudes-0.0.1-SNAPSHOT-jar-with-dependencies.jar`.

- Double-clic (si l’association .jar est configurée) OU en ligne de commande:

```bat
cd C:\Users\user\Downloads\appli_pre_etudes\appli_pre_etudes\target
java -jar appli_pre_etudes-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

Notes:
- Le JAR standard sans dépendances (`appli_pre_etudes-0.0.1-SNAPSHOT.jar`) nécessite un classpath complet; préférez le `-jar-with-dependencies`.
- L’application ouvre une interface graphique (Swing).
- En cas d’échec par double-clic, lancez via `cmd` pour voir les messages (pratique pour le diagnostic).


## 3) Compiler et exécuter avec Maven (mode développeur)

Depuis la racine du projet:

- Compiler + tests + packaging (produit les JAR dans `target/`):

```bat
mvn clean package
```

- Exécuter l’application (2 options):
  - Option A (exécuter le JAR assemblé):

```bat
java -jar target\appli_pre_etudes-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

  - Option B (exécution via Maven Exec):

```bat
mvn -DskipTests exec:java -Dexec.mainClass=main.Main
```

- Générer un JAR exécutable "avec dépendances" explicitement (si besoin):

```bat
mvn clean package -DskipTests
```

Cela utilise le `maven-assembly-plugin` configuré pour produire `appli_pre_etudes-0.0.1-SNAPSHOT-jar-with-dependencies.jar` avec `Main-Class: main.Main`.


## 4) Répertoires utiles

- `src/main/java` — code source
- `ressources/` et `src/main/resources/` — ressources embarquées (images, etc.)
- `docs/uml/` — diagrammes techniques (PlantUML)
- `target/` — artefacts de build (JAR, rapports)


## 5) Accéder à la JavaDoc (API)

Deux options:

- JavaDoc pré-générée (si présente dans ce dépôt):

```bat
start "" doc\index.html
```

- Générer la JavaDoc avec Maven puis ouvrir:

```bat
mvn javadoc:javadoc
start "" target\site\apidocs\index.html
```

Remarque: la génération est configurée via `maven-javadoc-plugin` (voir `pom.xml`).


## 6) Dépannage rapide (FAQ)

- "Aucune application Java associée" en double-clic
  - Lancez depuis `cmd` avec `java -jar ...`.

- "Java non reconnu"
  - Installez Java 21 et relancez l’invite de commandes. Vérifiez avec `java -version`.

- UI ne s’ouvre pas / erreur HeadlessException
  - Assurez-vous de ne pas forcer le mode headless (`-Djava.awt.headless=true`) quand vous utilisez l’UI.

- Réseau/Proxy
  - L’estimation PV utilise l’API PVGIS (HTTPs). Si vous êtes derrière un proxy, configurez Java avec:

```bat
java -Dhttps.proxyHost=mon-proxy -Dhttps.proxyPort=8080 -jar target\appli_pre_etudes-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```


## 7) Tests et couverture

- Lancer les tests:

```bat
mvn test
```

- Couverture ciblée (profil JaCoCo):

```bat
mvn verify -Pcoverage
```

Rapport: `target/site/jacoco/index.html`.

Important:
- Exécutez ces commandes depuis la racine du projet où se trouve le fichier `pom.xml` (chemin: `C:\Users\user\Downloads\appli_pre_etudes\appli_pre_etudes`).
- Si vous êtes dans un sous-répertoire (ex. `docs\rapport`), Maven ne trouve pas le POM et affichera: `The requested profile "coverage" could not be activated because it does not exist` suivi de `there is no POM in this directory`.
- Depuis un sous-dossier vous pouvez lancer quand même en ciblant le POM:

```bat
mvn -f ..\..\pom.xml verify -Pcoverage
```

Ou en utilisant un chemin absolu:
```bat
mvn -f C:\Users\user\Downloads\appli_pre_etudes\appli_pre_etudes\pom.xml verify -Pcoverage
```

Après exécution réussie, ouvrez le rapport JaCoCo:
```bat
start "" target\site\jacoco\index.html
```


## 8) Configurer JDK 21 (PATH / JAVA_HOME) sous Windows

Si plusieurs JDK sont installés, assurez-vous que Java 21 est prioritaire.

1) Vérifier quelle version est utilisée dans ce terminal:

```bat
java -version
where java
```

2) Utiliser JDK 21 pour la session courante (temporaire):

```bat
set "JAVA_HOME=C:\Program Files\Microsoft\jdk-21"
set "PATH=%JAVA_HOME%\bin;%PATH%"
java -version
```

3) Définir JDK 21 de façon permanente (recommandé):

- Ouvrir: Panneau de configuration > Système > Paramètres système avancés > Variables d’environnement
- Créer/modifier `JAVA_HOME` → `C:\Program Files\Microsoft\jdk-21` (ou Temurin: `C:\Program Files\Eclipse Adoptium\jdk-21`)
- Éditer `Path` → ajouter `%JAVA_HOME%\bin` tout en haut de la liste
- Ouvrir un nouveau `cmd` et vérifier `java -version`

Astuce:
- Sans changer le PATH, on peut invoquer explicitement:

```bat
"C:\Program Files\Microsoft\jdk-21\bin\java.exe" -jar target\appli_pre_etudes-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

Pour Maven, vérifiez la JVM utilisée:

```bat
mvn -v
```

La ligne `Java version` doit indiquer 21.x. Si besoin, relancez votre terminal après avoir ajusté `JAVA_HOME`/`Path`.


## 9) Licence et crédits

- Données PV via PVGIS (Commission européenne). Respecter les conditions d’utilisation du service.
- Bibliothèques: JSON (org.json), XChart, Apache PDFBox, Apache POI, JUnit/Mockito.

## 10) Idées d'amélioration :
- Rendre le logo en haute-définition
- Refaire les tables des fenêtres d'aide plutôt que de se contenter d'images
- Pour Surface, Puissance et Onduleurs, mettre une barre de défilement générale plutôt que d'en avoir une par table
- Faire des presets. Exemple : quand on clique sur une cellule "Modèle choisi" d'un tableau dans lequel il y a un choix d'élément à faire, afficher la liste des références et dès qu'on en choisit une, remplir automatiquement les cellules associées en-dessous (s'aider de la base de données BDD qui est déjà faite)
- Lier la touche ENTRÉE au bouton "Calculer"
- Aligner certains boutons "?" avec la ligne concernée, afin de supprimer de l'espace inutile entre par exemple K1, K2 et K3 
- Besoin d'appuyer 2 fois sur TAB (ou SHIFT + TAB) pour passer d'une zone de texte à l'autre lorsque celles-ci sont séparées par un bouton "?"
- La façon de créer chaque zone de texte dans la classe ZonesDeTexte est redondante, faire une méthode qui généralise tout
- Ajouter un bouton pour chaque colonne qui permet d'effacer son contenu pour les parties Surface et Onduleurs
- Ajouter un bouton pour tout vider