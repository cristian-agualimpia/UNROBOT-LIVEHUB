import { EquipoSimpleDTO } from "./equipo.model";

export type EstadoEnfrentamiento = 'PENDIENTE' | 'EN_CURSO' | 'FINALIZADO';

/**
 * DTO que representa un Enfrentamiento.
 * ACTUALIZADO para coincidir con la respuesta real de la API
 * (GET /enfrentamientos/categoria/{...})
 */
export interface EnfrentamientoDTO {
  id: string;
  categoriaTipo: string;
  
  // IDs planos (en lugar de objetos anidados)
  idEquipoA: string | null;
  idEquipoB: string | null;

  puntosA: number;
  puntosB: number;

  idGanador: string | null;
  
  // Campos de estado y ronda
  etiquetaRonda: string; // Ej: "Octavos-BYE"
  faltasNotas: string | null;

  // --- CAMPOS QUE YA NO VIENEN DE LA API (según tu JSON) ---
  // estado: EstadoEnfrentamiento; // Lo derivaremos lógicamente
  // ronda: number;
  // orden: number;
  // equipoA: EquipoSimpleDTO | null;
  // equipoB: EquipoSimpleDTO | null;
  // ganadorId: string | null;
  // perdedorId: string | null;
}