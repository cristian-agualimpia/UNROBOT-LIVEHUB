```markdown
# 🤖 UNROBOT-LIVEHUB — Guía de Onboarding para Nuevos Contribuyentes

> **Documento vivo.** Si modificas arquitectura, entidades o el roadmap, actualiza este archivo en el mismo PR. La edición 20 de UN Robot (2027) depende de que el conocimiento no se pierda con la rotación anual de CEIMTUN RAS / Rama IEEE UNAL Bogotá.

---

## 📌 1. Contexto del Proyecto

**UNROBOT-LIVEHUB** es la plataforma oficial de gestión y transmisión en vivo del torneo **UN Robot**, el torneo de robótica gratuito más grande de Colombia, organizado por la **Rama Estudiantil IEEE de la Universidad Nacional de Colombia sede Bogotá**, a través del capítulo **CEIMTUN RAS** (*Comité de Estudiantes de Ingeniería Mecatrónica — Robotics and Automation Society*).

- **Edición objetivo:** 20.ª edición (2027) — hito de aniversario.
- **Categorías activas:** Sumo, Bolabot, Velocistas, y otras por confirmar.
- **Modalidades soportadas:** `ENFRENTAMIENTO` (1v1, Sumo) y `RondaIndividual` (por tiempo/pista, Velocistas).
- **Usuarios del sistema:** organizadores, árbitros/jueces, operadores de mesa, público general (tableros en vivo).

> ⚠️ **Advertencia de escala:** la edición 20 espera un volumen significativamente mayor de equipos, categorías, público y cobertura mediática. Toda decisión técnica debe evaluarse contra ese escenario, no contra una edición promedio.

---

## 🏗 2. Arquitectura e Infraestructura

### 2.1 Diagrama

```text
[ Dispositivos Públicos / Jueces / Pantallas ]
                      │
                      ▼ (HTTPS seguro, sin abrir puertos en router)
            [ Cloudflare Tunnels ]
                      │
                      ▼
       ┌─────────────────────────────┐
       │      Servidor Proxmox       │
       │  ┌───────────────────────┐  │
       │  │   Nginx (Proxy Inv.)  │  │
       │  └─────┬───────────┬─────┘  │
       │        │           │        │
       │        ▼           ▼        │
       │  [ Frontend ] [ Backend ]   │
       │  (Angular)   (Spring Boot)  │
       │                    │        │
       │                    ▼        │
       │             [ PostgreSQL ]  │
       └─────────────────────────────┘
```

### 2.2 Ejes de infraestructura

| Eje | Descripción |
|---|---|
| **Desarrollo local** | Backend Spring Boot + servidor de desarrollo Angular accesibles vía IP local y puerto dentro de la LAN del evento. |
| **Contenerización** | Docker sobre Proxmox: PostgreSQL, backend y Nginx aislados en contenedores para evitar conflictos de dependencias. |
| **Exposición pública** | Cloudflare Tunnels publica frontend y API sin tocar NAT del router. Respaldo administrativo con mesh (Tailscale / ZeroTier). |

> 📝 **Nota de nomenclatura:** el proyecto se describe como "microservicios", pero **en la práctica es un monolito modular** (`categorias_service` con controllers/services/repositories). Esto es una decisión deliberada y correcta para el tamaño del equipo; no sobre-ingeniería. Evita dividir en microservicios reales sin una necesidad operativa concreta.

---

## 🛠 3. Stack Tecnológico

### Backend (`categorias_service`)
- **Lenguaje:** Java 21 (`21.0.8`)
- **Framework:** Spring Boot `3.2.7`
- **Persistencia:** Spring Data JPA / Hibernate ORM `6.4.9.Final`
- **Pool de conexiones:** HikariCP
- **Base de datos:** PostgreSQL
- **Utilidades:** Lombok
- **Puerto por defecto:** `8081`

### Frontend
- **Framework:** Angular (en rediseño visual y adaptación responsive para operación arbitral y tableros públicos).

---

## 📂 4. Estructura del Proyecto

```text
BACKEND/categorias_service/
└── src/main/java/com/unrobot_livehub/categorias_service/
    ├── CategoriasServiceApplication.java   # Punto de entrada Spring Boot (Puerto 8081)
    ├── controller/                         # REST: BracketController, EquipoController, etc.
    ├── service/                            # Lógica: BracketService, EquipoService, etc.
    ├── repository/                         # JPA: EquipoRepository, EnfrentamientoRepository
    ├── entity/                             # JPA + Enums: Equipo, Enfrentamiento, CategoriaTipo
    └── dtos/                               # DTOs: CategoriaInfoDTO, BracketDTO, etc.
