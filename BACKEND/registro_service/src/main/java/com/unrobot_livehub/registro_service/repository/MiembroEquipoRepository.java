package com.unrobot_livehub.registro_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unrobot_livehub.registro_service.entity.MiembroEquipo;

@Repository
public interface MiembroEquipoRepository extends JpaRepository<MiembroEquipo, Long> {
    List<MiembroEquipo> findByEquipoId(Long equipoId);
    List<MiembroEquipo> findByPersonaId(Long personaId);
    Optional<MiembroEquipo> findByEquipoIdAndPersonaId(Long equipoId, Long personaId);
    boolean existsByEquipoIdAndPersonaId(Long equipoId, Long personaId);
    int countByEquipoIdAndActivoTrue(Long equipoId);
}
