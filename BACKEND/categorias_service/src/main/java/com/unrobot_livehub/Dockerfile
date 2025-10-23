# ----- Etapa 1: Build -----
# CAMBIO: Usamos una imagen de Maven con JDK 21
FROM maven:3.8-openjdk-21 AS build

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
# CAMBIO: Usamos una imagen ligera de solo Java Runtime (JRE) 21
FROM eclipse-temurin:21-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos CUALQUIER .jar de la carpeta target.
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto en el que corre Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]