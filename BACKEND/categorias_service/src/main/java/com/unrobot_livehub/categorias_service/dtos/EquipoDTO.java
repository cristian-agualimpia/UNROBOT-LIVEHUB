package com.unrobot_livehub.categorias_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.unrobot_livehub.categorias_service.entity.CategoriaTipo;

/**
 * DTO para la transferencia de datos de la entidad Equipo.
 * Se usa para crear nuevos equipos y para mostrar equipos existentes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoDTO {

    private UUID id; // Se incluye para mostrar equipos, es null al crear
    private String nombre;
    private String institucion;
    
    // Usamos el String del nombre del Enum para la transferencia (simple y robusto)
    // Ej: "BOLABOT_SENIOR"
    private String categoriaTipo; 
    

    // (Puedes agregar más campos si los necesitas)
}