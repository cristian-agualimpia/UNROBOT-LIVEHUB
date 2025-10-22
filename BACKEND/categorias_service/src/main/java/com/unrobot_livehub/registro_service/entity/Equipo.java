package com.unrobot_livehub.registro_service.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // <-- Importar
import jakarta.persistence.Enumerated; // <-- Importar
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // @NoArgsConstructor es necesario para JPA
@Entity
@Table(name = "equipos")
public class Equipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // El nombre de equipo debe ser único [cite: 24]
    private String nombreEquipo;

    private String nombreRobot;

    private int puntaje = 0; // Es buena práctica inicializar el puntaje

    @Enumerated(EnumType.STRING) // Almacena el enum como String (ej. "MINI_SUMO")
    @Column(name = "categoria", nullable = false)
    private Categoria categoria; // Usamos el tipo Categoria

    @Column(nullable = false)
    private String nombreCapitan;

    @Column(nullable = false)
    private String correoCapitan;

    private Boolean activo = true;

    // El constructor que tenías para 'nombre' y 'codigo'
    // lo he quitado porque 'codigo' no existe en la entidad.
    // Lombok @AllArgsConstructor y @NoArgsConstructor ya manejan los constructores.
}