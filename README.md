# PicPay Simplificado

PicPay Simplificado est une plateforme de paiement simplifiée permettant de déposer et transférer de l'argent entre utilisateurs. Il y a deux types d'utilisateurs : les utilisateurs normaux et les commerçants, chacun possédant un portefeuille pour les transferts d'argent.

## Prérequis

Pour chaque type d'utilisateur, nous avons besoin des informations suivantes : 
- Nom complet 
- CPF 
- Email 
- Mot de passe 
Le CPF/CNPJ et les e-mails doivent être uniques dans le système. Ainsi, chaque utilisateur doit avoir un CPF ou une adresse e-mail unique.

## Fonctionnalités

- Les utilisateurs peuvent envoyer de l'argent (effectuer des transferts) aux commerçants et aux autres utilisateurs.
- Les commerçants ne peuvent que recevoir de l'argent ; ils ne peuvent pas en envoyer.
- Validation du solde avant la réalisation du transfert.
- Consultation d'un service d'autorisation externe avant de finaliser une transaction (utilisez le mock : [Service d'autorisation](https://util.devi.tools/api/v2/authorize)).
- Notification de réception de paiement via un service tiers (utilisez le mock : [Service de notification](https://util.devi.tools/api/v1/notify)).

## Requêtes API

### Transfert d'argent

- **Endpoint** : `POST /transfer`
- **Content-Type** : `application/json`

```json
{
  "value": 100.0,
  "payer": 4,
  "payee": 15
}
```

## Technologies utilisées

- **Java Spring Boot** : Pour la création du backend.
- **H2 Database** : Pour la gestion de la base de données.
- **Insomnia** : Pour tester les requêtes POST et GET.

## Comment lancer le projet

1. Clonez le dépôt
   ```bash
   git clone https://github.com/votre-utilisateur/picpay-simplificado.git
   ```
2. Naviguez dans le répertoire du projet
   ```bash
   cd picpay-simplificado
   ```
3. Lancez l'application
   ```bash
   ./mvnw spring-boot:run
   ```
4. Testez les endpoints via Insomnia

## Crédit
Ce projet est basé sur le défi backend de PicPay. Vous pouvez trouver plus de détails et l'original ici : PicPay Backend Challenge (https://github.com/PicPay/picpay-desafio-backend).
