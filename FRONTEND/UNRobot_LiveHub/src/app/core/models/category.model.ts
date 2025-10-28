/**
 * DTO que representa la información estática de una categoría.
 * Basado en GET /api/v1/categorias-info
 */
export interface CategoriaInfoDTO {
  tipo: string; // Ej: "BOLABOT"
  tipoLogica: 'ENFRENTAMIENTO' | 'RONDA_INDIVIDUAL';
  nombreCompleto: string;
  resumenDescriptivo: string;
  descripcionPista: string;
  publicoDirigido: string;
  integrantesMax: number;
  reglasResumen: string;
}