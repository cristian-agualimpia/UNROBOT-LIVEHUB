package com.unrobot_livehub.categorias_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unrobot_livehub.categorias_service.dtos.EnfrentamientoDTO;
import com.unrobot_livehub.categorias_service.dtos.EquipoDTO;
import com.unrobot_livehub.categorias_service.dtos.EquipoRegistroDTO;
import com.unrobot_livehub.categorias_service.dtos.EquipoUpdateDTO;
import com.unrobot_livehub.categorias_service.entity.CategoriaTipo;
import com.unrobot_livehub.categorias_service.entity.Enfrentamiento;
import com.unrobot_livehub.categorias_service.entity.Equipo;
import com.unrobot_livehub.categorias_service.repository.EquipoRepository;
import com.unrobot_livehub.categorias_service.repository.EnfrentamientoRepository;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired 
    private EnfrentamientoRepository enfrentamientoRepository;

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

    @Transactional
    public Equipo actualizarEquipo(UUID equipoId, EquipoUpdateDTO dto) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado"));

        equipo.setNombre(dto.getNombre().trim());
        if (dto.getInstitucion() != null) equipo.setInstitucion(dto.getInstitucion().trim());
        if (dto.getMiembros() != null) equipo.setMiembros(dto.getMiembros());
        if (dto.getDescripcion() != null) equipo.setDescripcion(dto.getDescripcion());

        // Lógica de cambio de categoría
        if (dto.getCategoriaTipo() != null) {
            CategoriaTipo nuevaCategoria = CategoriaTipo.valueOf(dto.getCategoriaTipo().toUpperCase());
            if (!equipo.getCategoria().equals(nuevaCategoria)) {
                // Verificar si ya tiene partidos en la categoría vieja
                List<Enfrentamiento> partidos = enfrentamientoRepository.findByIdEquipoAOrIdEquipoB(equipoId, equipoId);
                if (!partidos.isEmpty()) {
                    throw new IllegalStateException("No puedes cambiar la categoría porque el equipo ya tiene enfrentamientos generados. Elimínalo de la llave primero.");
                }
                equipo.setCategoria(nuevaCategoria);
            }
        }
        return equipoRepository.save(equipo);
    }

    @Transactional(readOnly = true)
    public List<Equipo> buscarEquipos(String q, String campo) {
        if (q == null || q.trim().isEmpty()) return equipoRepository.findAll();
        String query = q.trim();

        if (campo == null || campo.equalsIgnoreCase("todos")) {
            return equipoRepository.buscarPorCriterio(query);
        }
        
        return switch (campo.toLowerCase()) {
            case "nombre" -> equipoRepository.buscarSoloPorNombre(query);
            case "institucion" -> equipoRepository.buscarSoloPorInstitucion(query);
            // Puedes agregar más casos si sumas las queries al repositorio
            default -> equipoRepository.buscarPorCriterio(query);
        };
    }
}