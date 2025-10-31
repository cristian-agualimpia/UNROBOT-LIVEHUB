import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// --- Modelos de Datos ---

/**
 * DTO para enviar al backend al crear un nuevo equipo.
 * Sincronizado con el JSON de prueba.
 * CORRECCIÓN: Añadido 'export'
 */
export interface CreateTeamDTO {
  nombre: string;
  institucion?: string;
  categoriaTipo: string;
  nombreCapitan: string;
  emailCapitan: string;
  telefonoCapitan?: string;
  miembros: string[];
}

/**
 * DTO que representa un Equipo (respuesta de la API).
 * Usado para GET /equipos/{id} y como respuesta de POST /equipos
 * CORRECCIÓN: Añadido 'export'
 */
export interface EquipoDTO {
  id: string; // Es un UUID en el backend
  nombre: string;
  institucion: string;
  categoriaTipo: string;
}


// --- Servicio ---

@Injectable({
  providedIn: 'root'
})
export class TeamService {

  private http = inject(HttpClient);

  /**
   * POST /api/v1/equipos
   * Registra un nuevo equipo en el sistema.
   * Envía el DTO de creación completo.
   */
  registerTeam(payload: CreateTeamDTO): Observable<EquipoDTO> {
    return this.http.post<EquipoDTO>('/equipos', payload);
  }

  /**
   * GET /api/v1/equipos/{id}
   * Obtiene un equipo específico por su UUID.
   */
  getTeamById(id: string): Observable<EquipoDTO> {
    return this.http.get<EquipoDTO>(`/equipos/${id}`);
  }
// ... (imports y registerTeam, getTeamById) ...

  
  // --- ¡MÉTODO AÑADIDO! ---
  /**
   * GET /api/v1/equipos/categoria/{categoriaTipo}
   * Obtiene la lista de equipos inscritos en una categoría.
   */
  getTeamsByCategory(categoryId: string): Observable<EquipoDTO[]> {
    return this.http.get<EquipoDTO[]>(`/equipos/categoria/${categoryId}`);
  }
}