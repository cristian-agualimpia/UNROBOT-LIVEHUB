package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.dtos.RondaIndividualDTO;
import com.unrobot_livehub.registro_service.service.RondaIndividualService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rondas-individuales")
public class RondaIndividualController {

    @Autowired
    private RondaIndividualService rondaVelocistaService;

    /**
     * ¡Endpoint Clave! Usado por el juez de Seguidor de Línea
     * para registrar el tiempo y penalizaciones de una ronda.
     */
    @PostMapping
    public ResponseEntity<RondaIndividualDTO> createRonda(
            @RequestBody RondaIndividualDTO rondaDTO
    ) {
        RondaIndividualDTO nuevaRonda = rondaVelocistaService.createRonda(rondaDTO);
        return new ResponseEntity<>(nuevaRonda, HttpStatus.CREATED);
    }

    /**
     * Obtiene la tabla de posiciones (leaderboard) para una
     * categoría de velocista, ordenada por el mejor tiempo final.
     */
    @GetMapping("/posiciones/{categoriaTipo}")
    public ResponseEntity<List<RondaIndividualDTO>> getPosiciones(
            @PathVariable String categoriaTipo
    ) {
        List<RondaIndividualDTO> posiciones = rondaVelocistaService.getPosiciones(categoriaTipo);
        return ResponseEntity.ok(posiciones);
    }
}