package com.unrobot_livehub.registro_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unrobot_livehub.registro_service.entity.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    Optional<Equipo> findByCodigo(String codigo);
    List<Equipo> findByInstitucionId(Long institucionId);
    boolean existsByCodigo(String codigo);
    boolean existsByNombreAndInstitucionId(String nombre, Long institucionId);
}
