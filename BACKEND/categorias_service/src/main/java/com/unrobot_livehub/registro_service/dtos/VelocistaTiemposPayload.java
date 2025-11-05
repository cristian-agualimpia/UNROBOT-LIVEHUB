package com.unrobot_livehub.registro_service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Payload) que recibe el endpoint para registrar los tiempos
 * de una final de velocista (1 vs 1).
 */
@Data
@NoArgsConstructor
public class VelocistaTiemposPayload {
    // Usamos Long para Tiempos en milisegundos
    private Long tiempoEquipoA_ms;
    private Long tiempoEquipoB_ms;
}