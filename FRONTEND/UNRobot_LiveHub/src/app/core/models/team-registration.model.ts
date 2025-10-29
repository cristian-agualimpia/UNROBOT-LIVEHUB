/**
 * DTO para enviar al backend al crear un nuevo equipo.
 * Basado en HU03.
 */
export interface CreateTeamDTO {
  nombre: string;
  institucion: string;
  nombreCapitan: string;
  emailCapitan: string;
}