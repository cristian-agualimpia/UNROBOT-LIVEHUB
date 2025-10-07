package com.unrobot_livehub.registro_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unrobot_livehub.registro_service.entity.Institucion;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, Long> {
    Optional<Institucion> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}

