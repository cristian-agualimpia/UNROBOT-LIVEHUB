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

    // --- DTO interno SIMPLIFICADO ---
    @Data static class GenerarLlavesPayload {
        private String categoriaTipo;
        // --- 'etiquetaRonda' HA SIDO ELIMINADA DE AQUÍ ---
    }
    
    @Data static class AvanzarRondaPayload {
        private String categoriaTipo;
        private String rondaActual; // Ej: "Octavos"
    }

    /**
     * Endpoint de Admin para generar la primera ronda aleatoria.
     * ¡AHORA ES AUTOMÁTICO!
     */
    @PostMapping("/generar-inicial")
    public ResponseEntity<Void> generarLlavesIniciales(
            @RequestBody GenerarLlavesPayload payload // El payload ya no trae la etiqueta
    ) {
        // El servicio AHORA hace todo el trabajo
        bracketService.generarLlavesIniciales(payload.getCategoriaTipo());
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