```

---

## ⚠️ 5. Modelo de Datos y Reglas Críticas

> **LEER ANTES DE TOCAR ENTIDADES, JPQL O SERVICIOS.** Violar estas reglas rompe el mapeo de Hibernate y genera regresiones en cadena difíciles de diagnosticar.

### 5.1 `CategoriaTipo` (Enum) + `CategoriaInfoDTO`

Las categorías (`BOLABOT`, `SUMO`, Velocistas, etc.) **NO son una tabla**. Son un **Enum enriquecido** con configuración estática y reglamentaria.

- **Regla:** en `Equipo` y `Enfrentamiento`, `categoria` es de tipo `CategoriaTipo`. **No existe `categoria.id` ni `@ManyToOne` hacia una entidad categoría.**
- **Metadatos expuestos vía `CategoriaInfoDTO`:**
  - `tipo`: nombre del Enum (ej. `"BOLABOT"`)
  - `tipoLogica`: `"ENFRENTAMIENTO"` (1v1) o `"RondaIndividual"` (por tiempo/pista)
  - `nombreCompleto`, `resumenDescriptivo`, `descripcionPista`, `publicoDirigido`, `integrantesMax`, `reglasResumen`

### 5.2 Entidad `Equipo` (tabla `equipos`)

| Atributo | Tipo | Notas |
|---|---|---|
| `id` | `UUID` | PK |
| `nombre` | `String` | Nombre del equipo/robot |
| `institucion` | `String` | Universidad, colegio o club |
| `categoria` | `CategoriaTipo` | Enum |
| `nombreCapitan`, `emailCapitan`, `telefonoCapitan` | `String` | Contacto del líder |
| `miembros` | `List<String>` | **Se llama `miembros`, NUNCA `integrantes`** |
| `activo` | `boolean` (`NOT NULL`) | Habilitado en competencia |
| `motivoDescalificacion` | `String` | Si `activo = false` |
| `descripcion` | `String` | Notas técnicas libres |

> 🔴 **Regla JPQL:** cualquier consulta sobre miembros debe hacer `LEFT JOIN e.miembros m`. Renombrar a `integrantes` rompe `EquipoRepository.buscarPorCriterio` y `buscarPorCategoriaYCriterio`.

### 5.3 Entidad `Enfrentamiento` (tabla `enfrentamientos`)

| Atributo | Tipo | Notas |
|---|---|---|
| `id` | `UUID` | PK |
| `categoria` | `CategoriaTipo` (`@Enumerated(EnumType.STRING)`) | Categoría del combate |
| `idEquipoA` | `UUID` (`nullable = false`) | UUID plano, **no** `@ManyToOne` |
| `idEquipoB` | `UUID` (`nullable = true`) | `null` = BYE / pase automático |
| `puntosA` / `puntosB` | `int` (default `0`) | Marcador |
| `idGanador` | `UUID` (`nullable = true`) | Se setea al cerrar el combate |
| `etiquetaRonda` | `String` (`nullable = false`) | ej. `"Octavos-1"`, `"Semifinal-2"`, `"Final"` |
| `faltasNotas` | `String` | Bitácora arbitral (amonestaciones, reinicios, incidencias) |

> 🔴 **Regla:** `idEquipoA` e `idEquipoB` son `UUID` planos. **No** convertirlos en `@ManyToOne`. **No** renombrarlos. La integridad referencial depende 100% del servicio (no hay FKs a nivel BD).

---

## ✅ 6. Funcionalidades Implementadas (Estado Actual)

1. **Catálogo de categorías** — reglas, descripciones de pista y topes de integrantes vía `CategoriaInfoDTO`.
2. **Administración de equipos y casos borde en vivo:**
   - Registro, edición, cambio de categoría, notas técnicas.
   - **Protocolo de baja en 3 niveles:**
     1. *Hard Delete* — si el equipo no está en ningún enfrentamiento.
     2. *Sustitución por BYE* — si está sembrado en llave pero su combate no se ha disputado.
     3. *Soft Delete* — si ya participó; `activo = false` + `motivoDescalificacion`, preservando historial.
3. **Búsqueda multicriterio** (`EquipoRepository`):
   - JPQL `buscarPorCriterio` y `buscarPorCategoriaYCriterio`.
   - `LOWER` + `LIKE` sobre `nombre`, `institucion` y `miembros`.
   - Filtrado opcional por `CategoriaTipo`.
4. **Operaciones base de edición e intercambio en llaves** — estructura inicial para modificar participantes en enfrentamientos activos.
5. **Base de estimación de tiempos** — proyección preliminar de duración de rondas según inscritos.

---

## 🚀 7. Ejecución Local

### 7.1 Requisitos previos
1. **JDK 21** instalado y `JAVA_HOME` configurado.
2. **PostgreSQL** corriendo (local o Docker) con credenciales en `application.properties` / `application.yml`.
3. IDE con soporte Java/Spring Boot (VS Code + Extension Pack for Java, IntelliJ IDEA o Eclipse).
4. **Angular CLI** (para frontend) — versión alineada con el `package.json` del repo.

### 7.2 Backend
```bash
cd BACKEND/categorias_service
mvn spring-boot:run
# o ejecutar CategoriasServiceApplication.java desde el IDE
```
Verifica en consola:
- Tomcat en puerto **`8081`**.
- HikariCP conectado a PostgreSQL.

### 7.3 Frontend
```bash
cd FRONTEND   # ajustar según estructura real
npm install
ng serve --host 0.0.0.0 --port 4200
```
Accesible en `http://<IP-LAN>:4200` para pruebas desde tablets/dispositivos del evento.

