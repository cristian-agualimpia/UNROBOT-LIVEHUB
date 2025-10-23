package com.unrobot_livehub.registro_service.dtos;

import lombok.Data;

/**
 * DTO (CORREGIDO) específico (Payload) que recibe el endpoint
 * PUT /api/v1/enfrentamientos/{id}/score
 * para actualizar un puntaje o añadir una nota.
 */
@Data
public class ScoreUpdatePayload {

    /**
     * Define a qué equipo aplicar la acción.
     * Valores esperados: "A" o "B"
     * (Este era el campo que faltaba)
     */
    private String team;

    /**
     * Define la acción a realizar.
     * Valores esperados: "INCREMENT_POINT", "ADD_NOTE", "DECREMENT_POINT", etc.
     */
    private String action;

    /**
     * El contenido de la nota (solo se usa si action = "ADD_NOTE").
     * Es opcional y puede ser null.
     */
    private String note;
}