package com.unrobot_livehub.registro_service.dtos;

import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import lombok.Data;

/**
 * DTO (ACTUALIZADO) para transportar la información estática COMPLETA
 * de las categorías al frontend.
 * Se construye a partir del Enum CategoriaTipo.
 */
@Data
public class CategoriaInfoDTO {

    // --- Identificación y Lógica ---
    private String tipo; // El nombre del ENUM (ej: "BOLABOT")
    private String tipoLogica; // "ENFRENTAMIENTO" o "VELOCISTA" (ahora "RondaIndividual")

    // --- Información Descriptiva (Lo nuevo) ---
    private String nombreCompleto;
    private String resumenDescriptivo;
    private String descripcionPista;
    private String publicoDirigido;
    private int integrantesMax;
    private String reglasResumen;

    /**
     * Constructor (ACTUALIZADO) que mapea TODOS los campos del Enum al DTO.
     */
    public CategoriaInfoDTO(CategoriaTipo enumVal) {
        this.tipo = enumVal.name();
        this.tipoLogica = enumVal.getTipoLogica().name(); // Mapeamos el enum interno a String
        this.nombreCompleto = enumVal.getNombreCompleto();
        this.resumenDescriptivo = enumVal.getResumenDescriptivo();
        this.descripcionPista = enumVal.getDescripcionPista();
        this.publicoDirigido = enumVal.getPublicoDirigido();
        this.integrantesMax = enumVal.getIntegrantesMax();
        this.reglasResumen = enumVal.getReglasResumen();
    }
}