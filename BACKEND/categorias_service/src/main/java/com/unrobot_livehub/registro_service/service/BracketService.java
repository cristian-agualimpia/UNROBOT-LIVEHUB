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
    private EquipoService equipoService; // Asumo que se llama así

    /**
     * ¡MÉTODO MODIFICADO!
     * Genera la primera ronda de forma aleatoria Y CALCULA AUTOMÁTICAMENTE
     * la ronda inicial (Octavos, Cuartos, etc.) y los BYEs.
     */
    public void generarLlavesIniciales(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo.toUpperCase());

        // 1. Contar equipos
        List<Equipo> equipos = equipoService.getEquiposByCategoria(categoria);
        int nEquipos = equipos.size();

        if (nEquipos < 2) {
            // No se puede hacer un torneo con menos de 2 equipos
            throw new IllegalStateException("No se pueden generar llaves para menos de 2 equipos.");
        }

        // 2. Calcular el tamaño del bracket y la ronda inicial
        int tamanoBracket = calcularSiguientePotenciaDeDos(nEquipos);
        String etiquetaRondaInicial = mapearTamanoARonda(tamanoBracket); // Ej: "Cuartos"
        int byes = tamanoBracket - nEquipos; // Número de equipos que pasan directo

        // 3. Mezclar equipos
        Collections.shuffle(equipos);

        List<Enfrentamiento> nuevosEnfrentamientos = new ArrayList<>();
        int indiceEquipo = 0; // Para llevar la cuenta de los equipos que vamos asignando

        // 4. Crear los partidos BYE primero (los 'byes' equipos pasan directo)
        // (Ej: 6 equipos, bracket de 8. byes = 2. Los 2 primeros pasan directo)
        for (int i = 0; i < byes; i++) {
            Equipo equipoConBye = equipos.get(indiceEquipo++);
            Enfrentamiento match = new Enfrentamiento();
            match.setCategoria(categoria);
            match.setIdEquipoA(equipoConBye.getId());
            match.setIdEquipoB(null); // Sin oponente
            match.setEtiquetaRonda(etiquetaRondaInicial + "-BYE");
            match.setIdGanador(equipoConBye.getId()); // Gana automáticamente
            nuevosEnfrentamientos.add(match);
        }
        
        // 5. Crear los partidos REALES (el resto de equipos)
        // (Ej: 6 equipos, 2 byes. Quedan 4 equipos para 2 partidos)
        int partidosReales = (nEquipos - byes) / 2;
        for (int i = 0; i < partidosReales; i++) {
            Equipo equipoA = equipos.get(indiceEquipo++);
            Equipo equipoB = equipos.get(indiceEquipo++);

            Enfrentamiento match = new Enfrentamiento();
            match.setCategoria(categoria);
            match.setIdEquipoA(equipoA.getId());
            match.setIdEquipoB(equipoB.getId());
            match.setEtiquetaRonda(etiquetaRondaInicial + "-" + (i + 1)); // Ej: "Cuartos-1"
            match.setPuntosA(0);
            match.setPuntosB(0);
            match.setIdGanador(null); // Aún no hay ganador
            nuevosEnfrentamientos.add(match);
        }

        // 6. Guardar todos los enfrentamientos (reales y byes)
        enfrentamientoRepository.saveAll(nuevosEnfrentamientos);
    }
    
    /**
     * Avanza a la siguiente ronda usando los ganadores de la ronda actual.
     * (Este método ya estaba bien)
     */
    public void avanzarRonda(String categoriaTipo, String rondaActual) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo.toUpperCase());
        String proximaRonda = calcularProximaRonda(rondaActual); // Ej: "Octavos" -> "Cuartos"

        // 1. Buscar TODOS los enfrentamientos de la ronda actual (reales Y byes)
        List<Enfrentamiento> rondaAnterior = enfrentamientoRepository.findByCategoriaAndEtiquetaRondaStartingWith(
            categoria, 
            rondaActual
        );

        // 2. Obtener la lista de IDs de ganadores
        List<UUID> idsGanadores = new ArrayList<>();
        for (Enfrentamiento match : rondaAnterior) {
            if (match.getIdGanador() == null) {
                throw new IllegalStateException("No se puede avanzar la ronda. El enfrentamiento " + match.getEtiquetaRonda() + " aún no tiene un ganador.");
            }
            idsGanadores.add(match.getIdGanador());
        }

        if (idsGanadores.size() <= 1) {
            // Ya es la final, no hay más rondas que crear.
            return;
        }

        List<Enfrentamiento> nuevosEnfrentamientos = new ArrayList<>();
        Collections.shuffle(idsGanadores); // Mezclamos a los ganadores para la siguiente ronda

        // 3. Agrupar ganadores de 2 en 2 para crear la siguiente ronda
        for (int i = 0; i < idsGanadores.size(); i += 2) {
            // ... (El resto de la lógica de avanzar ronda que ya teníamos)
            // ...
        }
        
        // 4. Guardar la nueva ronda
        // enfrentamientoRepository.saveAll(nuevosEnfrentamientos);
    }


    // --- NUEVOS MÉTODOS HELPER (Añadir al final de la clase) ---

    /**
     * Calcula la siguiente potencia de 2 para un número de equipos.
     * (Ej: 6 equipos -> 8; 19 equipos -> 32)
     */
    private int calcularSiguientePotenciaDeDos(int n) {
        if (n <= 2) return 2;
        int power = 1;
        while (power < n) {
            power *= 2;
        }
        return power;
    }

    /**
     * Mapea el tamaño del bracket a la etiqueta de ronda correspondiente.
     */
    private String mapearTamanoARonda(int tamanoBracket) {
        switch (tamanoBracket) {
            case 2: return "Final";
            case 4: return "Semifinal";
            case 8: return "Cuartos";
            case 16: return "Octavos";
            case 32: return "Dieciseisavos";
            case 64: return "Ronda-de-64";
            default: return "Ronda-Inicial";
        }
    }
    
    /**
     * Helper simple para determinar el nombre de la siguiente ronda.
     * (Este ya lo teníamos, solo asegúrate de que esté)
     */
    private String calcularProximaRonda(String rondaActual) {
        // Asume que la rondaActual es el prefijo (ej: "Octavos")
        if (rondaActual.toUpperCase().startsWith("DIECISEISAVOS")) return "Octavos";
        if (rondaActual.toUpperCase().startsWith("OCTAVOS")) return "Cuartos";
        if (rondaActual.toUpperCase().startsWith("CUARTOS")) return "Semifinal";
        if (rondaActual.toUpperCase().startsWith("SEMIFINAL")) return "Final";
        return "RondaDesconocida";
    }
}