package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.EnfrentamientoDTO;
import com.unrobot_livehub.registro_service.dtos.ScoreUpdatePayload;
import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.Enfrentamiento;
import com.unrobot_livehub.registro_service.entity.Equipo; // <-- ¡IMPORTAR EQUIPO!
import com.unrobot_livehub.registro_service.repository.EnfrentamientoRepository;
import com.unrobot_livehub.registro_service.repository.EquipoRepository; // <-- ¡IMPORTAR REPO!
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional; // <-- ¡IMPORTAR OPTIONAL!
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnfrentamientoService {

    @Autowired
    private EnfrentamientoRepository enfrentamientoRepository;
    
    @Autowired
    private EquipoRepository equipoRepository; // <-- ¡INYECCIÓN AÑADIDA!

    
    /**
     * --- ¡NUEVO ENDPOINT! ---
     * Obtiene un solo enfrentamiento por su ID, incluyendo nombres de equipos.
     */
    public EnfrentamientoDTO getEnfrentamientoById(UUID matchId) {
        Enfrentamiento match = enfrentamientoRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Enfrentamiento no encontrado: " + matchId));
        return convertToDTO(match); // Reutilizamos el conversor
    }
    
    /**
     * Lógica clave de puntuación.
     */
    public EnfrentamientoDTO updateMatchScore(UUID matchId, ScoreUpdatePayload payload) {
        Enfrentamiento match = enfrentamientoRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Enfrentamiento no encontrado: " + matchId));

        // ... (la lógica del switch 'action' sigue igual) ...
        switch (payload.getAction()) {
            case "INCREMENT_POINT":
                if ("A".equals(payload.getTeam())) {
                    match.setPuntosA(match.getPuntosA() + 1);
                } else if ("B".equals(payload.getTeam())) {
                    match.setPuntosB(match.getPuntosB() + 1);
                }
                break;
            case "ADD_NOTE":
                String notaActual = StringUtils.hasText(match.getFaltasNotas()) ? match.getFaltasNotas() : "";
                match.setFaltasNotas(notaActual + " [" + java.time.LocalTime.now() + "] " + payload.getNote() + "; ");
                break;
            default:
                throw new IllegalArgumentException("Acción no válida: " + payload.getAction());
        }

        Enfrentamiento matchActualizado = enfrentamientoRepository.save(match);
        return convertToDTO(matchActualizado); // Usa el conversor actualizado
    }
    
    /**
     * Establece el ganador de un enfrentamiento.
     */
    public EnfrentamientoDTO setGanador(UUID matchId, UUID ganadorId) {
        Enfrentamiento match = enfrentamientoRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Enfrentamiento no encontrado: " + matchId));
        
        if (!ganadorId.equals(match.getIdEquipoA()) && (match.getIdEquipoB() != null && !ganadorId.equals(match.getIdEquipoB()))) {
             throw new IllegalArgumentException("El ganador debe ser uno de los equipos del enfrentamiento.");
        }
        
        match.setIdGanador(ganadorId);
        Enfrentamiento matchActualizado = enfrentamientoRepository.save(match);
        return convertToDTO(matchActualizado); // Usa el conversor actualizado
    }

    /**
     * Obtiene todos los enfrentamientos de una categoría.
     */
    public List<EnfrentamientoDTO> getEnfrentamientosByCategoria(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo.toUpperCase());
        List<Enfrentamiento> enfrentamientos = enfrentamientoRepository.findByCategoria(categoria);
        
        // Convertimos la lista usando el DTO actualizado
        return enfrentamientos.stream()
                .map(this::convertToDTO) // Llama al conversor por cada item
                .collect(Collectors.toList());
    }

    // --- Métodos de Conversión (¡ACTUALIZADO!) ---

    /**
     * Conversor actualizado para incluir nombres de equipos.
     */
    private EnfrentamientoDTO convertToDTO(Enfrentamiento match) {
        
        // Buscamos los nombres de los equipos
        String nombreA = equipoRepository.findById(match.getIdEquipoA())
                            .map(Equipo::getNombre) // Extrae el nombre si el equipo existe
                            .orElse("Equipo No Encontrado"); // Texto alternativo
        
        String nombreB = null;
        if (match.getIdEquipoB() != null) {
            nombreB = equipoRepository.findById(match.getIdEquipoB())
                            .map(Equipo::getNombre)
                            .orElse("Equipo No Encontrado");
        } else {
            nombreB = "BYE"; // Si el idEquipoB es null, es un BYE
        }

        return new EnfrentamientoDTO(
                match.getId(),
                match.getCategoria().name(),
                match.getIdEquipoA(),
                match.getIdEquipoB(),
                nombreA, // ¡Nombre A añadido!
                nombreB, // ¡Nombre B añadido!
                match.getPuntosA(),
                match.getPuntosB(),
                match.getIdGanador(),
                match.getEtiquetaRonda(),
                match.getFaltasNotas()
        );
    }
}