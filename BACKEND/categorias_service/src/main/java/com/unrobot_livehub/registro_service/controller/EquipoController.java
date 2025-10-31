package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.dtos.EquipoDTO;
import com.unrobot_livehub.registro_service.dtos.EquipoRegistroDTO;
import com.unrobot_livehub.registro_service.service.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    /**
     * Registra un nuevo equipo en una categoría.
     */
    @PostMapping
    public ResponseEntity<EquipoDTO> createEquipo(
            @RequestBody EquipoRegistroDTO equipoRegistroDTO // <-- Acepta el DTO de REGISTRO
    ) {
        EquipoDTO nuevoEquipo = equipoService.createEquipo(equipoRegistroDTO);
        return new ResponseEntity<>(nuevoEquipo, HttpStatus.CREATED);
    }

    /**
     * Obtiene un equipo específico por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EquipoDTO> getEquipoById(@PathVariable UUID id) {
        EquipoDTO equipo = equipoService.getEquipoById(id);
        return ResponseEntity.ok(equipo);
    }

    /**
     * Obtiene la lista de todos los equipos registrados.
     */
    @GetMapping
    public ResponseEntity<List<EquipoDTO>> getAllEquipos() {
        List<EquipoDTO> equipos = equipoService.getAllEquipos();
        return ResponseEntity.ok(equipos);
    }

    /**
     * --- ¡NUEVO ENDPOINT! ---
     * Obtiene la lista de equipos filtrados por categoría.
     *
     * Ej: GET /api/v1/equipos/categoria/BOLABOT
     */
    @GetMapping("/categoria/{categoriaTipo}")
    public ResponseEntity<List<EquipoDTO>> getEquiposPorCategoria(
            @PathVariable String categoriaTipo
    ) {
        List<EquipoDTO> equipos = equipoService.getEquiposPorCategoria(categoriaTipo);
        return ResponseEntity.ok(equipos);
    }
}