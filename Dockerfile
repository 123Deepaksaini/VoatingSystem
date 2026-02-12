FROM maven:3.9.9-eclipse-temurin-8 AS build
WORKDIR /app

COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

COPY src ./src
COPY WebContent ./WebContent
COPY .classpath ./
COPY .project ./

RUN mvn -q -DskipTests clean package

FROM eclipse-temurin:8-jre
WORKDIR /app

ENV PORT=8080

COPY --from=build /app/target/VotingSystem-1.0.jar /app/app.jar
COPY --from=build /app/WebContent /app/WebContent

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
