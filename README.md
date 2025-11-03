# 🚚 Delivery Tour Optimizer

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=spring-boot)
![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue?style=flat-square&logo=apache-maven)
![H2](https://img.shields.io/badge/Database-H2-blue?style=flat-square)
![License](https://img.shields.io/badge/License-Educational-yellow?style=flat-square)

**Application d'optimisation de tournées de livraison avec configuration Spring Boot 100% XML**

[Démarrage rapide](#-démarrage-rapide) • [API](#-api-rest) • [Architecture](#-architecture) • [Contribution](#-contribution)

</div>

---

## 📖 À propos du projet

**Delivery Tour Optimizer** est un service Spring Boot de démonstration qui optimise les tournées de livraison à l'aide d'algorithmes classiques :

- 🎯 **Nearest Neighbor** : Sélection du point de livraison le plus proche
- 💡 **Clarke-Wright** : Optimisation par économies de distance

### 🎓 Objectif pédagogique

Ce projet illustre une approche **sans annotations d'injection** (`@Autowired`, `@Service`, `@Repository`). Tous les beans Spring sont configurés de manière déclarative dans `applicationContext.xml`, démontrant ainsi la configuration XML traditionnelle avec des technologies modernes.

### ✨ Caractéristiques

- ✅ Configuration 100% XML (pas d'annotations d'injection)
- ✅ Spring Data JPA avec H2 en mémoire
- ✅ API REST complète (CRUD + optimisation)
- ✅ Algorithmes d'optimisation interchangeables
- ✅ Console H2 intégrée pour l'exploration des données

---

## 🏗️ Architecture

### Diagramme UML

> 📊 **Ajouter votre diagramme UML ici**
>
> ```
> ![Diagramme UML](uml.png)
> ```
>
> _Placez votre image dans le dossier `docs/` et décommentez la ligne ci-dessus_

### Structure du projet

```
delivery-tour-optimizer/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── controller/          # REST Controllers
│   │   │   │   ├── DeliveryController.java
│   │   │   │   ├── WarehouseController.java
│   │   │   │   └── TourController.java
│   │   │   ├── model/               # Entités JPA
│   │   │   │   ├── Delivery.java
│   │   │   │   └── Warehouse.java
│   │   │   ├── repository/          # Spring Data Repositories
│   │   │   │   ├── DeliveryRepository.java
│   │   │   │   └── WarehouseRepository.java
│   │   │   ├── service/             # Services métier
│   │   │   │   ├── DeliveryService.java
│   │   │   │   ├── WarehouseService.java
│   │   │   │   └── TourOptimizerService.java
│   │   │   └── optimizer/           # Algorithmes
│   │   │       ├── NearestNeighborOptimizer.java
│   │   │       └── ClarkeWrightOptimizer.java
│   │   └── resources/
│   │       ├── applicationContext.xml     # ⚙️ Configuration Spring
│   │       └── application.properties     # Propriétés
│   └── test/                              # Tests unitaires
├── docs/                                  # Documentation & UML
├── pom.xml
└── README.md
```

### Technologies utilisées

| Technologie         | Version | Usage                       |
| ------------------- | ------- | --------------------------- |
| **Java**            | 17      | Langage de programmation    |
| **Spring Boot**     | 3.x     | Framework principal         |
| **Spring Data JPA** | 3.x     | Couche de persistance       |
| **H2 Database**     | 2.x     | Base de données en mémoire  |
| **Maven**           | 3.6+    | Gestionnaire de dépendances |

---

## 🚀 Démarrage rapide

### Prérequis

- ☕ Java 17 ou supérieur
- 📦 Maven 3.6+
- 💻 PowerShell (pour les exemples Windows) ou terminal Unix

### Installation et lancement

```bash
# 1. Cloner le projet
git clone https://github.com/votre-username/delivery-tour-optimizer.git
cd delivery-tour-optimizer

# 2. Compiler et lancer
mvn clean install
mvn spring-boot:run
```

✅ L'application démarre sur **http://localhost:8080**

### Accès à la console H2

🗄️ **URL** : http://localhost:8080/h2-console

**Paramètres de connexion** :
| Champ | Valeur |
|-------|--------|
| JDBC URL | `jdbc:h2:mem:testdb` |
| Username | `sa` |
| Password | _(vide)_ |

---

## 📡 API REST

### Endpoints disponibles

#### 🏢 Warehouses (Entrepôts)

| Méthode | Endpoint          | Description              |
| ------- | ----------------- | ------------------------ |
| `GET`   | `/api/warehouses` | Liste tous les entrepôts |
| `POST`  | `/api/warehouses` | Crée un nouvel entrepôt  |

#### 📦 Deliveries (Livraisons)

| Méthode  | Endpoint               | Description                 |
| -------- | ---------------------- | --------------------------- |
| `GET`    | `/api/deliveries`      | Liste toutes les livraisons |
| `POST`   | `/api/deliveries`      | Crée une nouvelle livraison |
| `PUT`    | `/api/deliveries/{id}` | Met à jour une livraison    |
| `DELETE` | `/api/deliveries/{id}` | Supprime une livraison      |

#### 🗺️ Tours (Tournées)

| Méthode | Endpoint              | Description          |
| ------- | --------------------- | -------------------- |
| `POST`  | `/api/tours/optimize` | Optimise une tournée |

---

## 💻 Exemples d'utilisation

### 1️⃣ Créer un entrepôt

```powershell
curl.exe -X POST http://localhost:8080/api/warehouses `
  -H "Content-Type: application/json" `
  -d '{
    "name": "Entrepôt Principal",
    "latitude": 33.5731,
    "longitude": -7.5898,
    "openingHours": "08:00-18:00"
  }'
```

**Réponse** :

```json
{
  "id": 1,
  "name": "Entrepôt Principal",
  "latitude": 33.5731,
  "longitude": -7.5898,
  "openingHours": "08:00-18:00"
}
```

### 2️⃣ Créer des livraisons

```powershell
curl.exe -X POST http://localhost:8080/api/deliveries `
  -H "Content-Type: application/json" `
  -d '{
    "latitude": 33.5850,
    "longitude": -7.6000,
    "weight": 2.5,
    "volume": 0.2,
    "status": "PENDING"
  }'
```

### 3️⃣ Optimiser une tournée

```powershell
curl.exe -X POST http://localhost:8080/api/tours/optimize `
  -H "Content-Type: application/json" `
  -d '{
    "warehouseId": 1,
    "deliveryIds": [1, 2, 3, 4],
    "optimizer": "NEAREST"
  }'
```

**Paramètres `optimizer` disponibles** :

- `NEAREST` - Algorithme du plus proche voisin
- `CLARKE_WRIGHT` - Algorithme Clarke-Wright

**Réponse** : Liste ordonnée des livraisons formant la tournée optimisée

### 4️⃣ Lister toutes les livraisons

```powershell
curl.exe -X GET http://localhost:8080/api/deliveries
```

---

## ⚙️ Configuration

### Modifier le port

Dans `src/main/resources/application.properties` :

```properties
server.port=8080
```

### Utiliser une base H2 persistante

```properties
spring.datasource.url=jdbc:h2:file:./data/deliverydb
spring.jpa.hibernate.ddl-auto=update
```

### Configuration XML

Tous les beans sont déclarés dans `src/main/resources/applicationContext.xml` :

```xml
<!-- DataSource -->
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    ...
</bean>

<!-- EntityManagerFactory -->
<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
    ...
</bean>

<!-- Repositories -->
<jpa:repositories base-package="com.example.demo.repository" />
```

---

## 📝 Notes importantes

> ⚠️ **Configuration XML pure**
>
> Ce projet n'utilise **aucune annotation d'injection** (`@Autowired`, `@Service`, `@Repository`, `@Controller`) pour les beans métier. Cette approche permet de comprendre la configuration déclarative traditionnelle de Spring.

- 📌 Tous les beans sont dans `applicationContext.xml`
- 📌 Les repositories Spring Data sont activés via `<jpa:repositories/>`
- 📌 Adaptez les IDs dans vos requêtes selon les réponses de l'API

---

## 🎯 Roadmap

- [ ] Tests unitaires pour les algorithmes d'optimisation
- [ ] Tests d'intégration pour l'API REST
- [ ] Script de seed avec données de démonstration
- [ ] Contraintes de validation (capacité véhicule, fenêtres horaires)
- [ ] Algorithmes avancés (génétique, recuit simulé, colonies de fourmis)
- [ ] Interface web de visualisation des tournées
- [ ] Export des tournées (PDF, Excel)
- [ ] Calcul du coût et de la durée des tournées
- [ ] Support multi-véhicules

---

## 🤝 Contribution

Les contributions sont les bienvenues ! Pour contribuer :

1. 🍴 Fork le projet
2. 🔨 Créez votre branche (`git checkout -b feature/amazing-feature`)
3. 💾 Committez vos changements (`git commit -m 'Add amazing feature'`)
4. 📤 Push vers la branche (`git push origin feature/amazing-feature`)
5. 🔃 Ouvrez une Pull Request

### Guide de style

- ✅ Suivez les conventions Java standard
- ✅ Commentez le code complexe
- ✅ Ajoutez des tests pour les nouvelles fonctionnalités
- ✅ Mettez à jour la documentation si nécessaire

---

## 📄 Licence

Ce projet est un **exemple pédagogique** à des fins éducatives.

---

## 👥 Auteurs

Créé avec ❤️ pour démontrer la configuration XML de Spring Boot

---

## 📚 Ressources utiles

- 📖 [Documentation Spring Boot](https://spring.io/projects/spring-boot)
- 📖 [Spring Data JPA Reference](https://spring.io/projects/spring-data-jpa)
- 📖 [Algorithmes d'optimisation de tournées](https://en.wikipedia.org/wiki/Vehicle_routing_problem)
- 📖 [Guide de configuration XML Spring](https://docs.spring.io/spring-framework/reference/core/beans/basics.html)

---

<div align="center">

**⭐ N'hésitez pas à mettre une étoile si ce projet vous a été utile !**

Fait avec ☕ et Spring Boot

</div>
