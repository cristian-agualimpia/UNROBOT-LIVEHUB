package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.dtos.EnfrentamientoDTO;
import com.unrobot_livehub.registro_service.dtos.ScoreUpdatePayload;
import com.unrobot_livehub.registro_service.service.EnfrentamientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unrobot_livehub.registro_service.dtos.VelocistaTiemposPayload;

import java.util.List;
import java.util.UUID; // <-- ¡Asegúrate de importar UUID!

@RestController
@RequestMapping("/api/v1/enfrentamientos")
// (Recuerda quitar @CrossOrigin si estás usando WebConfig)
public class EnfrentamientoController {

    @Autowired
    private EnfrentamientoService enfrentamientoService;

    
    /**
     * --- ¡NUEVO ENDPOINT! ---
     * Obtiene un enfrentamiento específico por su ID.
     * Útil para que el juez cargue los datos de un partido.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnfrentamientoDTO> getEnfrentamientoById(@PathVariable UUID id) {
        EnfrentamientoDTO enfrentamiento = enfrentamientoService.getEnfrentamientoById(id);
        return ResponseEntity.ok(enfrentamiento);
    }
    
    /**
     * ¡Endpoint Clave! Usado por el juez para reportar un evento.
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
     * Obtiene todos los enfrentamientos de una categoría (para las llaves).
     */
    @GetMapping("/categoria/{categoriaTipo}")
    public ResponseEntity<List<EnfrentamientoDTO>> getEnfrentamientosPorCategoria(
            @PathVariable String categoriaTipo
    ) {
        List<EnfrentamientoDTO> enfrentamientos = enfrentamientoService.getEnfrentamientosByCategoria(categoriaTipo);
        return ResponseEntity.ok(enfrentamientos);
    }

    /**
     * --- ¡NUEVO ENDPOINT! ---
     * Endpoint de Juez para registrar los TIEMPOS de una final velocista (1v1).
     * El backend determina el ganador basado en el tiempo más bajo.
     */
    @PutMapping("/{id}/registrar-tiempos")
    public ResponseEntity<EnfrentamientoDTO> registrarTiemposVelocista(
            @PathVariable UUID id,
            @RequestBody VelocistaTiemposPayload payload
    ) {
        EnfrentamientoDTO updatedMatch = enfrentamientoService.registrarTiemposVelocista(id, payload);
        return ResponseEntity.ok(updatedMatch);
    }
}