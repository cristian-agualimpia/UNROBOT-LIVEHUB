package com.unrobot_livehub.registro_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Payload) que reciben los endpoints de /api/v1/bracket.
 * Solo necesita saber en qué categoría operar.
 */
@Data
@NoArgsConstructor
public class BracketPayload {
    private String categoriaTipo;
}