package com.unrobot_livehub.registro_service.entity;

import lombok.Getter;

/**
 * Enum que define TODAS las categorías de la competencia.
 * Funciona como la "fuente de verdad" estática para nombres, reglas,
 * descripciones, y la lógica de puntuación asociada.
 *
 * @Getter (de Lombok) genera los getters para todos los campos.
 */
@Getter
public enum CategoriaTipo {

    // --- 1. BOLABOT (Lógica de Enfrentamiento) ---
    BOLABOT(
            TipoLogica.ENFRENTAMIENTO,
            "Bolabot",
            "Fútbol de robots 2 vs 2. Los competidores deben buscar realizar la mayor cantidad de puntos (goles).",
            "Cancha de 180x90 cm, Fondo Verde, Líneas Blancas. Arcos de 50x20 cm. Material: MDF.",
            "Público General (Sin restricción de edad)",
            3, // 3 máximo (2 jugadores + 1 suplente)
            "Juego 2v2, 2 tiempos de 3 min.\n" +
            "Puntuación: 1 punto por gol.\n" +
            "Finaliza si un equipo tiene 5 puntos más que el rival.\n" +
            "Prohibido dañar intencionalmente al rival o la cancha.\n" +
            "Robots no pueden permanecer inmóviles por más de 5 seg."
    ),

    // --- 2. SUMO AMATEUR (Lógica de Enfrentamiento) ---
    SUMO_AMATEUR(
            TipoLogica.ENFRENTAMIENTO,
            "Sumo Amateur",
            "Enfrentamiento 1 vs 1 para sacar al oponente del ring (Dohyo). Orientada a robots 'desde cero' (ej. Arduino), valorando creatividad y materiales accesibles.",
            "Ring de Sumo (Dohyo) circular. Fondo negro, borde blanco.",
            "Escolar (Estudiantes activos en colegio)",
            0, // No especificado en el reglamento (0 para 'no especificado')
            "1 vs 1. Ganar un punto (Yuhkoh) al sacar al oponente.\n" +
            "El primero en ganar 2 puntos (Ippon) gana el round.\n" +
            "Enfrentamiento al mejor de 3 rounds.\n" +
            "Prohibido fijar el robot al ring o arrojar elementos."
    ),

    // --- 3. SUMO JUNIOR (Lógica de Enfrentamiento) ---
    SUMO_JUNIOR(
            TipoLogica.ENFRENTAMIENTO,
            "Sumo Junior (Plataforma)",
            "Enfrentamiento 1 vs 1 para sacar al oponente del ring. Dirigida a robots construidos con kits de desarrollo reconocidos (Lego, VEX, INNOBOT, etc.).",
            "Ring de Sumo (Dohyo) circular. Fondo negro, borde blanco.",
            "Escolar (Implícito por 'Junior' y 'Plataforma')",
            0, // No especificado en el reglamento (0 para 'no especificado')
            "1 vs 1. Ganar un punto (Yuhkoh) al sacar al oponente.\n" +
            "El primero en ganar 2 puntos (Ippon) gana el round.\n" +
            "Enfrentamiento al mejor de 3 rounds.\n" +
            "Solo se permiten robots de kits prefabricados."
    ),

    // --- 4. SEGUIDOR DE LÍNEA RONDA_INDIVIDUAL AMATEUR (Lógica RONDA_INDIVIDUAL) ---
    SEGUIDOR_LINEA_AMATEUR(
            TipoLogica.RONDA_INDIVIDUAL,
            "Seguidor de Línea RONDA_INDIVIDUAL Amateur",
            "Robot autónomo 'desde cero' (ej. Arduino) debe recorrer un circuito de línea negra sobre fondo blanco en el menor tiempo. Máximo 8 sensores.",
            "Pista plana de 183x244 cm. Fondo: Blanco Mate. Línea: Negro. Ancho de línea: 20 mm.",
            "Público General (Sin restricción de edad)",
            3, // Máximo 3 integrantes
            "Completar una vuelta en el menor tiempo.\n" +
            "Tiempo límite por intento: 2 min 30 seg.\n" +
            "Fases: Clasificación y Final (Llaves).\n" +
            "Descalificación si sale de pista por más de 5 seg."
    ),

    // --- 5. SEGUIDOR DE LÍNEA RONDA_INDIVIDUAL ESCOLAR (Lógica RONDA_INDIVIDUAL) ---
    SEGUIDOR_LINEA_ESCOLAR(
            TipoLogica.RONDA_INDIVIDUAL,
            "Seguidor de Línea RONDA_INDIVIDUAL Escolar",
            "Robot autónomo de kit (Lego, VEX, etc.) debe recorrer un circuito de línea negra sobre fondo blanco en el menor tiempo.",
            "Pista plana de 183x244 cm. Fondo: Blanco Mate. Línea: Negro. Ancho de línea: 20 mm.",
            "Escolar (Estudiantes de educación media, básica)",
            3, // Máximo 3 integrantes
            "Completar una vuelta en el menor tiempo.\n" +
            "Tiempo límite por intento: 3 min.\n" +
            "Clasificación: 3 intentos, se toma el mejor tiempo.\n" +
            "Fase Final: Los 4 más rápidos pasan a llaves."
    ),

