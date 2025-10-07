# Dockerfile genérico para cada servicio
FROM openjdk:11-jre-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]