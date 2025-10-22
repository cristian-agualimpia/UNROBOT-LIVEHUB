package com.unrobot_livehub.registro_service.repository;

import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.Enfrentamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la entidad Enfrentamiento.
 */
@Repository
public interface EnfrentamientoRepository extends JpaRepository<Enfrentamiento, UUID> {

    /**
     * Busca todos los enfrentamientos de una categoría específica Y que pertenezcan
     * a una ronda determinada (ej: "Octavos-1", "Octavos-2", ...).
     * * ¡Este es el método clave para avanzar las llaves!
     *
     * @param categoria El CategoriaTipo (Enum) por el cual filtrar.
     * @param etiquetaRonda El prefijo de la ronda (ej: "Octavos").
     * @return Una lista de enfrentamientos que coinciden.
     */
    List<Enfrentamiento> findByCategoriaAndEtiquetaRondaStartingWith(CategoriaTipo categoria, String etiquetaRonda);

    /**
     * Busca todos los enfrentamientos de una categoría, ordenados por ronda.
     * Útil para que el frontend pueda pintar el árbol de llaves completo.
     *
     * @param categoria El CategoriaTipo (Enum).
     * @return Una lista de todos los enfrentamientos de esa categoría.
     */
    List<Enfrentamiento> findByCategoria(CategoriaTipo categoria);
}