---

## 🧯 8. Solución de Problemas Frecuentes (Windows / PowerShell)

### Error: JPQL antiguo persiste tras editar `@Repository`
Los `.class` en `target/` están desactualizados.

```powershell
Remove-Item -Recurse -Force ".\BACKEND\categorias_service\target"
```

Luego en VS Code: `Ctrl + Shift + P` → **`Java: Clean Language Server Workspace`** → reiniciar la app.

### Otros problemas comunes
| Síntoma | Causa probable | Solución |
|---|---|---|
| `LazyInitializationException` en serialización | Acceso a colección lazy fuera de sesión | Revisar `spring.jpa.open-in-view` y usar DTOs |
| `Unknown column` o mapeo roto | Renombrar atributo (`miembros` → `integrantes`) | Revertir renombrado; ver §5.2 |
| Puerto 8081 ocupado | Otra instancia corriendo | `netstat -ano | findstr :8081` → matar PID |
| HikariCP timeout | PostgreSQL caído o credenciales | Verificar `docker ps` y `application.yml` |

---

## 🗺 9. Roadmap (Backlog Priorizado)

> **Nota crítica de priorización:** el roadmap original ordena por complejidad; para la edición 20 debe ordenarse por **riesgo operativo**. A continuación la propuesta vigente.

### 🔴 P0 — Imprescindibles antes del evento

| # | Tarea | Justificación |
|---|---|---|
| 1 | **Entidad `Edicion`** + aislamiento por año en `Equipo`/`Enfrentamiento` | Sin esto no hay histórico ni "20 años" |
| 2 | **`EstadoEnfrentamiento`** (`PENDIENTE`, `EN_JUEGO`, `FINALIZADO`, `ANULADO`) | El estado actual se infiere de `idGanador != null` → frágil |
| 3 | **Spring Security + RBAC** (`ADMIN`, `ARBITRO`, `MESA`, `PUBLICO`) | Sin auth, cualquiera con la URL altera puntajes |
| 4 | **Entidad `Pista`** + asignación de enfrentamientos | Múltiples arenas simultáneas en 2027 |
| 5 | **Backup + snapshot + runbook** de contingencia (incluye modo kiosco LAN) | Punto único de fallo en Proxmox |
| 6 | **Observabilidad mínima**: Actuator, `/health`, logs estructurados | Detección temprana en vivo |
| 7 | **Auditoría** (`usuario`, `entidad`, `antes`, `despues`, `timestamp`) | Trazabilidad arbitral |

### 🟠 P1 — Altamente deseables (Q2 2027)

