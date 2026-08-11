# UNROBOT-LIVEHUB

Plataforma backend para la gestión de información de participantes, atletas/robots y servicios asociados a **UNRobot / CEIMTUN**, construida bajo una arquitectura de microservicios.

El proyecto está diseñado para separar las responsabilidades del sistema en servicios independientes y permitir posteriormente su despliegue mediante Docker y Kubernetes.

---

## 1. Estado actual del proyecto

### Implementado / configurado

* Backend basado en **Spring Boot 2.7**.
* Arquitectura orientada a microservicios.
* Servicio de registro (`registro_service`).
* Persistencia mediante **PostgreSQL**.
* ORM mediante **Spring Data JPA / Hibernate**.
* Base de datos PostgreSQL dockerizada.
* Configuración de conexión entre Spring Boot ejecutándose localmente y PostgreSQL ejecutándose en Docker.
* Puerto HTTP del servicio de registro: `8081`.
* Docker Compose para levantar PostgreSQL.
* Estructura preparada para incorporar:

  * Spring Cloud Gateway.
  * Keycloak.
  * Apache Kafka.
  * Frontend Angular/Ionic.
  * Docker.
  * Kubernetes.

### Pendiente de desarrollo / consolidación

* Completar los demás microservicios.
* Definir y documentar todos los endpoints REST.
* Consolidar autenticación y autorización mediante Keycloak.
* Incorporar comunicación/eventos mediante Kafka.
* Configurar Spring Cloud Gateway.
* Completar frontend.
* Crear configuración Docker para todos los servicios.
* Crear infraestructura de despliegue mediante Kubernetes.
* Documentar variables de entorno y secretos.
* Crear pruebas automatizadas.
* Documentar completamente el contrato de las APIs.

---

# 2. Arquitectura general

La arquitectura propuesta es:

```text
                         ┌──────────────────────┐
                         │      Frontend        │
                         │   Angular / Ionic    │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │  Spring Cloud       │
                         │      Gateway         │
                         └──────────┬───────────┘
                                    │
              ┌─────────────────────┼─────────────────────┐
              │                     │                     │
              ▼                     ▼                     ▼
      ┌───────────────┐     ┌───────────────┐     ┌───────────────┐
      │ Registro      │     │  Otros        │     │  Otros        │
      │ Service       │     │ Microservicios│     │ Microservicios│
      │ :8081         │     │               │     │               │
      └───────┬───────┘     └───────────────┘     └───────────────┘
              │
              ▼
      ┌────────────────┐
      │   PostgreSQL   │
      │   :5432        │
      └────────────────┘

              ┌──────────────────────┐
              │       Keycloak       │
              │ Autenticación/AuthZ  │
              └──────────────────────┘

              ┌──────────────────────┐
              │        Kafka         │
              │ Comunicación eventos │
              └──────────────────────┘
```

La idea principal es que cada microservicio sea responsable de su propio dominio y pueda evolucionar o desplegarse independientemente.

---

# 3. Backend

## 3.1 Registro Service

El primer servicio desarrollado es:

```text
registro_service
```

Configuración actual:

```properties
spring.application.name=registro_service
server.port=8081
```

El servicio está desarrollado utilizando:

* Java 11
* Spring Boot 2.7.0
* Spring Data JPA
* Hibernate
* PostgreSQL
* Maven

Durante el desarrollo se ejecuta directamente desde Visual Studio Code, mientras PostgreSQL se ejecuta mediante Docker.

---

# 4. Base de datos

## PostgreSQL

La base de datos utilizada actualmente es:

```text
PostgreSQL 13
```

La configuración Docker actual es:

```yaml
version: '3.8'

services:
  postgres-db:
    image: postgres:13
    container_name: unrobot-postgres
    environment:
      POSTGRES_DB: robotics_platform
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - unrobot_data:/var/lib/postgresql/data

volumes:
  unrobot_data:
```

### Parámetros de conexión

| Parámetro                | Valor               |
| ------------------------ | ------------------- |
| Host                     | `localhost`         |
| Puerto                   | `5432`              |
| Base de datos            | `robotics_platform` |
| Usuario                  | `admin`             |
| Contraseña de desarrollo | `password`          |

La aplicación Spring Boot utiliza:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/robotics_platform
spring.datasource.username=admin
spring.datasource.password=password
```

---

# 5. Persistencia mediante JPA

Actualmente Hibernate está configurado para actualizar automáticamente el esquema:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Esto permite que durante el desarrollo las tablas necesarias sean creadas/modificadas a partir de las entidades JPA.

También se utiliza:

```properties
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Importante para producción

