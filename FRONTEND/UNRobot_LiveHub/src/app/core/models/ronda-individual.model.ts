import { EquipoDTO } from "./equipo.model"; 

/**
 * DTO que representa una Ronda Individual (la respuesta del backend).
 */
export interface RondaIndividualDTO {
  id: string; // UUID
  categoriaTipo: string;
  
  equipo: EquipoDTO; // Objeto anidado

  tiempoMs: number;
  penalizaciones: number;
  tiempoFinalMs: number; 
  etiquetaRonda: string;
  
  // El backend NO envía 'estadoRonda', 'puntos', o 'fallos'.
  // Los calcularemos o adaptaremos.
}