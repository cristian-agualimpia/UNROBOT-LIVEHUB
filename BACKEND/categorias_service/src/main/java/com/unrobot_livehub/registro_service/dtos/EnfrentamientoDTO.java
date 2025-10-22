package com.unrobot_livehub.registro_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para mostrar la información de un Enfrentamiento (Bolabot, Sumo).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnfrentamientoDTO {

    private UUID id;
    private String categoriaTipo; // Ej: "MINISUMO_RC"

    private UUID idEquipoA;
    private UUID idEquipoB;
    
    // (Opcional pero recomendado: podríamos traer los nombres de los equipos)
    // private String nombreEquipoA;
    // private String nombreEquipoB;

    private int puntosA;
    private int puntosB;

    private UUID idGanador;
    private String etiquetaRonda; // Ej: "Cuartos-1"
    private String faltasNotas;
}