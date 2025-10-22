package com.unrobot_livehub.registro_service.repository;

import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la entidad Equipo.
 * Proporciona métodos CRUD (Create, Read, Update, Delete) automáticamente.
 */
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, UUID> {

    /**
     * Busca todos los equipos inscritos en una categoría específica.
     * Esencial para generar las llaves iniciales.
     * * @param categoria El CategoriaTipo (Enum) por el cual filtrar.
     * @return Una lista de equipos pertenecientes a esa categoría.
     */
    List<Equipo> findByCategoria(CategoriaTipo categoria);
}