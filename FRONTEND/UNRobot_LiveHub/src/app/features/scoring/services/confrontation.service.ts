import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// CORRECCIÓN DE RUTA: ../../
import { EnfrentamientoDTO } from '../../../core/models/enfrentamiento.model'; 
import { EnfrentamientoListItemDTO } from '../../../core/models/enfrentamiento-list-item.model';

// DTO para /score (Puntos y Notas)
export interface UpdateScoreDTO {
  team: 'A' | 'B';
  action: 'INCREMENT_POINT' | 'DECREMENT_POINT' | 'ADD_NOTE';
  note?: string | null; 
}

// DTO para /bracket
export interface BracketActionDTO {
  categoriaTipo: string;
}

// DTO para /enfrentamientos/{id}/registrar-tiempos
export interface VelocistaTiemposPayload {
  tiempoEquipoA_ms: number;
  tiempoEquipoB_ms: number;
}


@Injectable({
  providedIn: 'root'
})
export class ConfrontationService {

  private http = inject(HttpClient);

  /**
   * GET /api/v1/enfrentamientos/categoria/{categoriaTipo}
   */
  getMatchesByCategory(categoryId: string): Observable<EnfrentamientoListItemDTO[]> {
    return this.http.get<EnfrentamientoListItemDTO[]>(`/enfrentamientos/categoria/${categoryId}`);
  }

  /**
   * POST /api/v1/bracket/generar-inicial
   */
  generateBrackets(categoryId: string): Observable<void> {
    const payload: BracketActionDTO = { categoriaTipo: categoryId };
    return this.http.post<void>(`/bracket/generar-inicial`, payload);
  }

  /**
   * POST /api/v1/bracket/avanzar-ronda
   */
  advanceRound(categoryId: string): Observable<void> {
    const payload: BracketActionDTO = { categoriaTipo: categoryId };
    return this.http.post<void>(`/bracket/avanzar-ronda`, payload);
  }

  /**
   * GET /api/v1/enfrentamientos/{id}
   */
  getMatchById(matchId: string): Observable<EnfrentamientoDTO> {
    return this.http.get<EnfrentamientoDTO>(`/enfrentamientos/${matchId}`);
  }

  /**
   * PUT /api/v1/enfrentamientos/{id}/score
   */
  updateScore(matchId: string, payload: UpdateScoreDTO): Observable<EnfrentamientoDTO> {
    return this.http.put<EnfrentamientoDTO>(`/enfrentamientos/${matchId}/score`, payload);
  }

  /**
   * PUT /api/v1/enfrentamientos/{id}/winner?ganadorId=...
   */
  declareWinner(matchId: string, winnerId: string): Observable<EnfrentamientoDTO> {
    return this.http.put<EnfrentamientoDTO>(`/enfrentamientos/${matchId}/winner?ganadorId=${winnerId}`, {});
  }
  
  /**
   * POST /api/v1/bracket/generar-final-velocista
   */
  generateVelocistaFinals(categoryId: string): Observable<void> {
    const payload: BracketActionDTO = { categoriaTipo: categoryId };
    return this.http.post<void>(`/bracket/generar-final-velocista`, payload);
  }

  /**
   * PUT /api/v1/enfrentamientos/{id}/registrar-tiempos
   */
  registerVelocistaTimes(matchId: string, payload: VelocistaTiemposPayload): Observable<EnfrentamientoDTO> {
    return this.http.put<EnfrentamientoDTO>(`/enfrentamientos/${matchId}/registrar-tiempos`, payload);
  }
}