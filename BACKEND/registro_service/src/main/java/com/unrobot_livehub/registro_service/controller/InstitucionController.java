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

import com.unrobot_livehub.registro_service.dto.InstitucionDTO;
import com.unrobot_livehub.registro_service.service.InstitucionService;

@RestController
@RequestMapping("/api/instituciones")
public class InstitucionController {

    @Autowired
    private InstitucionService institucionService;

    @PostMapping
    public ResponseEntity<InstitucionDTO> crearInstitucion(@Valid @RequestBody InstitucionDTO institucionDTO) {
        InstitucionDTO nuevaInstitucion = institucionService.crearInstitucion(institucionDTO);
        return new ResponseEntity<>(nuevaInstitucion, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<InstitucionDTO>> obtenerTodasLasInstituciones() {
        List<InstitucionDTO> instituciones = institucionService.obtenerTodasLasInstituciones();
        return ResponseEntity.ok(instituciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstitucionDTO> obtenerInstitucionPorId(@PathVariable Long id) {
        InstitucionDTO institucion = institucionService.obtenerInstitucionPorId(id);
        return ResponseEntity.ok(institucion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitucionDTO> actualizarInstitucion(@PathVariable Long id, @Valid @RequestBody InstitucionDTO institucionDTO) {
        InstitucionDTO institucionActualizada = institucionService.actualizarInstitucion(id, institucionDTO);
        return ResponseEntity.ok(institucionActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInstitucion(@PathVariable Long id) {
        institucionService.eliminarInstitucion(id);
        return ResponseEntity.noContent().build();
    }
}