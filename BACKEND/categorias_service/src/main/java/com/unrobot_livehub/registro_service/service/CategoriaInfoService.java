package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.CategoriaInfoDTO;
import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaInfoService {

    /**
     * Obtiene la información de TODAS las categorías definidas en el Enum.
     * (Este ya lo teníamos)
     */
    public List<CategoriaInfoDTO> getAllCategoriasInfo() {
        return Arrays.stream(CategoriaTipo.values())
                .map(CategoriaInfoDTO::new) // Llama al constructor CategoriaInfoDTO(CategoriaTipo)
                .collect(Collectors.toList());
    }

    /**
     * --- ¡NUEVO! ---
     * Obtiene la información específica de UNA categoría por su tipo (su nombre en el Enum).
     *
     * @param tipo El nombre del enum (ej: "BOLABOT" o "minisumo_rc")
     * @return El DTO con la información de esa categoría.
     * @throws IllegalArgumentException si el 'tipo' no existe en el Enum.
     */
    public CategoriaInfoDTO getCategoriaInfoByTipo(String tipo) {
        
        // 1. Convierte el String (ej: "bolabot") a mayúsculas ("BOLABOT")
        // 2. CategoriaTipo.valueOf() busca el valor del Enum que coincida.
        //    (Esto arrojará un error automático si el tipo no es válido)
        CategoriaTipo enumVal = CategoriaTipo.valueOf(tipo.toUpperCase());
        
        // 3. Llama al constructor del DTO con ese único valor
        return new CategoriaInfoDTO(enumVal);
    }
}