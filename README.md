# Mini-Projet Spring Boot - Gestion des Utilisateurs

## Description
Ce projet est une API REST construite avec **Spring Boot** qui permet de :
- Générer une liste d'utilisateurs fictifs sous format JSON.
- Importer ces utilisateurs dans une base de données avec vérification des doublons.
- Authentifier un utilisateur et générer un **JWT Token**.
- Consulter son propre profil utilisateur.
- Consulter le profil d'un autre utilisateur si le rôle **admin** est attribué.

---

## Technologies utilisées
- **Java 8+**
- **Spring Boot (Spring Security, Spring Data JPA, Spring Web)**
- **JWT (JSON Web Token)** pour l'authentification
- **H2 Database** (base de données en mémoire)
- **Lombok** (réduction du code boilerplate)
- **SpringDoc OpenAPI** pour la documentation Swagger
- **JUnit 5 & Mockito** pour les tests unitaires

---

## Installation et Configuration

### Prérequis
- **Java Development Kit 23** installé
- **Maven**

### Étapes d'installation
1. **Cloner le projet**
```bash
  git clone https://github.com/Mousta-Med/user-API.git
```
2. **Accéder au projet**
```bash
cd mini-projet-springboot
```
3. **Construire et exécuter l'application**
```bash ou importer le projet s
mvn clean install
mvn spring-boot:run
```
4. **Accéder à l'API Swagger**
```
http://localhost:9090/swagger-ui
```

---

## Contraintes et Bonnes Pratiques
- Utilisation de **Spring Security** et JWT pour sécuriser les endpoints.
- Vérification des **doublons** (email, username) avant insertion.
- **Encodage des mots de passe** avec `BCryptPasswordEncoder`.
- Documentation API avec **Swagger UI**.
- Utilisation de **H2 Database** pour simplifier le développement.
- **Tests unitaires** avec `JUnit 5` et `Mockito`.

---
