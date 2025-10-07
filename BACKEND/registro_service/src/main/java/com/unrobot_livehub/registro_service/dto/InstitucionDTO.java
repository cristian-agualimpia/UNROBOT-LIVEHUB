package com.unrobot_livehub.registro_service.dto;

import jakarta.validation.constraints.NotBlank;

public class InstitucionDTO {
    private Long id;

    @NotBlank(message = "El nombre de la institución es obligatorio")
    private String nombre;

    private String direccion;
    private String telefono;
    private String emailContacto;
    private String tipoInstitucion;

    // Constructores
    public InstitucionDTO() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }

    public String getTipoInstitucion() { return tipoInstitucion; }
    public void setTipoInstitucion(String tipoInstitucion) { this.tipoInstitucion = tipoInstitucion; }
}