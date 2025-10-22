package com.unrobot_livehub.registro_service.entity;

import lombok.Getter;

/**
 * Enum que define las categorías.
 * Funciona como la "fuente de verdad" estática para nombres, reglas, etc.
 * @Getter (de Lombok) genera los getters para los campos.
 */
@Getter
public enum CategoriaTipo {

    // --- Categorías de Puntos (Enfrentamiento) ---
    BOLABOT_SENIOR("Bolabot Senior 2v2", "Reglas de Bolabot..."),
    MINISUMO_RC("Minisumo Radio Control", "Reglas de Sumo RC..."),
    MINISUMO_AUTONOMO("Minisumo Autónomo", "Reglas de Sumo Autónomo..."),
    // ... agrega aquí más categorías tipo Bolabot o Sumo

    // --- Categorías de Tiempo (Velocista) ---
    SEGUIDOR_LINEA_ESCOLAR("Seguidor de Línea Escolar", "Reglas de seguidor..."),
    SEGUIDOR_LINEA_PRO("Seguidor de Línea Profesional", "Reglas de seguidor pro...");
    // ... agrega aquí más categorías tipo Seguidor o Velocista

    private final String nombreCompleto;
    private final String reglas;

    // Constructor privado para el Enum
    CategoriaTipo(String nombreCompleto, String reglas) {
        this.nombreCompleto = nombreCompleto;
        this.reglas = reglas;
    }
}