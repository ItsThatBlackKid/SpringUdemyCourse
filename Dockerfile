FROM openjdk:16-alpine

WORKDIR /app


COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN dos2unix mvnw

COPY src ./src

RUN ./mvnw dependency:go-offline

CMD ["./mvnw", "spring-boot:run"]
