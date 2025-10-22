package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.EnfrentamientoDTO;
import com.unrobot_livehub.registro_service.dtos.ScoreUpdatePayload;
import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.Enfrentamiento;
import com.unrobot_livehub.registro_service.repository.EnfrentamientoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils; // Para chequear strings

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EnfrentamientoService {

    @Autowired
    private EnfrentamientoRepository enfrentamientoRepository;

    /**
     * Lógica clave de puntuación (tu "lógica sucia y rápida").
     * Actualiza puntos o notas basado en el payload.
     */
    public EnfrentamientoDTO updateMatchScore(UUID matchId, ScoreUpdatePayload payload) {
        Enfrentamiento match = enfrentamientoRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Enfrentamiento no encontrado: " + matchId));

        switch (payload.getAction()) {
            case "INCREMENT_POINT":
                if ("A".equals(payload.getTeam())) {
                    match.setPuntosA(match.getPuntosA() + 1);
                } else if ("B".equals(payload.getTeam())) {
                    match.setPuntosB(match.getPuntosB() + 1);
                }
                break;
            
            // (Opcional: puedes añadir decremento)
            // case "DECREMENT_POINT":
            //    if ("A".equals(payload.getTeam()) && match.getPuntosA() > 0) {
            //        match.setPuntosA(match.getPuntosA() - 1);
            //    } else if ("B".equals(payload.getTeam()) && match.getPuntosB() > 0) {
            //        match.setPuntosB(match.getPuntosB() - 1);
            //    }
            //    break;

            case "ADD_NOTE":
                String notaActual = StringUtils.hasText(match.getFaltasNotas()) ? match.getFaltasNotas() : "";
                match.setFaltasNotas(notaActual + " [" + java.time.LocalTime.now() + "] " + payload.getNote() + "; ");
                break;

            default:
                throw new IllegalArgumentException("Acción no válida: " + payload.getAction());
        }

        Enfrentamiento matchActualizado = enfrentamientoRepository.save(match);
        
        // (Aquí iría la llamada al WebSocketService si lo tuviéramos)
        
        return convertToDTO(matchActualizado);
    }
    
    /**
     * Establece el ganador de un enfrentamiento.
     * (Usado por el BracketService o manualmente por un Juez)
     */
    public EnfrentamientoDTO setGanador(UUID matchId, UUID ganadorId) {
        Enfrentamiento match = enfrentamientoRepository.findById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Enfrentamiento no encontrado: " + matchId));
        
        // Validación simple: el ganador debe ser A o B
        if (!ganadorId.equals(match.getIdEquipoA()) && !ganadorId.equals(match.getIdEquipoB())) {
             throw new IllegalArgumentException("El ganador debe ser uno de los equipos del enfrentamiento.");
        }
        
        match.setIdGanador(ganadorId);
        Enfrentamiento matchActualizado = enfrentamientoRepository.save(match);
        return convertToDTO(matchActualizado);
    }

    /**
     * Obtiene todos los enfrentamientos de una categoría (para pintar las llaves).
     */
    public List<EnfrentamientoDTO> getEnfrentamientosByCategoria(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo);
        List<Enfrentamiento> enfrentamientos = enfrentamientoRepository.findByCategoria(categoria);
        return enfrentamientos.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // --- Métodos de Conversión ---

    private EnfrentamientoDTO convertToDTO(Enfrentamiento match) {
        return new EnfrentamientoDTO(
                match.getId(),
                match.getCategoria().name(),
                match.getIdEquipoA(),
                match.getIdEquipoB(),
                match.getPuntosA(),
                match.getPuntosB(),
                match.getIdGanador(),
                match.getEtiquetaRonda(),
                match.getFaltasNotas()
        );
    }
}