`ddl-auto=update` es apropiado para desarrollo, pero no debería utilizarse como mecanismo principal de migración en producción.

Para una versión productiva se recomienda utilizar una herramienta de migraciones como:

* Flyway
* Liquibase

De esta manera las modificaciones del esquema quedan versionadas junto con el código.

---

# 6. Persistencia y entidades

Los microservicios utilizan el patrón:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
JPA / Hibernate
    ↓
PostgreSQL
```

Por ejemplo:

```text
HTTP Request
     │
     ▼
@RestController
     │
     ▼
@Service
     │
     ▼
@Repository
     │
     ▼
PostgreSQL
```

Los repositorios utilizan Spring Data JPA, evitando tener que implementar manualmente las operaciones CRUD básicas.

---

# 7. Endpoints del backend

## Registro Service

El servicio de registro contiene actualmente **4 interfaces Repository**, de acuerdo con la última ejecución registrada de Spring Boot.

La API REST debe documentarse directamente a partir de los `@RestController` existentes.

### Estructura esperada

```text
/api/...
```

Los endpoints definitivos deben registrarse en esta sección a partir de las rutas implementadas actualmente.

> **IMPORTANTE:** No se deben inventar endpoints en esta documentación. Esta sección debe completarse revisando las clases `@RestController` del repositorio y anotando:
>
> * Método HTTP.
> * Ruta.
> * Parámetros.
> * Body.
> * Respuesta.
> * Código HTTP.
> * Requisitos de autenticación.
> * Ejemplo de request.
> * Ejemplo de response.

### Tabla de endpoints

| Método   | Endpoint  | Descripción        | Estado        |
| -------- | --------- | ------------------ | ------------- |
| `GET`    | Pendiente | Consultar recurso  | 🟡 Documentar |
| `POST`   | Pendiente | Crear recurso      | 🟡 Documentar |
| `PUT`    | Pendiente | Actualizar recurso | 🟡 Documentar |
| `DELETE` | Pendiente | Eliminar recurso   | 🟡 Documentar |

Esta tabla debe sustituirse por el inventario real de endpoints del código antes de considerar terminada la documentación de la API.

---

# 8. Configuración de Spring Boot

Archivo:

```text
src/main/resources/application.properties
```

Configuración actual:

```properties
spring.application.name=registro_service

# Server Configuration
server.port=8081

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/robotics_platform
spring.datasource.username=admin
spring.datasource.password=password

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true

# Logging
logging.level.com.robotics.registration=DEBUG
logging.level.org.hibernate.SQL=DEBUG
```

---

# 9. Logging

Durante el desarrollo se mantiene habilitado el logging de SQL:

```properties
spring.jpa.show-sql=true
logging.level.org.hibernate.SQL=DEBUG
```

Esto permite observar las consultas que Hibernate genera contra PostgreSQL.

También existe logging específico para el paquete de la aplicación:

```properties
logging.level.com.robotics.registration=DEBUG
```

En producción se debería reducir el nivel de logging y evitar imprimir información sensible.

---

# 10. Docker

Actualmente Docker se utiliza principalmente para PostgreSQL.

El objetivo de la dockerización completa es llegar a:

```text
Frontend
    ↓
Gateway
    ↓
Microservicios
    ↓
Bases de datos

       +
       
Keycloak
Kafka
```

Cada componente debería poder ejecutarse como un contenedor independiente.

## PostgreSQL

Para levantar PostgreSQL:

```bash
docker compose up -d
```

Para comprobar los contenedores:

```bash
docker ps
```

Para consultar los logs:

```bash
docker logs unrobot-postgres
```

Para detener los servicios:

```bash
docker compose down
```

Para reiniciarlos:

```bash
docker compose up -d
```

---

# 11. Verificación de PostgreSQL

Una señal importante en los logs es:

```text
database system is ready to accept connections
```

Esto significa que PostgreSQL terminó su proceso de inicialización y está aceptando conexiones.

También debería aparecer:

```text
0.0.0.0:5432->5432/tcp
```

al ejecutar:

```bash
docker ps
```

Esto significa que el puerto 5432 del contenedor está publicado en el puerto 5432 de Windows.

---

# 12. Conexión mediante pgAdmin

No es necesario tener pgAdmin abierto para que PostgreSQL funcione.

La arquitectura es:

```text
Spring Boot
     │
     │ JDBC
     ▼
localhost:5432
     │
     ▼
Docker
     │
     ▼
