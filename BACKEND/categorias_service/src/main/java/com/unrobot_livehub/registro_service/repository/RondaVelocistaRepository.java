package com.unrobot_livehub.registro_service.repository;

import com.unrobot_livehub.registro_service.entity.CategoriaTipo;
import com.unrobot_livehub.registro_service.entity.RondaVelocista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio Spring Data JPA para la entidad RondaVelocista.
 */
@Repository
public interface RondaVelocistaRepository extends JpaRepository<RondaVelocista, UUID> {

    /**
     * Busca todas las rondas de una categoría específica, ordenadas por el
     * tiempo final (de menor a mayor), para generar la tabla de posiciones.
     *
     * @param categoria El CategoriaTipo (Enum).
     * @return Una lista ordenada de las rondas (mejores tiempos primero).
     */
    List<RondaVelocista> findByCategoriaOrderByTiempoFinalMsAsc(CategoriaTipo categoria);

    /**
     * Busca todas las rondas de un equipo específico en una categoría.
     * Útil si necesitas mostrar el historial de un solo equipo.
     *
     * @param categoria El CategoriaTipo (Enum).
     * @param idEquipo El UUID del equipo.
     * @return Una lista de las rondas de ese equipo.
     */
    List<RondaVelocista> findByCategoriaAndIdEquipo(CategoriaTipo categoria, UUID idEquipo);
}