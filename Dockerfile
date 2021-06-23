FROM openjdk:16-alpine

WORKDIR /app


COPY .env ./
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN source .env

COPY src ./src

RUN ./mvnw package -Dmaven.test.skip=true

RUN ./mvnw install

ENTRYPOINT ["java", "-jar", "target/mobile-app-ws-0.1.jar"]
