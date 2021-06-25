FROM openjdk:16-alpine

WORKDIR /app

COPY target/mobile-app-ws-0.1.jar .

ENTRYPOINT ["java", "-jar", "mobile-app-ws-0.1.jar"]
