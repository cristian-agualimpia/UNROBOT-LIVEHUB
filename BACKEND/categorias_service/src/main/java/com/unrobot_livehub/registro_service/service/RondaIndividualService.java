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
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

import java.util.Comparator; // <-- ¡AÑADE ESTA LÍNEA!
import java.util.Map;       // <-- ¡AÑADE ESTA LÍNEA!

@Service
public class RondaIndividualService {

    @Autowired
    private RondaIndividualRepository rondaRepository;

    // Define el tiempo de penalización en milisegundos (ej: 5 segundos)
    private static final long PENALIZACION_MS = 5000;

    /**
     * Registra un nuevo intento.
     * (¡ACTUALIZADO! para calcular el tiempo final aquí)
     */
    public RondaIndividualDTO createRonda(RondaIndividualDTO rondaDTO) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(rondaDTO.getCategoriaTipo().toUpperCase());

        // Lógica de cálculo de tiempo/puntos
        long tiempoFinalCalculado;
        if (categoria == CategoriaTipo.ROVER_RC_EXPLORADOR || categoria == CategoriaTipo.INNOVATION_CHALLENGE) {
            // Para categorías de PUNTOS, el "tiempoFinalMs" ES el puntaje
            tiempoFinalCalculado = rondaDTO.getTiempoFinalMs(); // Asumimos que el front envía el puntaje aquí
        } else {
            // Para categorías de TIEMPO, lo calculamos
            tiempoFinalCalculado = rondaDTO.getTiempoMs() + (rondaDTO.getPenalizaciones() * PENALIZACION_MS);
        }

        RondaIndividual ronda = new RondaIndividual();
        ronda.setCategoria(categoria);
        ronda.setIdEquipo(rondaDTO.getIdEquipo());
        ronda.setTiempoMs(rondaDTO.getTiempoMs());
        ronda.setPenalizaciones(rondaDTO.getPenalizaciones());
        ronda.setTiempoFinalMs(tiempoFinalCalculado); // Se guarda el tiempo/puntaje calculado
        ronda.setEtiquetaRonda(rondaDTO.getEtiquetaRonda());

        RondaIndividual rondaGuardada = rondaRepository.save(ronda);
        return convertToDTO(rondaGuardada);
    }

    public List<RondaIndividualDTO> getPosiciones(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo.toUpperCase());

        // 1. Determinar el criterio de ordenamiento
        boolean esPorPuntos = (categoria == CategoriaTipo.ROVER_RC_EXPLORADOR || categoria == CategoriaTipo.INNOVATION_CHALLENGE);
        
        Comparator<RondaIndividual> comparador;
        BinaryOperator<RondaIndividual> mejorIntento;

        if (esPorPuntos) {
            // MÁS puntos es mejor
            comparador = Comparator.comparing(RondaIndividual::getTiempoFinalMs).reversed(); // Orden DESC
            mejorIntento = BinaryOperator.maxBy(Comparator.comparing(RondaIndividual::getTiempoFinalMs));
        } else {
            // MENOS tiempo es mejor
            comparador = Comparator.comparing(RondaIndividual::getTiempoFinalMs); // Orden ASC
            mejorIntento = BinaryOperator.minBy(Comparator.comparing(RondaIndividual::getTiempoFinalMs));
        }

        // 2. Obtener todas las rondas
        List<RondaIndividual> todasLasRondas = rondaRepository.findByCategoria(categoria);

        // 3. Agrupar por equipo y encontrar el MEJOR intento de cada uno
        Map<UUID, RondaIndividual> mejorIntentoPorEquipo = todasLasRondas.stream()
                .collect(Collectors.toMap(
                        RondaIndividual::getIdEquipo,  // Clave: ID del equipo
                        r -> r,                         // Valor: la ronda
                        mejorIntento                    // Función de fusión: quédate con el mejor
                ));

        // 4. Convertir a DTO y ordenar la lista final
        return mejorIntentoPorEquipo.values().stream()
                .sorted(comparador)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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