PostgreSQL
```

pgAdmin es únicamente una herramienta cliente para administrar/visualizar PostgreSQL.

Para conectarse desde pgAdmin:

```text
Host:                  localhost
Port:                  5432
Maintenance database:  robotics_platform
Username:              admin
Password:              password
```

También puede utilizarse:

```text
127.0.0.1
```

como host.

---

# 13. Ejecución local completa

## Requisitos

Para ejecutar el proyecto localmente se necesita:

* Windows
* Git
* Docker Desktop
* JDK 11
* Maven, o Maven Wrapper incluido en el proyecto
* Visual Studio Code
* Extensiones de Java/Spring recomendadas
* Opcional: pgAdmin 4

No es necesario instalar PostgreSQL directamente en Windows si se utiliza el contenedor Docker.

---

# 14. Clonar el proyecto

Desde una terminal:

```bash
git clone <URL_DEL_REPOSITORIO>
```

Entrar al proyecto:

```bash
cd UNROBOT-LIVEHUB
```

---

# 15. Levantar PostgreSQL

Desde el directorio donde se encuentra `docker-compose.yml`:

```bash
docker compose up -d
```

Verificar:

```bash
docker ps
```

Debe aparecer:

```text
unrobot-postgres
```

y el puerto:

```text
5432->5432
```

También se pueden revisar los logs:

```bash
docker logs unrobot-postgres
```

Esperar hasta encontrar:

```text
database system is ready to accept connections
```

---

# 16. Ejecutar el backend

Entrar al directorio del servicio:

```bash
cd BACKEND/registro_service
```

Si el proyecto utiliza Maven Wrapper:

### Windows

```bash
mvnw.cmd spring-boot:run
```

Alternativamente, desde Visual Studio Code se puede ejecutar la clase:

```text
RegistroServiceApplication
```

La aplicación debería iniciar en:

```text
http://localhost:8081
```

---

# 17. Orden recomendado de inicio

Para desarrollo local:

```text
1. Docker Desktop
       ↓
2. PostgreSQL
       ↓
3. Verificar PostgreSQL
       ↓
4. Spring Boot / registro_service
       ↓
5. Gateway
       ↓
6. Otros microservicios
       ↓
7. Frontend
```

PostgreSQL debe estar disponible antes de iniciar los servicios que dependen de él.

---

# 18. Diagnóstico de problemas

## PostgreSQL no inicia

Ejecutar:

```bash
docker ps
```

y:

```bash
docker logs unrobot-postgres
```

Buscar:

```text
database system is ready to accept connections
```

---

## Spring Boot no puede conectarse a PostgreSQL

Si aparece:

```text
FATAL: password authentication failed for user "admin"
```

el servidor PostgreSQL **sí está siendo alcanzado**, pero las credenciales no coinciden.

Revisar:

```properties
spring.datasource.username=admin
spring.datasource.password=password
```

y compararlas con la configuración con la que se inicializó PostgreSQL.

### Atención con los volúmenes

PostgreSQL solamente utiliza:

```yaml
POSTGRES_USER
POSTGRES_PASSWORD
POSTGRES_DB
```

durante la **inicialización del cluster**.

Si ya existe el volumen:

```text
unrobot_data
```

cambiar esas variables en `docker-compose.yml` no necesariamente cambia las credenciales de una base existente.

Para una instalación de desarrollo que pueda eliminarse completamente:

```bash
docker compose down -v
```

y posteriormente:

```bash
docker compose up -d
```

**ADVERTENCIA:** `down -v` elimina el volumen y, por tanto, los datos almacenados en PostgreSQL.

---

## Error: database "admin" does not exist

Si se ejecuta:

```bash
docker exec -it unrobot-postgres psql -U admin
```

PostgreSQL puede intentar conectarse a una base de datos llamada `admin`.

Pero en nuestro Compose la base de datos es:

```text
robotics_platform
```

Por lo tanto, utilizar:

```bash
docker exec -it unrobot-postgres psql -U admin -d robotics_platform
```

---

## Error: role "postgres" does not exist

La configuración actual crea:

```text
POSTGRES_USER=admin
```

por lo que no necesariamente existe un usuario llamado:

```text
postgres
```

El usuario configurado para la aplicación es:

```text
admin
```

---

# 19. Comunicación entre contenedores

Existe una diferencia importante dependiendo de dónde se ejecuta Spring Boot.

### Spring Boot ejecutándose directamente en Windows

Actualmente:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/robotics_platform
```

Esto es correcto porque Docker publica:

