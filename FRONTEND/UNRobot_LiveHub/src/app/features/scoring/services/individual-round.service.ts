import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RondaIndividualDTO } from '../../../core/models/ronda-individual.model';

/**
 * DTO para enviar los resultados de una ronda individual.
 * Asumimos que la API espera estos campos.
 */
export interface UpdateRondaIndividualDTO {
  puntos: number;
  tiempoMs: number;
  fallos: number;
  estadoRonda: 'FINALIZADA'; // Marcamos la ronda como finalizada
}


export interface CreateRondaIndividualDTO {
  categoriaTipo: string;
  idEquipo: string;
  tiempoMs: number;
  penalizaciones: number;
  etiquetaRonda: string;
}

@Injectable({
  providedIn: 'root'
})
export class IndividualRoundService {

  private http = inject(HttpClient);

  /**
   * POST /api/v1/rondas-individuales
   * Crea una nueva ronda (intento) para un equipo.
   */
  createRound(payload: CreateRondaIndividualDTO): Observable<RondaIndividualDTO> {
    return this.http.post<RondaIndividualDTO>('/rondas-individuales', payload);
  }

  /**
   * GET /api/v1/rondas-individuales/categoria/{categoriaTipo}
   * (Lo mantenemos por si lo usamos en el futuro para ver un historial)
   */
  getRoundsByCategory(categoryId: string): Observable<RondaIndividualDTO[]> {
    return this.http.get<RondaIndividualDTO[]>(`/rondas-individuales/categoria/${categoryId}`);
  }

  /**
   * GET /api/v1/rondas-individuales/{id}
   */
  getRoundById(roundId: string): Observable<RondaIndividualDTO> {
    return this.http.get<RondaIndividualDTO>(`/rondas-individuales/${roundId}`);
  }

  /**
   * PUT /api/v1/rondas-individuales/{id}
   * (Este DTO es de judge-form-individual, lo dejamos como está)
   */
  updateRound(roundId: string, payload: any): Observable<RondaIndividualDTO> {
    return this.http.put<RondaIndividualDTO>(`/rondas-individuales/${roundId}`, payload);
  }
}