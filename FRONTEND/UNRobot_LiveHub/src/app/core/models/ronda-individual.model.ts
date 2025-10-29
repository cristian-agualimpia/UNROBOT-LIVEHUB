import { EquipoSimpleDTO } from "./equipo.model";

export type EstadoRonda = 'PENDIENTE' | 'FINALIZADA';

export interface RondaIndividualDTO {
  id: string;
  categoriaTipo: string;
  equipo: EquipoSimpleDTO;
  
  puntos: number;
  tiempoMs: number;
  fallos: number;
  
  estadoRonda: EstadoRonda;
}