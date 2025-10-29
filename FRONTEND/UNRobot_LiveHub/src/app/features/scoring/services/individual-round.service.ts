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

@Injectable({
  providedIn: 'root'
})
export class IndividualRoundService {

  private http = inject(HttpClient);

  /**
   * GET /api/v1/rondas-individuales/categoria/{categoriaTipo}
   */
  getRoundsByCategory(categoryId: string): Observable<RondaIndividualDTO[]> {
    return this.http.get<RondaIndividualDTO[]>(`/rondas-individuales/categoria/${categoryId}`);
  }

  // --- NUEVOS MÉTODOS ---

  /**
   * GET /api/v1/rondas-individuales/{id}
   * Obtiene los datos de una única ronda individual por su ID.
   * (Asumimos que este endpoint existe)
   */
  getRoundById(roundId: string): Observable<RondaIndividualDTO> {
    return this.http.get<RondaIndividualDTO>(`/rondas-individuales/${roundId}`);
  }

  /**
   * PUT /api/v1/rondas-individuales/{id}
   * Actualiza una ronda individual con su puntaje, tiempo y fallos.
   * (Asumimos que este endpoint existe)
   */
  updateRound(roundId: string, payload: UpdateRondaIndividualDTO): Observable<RondaIndividualDTO> {
    return this.http.put<RondaIndividualDTO>(`/rondas-individuales/${roundId}`, payload);
  }
}