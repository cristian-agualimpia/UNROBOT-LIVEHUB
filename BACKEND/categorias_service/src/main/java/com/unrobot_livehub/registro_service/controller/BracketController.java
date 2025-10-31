package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.dtos.BracketPayload; // <-- ¡IMPORTA EL NUEVO DTO!
import com.unrobot_livehub.registro_service.service.BracketService;
// import lombok.Data; // <-- Ya no es necesario aquí
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}