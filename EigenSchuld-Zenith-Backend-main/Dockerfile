# Build stap
FROM maven:sapmachine as build
WORKDIR /app
COPY ./src ./src
COPY ./pom.xml .

RUN mvn clean install -Dmaven.test.skip=true

# Run stap
FROM openjdk:22-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]

