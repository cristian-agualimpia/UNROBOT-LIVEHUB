package com.unrobot_livehub.registro_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unrobot_livehub.registro_service.dto.MiembroEquipoDTO;
import com.unrobot_livehub.registro_service.entity.Equipo;
import com.unrobot_livehub.registro_service.entity.MiembroEquipo;
import com.unrobot_livehub.registro_service.entity.Persona;
import com.unrobot_livehub.registro_service.repository.EquipoRepository;
import com.unrobot_livehub.registro_service.repository.MiembroEquipoRepository;
import com.unrobot_livehub.registro_service.repository.PersonaRepository;

@Service
public class MiembroEquipoService {

    @Autowired
    private MiembroEquipoRepository miembroEquipoRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public MiembroEquipoDTO agregarMiembro(MiembroEquipoDTO miembroEquipoDTO) {
        // Validar que no exista ya esta relación
        if (miembroEquipoRepository.existsByEquipoIdAndPersonaId(
                miembroEquipoDTO.getEquipoId(), miembroEquipoDTO.getPersonaId())) {
            throw new RuntimeException("Esta persona ya es miembro del equipo");
        }

        Equipo equipo = equipoRepository.findById(miembroEquipoDTO.getEquipoId())
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + miembroEquipoDTO.getEquipoId()));

        Persona persona = personaRepository.findById(miembroEquipoDTO.getPersonaId())
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + miembroEquipoDTO.getPersonaId()));

        MiembroEquipo miembroEquipo = new MiembroEquipo(equipo, persona, miembroEquipoDTO.getRol());
        miembroEquipo = miembroEquipoRepository.save(miembroEquipo);

        return convertirAMiembroEquipoDTO(miembroEquipo);
    }

    public List<MiembroEquipoDTO> obtenerMiembrosPorEquipo(Long equipoId) {
        return miembroEquipoRepository.findByEquipoId(equipoId)
                .stream()
                .map(this::convertirAMiembroEquipoDTO)
                .collect(Collectors.toList());
    }

    public List<MiembroEquipoDTO> obtenerEquiposPorPersona(Long personaId) {
        return miembroEquipoRepository.findByPersonaId(personaId)
                .stream()
                .map(this::convertirAMiembroEquipoDTO)
                .collect(Collectors.toList());
    }

    public MiembroEquipoDTO actualizarRolMiembro(Long id, String nuevoRol) {
        MiembroEquipo miembroEquipo = miembroEquipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Miembro de equipo no encontrado con ID: " + id));

        miembroEquipo.setRol(nuevoRol);
        miembroEquipo = miembroEquipoRepository.save(miembroEquipo);

        return convertirAMiembroEquipoDTO(miembroEquipo);
    }

    public void removerMiembro(Long id) {
        if (!miembroEquipoRepository.existsById(id)) {
            throw new RuntimeException("Miembro de equipo no encontrado con ID: " + id);
        }
        miembroEquipoRepository.deleteById(id);
    }

    public void desactivarMiembro(Long id) {
        MiembroEquipo miembroEquipo = miembroEquipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Miembro de equipo no encontrado con ID: " + id));

        miembroEquipo.setActivo(false);
        miembroEquipoRepository.save(miembroEquipo);
    }

    private MiembroEquipoDTO convertirAMiembroEquipoDTO(MiembroEquipo miembroEquipo) {
    MiembroEquipoDTO dto = modelMapper.map(miembroEquipo, MiembroEquipoDTO.class);
    
    // Temporal: verifica los nombres de los métodos REALES
    String nombres = miembroEquipo.getPersona().getNombres();
    String apellidos = miembroEquipo.getPersona().getApellidos();
    
    dto.setPersonaNombre(nombres + " " + apellidos);
    dto.setEquipoNombre(miembroEquipo.getEquipo().getNombre());
    
    return dto;
    }
}
