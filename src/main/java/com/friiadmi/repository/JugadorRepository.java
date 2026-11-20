package com.friiadmi.repository;

import com.friiadmi.model.EstadoPago;
import com.friiadmi.model.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    List<Jugador> findAllByOrderByNombreAsc();

    List<Jugador> findByEquipoId(Long equipoId);

    List<Jugador> findByEstadoPago(EstadoPago estadoPago);

    long countByEstadoPago(EstadoPago estadoPago);

    boolean existsByIdFreeFire(String idFreeFire);

    boolean existsByIdFreeFireAndIdNot(String idFreeFire, Long id);

    long countByEquipoIdAndIdNot(Long equipoId, Long id);

    boolean existsByEquipoIdAndCapitanTrueAndIdNot(Long equipoId, Long id);
}
