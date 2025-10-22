package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.service.BracketService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bracket")
@CrossOrigin(origins = "*")
public class BracketController {

    @Autowired
    private BracketService bracketService;

    // --- DTOs internos para los Payloads ---
    @Data static class GenerarLlavesPayload {
        private String categoriaTipo;
        private String etiquetaRonda; // Ej: "Octavos"
    }
    
    @Data static class AvanzarRondaPayload {
        private String categoriaTipo;
        private String rondaActual; // Ej: "Octavos"
    }

    /**
     * Endpoint de Admin para generar la primera ronda aleatoria.
     */
    @PostMapping("/generar-inicial")
    public ResponseEntity<Void> generarLlavesIniciales(
            @RequestBody GenerarLlavesPayload payload
    ) {
        bracketService.generarLlavesIniciales(payload.getCategoriaTipo(), payload.getEtiquetaRonda());
        // 200 OK sin cuerpo
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint de Admin para avanzar a la siguiente ronda (ej: de Octavos a Cuartos).
     */
    @PostMapping("/avanzar-ronda")
    public ResponseEntity<Void> avanzarRonda(
            @RequestBody AvanzarRondaPayload payload
    ) {
        bracketService.avanzarRonda(payload.getCategoriaTipo(), payload.getRondaActual());
        // 200 OK sin cuerpo
        return ResponseEntity.ok().build();
    }
}