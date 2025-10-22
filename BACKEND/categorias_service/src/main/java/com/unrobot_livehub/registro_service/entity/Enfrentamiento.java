package com.unrobot_livehub.registro_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Entidad JPA para enfrentamientos de 2 equipos (Bolabot, Sumo).
 */
@Entity
@Table(name = "enfrentamientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enfrentamiento {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // La categoría a la que pertenece este enfrentamiento
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaTipo categoria;

    // --- Equipos ---
    // Usamos UUID para vincularlos al ID de la tabla 'equipos'
    @Column(nullable = false)
    private UUID idEquipoA;

    private UUID idEquipoB; // Nullable por si un equipo pasa por BYE

    // --- Puntuación ---
    @Column(columnDefinition = "integer default 0")
    private int puntosA = 0;

    @Column(columnDefinition = "integer default 0")
    private int puntosB = 0;

    // --- Gestión de Llaves ---
    @Column(nullable = true)
    private UUID idGanador; // Se llena cuando el match termina

    @Column(nullable = false)
    private String etiquetaRonda; // Ej: "Octavos-1", "Final"

    // --- Otros Datos ---
    @Lob // Para texto largo (en algunas BD) o usa @Column(length = 2000)
    private String faltasNotas; // Tu idea de la nota en string
    
    // (Puedes agregar un estado: "PENDIENTE", "EN_JUEGO", "FINALIZADO")
}