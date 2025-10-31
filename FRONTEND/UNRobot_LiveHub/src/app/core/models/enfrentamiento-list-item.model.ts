/**
 * DTO de LISTA (Plano) para un Enfrentamiento.
 * Usado por 'round-list'.
 */
export interface EnfrentamientoListItemDTO {
  id: string;
  categoriaTipo: string;
  
  idEquipoA: string | null;
  idEquipoB: string | null;

  puntosA: number;
  puntosB: number;
  
  idGanador: string | null;
  etiquetaRonda: string; 
  faltasNotas: string | null;
}