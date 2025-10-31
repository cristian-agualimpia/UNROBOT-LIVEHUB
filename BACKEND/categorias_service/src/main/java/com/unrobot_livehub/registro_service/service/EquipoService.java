package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.EquipoDTO; // El DTO público
import com.unrobot_livehub.registro_service.dtos.EquipoRegistroDTO; // El DTO de registro
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
     * Recibe el DTO de Registro, Devuelve el DTO Público.
     */
    public EquipoDTO createEquipo(EquipoRegistroDTO registroDTO) {
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
        
        // Devuelve la versión PÚBLICA
        return convertToDTO(equipoGuardado);
    }

    /**
     * Obtiene un equipo por su ID (vista pública).
     */
    public EquipoDTO getEquipoById(UUID id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con id: " + id));
        return convertToDTO(equipo);
    }

    /**
     * Obtiene todos los equipos (vista pública).
     */
    public List<EquipoDTO> getAllEquipos() {
        return equipoRepository.findAll().stream()
                .map(this::convertToDTO) // Usa el conversor público
                .collect(Collectors.toList());
    }

    /**
     * --- ¡NUEVO MÉTODO! ---
     * Obtiene todos los equipos (DTOs públicos) de una categoría específica.
     * (Usado por el Frontend para filtrar listas de competidores).
     */
    public List<EquipoDTO> getEquiposByCategoriaDTO(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo.toUpperCase());
        List<Equipo> equipos = equipoRepository.findByCategoria(categoria);
        
        return equipos.stream()
                .map(this::convertToDTO) // Reutiliza el conversor DTO público
                .collect(Collectors.toList());
    }

    /**
     * (SIN CAMBIOS - Usado por BracketService)
     * Obtiene la entidad Equipo (no DTO) por categoría.
     */
    public List<Equipo> getEquiposByCategoria(CategoriaTipo categoria) {
        return equipoRepository.findByCategoria(categoria);
    }

    
    /**
     * Conversor al DTO Público (el EquipoDTO que ya tenías).
     */
    /**
     * --- ¡NUEVO MÉTODO! ---
     * Obtiene todos los equipos de una categoría específica (vista pública).
     * Esto es para que el frontend pueda poblar listas desplegables.
     *
     * @param categoriaTipo El String de la categoría (ej: "SEGUIDOR_LINEA_AMATEUR")
     * @return Una lista de DTOs públicos (EquipoDTO)
     */
    public List<EquipoDTO> getEquiposPorCategoria(String categoriaTipo) {
        // 1. Convertir String a Enum
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo.toUpperCase());
        
        // 2. Obtener las entidades de la BD (usando el método del repositorio)
        List<Equipo> equipos = equipoRepository.findByCategoria(categoria);
        
        // 3. Convertir la lista de Entidades a una lista de DTOs públicos
        return equipos.stream()
                .map(this::convertToDTO) // Reutiliza tu conversor a DTO público
                .collect(Collectors.toList());
    }
    

    /**
     * Conversor al DTO Público (el EquipoDTO que ya tenías).
     * (Asegúrate de que oculte los datos sensibles)
     */
    private EquipoDTO convertToDTO(Equipo equipo) {
        EquipoDTO dto = new EquipoDTO();
        dto.setId(equipo.getId());
        dto.setNombre(equipo.getNombre());
        dto.setInstitucion(equipo.getInstitucion());
        dto.setCategoriaTipo(equipo.getCategoria().name());
        
        // (Campos sensibles como email/teléfono NO se incluyen aquí)
        
        return dto;
    }
}