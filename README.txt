# E-AUBONNE pré-études 1.0.0

## Présentation

Cette application est conçue pour standardiser les calculs lors des pré-études 
photovoltaïques. Elle intègre les données relatives aux toitures, modules, onduleurs, 
câbles et systèmes de protection, permettant aux utilisateurs d'obtenir des résultats 
précis et utiles.

## Pré-requis

- Système d’exploitation : Windows
- JRE : Java™ SE Runtime Environment (22.0.2 ou plus récent)

Idées d'amélioration :
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