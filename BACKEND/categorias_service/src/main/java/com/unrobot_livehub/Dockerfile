# ----- Etapa 1: Build -----
# Usamos una imagen de Maven con JDK 17 (ajusta la versión si usas otra)
FROM maven:3.8.5-openjdk-17 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de definición del proyecto
COPY pom.xml .

# Descargamos las dependencias (esto se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos la aplicación y generamos el .jar
RUN mvn package -DskipTests

# ----- Etapa 2: Run -----
# Usamos una imagen ligera de solo Java Runtime (JRE)
FROM eclipse-temurin:17-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos el .jar compilado desde la etapa 'build'
# OJO: La ruta del .jar puede cambiar según tu pom.xml (artifactId/version)
# Asumo que se llama 'registro-service-0.0.1-SNAPSHOT.jar'
# Ajusta este nombre si es diferente.
COPY --from=build /app/target/registro-service-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto en el que corre Spring Boot (por defecto 8080)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]