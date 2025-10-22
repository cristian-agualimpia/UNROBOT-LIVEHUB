package com.unrobot_livehub.registro_service.service;

import com.unrobot_livehub.registro_service.dtos.CategoriaInfoDTO;
import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejar la lógica de negocio relacionada con la
 * información estática de las categorías (leída desde el Enum).
 * No accede a la base de datos.
 */
@Service
public class CategoriaInfoService {

    /**
     * Obtiene la información de todas las categorías definidas en el Enum.
     *
     * @return Una lista de DTOs, cada uno representando una categoría.
     */
    public List<CategoriaInfoDTO> getAllCategoriasInfo() {
        
        // 1. Obtener todos los valores del Enum CategoriaTipo
        // 2. Usar stream() para procesar la lista
        // 3. Mapear cada valor del Enum a un nuevo CategoriaInfoDTO
        // 4. Coleccionar los resultados en una Lista
        
        return Arrays.stream(CategoriaTipo.values())
                .map(CategoriaInfoDTO::new) // Llama al constructor CategoriaInfoDTO(CategoriaTipo enumVal)
                .collect(Collectors.toList());
    }

    // (Si en el futuro necesitas un DTO para UNA sola categoría,
    // podrías añadir un método aquí)
    //
    // public CategoriaInfoDTO getCategoriaInfoByTipo(String tipo) {
    //     CategoriaTipo enumVal = CategoriaTipo.valueOf(tipo);
    //     return new CategoriaInfoDTO(enumVal);
    // }
}