    // --- 6. SEGUIDOR DE LÍNEA MULTINIVEL (Lógica RONDA_INDIVIDUAL) ---
    SEGUIDOR_LINEA_MULTINIVEL(
            TipoLogica.RONDA_INDIVIDUAL,
            "Seguidor de Línea RONDA_INDIVIDUAL MULTINIVEL (Turbina)",
            "Robot autónomo (plataforma no comercial) con turbina, debe recorrer una pista multinivel (blanco/negra) en el menor tiempo. La turbina debe estar encendida.",
            "Pista multinivel de 500x800 cm. Fondo: Blanco mate. Línea: Negro mate. Ancho: 20 mm. Puentes e inclinación máx 45°.",
            "Profesional (Categoría: Profesional)",
            3, // Máximo 3 integrantes
            "Completar una vuelta en el menor tiempo.\n" +
            "Tiempo límite por intento: 2 min 30 seg.\n" +
            "Pista de configuración ajustable.\n" +
            "La turbina DEBE estar encendida durante todo el recorrido."
    ),

    // --- 7. DRONE RACE SIMULACIÓN (Lógica RONDA_INDIVIDUAL) ---
    DRONE_RACE_SIM(
            TipoLogica.RONDA_INDIVIDUAL,
            "Drone Race Ultimate (Simulación)",
            "Completar la simulación de un circuito en el simulador LIFTOFF en el menor tiempo posible usando drones (virtuales) 5 pulgadas.",
            "Simulador LIFTOFF. El evento proporciona el simulador y control.",
            "Público General (Sin restricción de edad)",
            1, // Individual (Nombre del competidor)
            "Eliminatoria tipo pirámide. Rondas de 4 participantes.\n" +
            "3 heats oficiales, se toma el mejor tiempo.\n" +
            "Los 3 participantes con mejores tiempos pasan a la ronda final.\n" +
            "Ronda final: 5 heats oficiales, se toma el mejor tiempo."
    ),

    // --- 8. DRONE RACE TINY (Lógica RONDA_INDIVIDUAL) ---
    DRONE_RACE_TINY(
            TipoLogica.RONDA_INDIVIDUAL,
            "Drone Race Ultimate (Tiny)",
            "Completar un circuito físico en el menor tiempo posible usando drones tiny (reales).",
            "Circuito físico de 500x500x500m. Se usará arquitectura urbana (espacio semicerrado) para posicionar los gates.",
            "Público General (Sin restricción de edad)",
            1, // Individual (Nombre del competidor)
            "Heats en grupos. Se toma el tiempo total de 3 vueltas.\n" +
            "Si no completa 3 vueltas, no se toma el tiempo del Heat.\n" +
            "Los 2 mejores tiempos de cada grupo avanzan a siguientes rondas.\n" +
            "Rondas sucesivas hasta determinar 4 finalistas."
    ),

    // --- 9. ROVER RC EXPLORADOR (Lógica RONDA_INDIVIDUAL, score-based) ---
    ROVER_RC_EXPLORADOR(
            TipoLogica.RONDA_INDIVIDUAL, // Se usa la lógica de Ronda (1 jugador) pero 'tiempoMs' se usará como 'puntos'
            "Rover RC Explorador",
            "Diseñar y operar un robot tipo rover RC capaz de desplazarse por terreno irregular y superar retos de exploración (recoger muestra, transportar objetos, navegar).",
            "Terreno irregular, obstáculos naturales, puntos designados. Pista no estándar.",
            "Público General (Sin restricción de edad)",
            4, // 4 máximo
            "Competencia basada en puntos por retos.\n" +
            "Las pruebas combinan desplazamiento, precisión y creatividad.\n" +
            "Robot controlado remotamente (RC).\n" +
            "Dimensiones máx: 30x30x30 cm. Peso máx: 10 kg."
    ),

    // --- 10. INNOVATION CHALLENGE (Lógica RONDA_INDIVIDUAL, score-based) ---
    INNOVATION_CHALLENGE(
            TipoLogica.RONDA_INDIVIDUAL, // Se usa la lógica de Ronda (1 equipo) pero 'tiempoMs' se usará como 'puntaje de pitch'
            "Innovation Challenge",
            "Elaborar una propuesta de proyecto para dar una solución tecnológica e ingenieril enfocada en género, cuidado y accesibilidad. Presentación en modalidad 'pitch'.",
            "No aplica (Es una presentación/pitch).",
            "Público General (No especificado)",
            0, // No especificado en el reglamento (0 para 'no especificado')
            "Presentación general de la propuesta y un documento detallado.\n" +
            "El documento debe incluir: Título, Resumen, Introducción, Planteamiento del problema.\n" +
            "Se valora la innovación, ingenio y habilidades de los participantes."
    );

    /**
     * Define qué lógica de servicio se debe usar para esta categoría.
     * ENFRENTAMIENTO = Usa EnfrentamientoService (2 equipos, puntos A/B)
     * RONDA_INDIVIDUAL = Usa RondaRONDA_INDIVIDUALService (1 equipo, tiempo o puntos)
     */
    public enum TipoLogica {
        ENFRENTAMIENTO, // Lógica 1v1 (Bolabot, Sumo)
        RONDA_INDIVIDUAL       // Lógica 1-jugador (Seguidor, Drones, Rover, Innovation)
    }

    // --- Campos del Enum ---
    
    private final TipoLogica tipoLogica;
    private final String nombreCompleto;
    private final String resumenDescriptivo;
    private final String descripcionPista;
    private final String publicoDirigido;
    private final int integrantesMax;
    private final String reglasResumen;

    // --- Constructor del Enum ---
    
    CategoriaTipo(
            TipoLogica tipoLogica,
            String nombreCompleto,
            String resumenDescriptivo,
            String descripcionPista,
            String publicoDirigido,
            int integrantesMax,
            String reglasResumen
    ) {
        this.tipoLogica = tipoLogica;
        this.nombreCompleto = nombreCompleto;
        this.resumenDescriptivo = resumenDescriptivo;
        this.descripcionPista = descripcionPista;
        this.publicoDirigido = publicoDirigido;
        this.integrantesMax = integrantesMax;
        this.reglasResumen = reglasResumen;
    }
}