```yaml
ports:
  - "5432:5432"
```

La aplicación Windows accede al puerto publicado del host.

### Spring Boot ejecutándose dentro de Docker

En ese caso no se debería utilizar:

```text
localhost
```

para referirse al contenedor PostgreSQL.

Se utilizaría el nombre del servicio Docker:

```text
postgres-db
```

por ejemplo:

```text
jdbc:postgresql://postgres-db:5432/robotics_platform
```

Esto es importante cuando se complete la dockerización del backend.

---

# 20. Arquitectura de microservicios futura

La plataforma está planteada para evolucionar hacia varios servicios independientes.

Componentes principales:

### API Gateway

Responsable de:

* Punto de entrada único.
* Enrutamiento hacia microservicios.
* Políticas comunes.
* Integración con autenticación.
* Manejo de rutas.

Tecnología prevista:

```text
Spring Cloud Gateway
```

### Keycloak

Responsable de:

* Autenticación.
* Gestión de usuarios.
* Roles.
* Autorización.
* Tokens JWT.

El backend debería validar los tokens emitidos por Keycloak.

### Apache Kafka

Responsable de comunicación basada en eventos entre microservicios.

Ejemplo conceptual:

```text
Registro Service
      │
      │ evento
      ▼
    Kafka
      │
      ├──────────────► Servicio A
      │
      └──────────────► Servicio B
```

Esto permite desacoplar servicios que no necesitan comunicarse directamente mediante HTTP.

---

# 21. Frontend

El frontend está planteado utilizando:

```text
Angular
```

con migración/evolución hacia:

```text
Ionic
```

La comunicación con el backend debería realizarse mediante HTTP/REST.

En una arquitectura completa:

```text
Ionic / Angular
       │
       ▼
Spring Cloud Gateway
       │
       ├── Registro Service
       ├── Servicio de atletas
       ├── Otros servicios
       └── ...
```

El frontend no debería conectarse directamente a cada microservicio una vez implementado el Gateway.

---

# 22. Seguridad

La arquitectura contempla **Keycloak** como servidor de identidad.

Flujo esperado:

```text
Usuario
   │
   ▼
Frontend
   │
   ▼
Keycloak
   │
   │ JWT
   ▼
Frontend
   │
   │ Authorization: Bearer <token>
   ▼
API Gateway
   │
   ▼
Microservicio
```

Los endpoints públicos y protegidos deben definirse explícitamente.

---

# 23. Kubernetes

Una vez completada la dockerización de los componentes, el siguiente nivel de despliegue es Kubernetes.

La arquitectura esperada sería:

```text
                    Kubernetes Cluster
                           │
             ┌─────────────┴─────────────┐
             │                           │
          Gateway                    Keycloak
             │
      ┌──────┼─────────┐
      │      │         │
 Registro  Servicio A  Servicio B
      │
      ▼
 PostgreSQL

             +
             
           Kafka
```

Los manifiestos de Kubernetes deberán incluir como mínimo:

* Deployments.
* Services.
* ConfigMaps.
* Secrets.
* PersistentVolumes/PersistentVolumeClaims cuando corresponda.
* Ingress o mecanismo equivalente de exposición.
* Configuración de variables de entorno.

---

# 24. Variables de entorno

La configuración actual contiene credenciales directamente en archivos:

```text
POSTGRES_USER=admin
POSTGRES_PASSWORD=password
```

Esto es aceptable únicamente para desarrollo local.

Para producción deberán utilizarse:

```text
Environment variables
```

o:

```text
Docker Secrets
Kubernetes Secrets
```

Nunca se deben subir credenciales reales al repositorio.

Una configuración futura debería ser similar a:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
```

---

# 25. Estructura recomendada del repositorio

La estructura general esperada es:

```text
UNROBOT-LIVEHUB/
│
├── BACKEND/
│   │
│   ├── registro_service/
│   │   ├── src/
│   │   ├── pom.xml
│   │   └── ...
│   │
│   ├── ...
│   │
│   └── ...
│
├── FRONTEND/
│   └── ...
│
├── docker-compose.yml
│
├── README.md
│
└── ...
```

Los nombres exactos de los demás servicios deben actualizarse conforme se incorporen al repositorio.

---

# 26. Flujo de desarrollo

El flujo recomendado para desarrollar una nueva funcionalidad es:

```text
1. Definir funcionalidad
        ↓
2. Definir entidad/dominio
        ↓
3. Crear entidad JPA
        ↓
4. Crear Repository
        ↓
5. Crear Service
        ↓
