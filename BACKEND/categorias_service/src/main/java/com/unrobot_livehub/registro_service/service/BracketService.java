package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.Enfrentamiento;
import com.unrobot_livehub.registro_service.entity.Equipo;
import com.unrobot_livehub.registro_service.repository.EnfrentamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BracketService {

    @Autowired
    private EnfrentamientoRepository enfrentamientoRepository;
    
    @Autowired
    private EquipoService equipoService; // Usamos el servicio de equipo

    /**
     * Genera la primera ronda de enfrentamientos de forma aleatoria.
     */
    public void generarLlavesIniciales(String categoriaTipo, String etiquetaRonda) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo);

        // 1. Obtener todos los equipos de esa categoría
        List<Equipo> equipos = equipoService.getEquiposByCategoria(categoria);
        
        // 2. Mezclar aleatoriamente
        Collections.shuffle(equipos);

        List<Enfrentamiento> nuevosEnfrentamientos = new ArrayList<>();
        
        // 3. Agrupar de 2 en 2
        for (int i = 0; i < equipos.size(); i += 2) {
            Equipo equipoA = equipos.get(i);
            Equipo equipoB = (i + 1 < equipos.size()) ? equipos.get(i + 1) : null;

            Enfrentamiento match = new Enfrentamiento();
            match.setCategoria(categoria);
            match.setIdEquipoA(equipoA.getId());
            
            if (equipoB != null) {
                // Es un enfrentamiento normal
                match.setIdEquipoB(equipoB.getId());
                match.setEtiquetaRonda(etiquetaRonda + "-" + (i / 2 + 1)); // Ej: "Octavos-1"
                match.setPuntosA(0);
                match.setPuntosB(0);
            } else {
                // Equipo A pasa por BYE (queda solo)
                match.setIdEquipoB(null); // No hay oponente
                match.setEtiquetaRonda(etiquetaRonda + "-BYE");
                match.setIdGanador(equipoA.getId()); // Gana automáticamente
            }
            nuevosEnfrentamientos.add(match);
        }
        
        // 4. Guardar todos los nuevos enfrentamientos en la BD
        enfrentamientoRepository.saveAll(nuevosEnfrentamientos);
    }

    /**
     * Avanza a la siguiente ronda usando los ganadores de la ronda actual.
     * (¡Implementación corregida con tu anotación!)
     */
    public void avanzarRonda(String categoriaTipo, String rondaActual) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo);
        String proximaRonda = calcularProximaRonda(rondaActual); // Ej: "Octavos" -> "Cuartos"

        // 1. Buscar TODOS los enfrentamientos de la ronda actual (ej: "Octavos-1", "Octavos-2", ...)
        List<Enfrentamiento> rondaAnterior = enfrentamientoRepository.findByCategoriaAndEtiquetaRondaStartingWith(
            categoria, 
            rondaActual
        );

        // 2. Obtener la lista de IDs de ganadores (Validación simple)
        List<UUID> idsGanadores = new ArrayList<>();
        for (Enfrentamiento match : rondaAnterior) {
            if (match.getIdGanador() == null) {
                throw new IllegalStateException("No se puede avanzar la ronda. El enfrentamiento " + match.getEtiquetaRonda() + " aún no tiene un ganador.");
            }
            idsGanadores.add(match.getIdGanador());
        }

        // (Si la lista de ganadores es 1, es la final, no se crean más llaves)
        if (idsGanadores.size() <= 1) {
            // Ya es la final, no hay más rondas que crear.
            return;
        }

        List<Enfrentamiento> nuevosEnfrentamientos = new ArrayList<>();

        // 3. Agrupar ganadores de 2 en 2 para crear la siguiente ronda
        for (int i = 0; i < idsGanadores.size(); i += 2) {
            UUID idGanadorA = idsGanadores.get(i);
            UUID idGanadorB = (i + 1 < idsGanadores.size()) ? idsGanadores.get(i + 1) : null;

            Enfrentamiento matchNuevo = new Enfrentamiento();
            matchNuevo.setCategoria(categoria);
            matchNuevo.setIdEquipoA(idGanadorA);
            
            if (idGanadorB != null) {
                matchNuevo.setIdEquipoB(idGanadorB);
                matchNuevo.setEtiquetaRonda(proximaRonda + "-" + (i / 2 + 1)); // Ej: "Cuartos-1"
                matchNuevo.setPuntosA(0);
                matchNuevo.setPuntosB(0);
            } else {
                // Un ganador pasa por BYE (raro en rondas avanzadas, pero posible)
                matchNuevo.setIdEquipoB(null);
                matchNuevo.setEtiquetaRonda(proximaRonda + "-BYE");
                matchNuevo.setIdGanador(idGanadorA);
            }
            nuevosEnfrentamientos.add(matchNuevo);
        }
        
        // 4. Guardar la nueva ronda
        enfrentamientoRepository.saveAll(nuevosEnfrentamientos);
    }

    /**
     * Helper simple para determinar el nombre de la siguiente ronda.
     */
    private String calcularProximaRonda(String rondaActual) {
        // Asume que la rondaActual es el prefijo (ej: "Octavos")
        if (rondaActual.toUpperCase().startsWith("DIECISEISAVOS")) return "Octavos";
        if (rondaActual.toUpperCase().startsWith("OCTAVOS")) return "Cuartos";
        if (rondaActual.toUpperCase().startsWith("CUARTOS")) return "Semifinal";
        if (rondaActual.toUpperCase().startsWith("SEMIFINAL")) return "Final";
        // Si es "Final", podríamos devolver "Ganador" o simplemente no llamar a este método.
        return "RondaDesconocida";
    }
}