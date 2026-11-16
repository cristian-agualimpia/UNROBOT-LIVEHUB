package com.unrobot_livehub.categorias_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.unrobot_livehub.categorias_service.entity.CategoriaTipo;
import com.unrobot_livehub.categorias_service.entity.Equipo;

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

   @Query("SELECT DISTINCT e FROM Equipo e LEFT JOIN e.miembros m WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(e.institucion) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(m) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Equipo> buscarPorCriterio(@Param("q") String q);

    @Query("SELECT DISTINCT e FROM Equipo e LEFT JOIN e.miembros m WHERE e.categoria = :categoria AND (LOWER(e.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(e.institucion) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(m) LIKE LOWER(CONCAT('%', :q, '%')))")
    List<Equipo> buscarPorCategoriaYCriterio(@Param("categoria") CategoriaTipo categoria, @Param("q") String q);
    
    @Query("SELECT e FROM Equipo e WHERE LOWER(e.nombre) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Equipo> buscarSoloPorNombre(@Param("q") String query);

    @Query("SELECT e FROM Equipo e WHERE LOWER(e.institucion) LIKE LOWER(CONCAT('%', :q, '%'))")
    List<Equipo> buscarSoloPorInstitucion(@Param("q") String query);

}
