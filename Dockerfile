FROM openjdk:16-alpine

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

COPY src ./src

RUN ./mvnw install

CMD ["java", "-jar", "target/mobile-app-ws-0.1.jar"]
