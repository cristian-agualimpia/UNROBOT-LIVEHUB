package com.unrobot_livehub.registro_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unrobot_livehub.registro_service.dto.PersonaDTO;
import com.unrobot_livehub.registro_service.entity.Persona;
import com.unrobot_livehub.registro_service.repository.PersonaRepository;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public PersonaDTO crearPersona(PersonaDTO personaDTO) {
        // Validar que no exista otra persona con la misma identificación
        if (personaRepository.existsByIdentificacion(personaDTO.getIdentificacion())) {
            throw new RuntimeException("Ya existe una persona con esta identificación");
        }

        // Validar que no exista otro email si se proporciona
        if (personaDTO.getEmail() != null && personaRepository.existsByEmail(personaDTO.getEmail())) {
            throw new RuntimeException("Ya existe una persona con este email");
        }

        Persona persona = modelMapper.map(personaDTO, Persona.class);
        persona = personaRepository.save(persona);
        return modelMapper.map(persona, PersonaDTO.class);
    }

    public List<PersonaDTO> obtenerTodasLasPersonas() {
        return personaRepository.findAll()
                .stream()
                .map(persona -> modelMapper.map(persona, PersonaDTO.class))
                .collect(Collectors.toList());
    }

    public PersonaDTO obtenerPersonaPorId(Long id) {
        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));
        return modelMapper.map(persona, PersonaDTO.class);
    }

    public PersonaDTO actualizarPersona(Long id, PersonaDTO personaDTO) {
        Persona personaExistente = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada con ID: " + id));

        // Validar identificación única si cambió
        if (!personaExistente.getIdentificacion().equals(personaDTO.getIdentificacion()) &&
            personaRepository.existsByIdentificacion(personaDTO.getIdentificacion())) {
            throw new RuntimeException("Ya existe otra persona con esta identificación");
        }

        // Validar email único si cambió
        if (personaDTO.getEmail() != null && 
            !personaDTO.getEmail().equals(personaExistente.getEmail()) &&
            personaRepository.existsByEmail(personaDTO.getEmail())) {
            throw new RuntimeException("Ya existe otra persona con este email");
        }

        modelMapper.map(personaDTO, personaExistente);
        personaExistente = personaRepository.save(personaExistente);
        return modelMapper.map(personaExistente, PersonaDTO.class);
    }

    public void eliminarPersona(Long id) {
        if (!personaRepository.existsById(id)) {
            throw new RuntimeException("Persona no encontrada con ID: " + id);
        }
        personaRepository.deleteById(id);
    }
}