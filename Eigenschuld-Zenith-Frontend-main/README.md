# Introduction

Dit is de frontend Angular applicatie voor de EigenSchuld applicatie

### Wat je nodig hebt
- [Node JS](https://nodejs.org/en/download/package-manager)
- [Backend](https://github.com/Spine-ngo/EigenSchuld-Zenith-Backend)

### Dependencies
In dit project gebruiken wij verschillende dependencies, om te zien welke dependencies we gebruiken kijk dan in de `pom.xml`

### Installeren

Open het project in je terminal en run `npm install`

### Runnen
1. Start de backend op, als je niet weet hoe dit werkt lees de Reame van het backend project. 
2. Open het project in je terminal en run `ng serve`
3. De applicatie staat nu aan en is te bereiken op http://localhost:4200

### Deployen
We hebben voor de Frontend en voor de API/Backend een `docker-compose.yml` gemaakt, deze ziet er als volgt uit:
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
