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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unrobot_livehub.registro_service.dto.MiembroEquipoDTO;
import com.unrobot_livehub.registro_service.service.MiembroEquipoService;

@RestController
@RequestMapping("/api/miembros-equipo")
public class MiembroEquipoController {

    @Autowired
    private MiembroEquipoService miembroEquipoService;

    @PostMapping
    public ResponseEntity<MiembroEquipoDTO> agregarMiembro(@Valid @RequestBody MiembroEquipoDTO miembroEquipoDTO) {
        MiembroEquipoDTO nuevoMiembro = miembroEquipoService.agregarMiembro(miembroEquipoDTO);
        return new ResponseEntity<>(nuevoMiembro, HttpStatus.CREATED);
    }

    @GetMapping("/equipo/{equipoId}")
    public ResponseEntity<List<MiembroEquipoDTO>> obtenerMiembrosPorEquipo(@PathVariable Long equipoId) {
        List<MiembroEquipoDTO> miembros = miembroEquipoService.obtenerMiembrosPorEquipo(equipoId);
        return ResponseEntity.ok(miembros);
    }

    @GetMapping("/persona/{personaId}")
    public ResponseEntity<List<MiembroEquipoDTO>> obtenerEquiposPorPersona(@PathVariable Long personaId) {
        List<MiembroEquipoDTO> miembros = miembroEquipoService.obtenerEquiposPorPersona(personaId);
        return ResponseEntity.ok(miembros);
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<MiembroEquipoDTO> actualizarRolMiembro(@PathVariable Long id, @RequestParam String rol) {
        MiembroEquipoDTO miembroActualizado = miembroEquipoService.actualizarRolMiembro(id, rol);
        return ResponseEntity.ok(miembroActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerMiembro(@PathVariable Long id) {
        miembroEquipoService.removerMiembro(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivarMiembro(@PathVariable Long id) {
        miembroEquipoService.desactivarMiembro(id);
        return ResponseEntity.noContent().build();
    }
}