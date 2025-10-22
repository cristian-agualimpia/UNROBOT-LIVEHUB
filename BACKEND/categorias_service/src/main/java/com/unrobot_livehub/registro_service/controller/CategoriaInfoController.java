package com.unrobot_livehub.registro_service.controller;

import com.unrobot_livehub.registro_service.dtos.CategoriaInfoDTO;
import com.unrobot_livehub.registro_service.service.CategoriaInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias-info")
@CrossOrigin(origins = "*") // Permite peticiones desde cualquier frontend
public class CategoriaInfoController {

    @Autowired
    private CategoriaInfoService categoriaInfoService;

    /**
     * Endpoint para que el frontend obtenga la lista de todas
     * las categorías, sus nombres y reglas.
     */
    @GetMapping
    public ResponseEntity<List<CategoriaInfoDTO>> getCategoriasInfo() {
        List<CategoriaInfoDTO> infoList = categoriaInfoService.getAllCategoriasInfo();
        return ResponseEntity.ok(infoList);
    }
}