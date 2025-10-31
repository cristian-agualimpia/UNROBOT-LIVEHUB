/**
 * DTO de DETALLE (Plano con Nombres) para un Enfrentamiento.
 * Usado por 'judge-form-confrontation' (el formulario).
 * Coincide con la respuesta de: GET /enfrentamientos/{id}
 */
export interface EnfrentamientoDTO {
  id: string; // Es un UUID
  categoriaTipo: string;

  idEquipoA: string | null; // Es un UUID
  idEquipoB: string | null; // Es un UUID
  
  nombreEquipoA: string | null;
  nombreEquipoB: string | null;

  puntosA: number;
  puntosB: number;

  idGanador: string | null; // Es un UUID
  etiquetaRonda: string;
  faltasNotas: string | null;
  
  // NOTA: El campo 'estado' NO viene del backend.
}