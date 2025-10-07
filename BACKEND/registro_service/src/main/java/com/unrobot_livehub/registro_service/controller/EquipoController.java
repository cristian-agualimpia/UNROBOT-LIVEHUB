package com.unrobot_livehub.registro_service.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unrobot_livehub.registro_service.dto.EquipoDTO;
import com.unrobot_livehub.registro_service.service.EquipoService;

@RestController
@RequestMapping("/api/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @PostMapping
    public ResponseEntity<EquipoDTO> crearEquipo(@Valid @RequestBody EquipoDTO equipoDTO) {
        EquipoDTO nuevoEquipo = equipoService.crearEquipo(equipoDTO);
        return new ResponseEntity<>(nuevoEquipo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EquipoDTO>> obtenerTodosLosEquipos() {
        List<EquipoDTO> equipos = equipoService.obtenerTodosLosEquipos();
        return ResponseEntity.ok(equipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoDTO> obtenerEquipoPorId(@PathVariable Long id) {
        EquipoDTO equipo = equipoService.obtenerEquipoPorId(id);
        return ResponseEntity.ok(equipo);
    }

    @GetMapping("/institucion/{institucionId}")
    public ResponseEntity<List<EquipoDTO>> obtenerEquiposPorInstitucion(@PathVariable Long institucionId) {
        List<EquipoDTO> equipos = equipoService.obtenerEquiposPorInstitucion(institucionId);
        return ResponseEntity.ok(equipos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoDTO> actualizarEquipo(@PathVariable Long id, @Valid @RequestBody EquipoDTO equipoDTO) {
        EquipoDTO equipoActualizado = equipoService.actualizarEquipo(id, equipoDTO);
        return ResponseEntity.ok(equipoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminarEquipo(id);
        return ResponseEntity.noContent().build();
    }
}