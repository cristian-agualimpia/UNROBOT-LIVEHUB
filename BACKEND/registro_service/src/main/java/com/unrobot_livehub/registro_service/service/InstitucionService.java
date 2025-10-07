package com.unrobot_livehub.registro_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unrobot_livehub.registro_service.dto.InstitucionDTO;
import com.unrobot_livehub.registro_service.entity.Institucion;
import com.unrobot_livehub.registro_service.repository.InstitucionRepository;

@Service
public class InstitucionService {

    @Autowired
    private InstitucionRepository institucionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public InstitucionDTO crearInstitucion(InstitucionDTO institucionDTO) {
        if (institucionRepository.existsByNombre(institucionDTO.getNombre())) {
            throw new RuntimeException("Ya existe una institución con este nombre");
        }

        Institucion institucion = modelMapper.map(institucionDTO, Institucion.class);
        institucion = institucionRepository.save(institucion);
        return modelMapper.map(institucion, InstitucionDTO.class);
    }

    public List<InstitucionDTO> obtenerTodasLasInstituciones() {
        return institucionRepository.findAll()
                .stream()
                .map(institucion -> modelMapper.map(institucion, InstitucionDTO.class))
                .collect(Collectors.toList());
    }

    public InstitucionDTO obtenerInstitucionPorId(Long id) {
        Institucion institucion = institucionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada con ID: " + id));
        return modelMapper.map(institucion, InstitucionDTO.class);
    }

    public InstitucionDTO actualizarInstitucion(Long id, InstitucionDTO institucionDTO) {
        Institucion institucionExistente = institucionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada con ID: " + id));

        if (!institucionExistente.getNombre().equals(institucionDTO.getNombre()) &&
            institucionRepository.existsByNombre(institucionDTO.getNombre())) {
            throw new RuntimeException("Ya existe otra institución con este nombre");
        }

        modelMapper.map(institucionDTO, institucionExistente);
        institucionExistente = institucionRepository.save(institucionExistente);
        return modelMapper.map(institucionExistente, InstitucionDTO.class);
    }

    public void eliminarInstitucion(Long id) {
        Institucion institucion = institucionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Institución no encontrada con ID: " + id));
        
        // Verificar que no tenga equipos asociados
        if (!institucion.getEquipos().isEmpty()) {
            throw new RuntimeException("No se puede eliminar la institución porque tiene equipos asociados");
        }

        institucionRepository.delete(institucion);
    }
}