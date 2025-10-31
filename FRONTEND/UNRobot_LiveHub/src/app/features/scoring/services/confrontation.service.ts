import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { EnfrentamientoDTO } from '../../../core/models/enfrentamiento.model';

// DTO para la acción de puntaje
export interface UpdateScoreDTO {
  team: 'A' | 'B';
  action: 'INCREMENT_POINT' | 'DECREMENT_POINT';
}

// --- NUEVO DTO ---
// DTO para los endpoints del bracket (generar/avanzar)
export interface BracketActionDTO {
  categoriaTipo: string;
}

@Injectable({
  providedIn: 'root'
})
export class ConfrontationService {

  private http = inject(HttpClient);

  /**
   * GET /api/v1/enfrentamientos/categoria/{categoriaTipo}
   * Obtiene la lista de todos los enfrentamientos para una categoría.
   */
  getMatchesByCategory(categoryId: string): Observable<EnfrentamientoDTO[]> {
    return this.http.get<EnfrentamientoDTO[]>(`/enfrentamientos/categoria/${categoryId}`);
  }

  // --- MÉTODO CORREGIDO ---
  /**
   * POST /api/v1/bracket/generar-inicial
   * Solicita al backend que genere las llaves (brackets) iniciales.
   * Envía un body: { "categoriaTipo": "..." }
   */
  generateBrackets(categoryId: string): Observable<void> {
    const payload: BracketActionDTO = { categoriaTipo: categoryId };
    // El interceptor añade /api/v1
    return this.http.post<void>(`/bracket/generar-inicial`, payload);
  }

  // --- MÉTODO CORREGIDO (Asumido) ---
  /**
   * POST /api/v1/bracket/avanzar-ronda
   * Le dice al backend que finalice la ronda actual y genere la siguiente.
   * Asumimos que sigue el mismo patrón que /generar-inicial
   */
  advanceRound(categoryId: string): Observable<void> {
    const payload: BracketActionDTO = { categoriaTipo: categoryId };
    // El interceptor añade /api/v1
    return this.http.post<void>(`/bracket/avanzar-ronda`, payload);
  }

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