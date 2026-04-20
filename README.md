# ProgM_Projet
## Description

ProgM_Projet est une application Android développée dans le cadre d’un projet de programmation.
L’application propose un ensemble de mini-jeux jouables en mode solo ou en mode multijoueur.

Le mode multijoueur repose sur une communication Bluetooth permettant de synchroniser les parties entre deux appareils. Les joueurs peuvent ainsi lancer les mêmes jeux simultanément et comparer leurs scores à la fin d’une session.

---

## Fonctionnalités
- Mode solo permettant de jouer librement aux différents mini-jeux
- Mode "Partie rapide" enchaînant plusieurs jeux aléatoires
- Mode multijoueur via Bluetooth
- Synchronisation des parties entre deux appareils
- Comparaison des scores en fin de session
- Interface adaptée aux appareils Android

---

## Technologies utilisées

Langage : Kotlin

Environnement : Android Studio

Plateforme : Android

Communication : Bluetooth

---

## Installation
Cloner le dépôt :

git clone https://github.com/M4tt5/ProgM_Projet.git

Ouvrir le projet avec Android Studio

Laisser Android Studio synchroniser les dépendances Gradle

Connecter un appareil Android ou lancer un émulateur

---

## Exécution
Lancer l’application depuis Android Studio

Choisir un mode de jeu :
- Solo
- Partie rapide
- Multijoueur Bluetooth

---

## Mode multijoueur

Le mode multijoueur fonctionne via Bluetooth :

Un joueur agit en tant que serveur

L’autre joueur se connecte en tant que client

Le Bluetooth est utilisé uniquement pour :
- synchroniser le lancement des jeux
- transmettre les résultats

Les deux joueurs jouent ensuite localement sur leur appareil respectif.

---

## Objectifs du projet
- Développer une application Android complète
- Mettre en œuvre la programmation en Kotlin
- Implémenter une communication Bluetooth simple
- Concevoir une architecture modulaire pour des mini-jeux
- Gérer la synchronisation entre plusieurs appareils

---

### Auteurs
M4tt5
