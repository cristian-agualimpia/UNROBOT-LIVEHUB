package com.unrobot_livehub.registro_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email; // Para validación de email
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Entidad JPA que representa a un equipo participante.
 * Contiene la información de registro, categoría y miembros.
 *
 * @Data (Lombok) = @Getter, @Setter, @ToString, @EqualsAndHashCode
 * @NoArgsConstructor - Constructor vacío (requerido por JPA)
 * @AllArgsConstructor - Constructor con todos los campos
 */
@Entity
@Table(name = "equipos", 
       // Agregamos una restricción para asegurar que el nombre del equipo sea único
       uniqueConstraints = @UniqueConstraint(columnNames = "nombre"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipo {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = true)
    private String institucion;

    /**
     * La categoría a la que se inscribe el equipo.
     * Se almacena como String (ej: "BOLABOT") en la BD.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaTipo categoria;

    // --- Datos del Capitán (Requerido por HU03 y Reglamentos) ---

    @Column(nullable = false)
    private String nombreCapitan;

    @Email // Asegura que el string tenga formato de email
    @Column(nullable = false)
    private String emailCapitan;

    @Column(nullable = true) // El teléfono puede ser opcional
    private String telefonoCapitan;

    // --- Lista de Miembros (Requerido por HU03) ---
    
    /**
     * Almacena la lista de nombres de los otros miembros del equipo.
     * JPA creará una tabla separada (ej: equipo_miembros)
     * para almacenar esta lista, vinculada por el ID del equipo.
     * Es la forma más simple de tener una lista sin crear una entidad @Miembro.
     */
    @ElementCollection(fetch = FetchType.EAGER) // Carga los miembros junto con el equipo
    @CollectionTable(name = "equipo_miembros", joinColumns = @JoinColumn(name = "equipo_id"))
    @Column(name = "nombre_miembro")
    private List<String> miembros;
    
}