| # | Tarea |
|---|---|
| 8 | **Fase de grupos (round-robin) + tabla de posiciones + clasificación automática** (unifica roadmap #1 y #2) |
| 9 | **Tiempo real público (SSE)** para scoreboards y brackets |
| 10 | **Resorteo/reordenamiento manual** con bloqueo de combates finalizados |
| 11 | **Frontend responsive/PWA para tablets de árbitros** |
| 12 | **Actualización a Spring Boot vigente** + revisión de CVEs |

### 🟢 P2 — Valor añadido para el aniversario (Q3–Q4 2027)

| # | Tarea |
|---|---|
| 13 | Salón de la fama / histórico multi-edición y rankings acumulados |
| 14 | Check-in QR y credenciales digitales |
| 15 | API de overlay para OBS (marcador en vivo, sponsors) |
| 16 | Algoritmo de optimización tiempo vs. combates (roadmap original #4) |
| 17 | Marcador extensible (`JSONB`) para categorías con puntuación compuesta |
| 18 | Portal de patrocinadores con métricas de exposición |

### 🧹 Limpieza técnica (transversal)
- Eliminar `hibernate.dialect` explícito (Hibernate 6 lo infiere).
- `spring.jpa.open-in-view=false`.
- Sustituir inferencia de estado por `EstadoEnfrentamiento` formal.

---

## 🧭 10. Convenciones de Contribución

### 10.1 Antes de abrir un PR
- [ ] `mvn clean verify` pasa en local.
- [ ] No se renombraron atributos de §5 (`miembros`, `idEquipoA`, `idEquipoB`, `etiquetaRonda`).
- [ ] No se añadieron `@ManyToOne` hacia categoría ni hacia equipo en `Enfrentamiento`.
- [ ] Se actualizó este `README` si cambió arquitectura, entidad o roadmap.
- [ ] Se documentó la decisión en un ADR si es arquitectónicamente relevante (ver §10.2).

### 10.2 ADRs (Architecture Decision Records)
Cada decisión no obvia debe quedar registrada en `docs/adr/NNN-titulo.md`. Ejemplos de ADRs que **deberían existir ya**:
- `001-categoria-como-enum.md` — por qué `CategoriaTipo` no es tabla.
- `002-uuid-planos-enfrentamiento.md` — por qué `idEquipoA/B` no son `@ManyToOne`.
- `003-baja-en-3-niveles.md` — protocolo de eliminación de equipos.
- `004-monolito-modular.md` — por qué no microservicios reales.

Sin ADRs, el próximo equipo "corregirá" decisiones deliberadas y romperá el sistema.

### 10.3 Estilo de commits
Convención sugerida: `feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`.

### 10.4 Nomenclatura
- **Español** para dominio (`Equipo`, `Enfrentamiento`, `miembros`, `institucion`).
- **Inglés** para infraestructura Spring (`@Controller`, `@Service`, `@Repository`).

---

## ⚠️ 11. Riesgos Conocidos y Deuda Técnica

| Riesgo | Mitigación actual | Acción pendiente |
|---|---|---|
| SPOF en Proxmox | Mesh Tailscale/ZeroTier | Snapshot + VM respaldo + runbook |
| Falla de internet del recinto | Cloudflare Tunnels | Modo kiosco LAN para pantallas |
| Sin autenticación | — | RBAC (P0) |
| Sin `EstadoEnfrentamiento` | Inferencia vía `idGanador` | Enum formal (P0) |
| Sin entidad `Edicion` | — | Aislamiento por año (P0) |
| Sin entidad `Pista` | — | Agenda multi-arena (P0) |
| Spring Boot 3.2.7 fuera de soporte OSS hacia 2027 | — | Upgrade planificado (P1) |
| Inconsistencia `idGanador` ↔ `idEquipoA/B` | Validación en servicio | Job de consistencia + checks |
| Rotación anual de organizadores | Este documento | ADRs + runbook + video de despliegue |

---

## 📞 12. Contacto y Gobernanza

- **Organización:** Rama Estudiantil IEEE — Universidad Nacional de Colombia sede Bogotá.
- **Capítulo ejecutor:** CEIMTUN RAS (*Comité de Estudiantes de Ingeniería Mecatrónica — Robotics and Automation Society*).
- **Edición objetivo:** 20.ª (2027).
- **Canal de coordinación técnica:** definir con la dirección vigente de CEIMTUN RAS (Slack/Discord/WhatsApp del equipo).

---

## 🎯 13. Primeros Pasos para un Nuevo Contribuyente

1. **Lee §5 completo** (Modelo de Datos y Reglas Críticas). Es el corazón del proyecto.
2. **Levanta el backend en local** siguiendo §7. Verifica `/health` (si existe) o al menos que Tomcat arranque en `8081`.
3. **Explora `CategoriaTipo`** en `entity/` — entiende el enum enriquecido.
4. **Revisa `EquipoRepository`** — las dos consultas JPQL y el uso de `miembros`.
5. **Identifica una tarea P0 del §9** y propón un plan de implementación en el canal del equipo **antes de codificar**.
6. **Documenta tu decisión** en un ADR si toca arquitectura.
7. **Abre PR pequeño y revisable** — la rotación de equipo hace que los PRs grandes se pierdan.

> 💡 **Regla de oro:** ante la duda, **no renombres atributos de dominio ni conviertas UUIDs planos en relaciones JPA**. Pregunta primero. El costo de un malentendido en este modelo es alto.

---

*Última actualización: [16 de Noviembre 2026] — Mantener sincronizado con el avance real del repositorio.*
```