/**
 * DTO para enviar al backend al crear un nuevo equipo.
 * Sincronizado con el JSON de prueba.
 */
export interface CreateTeamDTO {
  nombre: string;
  institucion?: string; // Opcional (en tu entidad es nullable)
  categoriaTipo: string;
  
  // Datos del Capitán
  nombreCapitan: string;
  emailCapitan: string;
  telefonoCapitan?: string; // Opcional (en tu entidad es nullable)
  
  // Lista de Miembros
  miembros: string[];
}