6. Crear Controller
        ↓
7. Definir endpoint REST
        ↓
8. Probar API
        ↓
9. Verificar PostgreSQL
        ↓
10. Integrar frontend
        ↓
11. Añadir seguridad
        ↓
12. Dockerizar
```

---

# 27. Checklist para un nuevo desarrollador

Después de clonar el repositorio:

```text
[ ] Instalar Docker Desktop
[ ] Instalar JDK 11
[ ] Instalar Git
[ ] Instalar Visual Studio Code
[ ] Clonar repositorio
[ ] Levantar Docker Compose
[ ] Verificar PostgreSQL
[ ] Revisar application.properties
[ ] Ejecutar registro_service
[ ] Verificar puerto 8081
[ ] Probar endpoints
[ ] Verificar tablas en PostgreSQL/pgAdmin
```

---

# 28. Estado de madurez

Actualmente el proyecto se encuentra en una etapa de desarrollo en la que:

```text
                    ┌─────────────────┐
                    │    Concepto     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Arquitectura    │
                    │ de microservicios│
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Backend inicial │
                    │ Spring Boot     │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ PostgreSQL      │
                    │ Docker          │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Integración     │
                    │ Frontend/Auth   │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Docker completo │
                    └────────┬────────┘
                             │
                             ▼
                    ┌─────────────────┐
                    │ Kubernetes      │
                    └─────────────────┘
```

El objetivo final es disponer de una plataforma distribuida, modular y desplegable, en la que los componentes puedan escalarse y mantenerse independientemente.

---

# 29. Próximos pasos prioritarios

### Backend

1. Inventariar todos los `@RestController`.
2. Documentar todos los endpoints.
3. Completar validaciones.
4. Implementar manejo global de excepciones.
5. Añadir DTOs donde sea necesario.
6. Añadir pruebas unitarias.
7. Añadir pruebas de integración.
8. Implementar documentación OpenAPI/Swagger.

### Base de datos

1. Revisar modelo relacional.
2. Confirmar relaciones entre entidades.
3. Definir índices.
4. Incorporar migraciones con Flyway/Liquibase.
5. Separar configuración de desarrollo y producción.
6. Sacar credenciales del repositorio.

### Arquitectura

1. Completar microservicios.
2. Incorporar Spring Cloud Gateway.
3. Incorporar Keycloak.
4. Incorporar Kafka.
5. Definir comunicación síncrona vs. asíncrona.
6. Definir contratos entre servicios.

### Docker

1. Crear `Dockerfile` para cada microservicio.
2. Incorporar todos los servicios al Compose.
3. Configurar redes Docker.
4. Configurar variables de entorno.
5. Configurar persistencia.
6. Separar Compose de desarrollo y producción.

### Kubernetes

1. Crear Deployments.
2. Crear Services.
3. Crear ConfigMaps.
4. Crear Secrets.
5. Configurar almacenamiento persistente.
6. Configurar Ingress.
7. Documentar despliegue.

### Frontend

1. Completar migración/integración Angular/Ionic.
2. Configurar comunicación con Gateway.
3. Implementar autenticación.
4. Manejar JWT.
5. Implementar vistas correspondientes a los servicios.
6. Configurar variables de entorno.

---

# 30. Resumen rápido

Para desarrollo local:

```bash
# 1. Clonar
git clone <URL>

# 2. Entrar al proyecto
cd UNROBOT-LIVEHUB

# 3. Levantar PostgreSQL
docker compose up -d

# 4. Verificar PostgreSQL
docker ps

# 5. Revisar logs si es necesario
docker logs unrobot-postgres

# 6. Entrar al backend
cd BACKEND/registro_service

# 7. Ejecutar Spring Boot
mvnw.cmd spring-boot:run
```

PostgreSQL:

```text
localhost:5432
```

Base de datos:

```text
robotics_platform
```

Usuario:

```text
admin
```

Backend:

```text
http://localhost:8081
```

---

## Nota sobre esta documentación

Este README documenta el estado y la arquitectura conocidos del proyecto hasta la etapa actual de desarrollo.

Los elementos marcados como **pendientes** deben actualizarse a partir del código real del repositorio antes de considerar esta documentación definitiva, especialmente:

* Inventario completo de endpoints.
* Lista definitiva de microservicios.
* Entidades y relaciones de la base de datos.
* Puertos de cada servicio.
* Configuración definitiva de Gateway.
* Configuración de Keycloak.
* Topics de Kafka.
* Estructura final del frontend.
* Manifiestos Kubernetes.
