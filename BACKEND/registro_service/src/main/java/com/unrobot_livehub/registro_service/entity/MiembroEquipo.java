package com.unrobot_livehub.registro_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "miembros_equipo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"equipo_id", "persona_id"})
})
public class MiembroEquipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @Column(nullable = false)
    private String rol; // CAPITAN, MIEMBRO, MENTOR

    private Boolean activo = true;

    // Constructores
    public MiembroEquipo() {}

    public MiembroEquipo(Equipo equipo, Persona persona, String rol) {
        this.equipo = equipo;
        this.persona = persona;
        this.rol = rol;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Equipo getEquipo() { return equipo; }
    public void setEquipo(Equipo equipo) { this.equipo = equipo; }

    public Persona getPersona() { return persona; }
    public void setPersona(Persona persona) { this.persona = persona; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}