// Importamos el DTO de equipo para anidarlo
import { EquipoDTO } from "./equipo.model"; 

/**
 * DTO que representa una Ronda Individual (la respuesta del backend).
 */
export interface RondaIndividualDTO {
  id: string; // UUID
  categoriaTipo: string;
  
  // CORRECCIÓN: El backend devuelve un objeto, no un ID
  equipo: EquipoDTO; 

  tiempoMs: number;
  penalizaciones: number;
  tiempoFinalMs: number; 
  etiquetaRonda: string;
}