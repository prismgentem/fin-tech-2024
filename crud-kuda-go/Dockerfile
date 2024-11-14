FROM openjdk:17-jdk-slim

WORKDIR /app

COPY crud-kuda-go/build/libs/crud-kuda-go.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]