# Introduction

Dit project is de Springboot REST API voor de EigenSchuld applicatie

### Wat je nodig hebt
[Java 22](https://www.oracle.com/nl/java/technologies/downloads/) \
[PostgreSQL 16](https://www.postgresql.org/download/) \
[Frontend (Optioneel)](https://github.com/Spine-ngo/Eigenschuld-Zenith-Frontend) \
[Springboot 3.2.4](https://docs.spring.io/spring-boot/docs/3.2.4/reference/html/getting-started.html)

### Dependencies
In dit project maken we gebruiken van veel Springboot dependencies.
Daarnaast gebruiken we ook de dependency voor postgres en hebben we een dependency voor de JWT token.
Voor de specifieke versies bekijk de `pom.xml`.

### Installeren

Maak een database aan in PostgreSQL genaamd `eigenschuld`
Maak onder het mapje resources een secrets bestand aan genaamd `application.yml` hieronder staat welke keys je allemaal nodig hebt.

```yaml
database:
    url: "jdbc:postgresql://localhost:5432/EigenSchuld"
    username: {{DATABASE_USERNAME}}
    password: {{DATABASE_PASSWORD}}

keys:
    aes: {{AES_ENCRYPT_STRING}}
    jwt: {{JWT_SERCRET}}

email:
    host: {{MAIL_SERVER}}
    username: {{MAIL_USERNAME}}
    password: {{MAIL_PASSWORD}}
```


### Runnen
Als je Database aan staat dan kan je het project gaan starten. \
Je kan het project starten met het volgende bestand: ```EigenSchuldApiApplication``` \
Ga naar  ```EigenSchuldApiApplication``` en klik dan rechts boven op de groene play knop.

### Deployen
We hebben voor de API en voor de Frontend een `docker-compose.yml` gemaakt, deze ziet er als volgt uit:
```dockerfile
services:
  database:
    image: postgres:16-alpine
    restart: always
    environment:
      - POSTGRES_DB=eigenschuld
      - POSTGRES_USER=q032409pqw
      - POSTGRES_PASSWORD=ipD4MM5BwJcY14Z1k7tqF8x0M

  backend:
    build:
      context: ./EigenSchuld-Zenith-Backend/
      dockerfile: Dockerfile
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/eigenschuld
      - SPRING_DATASOURCE_USERNAME=q032409pqw
      - SPRING_DATASOURCE_PASSWORD=ipD4MM5BwJcY14Z1k7tqF8x0M
      - SERVER_PORT=8080
      - DEBUG=false
    depends_on:
      - database

  frontend:
    build:
      context: ./EigenSchuld-Zenith-FrontEnd/
      dockerfile: Dockerfile
    ports:
      - "4200:80"
    depends_on:
      - backend
```
Dit document moet worden toegevoegd aan de map waar de frontend en backend in staan als volgt:
```bash
├── docker-compose.yml
├── frontend
└── backend
```

Als dit allemaal staat dan kan je de containers bouwen dit doe je door `docker compose build` te runnen. Dit kan even duren doordat die nu alles aan het bouwen is dus ook alle packages aan het downloaden is. Als dit klaar is kan je de container starten door `docker compose up` te runnen. Nu kan je naar http://localhost:4200/ en hier zijn de site moeten staan