package com.unrobot_livehub.categorias_service.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class EquipoUpdateDTO {
    @NotBlank(message = "El nombre del equipo no puede estar vacío")
    private String nombre;
    private String institucion;
    private String categoriaTipo; // Permite corregir la categoría en mesa
    private String descripcion;   // Nombres de robots o notas
    private List<String> miembros;
}