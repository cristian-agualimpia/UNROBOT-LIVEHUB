package com.unrobot_livehub.registro_service.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ¡NUEVO DTO!
 * DTO de ENTRADA. Usado solo para el POST /api/v1/equipos (Registro).
 * Contiene todos los campos del formulario, incluyendo los sensibles.
 */
@Data
@NoArgsConstructor
public class EquipoRegistroDTO {
    
    // No tiene 'id', porque aún no existe
    
    private String nombre;
    private String institucion;
    private String categoriaTipo; 
    
    // Campos sensibles (requeridos para crear)
    private String nombreCapitan;
    private String emailCapitan;
    private String telefonoCapitan;
    
    private List<String> miembros;
}