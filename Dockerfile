FROM openjdk:17-jdk-slim

WORKDIR /app

COPY spring-petclinic/app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
