package com.unrobot_livehub.registro_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unrobot_livehub.registro_service.dto.EquipoDTO;
import com.unrobot_livehub.registro_service.entity.Equipo;
import com.unrobot_livehub.registro_service.entity.Institucion;
import com.unrobot_livehub.registro_service.repository.EquipoRepository;
import com.unrobot_livehub.registro_service.repository.InstitucionRepository;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private InstitucionRepository institucionRepository;

    @Autowired
    private ModelMapper modelMapper;

    public EquipoDTO crearEquipo(EquipoDTO equipoDTO) {
        // Validar código único
        if (equipoRepository.existsByCodigo(equipoDTO.getCodigo())) {
            throw new RuntimeException("Ya existe un equipo con este código");
        }

        // Validar nombre único por institución
        if (equipoRepository.existsByNombreAndInstitucionId(equipoDTO.getNombre(), equipoDTO.getInstitucionId())) {
            throw new RuntimeException("Ya existe un equipo con este nombre en la institución");
        }

        // Obtener la institución
        Institucion institucion = institucionRepository.findById(equipoDTO.getInstitucionId())
                .orElseThrow(() -> new RuntimeException("Institución no encontrada con ID: " + equipoDTO.getInstitucionId()));

        Equipo equipo = modelMapper.map(equipoDTO, Equipo.class);
        equipo.setInstitucion(institucion);
        equipo = equipoRepository.save(equipo);

        return convertirAEquipoDTO(equipo);
    }

    public List<EquipoDTO> obtenerTodosLosEquipos() {
        return equipoRepository.findAll()
                .stream()
                .map(this::convertirAEquipoDTO)
                .collect(Collectors.toList());
    }

    public EquipoDTO obtenerEquipoPorId(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));
        return convertirAEquipoDTO(equipo);
    }

    public List<EquipoDTO> obtenerEquiposPorInstitucion(Long institucionId) {
        return equipoRepository.findByInstitucionId(institucionId)
                .stream()
                .map(this::convertirAEquipoDTO)
                .collect(Collectors.toList());
    }

    public EquipoDTO actualizarEquipo(Long id, EquipoDTO equipoDTO) {
        Equipo equipoExistente = equipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));

        // Validar código único si cambió
        if (!equipoExistente.getCodigo().equals(equipoDTO.getCodigo()) &&
            equipoRepository.existsByCodigo(equipoDTO.getCodigo())) {
            throw new RuntimeException("Ya existe otro equipo con este código");
        }

        // Validar nombre único por institución si cambió
        if (!equipoExistente.getNombre().equals(equipoDTO.getNombre()) &&
            equipoRepository.existsByNombreAndInstitucionId(equipoDTO.getNombre(), equipoDTO.getInstitucionId())) {
            throw new RuntimeException("Ya existe otro equipo con este nombre en la institución");
        }

        // Actualizar institución si cambió
        if (!equipoExistente.getInstitucion().getId().equals(equipoDTO.getInstitucionId())) {
            Institucion nuevaInstitucion = institucionRepository.findById(equipoDTO.getInstitucionId())
                    .orElseThrow(() -> new RuntimeException("Institución no encontrada con ID: " + equipoDTO.getInstitucionId()));
            equipoExistente.setInstitucion(nuevaInstitucion);
        }

        modelMapper.map(equipoDTO, equipoExistente);
        equipoExistente = equipoRepository.save(equipoExistente);
        return convertirAEquipoDTO(equipoExistente);
    }

    public void eliminarEquipo(Long id) {
        if (!equipoRepository.existsById(id)) {
            throw new RuntimeException("Equipo no encontrado con ID: " + id);
        }
        equipoRepository.deleteById(id);
    }

    private EquipoDTO convertirAEquipoDTO(Equipo equipo) {
        EquipoDTO dto = modelMapper.map(equipo, EquipoDTO.class);
        dto.setInstitucionNombre(equipo.getInstitucion().getNombre());
        return dto;
    }
}