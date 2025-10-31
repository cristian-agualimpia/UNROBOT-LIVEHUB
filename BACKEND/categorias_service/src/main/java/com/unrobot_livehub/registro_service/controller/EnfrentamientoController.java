package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.dtos.EnfrentamientoDTO;
import com.unrobot_livehub.registro_service.dtos.ScoreUpdatePayload;
import com.unrobot_livehub.registro_service.service.EnfrentamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enfrentamientos")
public class EnfrentamientoController {

    @Autowired
    private EnfrentamientoService enfrentamientoService;

    /**
     * ¡Endpoint Clave! Usado por el juez para reportar un evento
     * (gol, falta, etc.) en un enfrentamiento.
     */
    @PutMapping("/{id}/score")
    public ResponseEntity<EnfrentamientoDTO> updateScore(
            @PathVariable UUID id,
            @RequestBody ScoreUpdatePayload payload
    ) {
        EnfrentamientoDTO updatedMatch = enfrentamientoService.updateMatchScore(id, payload);
        return ResponseEntity.ok(updatedMatch);
    }

    /**
     * Endpoint para que el juez o admin establezca manualmente el ganador.
     * El ID del ganador se pasa como parámetro de consulta.
     * Ej: PUT /api/v1/enfrentamientos/{matchId}/winner?ganadorId={teamId}
     */
    @PutMapping("/{id}/winner")
    public ResponseEntity<EnfrentamientoDTO> setWinner(
            @PathVariable UUID id,
            @RequestParam UUID ganadorId
    ) {
        EnfrentamientoDTO updatedMatch = enfrentamientoService.setGanador(id, ganadorId);
        return ResponseEntity.ok(updatedMatch);
    }

    /**
     * Obtiene todos los enfrentamientos de una categoría.
     * Usado por el frontend para dibujar el árbol de llaves.
     */
    @GetMapping("/categoria/{categoriaTipo}")
    public ResponseEntity<List<EnfrentamientoDTO>> getEnfrentamientosPorCategoria(
            @PathVariable String categoriaTipo
    ) {
        List<EnfrentamientoDTO> enfrentamientos = enfrentamientoService.getEnfrentamientosByCategoria(categoriaTipo);
        return ResponseEntity.ok(enfrentamientos);
    }
}