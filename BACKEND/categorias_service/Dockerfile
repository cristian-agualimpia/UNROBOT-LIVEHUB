# ----- Etapa 1: Build -----
# CORRECCIÓN: Usar una etiqueta específica y disponible
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de definición del proyecto
COPY pom.xml .

# Descargamos las dependencias
RUN mvn dependency:go-offline -B

# Copiamos el resto del código fuente
COPY src ./src

# Compilamos la aplicación y generamos el .jar
RUN mvn package -DskipTests

# ----- Etapa 2: Run -----
# CORRECCIÓN: Usar etiqueta específica para Temurin
FROM eclipse-temurin:21.0.2_13-jre-jammy

# Establecemos el directorio de trabajo
WORKDIR /app

# CORRECCIÓN: Copiar el JAR con nombre específico
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto en el que corre Spring Boot
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]