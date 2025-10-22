package com.unrobot_livehub.registro_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

/**
 * Entidad JPA que representa a un equipo participante.
 * @Data (de Lombok) incluye: @Getter, @Setter, @ToString, @EqualsAndHashCode
 * @NoArgsConstructor - Constructor vacío (requerido por JPA)
 * @AllArgsConstructor - Constructor con todos los campos
 */
@Entity
@Table(name = "equipos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String nombre;
    
    private String institucion;

    // --- La clave ---
    // Almacena el CategoriaTipo como un String en la BD (ej: "BOLABOT_SENIOR")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaTipo categoria;
    
    // (Puedes agregar más campos como datos de contacto del capitán, etc.)
}