package com.unrobot_livehub.categorias_service.dtos;

public enum TipoBorradoEquipoResult {
    HARD_DELETE,          // Se borró físicamente de la BD (sin partidos asociados)
    REEMPLAZADO_POR_BYE,  // Tenía partidos pendientes; se desvinculó y reemplazó su cupo
    BLOQUEADO             // Ya jugó partidos; no se puede eliminar
}