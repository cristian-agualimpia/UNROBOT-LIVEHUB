package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.dtos.CategoriaInfoDTO;
import com.unrobot_livehub.registro_service.service.CategoriaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Asegúrate de tener @PathVariable

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias-info")
public class CategoriaInfoController {

    @Autowired
    private CategoriaInfoService categoriaInfoService;

    /**
     * Endpoint para que el frontend obtenga la LISTA de todas las categorías.
     * (Este ya lo teníamos)
     * * GET /api/v1/categorias-info
     */
    @GetMapping
    public ResponseEntity<List<CategoriaInfoDTO>> getCategoriasInfo() {
        List<CategoriaInfoDTO> infoList = categoriaInfoService.getAllCategoriasInfo();
        return ResponseEntity.ok(infoList);
    }

    /**
     * --- ¡NUEVO! ---
     * Endpoint para que el frontend obtenga la info de UNA sola categoría.
     * El frontend llamará a esta ruta cuando el usuario haga clic en una categoría.
     *
     * Ej: GET /api/v1/categorias-info/BOLABOT
     * Ej: GET /api/v1/categorias-info/SEGUIDOR_LINEA_ESCOLAR
     */
    @GetMapping("/{tipo}")
    public ResponseEntity<CategoriaInfoDTO> getCategoriaInfoPorTipo(
            @PathVariable String tipo
    ) {
        // Llama al nuevo método del servicio
        CategoriaInfoDTO info = categoriaInfoService.getCategoriaInfoByTipo(tipo);
        return ResponseEntity.ok(info);
    }
}