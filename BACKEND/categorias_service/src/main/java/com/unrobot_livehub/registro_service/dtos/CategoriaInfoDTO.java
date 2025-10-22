package com.unrobot_livehub.registro_service.dtos;

import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import lombok.Data;

/**
 * DTO para transportar la información estática de las categorías al frontend.
 * No se mapea a una entidad, se construye desde el Enum.
 * @Data (Lombok) = @Getter, @Setter, @ToString, @EqualsAndHashCode
 */
@Data
public class CategoriaInfoDTO {

    private String tipo; // El nombre del ENUM (ej: "BOLABOT_SENIOR")
    private String nombre; // El nombre completo (ej: "Bolabot Senior 2v2")
    private String reglas; // El texto de las reglas

    /**
     * Constructor conveniente para crear el DTO a partir del Enum.
     */
    public CategoriaInfoDTO(CategoriaTipo enumVal) {
        this.tipo = enumVal.name();
        this.nombre = enumVal.getNombreCompleto();
        this.reglas = enumVal.getReglas();
    }
}