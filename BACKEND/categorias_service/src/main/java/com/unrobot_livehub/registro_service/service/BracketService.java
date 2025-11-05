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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.unrobot_livehub.registro_service.dtos.RondaIndividualDTO; // <-- ¡AÑADE ESTA LÍNEA!

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BracketService {

    @Autowired
    private EnfrentamientoRepository enfrentamientoRepository;
    
    @Autowired
    private EquipoService equipoService;

    @Autowired // <-- ¡AÑADE ESTA LÍNEA!
    private RondaIndividualService rondaIndividualService; // <-- ¡Y ESTA LÍNEA!

    // Define el orden de las rondas, de la primera a la última
    private static final List<String> ORDEN_RONDAS = List.of(
        "Ronda-de-64", "Dieciseisavos", "Octavos", "Cuartos", "Semifinal", "Final"
    );
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
     * ¡MÉTODO MODIFICADO!
     * Avanza a la siguiente ronda, detectando automáticamente la ronda actual.
     */
    public void avanzarRonda(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo.toUpperCase());
        
        // 1. Obtener TODOS los enfrentamientos de la categoría
        List<Enfrentamiento> todosLosPartidos = enfrentamientoRepository.findByCategoria(categoria);

        // 2. Encontrar la última ronda que se generó
        String rondaActual = null;
        for (int i = ORDEN_RONDAS.size() - 1; i >= 0; i--) { // Itera al revés (de "Final" a "Ronda-de-64")
            String ronda = ORDEN_RONDAS.get(i);
            boolean estaRondaExiste = todosLosPartidos.stream()
                                    .anyMatch(p -> p.getEtiquetaRonda().startsWith(ronda));
            if (estaRondaExiste) {
                rondaActual = ronda;
                break;
            }
        }

        if (rondaActual == null) {
            throw new IllegalStateException("No se han generado llaves iniciales para esta categoría.");
        }
        if (rondaActual.equals("Final")) {
            throw new IllegalStateException("El torneo ya está en la Final. No se puede avanzar más.");
        }

        // 3. Verificar si esta ronda actual está 100% completada
        String finalRondaActual = rondaActual; // Necesario para el lambda
        List<Enfrentamiento> partidosRondaActual = todosLosPartidos.stream()
                .filter(p -> p.getEtiquetaRonda().startsWith(finalRondaActual))
                .toList();

        boolean todosTienenGanador = partidosRondaActual.stream()
                                    .allMatch(p -> p.getIdGanador() != null);

        if (!todosTienenGanador) {
            throw new IllegalStateException("No se puede avanzar. La ronda '" + rondaActual + "' aún tiene partidos pendientes.");
        }

        // 4. ¡La ronda está completa! Procedemos a generar la siguiente
        String proximaRonda = calcularProximaRonda(rondaActual); // Ej: "Octavos" -> "Cuartos"
        
        List<UUID> idsGanadores = partidosRondaActual.stream()
                                    .map(Enfrentamiento::getIdGanador)
                                    .collect(Collectors.toList());
        
        // (Opcional: Mezclar los ganadores para que los emparejamientos no sean predecibles)
        Collections.shuffle(idsGanadores);

        List<Enfrentamiento> nuevosEnfrentamientos = new ArrayList<>();
        
        for (int i = 0; i < idsGanadores.size(); i += 2) {
            UUID idGanadorA = idsGanadores.get(i);
            // Manejo de BYE en rondas avanzadas (raro, pero posible si el # de ganadores es impar)
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
                // Un ganador pasa por BYE
                matchNuevo.setIdEquipoB(null);
                matchNuevo.setEtiquetaRonda(proximaRonda + "-BYE");
                matchNuevo.setIdGanador(idGanadorA); // Gana automáticamente
            }
            nuevosEnfrentamientos.add(matchNuevo);
        }
        
        // 5. Guardar la nueva ronda
        enfrentamientoRepository.saveAll(nuevosEnfrentamientos);
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

    /**
     * --- ¡NUEVO MÉTODO! ---
     * Genera la llave final (Top 4) para categorías velocistas.
     */
    public void generarLlavesFinalesVelocista(String categoriaTipo) {
        CategoriaTipo categoria = CategoriaTipo.valueOf(categoriaTipo.toUpperCase());
        
        // 1. Obtener el leaderboard (Top 4) del servicio de rondas
        // (Gracias a la corrección, esto ya devuelve el Top 4 ordenado)
        List<RondaIndividualDTO> leaderboard = rondaIndividualService.getPosiciones(categoriaTipo);

        if (leaderboard.size() < 4) {
            throw new IllegalStateException("No hay suficientes (4) equipos clasificados para generar la final.");
        }

        // 2. Extraer los IDs de los Top 4
        List<UUID> top4_ids = leaderboard.subList(0, 4).stream()
                                    .map(RondaIndividualDTO::getIdEquipo)
                                    .collect(Collectors.toList());
        
        // 3. Sorteo (Mezclar la lista)
        Collections.shuffle(top4_ids);

        // 4. Crear los 2 enfrentamientos de Semifinal
        List<Enfrentamiento> finales = List.of(
            crearEnfrentamientoVelocista(categoria, "Semifinal-1", top4_ids.get(0), top4_ids.get(1)),
            crearEnfrentamientoVelocista(categoria, "Semifinal-2", top4_ids.get(2), top4_ids.get(3))
        );

        // 5. Guardar las nuevas semifinales
        enfrentamientoRepository.saveAll(finales);
    }
    
    // Helper para crear el enfrentamiento
    private Enfrentamiento crearEnfrentamientoVelocista(CategoriaTipo cat, String etiqueta, UUID idA, UUID idB) {
        Enfrentamiento match = new Enfrentamiento();
        match.setCategoria(cat);
        match.setEtiquetaRonda(etiqueta);
        match.setIdEquipoA(idA);
        match.setIdEquipoB(idB);
        match.setPuntosA(0); // Se usará para guardar el tiempo_A
        match.setPuntosB(0); // Se usará para guardar el tiempo_B
        return match;
    }
}