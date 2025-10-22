package com.unrobot_livehub.registro_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para crear y mostrar una ronda de categoría velocista (Seguidor de Línea).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RondaVelocistaDTO {

    private UUID id; // Null al crear
    private String categoriaTipo; // Ej: "SEGUIDOR_LINEA_PRO"
    private UUID idEquipo;
    
    // (Opcional pero recomendado: podríamos traer el nombre del equipo)
    // private String nombreEquipo;

    private long tiempoMs;
    private int penalizaciones;
    private long tiempoFinalMs; // Se calcula en el backend

    private String etiquetaRonda; // Ej: "Clasificatoria-1"
}