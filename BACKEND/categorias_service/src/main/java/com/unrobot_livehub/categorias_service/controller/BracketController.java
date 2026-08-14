package com.unrobot_livehub.categorias_service.controller;

// import lombok.Data; // <-- Ya no es necesario aquí
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.unrobot_livehub.categorias_service.dtos.BracketPayload;
import com.unrobot_livehub.categorias_service.service.BracketService;

@RestController
@RequestMapping("/api/v1/bracket")
public class BracketController {

    @Autowired
    private BracketService bracketService;

    // --- DTO interno ---
    // @Data static class BracketPayload { ... } // <-- ¡BORRA LA CLASE INTERNA DE AQUÍ!
    

    /**
     * Endpoint de Admin para generar la primera ronda aleatoria (automática).
     */
    @PostMapping("/generar-inicial")
    public ResponseEntity<Void> generarLlavesIniciales(
            @RequestBody BracketPayload payload // <-- Esto ahora usa el DTO importado
    ) {
        bracketService.generarLlavesIniciales(payload.getCategoriaTipo());
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint de Admin para avanzar a la siguiente ronda (automático).
     */
    @PostMapping("/avanzar-ronda")
    public ResponseEntity<Void> avanzarRonda(
            @RequestBody BracketPayload payload // <-- Esto ahora usa el DTO importado
    ) {
        bracketService.avanzarRonda(payload.getCategoriaTipo());
        return ResponseEntity.ok().build();
    }
/**
     * --- ¡NUEVO ENDPOINT! ---
     * Endpoint de Admin para generar la FINAL de 4 de una categoría velocista.
     * Lee el Top 4 del leaderboard y crea 2 enfrentamientos de semifinal.
     */
    @PostMapping("/generar-final-velocista")
    public ResponseEntity<Void> generarFinalVelocista(
            @RequestBody BracketPayload payload
    ) {
        bracketService.generarLlavesFinalesVelocista(payload.getCategoriaTipo());
        return ResponseEntity.ok().build();
    }
}