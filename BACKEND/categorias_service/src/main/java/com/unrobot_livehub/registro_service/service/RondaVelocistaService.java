package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.RondaVelocistaDTO;
import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.RondaVelocista;
import com.unrobot_livehub.registro_service.repository.RondaVelocistaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RondaVelocistaService {

    @Autowired
    private RondaVelocistaRepository rondaRepository;

    // Define el tiempo de penalización en milisegundos (ej: 5 segundos)
    private static final long PENALIZACION_MS = 5000;

    /**
     * Registra una nueva ronda para un velocista y calcula su tiempo final.
     */
    public RondaVelocistaDTO createRonda(RondaVelocistaDTO rondaDTO) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(rondaDTO.getCategoriaTipo());

        // --- Lógica de Negocio Clave ---
        long tiempoFinalCalculado = rondaDTO.getTiempoMs() + (rondaDTO.getPenalizaciones() * PENALIZACION_MS);

        RondaVelocista ronda = new RondaVelocista();
        ronda.setCategoria(categoria);
        ronda.setIdEquipo(rondaDTO.getIdEquipo());
        ronda.setTiempoMs(rondaDTO.getTiempoMs());
        ronda.setPenalizaciones(rondaDTO.getPenalizaciones());
        ronda.setTiempoFinalMs(tiempoFinalCalculado); // Se guarda el tiempo calculado
        ronda.setEtiquetaRonda(rondaDTO.getEtiquetaRonda());

        RondaVelocista rondaGuardada = rondaRepository.save(ronda);
        return convertToDTO(rondaGuardada);
    }

    /**
     * Obtiene la tabla de posiciones de una categoría velocista.
     * (Ordenada por el mejor tiempo final).
     */
    public List<RondaVelocistaDTO> getPosiciones(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo);
        List<RondaVelocista> rondas = rondaRepository.findByCategoriaOrderByTiempoFinalMsAsc(categoria);
        
        return rondas.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // --- Métodos de Conversión ---

    private RondaVelocistaDTO convertToDTO(RondaVelocista ronda) {
        return new RondaVelocistaDTO(
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