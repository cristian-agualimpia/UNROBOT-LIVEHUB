package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.dtos.EquipoDTO;
import com.unrobot_livehub.registro_service.service.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    /**
     * Registra un nuevo equipo en una categoría.
     */
    @PostMapping
    public ResponseEntity<EquipoDTO> createEquipo(@RequestBody EquipoDTO equipoDTO) {
        EquipoDTO nuevoEquipo = equipoService.createEquipo(equipoDTO);
        // Retornamos 201 CREATED
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
}