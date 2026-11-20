package com.friiadmi.service;

import com.friiadmi.dto.EquipoDTO;
import com.friiadmi.dto.PosicionDTO;
import com.friiadmi.exception.FriiadmiException;
import com.friiadmi.model.Equipo;
import com.friiadmi.model.Jugador;
import com.friiadmi.repository.EquipoRepository;
import com.friiadmi.repository.JugadorRepository;
import com.friiadmi.repository.ResultadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class EquipoService {

    private static final int MAX_INTEGRANTES = Equipo.MAX_JUGADORES - 1;

    private final EquipoRepository equipoRepository;
    private final JugadorRepository jugadorRepository;
    private final ResultadoRepository resultadoRepository;

    public EquipoService(EquipoRepository equipoRepository,
                         JugadorRepository jugadorRepository,
                         ResultadoRepository resultadoRepository) {
        this.equipoRepository = equipoRepository;
        this.jugadorRepository = jugadorRepository;
        this.resultadoRepository = resultadoRepository;
    }

    @Transactional(readOnly = true)
    public List<Equipo> listar() {
        return equipoRepository.findAllByOrderByNombreAsc();
    }

    /** Crea o edita un equipo: nombre + capitán + hasta 4 integrantes. */
    @Transactional
    public Equipo guardar(EquipoDTO dto) {
        Long id = dto.getId();
        String nombre = dto.getNombre().trim();

        boolean repetido = id == null
                ? equipoRepository.existsByNombreIgnoreCase(nombre)
                : equipoRepository.existsByNombreIgnoreCaseAndIdNot(nombre, id);
        if (repetido) {
            throw new FriiadmiException("Ya existe un equipo llamado «" + nombre + "».");
        }
        if (dto.getCapitanId() == null) {
            throw new FriiadmiException("Elige un capitán para el equipo.");
        }

        Set<Long> integrantes = new LinkedHashSet<>();
        if (dto.getIntegrantesIds() != null) {
            integrantes.addAll(dto.getIntegrantesIds());
        }
        integrantes.remove(dto.getCapitanId());
        if (integrantes.size() > MAX_INTEGRANTES) {
            throw new FriiadmiException("Un equipo tiene como máximo 1 capitán y 4 jugadores.");
        }

        Equipo equipo = id == null
                ? new Equipo()
                : equipoRepository.findById(id).orElseThrow(() -> new FriiadmiException("El equipo no existe."));

        // Validar todos los jugadores antes de modificar nada.
        List<Long> todos = new ArrayList<>();
        todos.add(dto.getCapitanId());
        todos.addAll(integrantes);
        List<Jugador> seleccionados = new ArrayList<>();
        for (Long jugadorId : todos) {
            Jugador jugador = jugadorRepository.findById(jugadorId)
                    .orElseThrow(() -> new FriiadmiException("Uno de los jugadores seleccionados no existe."));
            Equipo actual = jugador.getEquipo();
            if (actual != null && (id == null || !actual.getId().equals(id))) {
                throw new FriiadmiException("«" + jugador.getNombre() + "» ya pertenece al equipo " + actual.getNombre() + ".");
            }
            seleccionados.add(jugador);
        }

        equipo.setNombre(nombre);
        equipo = equipoRepository.save(equipo);

        if (id != null) {
            for (Jugador anterior : jugadorRepository.findByEquipoId(equipo.getId())) {
                anterior.setEquipo(null);
                anterior.setCapitan(false);
            }
        }
        for (Jugador jugador : seleccionados) {
            jugador.setEquipo(equipo);
            jugador.setCapitan(jugador.getId().equals(dto.getCapitanId()));
        }
        jugadorRepository.saveAll(seleccionados);
        return equipo;
    }

    @Transactional
    public void eliminar(Long id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new FriiadmiException("El equipo no existe."));
        for (Jugador jugador : jugadorRepository.findByEquipoId(id)) {
            jugador.setEquipo(null);
            jugador.setCapitan(false);
        }
        jugadorRepository.flush();
        equipoRepository.delete(equipo);
    }

    /** Tabla de posiciones automática: puntos, luego kills, luego nombre. */
    @Transactional(readOnly = true)
    public List<PosicionDTO> tablaPosiciones() {
        List<PosicionDTO> tabla = new ArrayList<>();
        int posicion = 1;
        for (Equipo equipo : equipoRepository.findAllByOrderByPuntosTotalesDescTotalKillsDescNombreAsc()) {
            Jugador capitan = equipo.getCapitan();
            tabla.add(new PosicionDTO(
                    posicion++,
                    equipo.getId(),
                    equipo.getNombre(),
                    capitan != null ? capitan.getNombre() : "Sin capitán",
                    (int) resultadoRepository.countByEquipoId(equipo.getId()),
                    equipo.getTotalKills(),
                    equipo.getPuntosTotales()));
        }
        return tabla;
    }
}
