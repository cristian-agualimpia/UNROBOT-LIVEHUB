import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Modelo "Rico" (para el formulario)
import { EnfrentamientoDTO } from '../../../core/models/enfrentamiento.model'; 
// Modelo "Plano" (para la lista)
import { EnfrentamientoListItemDTO } from '../../../core/models/enfrentamiento-list-item.model';

// CORRECCIÓN: 'action' ahora acepta 'ADD_NOTE'
export interface UpdateScoreDTO {
  team: 'A' | 'B';
  action: 'INCREMENT_POINT' | 'DECREMENT_POINT' | 'ADD_NOTE';
  note?: string | null; 
}

export interface BracketActionDTO {
  categoriaTipo: string;
}

@Injectable({
  providedIn: 'root'
})
export class ConfrontationService {

  private http = inject(HttpClient);

  getMatchesByCategory(categoryId: string): Observable<EnfrentamientoListItemDTO[]> {
    return this.http.get<EnfrentamientoListItemDTO[]>(`/enfrentamientos/categoria/${categoryId}`);
  }

  generateBrackets(categoryId: string): Observable<void> {
    const payload: BracketActionDTO = { categoriaTipo: categoryId };
    return this.http.post<void>(`/bracket/generar-inicial`, payload);
  }

  advanceRound(categoryId: string): Observable<void> {
    const payload: BracketActionDTO = { categoriaTipo: categoryId };
    return this.http.post<void>(`/bracket/avanzar-ronda`, payload);
  }

  getMatchById(matchId: string): Observable<EnfrentamientoDTO> {
    return this.http.get<EnfrentamientoDTO>(`/enfrentamientos/${matchId}`);
  }

  /**
   * Este endpoint ahora maneja Puntos Y Notas,
   * dependiendo del 'payload' que se envíe.
   */
  updateScore(matchId: string, payload: UpdateScoreDTO): Observable<EnfrentamientoDTO> {
    return this.http.put<EnfrentamientoDTO>(`/enfrentamientos/${matchId}/score`, payload);
  }

  declareWinner(matchId: string, winnerId: string): Observable<EnfrentamientoDTO> {
    return this.http.put<EnfrentamientoDTO>(`/enfrentamientos/${matchId}/winner?ganadorId=${winnerId}`, {});
  }
}