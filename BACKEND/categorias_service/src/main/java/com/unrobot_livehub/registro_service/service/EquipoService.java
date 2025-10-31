package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.EquipoDTO;
import com.unrobot_livehub.registro_service.dtos.EquipoRegistroDTO;
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
    public EquipoDTO createEquipo(EquipoRegistroDTO registroDTO) { // <-- Recibe DTO de Registro
        CategoriaTipo categoria = CategoriaTipo.valueOf(registroDTO.getCategoriaTipo().toUpperCase());
        
        Equipo equipo = new Equipo();
        
        // Mapeo COMPLETO desde el DTO de Registro a la Entidad
        equipo.setNombre(registroDTO.getNombre());
        equipo.setInstitucion(registroDTO.getInstitucion());
        equipo.setCategoria(categoria);
        equipo.setNombreCapitan(registroDTO.getNombreCapitan());
        equipo.setEmailCapitan(registroDTO.getEmailCapitan());
        equipo.setTelefonoCapitan(registroDTO.getTelefonoCapitan());
        equipo.setMiembros(registroDTO.getMiembros());

        Equipo equipoGuardado = equipoRepository.save(equipo);
        
        // Devuelve la versión PÚBLICA (usando el convertToDTO de abajo)
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