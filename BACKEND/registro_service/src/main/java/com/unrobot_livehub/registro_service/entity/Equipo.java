package com.unrobot_livehub.registro_service.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "equipos")
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El código del equipo es obligatorio")
    @Column(unique = true, nullable = false)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institucion_id", nullable = false)
    private Institucion institucion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    private Boolean activo = true;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MiembroEquipo> miembros = new ArrayList<>();

    // Constructores
    public Equipo() {}

    public Equipo(String nombre, String codigo, Institucion institucion) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.institucion = institucion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Institucion getInstitucion() { return institucion; }
    public void setInstitucion(Institucion institucion) { this.institucion = institucion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public List<MiembroEquipo> getMiembros() { return miembros; }
    public void setMiembros(List<MiembroEquipo> miembros) { this.miembros = miembros; }
}