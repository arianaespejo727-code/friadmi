package com.friiadmi.repository;

import com.friiadmi.model.Resultado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoRepository extends JpaRepository<Resultado, Long> {

    List<Resultado> findByEquipoId(Long equipoId);

    long countByEquipoId(Long equipoId);

    boolean existsByPartidaIdAndEquipoId(Long partidaId, Long equipoId);

    boolean existsByPartidaIdAndPosicion(Long partidaId, int posicion);
}
