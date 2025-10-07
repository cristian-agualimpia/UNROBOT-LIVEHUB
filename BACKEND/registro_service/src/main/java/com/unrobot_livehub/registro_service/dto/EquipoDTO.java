package com.unrobot_livehub.registro_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EquipoDTO {
    private Long id;

    @NotBlank(message = "El nombre del equipo es obligatorio")
    private String nombre;

    @NotBlank(message = "El código del equipo es obligatorio")
    private String codigo;

    @NotNull(message = "La institución es obligatoria")
    private Long institucionId;

    private String institucionNombre;

    // Constructores
    public EquipoDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public Long getInstitucionId() { return institucionId; }
    public void setInstitucionId(Long institucionId) { this.institucionId = institucionId; }

    public String getInstitucionNombre() { return institucionNombre; }
    public void setInstitucionNombre(String institucionNombre) { this.institucionNombre = institucionNombre; }
}