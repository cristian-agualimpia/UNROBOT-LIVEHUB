package com.unrobot_livehub.registro_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO para mostrar la información de un Enfrentamiento (Bolabot, Sumo).
 * ¡MODIFICADO para incluir nombres de equipos!
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnfrentamientoDTO {

    private UUID id;
    private String categoriaTipo;

    private UUID idEquipoA;
    private UUID idEquipoB;
    
    // --- ¡CAMPOS AÑADIDOS! ---
    // (Serán 'null' si el equipo no existe o es un BYE)
    private String nombreEquipoA;
    private String nombreEquipoB;

    private int puntosA;
    private int puntosB;

    private UUID idGanador;
    private String etiquetaRonda;
    private String faltasNotas;
}