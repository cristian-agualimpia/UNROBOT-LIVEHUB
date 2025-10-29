import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateTeamDTO } from '../../../core/models/team-registration.model';
import { EquipoSimpleDTO } from '../../../core/models/equipo.model'; // Asumimos que la respuesta es el equipo creado

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  private http = inject(HttpClient);

  /**
   * POST /api/v1/equipos
   * Registra un nuevo equipo en el sistema.
   * (Asumimos el endpoint /equipos basado en la HU03)
   */
  registerTeam(payload: CreateTeamDTO): Observable<EquipoSimpleDTO> {
    return this.http.post<EquipoSimpleDTO>('/equipos', payload);
  }
}