import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// CORRECCIÓN DE RUTA: ../../
import { RondaIndividualDTO } from '../../../core/models/ronda-individual.model';

/**
 * DTO para CREAR una nueva ronda individual.
 */
export interface CreateRondaIndividualDTO {
  categoriaTipo: string;
  idEquipo: string;
  tiempoMs: number;
  penalizaciones: number;
  etiquetaRonda: string;
}

/**
 * DTO para ACTUALIZAR una ronda (lo que usa judge-form-individual).
 * Asumimos que no podemos cambiar el equipo, solo los resultados.
 */
export interface UpdateRondaIndividualDTO {
  tiempoMs: number;
  penalizaciones: number;
  etiquetaRonda: string;
}

// --- ¡NUEVO DTO! ---
// DTO para la respuesta de /posiciones
// CORRECCIÓN: Añadido 'export'
export interface LeaderboardEntryDTO {
  posicion: number;
  nombreEquipo: string;
  idEquipo: string;
  mejorTiempoMs: number;
  mejorPenalizaciones: number;
  mejorTiempoFinalMs: number;
  etiquetaMejorRonda: string;
}


@Injectable({
  providedIn: 'root'
})
export class IndividualRoundService {

  private http = inject(HttpClient);

  // --- ¡NUEVO MÉTODO PARA VELOCISTA! ---
  /**
   * GET /api/v1/rondas-individuales/posiciones/{categoriaTipo}
   */
  getLeaderboard(categoryId: string): Observable<LeaderboardEntryDTO[]> {
    return this.http.get<LeaderboardEntryDTO[]>(`/rondas-individuales/posiciones/${categoryId}`);
  }

  /**
   * POST /api/v1/rondas-individuales
   * Crea una nueva ronda (intento) para un equipo.
   */
  createRound(payload: CreateRondaIndividualDTO): Observable<RondaIndividualDTO> {
    return this.http.post<RondaIndividualDTO>('/rondas-individuales', payload);
  }

  /**
   * GET /api/v1/rondas-individuales/categoria/{categoriaTipo}
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
   */
  updateRound(roundId: string, payload: UpdateRondaIndividualDTO): Observable<RondaIndividualDTO> {
    return this.http.put<RondaIndividualDTO>(`/rondas-individuales/${roundId}`, payload);
  }
}