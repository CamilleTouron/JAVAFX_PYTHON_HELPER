# Python Helper 
Python Helper est un outil d'assistance pour les développeurs travaillant avec Python. Il permet de vérifier les versions de Python installées sur machine, de choisir une version à installer et de détecter des clés USB. Ce guide est une explication détaillée des fonctionnalités, des composants, des instructions pour exécuter l'application sur Windows et Linux, ainsi que des informations sur les difficultés rencontrés.

## Technologies 
- JavaFX
- JDK 17
- Maven

## Fonctionnalités
- Vérification de la version de Python installée
- Liste des versions disponibles de Python
- Sélection d'une version de Python à installer
- Installation de Python
- Détection des clés USB connectées
- Ecriture, lecture et execution de script Python

## Comment lancer l'application sur Windows
- Executer pythonhelper.exe
ou 
- Utiliser le launch4j.xml via l'application Lauch4j.

## Comment lancer l'application sur Linux
- Executer le script build.sh

## Difficultés du code
- La gestion des différences entre les systèmes d'exploitation (Linux et Windows) pour vérifier et installer les versions de Python.
- La gestion des exceptions lors de l'exécution des commandes système pour vérifier et installer les versions de Python.
- La détection des clés USB connectées en fonction du système d'exploitation.
- Utilisation de JavaFX pour créer l'interface utilisateur graphique peut être difficile.
- La mise en place de l'utilisation de Maven et de Lauch4j.

## Compétences visant la validation du modul de DEV DESKTOP au sein de Ynov Campus
### Maitriser un langage ou un framework de développement d'application desktop
Utilisation de Java 17 avec Eclipse. La difficulté a été d'implémenter Maven avec JavaFX pour réussir j'ai du installer JavaFX à Eclipse via le MarketPlace et ajouter des options de commandes à la JVM. Utilisation d'héritatage et d'abstraction.
- pom.xml
- lauch4j.xml
### Implémenter une interface graphique dans votre application à l'aide d'un toolkit graphique
Utilisation de JavaFX, créer des classes qui hérites de classes spécifiques à JavaFX comme HBox ou VBox.
- ExportBox.java
- MainBox.java
- Main.java
### Lire, écrire des fichiers sur la machine hôte
Lecture, ecriture et execution de script python.
- NewScriptBox.java
### Effectuer des communications réseaux (HTTP ou TCP) depuis une application desktop
Téléchargement des zip pour l'installation de python.
- PythonInstaller.java
- PythonVersionBox.java
### Communiquer avec un périphérique physique de la machine (USB et liaison série)
Détection d'usb et écriture dessus.
- ExportBox.java
- Usb.java
- UsbDetector.java
### Intégrer une webview dans une application desktop et configurer des interactions entre la webview le programme hôte
Affichage de la page officiel de téléchargement de python pour récupérer les 6 dernières versions de python et permettre de les installer.
- PythonVersionBox.java
### Exécuter des commandes systèmes ou des exécutables depuis une application desktop 
Detection de la version de python, intallation de python et detection d'usb via commande cmd pour windows et shell pour linux.
- PythonInstaller.java
- UsbDetector.java
### Savoir mettre en place un système de mise à jour adapté aux besoins
Mise en place d'un onglet permetant l'installation au choix d'une version de python.
- PythonInstaller.java
### Créer une application packagée (installable ou portable) pour au moins deux systèmes d'exploitation différent
Création d'un executable avec Lauch4j pour windows et d'un script exploitant le jar créé par Maven pour Linux :
- pythonhelper.exe
- build.sh
