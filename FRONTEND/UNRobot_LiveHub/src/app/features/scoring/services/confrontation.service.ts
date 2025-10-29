import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnfrentamientoDTO } from '../../../core/models/enfrentamiento.model';

// DTO para la acción de puntaje
export interface UpdateScoreDTO {
  team: 'A' | 'B';
  action: 'INCREMENT_POINT' | 'DECREMENT_POINT'; // Asumimos que la API soporta DECREMENT
}

@Injectable({
  providedIn: 'root'
})
export class ConfrontationService {

  private http = inject(HttpClient);

  /**
   * GET /api/v1/enfrentamientos/categoria/{categoriaTipo}
   */
  getMatchesByCategory(categoryId: string): Observable<EnfrentamientoDTO[]> {
    return this.http.get<EnfrentamientoDTO[]>(`/enfrentamientos/categoria/${categoryId}`);
  }

  /**
   * POST /api/v1/enfrentamientos/generar-llaves/{categoriaTipo}
   */
  generateBrackets(categoryId: string): Observable<void> {
    return this.http.post<void>(`/enfrentamientos/generar-llaves/${categoryId}`, {});
  }

  /**
   * POST /api/v1/enfrentamientos/avanzar-ronda/{categoriaTipo}
   */
  advanceRound(categoryId: string): Observable<void> {
    return this.http.post<void>(`/enfrentamientos/avanzar-ronda/${categoryId}`, {});
  }

  // --- NUEVOS MÉTODOS ---

  /**
   * GET /api/v1/enfrentamientos/{id}
   * Obtiene los datos de un único enfrentamiento por su ID.
   */
  getMatchById(matchId: string): Observable<EnfrentamientoDTO> {
    return this.http.get<EnfrentamientoDTO>(`/enfrentamientos/${matchId}`);
  }

  /**
   * PUT /api/v1/enfrentamientos/{id}/score
   * Actualiza el puntaje de un match (incrementa o decrementa).
   */
  updateScore(matchId: string, payload: UpdateScoreDTO): Observable<EnfrentamientoDTO> {
    return this.http.put<EnfrentamientoDTO>(`/enfrentamientos/${matchId}/score`, payload);
  }

  /**
   * PUT /api/v1/enfrentamientos/{id}/winner?ganadorId=...
   * Declara un ganador y finaliza el match.
   */
  declareWinner(matchId: string, winnerId: string): Observable<EnfrentamientoDTO> {
    return this.http.put<EnfrentamientoDTO>(`/enfrentamientos/${matchId}/winner?ganadorId=${winnerId}`, {});
  }
}