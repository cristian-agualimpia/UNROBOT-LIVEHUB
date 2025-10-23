package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.RondaIndividualDTO;
import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.RondaIndividual;
import com.unrobot_livehub.registro_service.repository.RondaIndividualRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RondaIndividualService {

    @Autowired
    private RondaIndividualRepository rondaRepository;

    // Define el tiempo de penalización en milisegundos (ej: 5 segundos)
    private static final long PENALIZACION_MS = 5000;

    /**
     * Registra una nueva ronda para un velocista y calcula su tiempo final.
     */
    public RondaIndividualDTO createRonda(RondaIndividualDTO rondaDTO) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(rondaDTO.getCategoriaTipo());

        // --- Lógica de Negocio Clave ---
        long tiempoFinalCalculado = rondaDTO.getTiempoMs() + (rondaDTO.getPenalizaciones() * PENALIZACION_MS);

        RondaIndividual ronda = new RondaIndividual();
        ronda.setCategoria(categoria);
        ronda.setIdEquipo(rondaDTO.getIdEquipo());
        ronda.setTiempoMs(rondaDTO.getTiempoMs());
        ronda.setPenalizaciones(rondaDTO.getPenalizaciones());
        ronda.setTiempoFinalMs(tiempoFinalCalculado); // Se guarda el tiempo calculado
        ronda.setEtiquetaRonda(rondaDTO.getEtiquetaRonda());

        RondaIndividual rondaGuardada = rondaRepository.save(ronda);
        return convertToDTO(rondaGuardada);
    }

    /**
     * Obtiene la tabla de posiciones de una categoría velocista.
     * (Ordenada por el mejor tiempo final).
     */
    public List<RondaIndividualDTO> getPosiciones(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo);
        List<RondaIndividual> rondas = rondaRepository.findByCategoriaOrderByTiempoFinalMsAsc(categoria);
        
        return rondas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // --- Métodos de Conversión ---

    private RondaIndividualDTO convertToDTO(RondaIndividual ronda) {
        return new RondaIndividualDTO(
                ronda.getId(),
                ronda.getCategoria().name(),
                ronda.getIdEquipo(),
                ronda.getTiempoMs(),
                ronda.getPenalizaciones(),
                ronda.getTiempoFinalMs(),
                ronda.getEtiquetaRonda()
        );
    }
}