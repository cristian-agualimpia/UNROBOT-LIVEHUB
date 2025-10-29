import { EquipoSimpleDTO } from "./equipo.model";

export type EstadoEnfrentamiento = 'PENDIENTE' | 'EN_CURSO' | 'FINALIZADO';

export interface EnfrentamientoDTO {
  id: string;
  categoriaTipo: string;
  estado: EstadoEnfrentamiento;
  ronda: number;
  orden: number;
  
  equipoA: EquipoSimpleDTO | null;
  equipoB: EquipoSimpleDTO | null;
  
  puntosA: number;
  puntosB: number;
  
  ganadorId: string | null;
  perdedorId: string | null;
}