package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.EquipoDTO;
import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.Equipo;
import com.unrobot_livehub.registro_service.repository.EquipoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    /**
     * Registra un nuevo equipo.
     */
    public EquipoDTO createEquipo(EquipoDTO equipoDTO) {
        // Validación del Enum
        CategoriaTipo categoria = CategoriaTipo.valueOf(equipoDTO.getCategoriaTipo());
        
        Equipo equipo = new Equipo();
        equipo.setNombre(equipoDTO.getNombre());
        equipo.setInstitucion(equipoDTO.getInstitucion());
        equipo.setCategoria(categoria);

        Equipo equipoGuardado = equipoRepository.save(equipo);
        return convertToDTO(equipoGuardado);
    }

    /**
     * Obtiene un equipo por su ID.
     */
    public EquipoDTO getEquipoById(UUID id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));
        return convertToDTO(equipo);
    }

    /**
     * Obtiene todos los equipos registrados.
     */
    public List<EquipoDTO> getAllEquipos() {
        return equipoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los equipos de una categoría específica.
     * (Usado por BracketService)
     */
    public List<Equipo> getEquiposByCategoria(CategoriaTipo categoria) {
        return equipoRepository.findByCategoria(categoria);
    }

    // --- Métodos de Conversión ---
    
    private EquipoDTO convertToDTO(Equipo equipo) {
        return new EquipoDTO(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getInstitucion(),
                equipo.getCategoria().name() // Convertir Enum a String
        );
    }
}