package com.unrobot_livehub.registro_service.dto;

import javax.validation.constraints.NotNull;

public class MiembroEquipoDTO {
    private Long id;

    @NotNull(message = "El equipo es obligatorio")
    private Long equipoId;

    @NotNull(message = "La persona es obligatoria")
    private Long personaId;

    @NotNull(message = "El rol es obligatorio")
    private String rol;

    private String personaNombre;
    private String equipoNombre;

    // Constructores
    public MiembroEquipoDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }

    public Long getPersonaId() { return personaId; }
    public void setPersonaId(Long personaId) { this.personaId = personaId; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getPersonaNombre() { return personaNombre; }
    public void setPersonaNombre(String personaNombre) { this.personaNombre = personaNombre; }

    public String getEquipoNombre() { return equipoNombre; }
    public void setEquipoNombre(String equipoNombre) { this.equipoNombre = equipoNombre; }
}
