package com.unrobot_livehub.registro_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Entidad JPA para rondas de categorías de tiempo/velocidad (Seguidor de Línea).
 */
@Entity
@Table(name = "ronda_individual")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RondaIndividual {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    // La categoría a la que pertenece esta ronda
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaTipo categoria;

    // El equipo que corre
    @Column(nullable = false)
    private UUID idEquipo;

    // --- Tiempos (en milisegundos) ---
    @Column(nullable = false)
    private long tiempoMs; // El tiempo crudo del cronómetro

    @Column(columnDefinition = "integer default 0")
    private int penalizaciones; // Ej: 2 (por 2 salidas de pista)

    @Column(nullable = true) // Se calcula en el servicio antes de guardar
    private long tiempoFinalMs; // Calculado: tiempoMs + (penalizaciones * 5000ms)

    // --- Gestión de Rondas ---
    @Column(nullable = false)
    private String etiquetaRonda; // Ej: "Clasificatoria-1", "Ronda-Final"
}