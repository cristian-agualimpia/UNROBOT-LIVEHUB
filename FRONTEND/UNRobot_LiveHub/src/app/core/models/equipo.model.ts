/**
 * DTO para la respuesta de /api/v1/equipos/{id}
 * Y para ser anidado dentro de EnfrentamientoDTO
 */
export interface EquipoDTO {
  id: string; 
  nombre: string;
  institucion: string;
  categoriaTipo: string; 
}

// Eliminamos EquipoSimpleDTO para evitar conflictos