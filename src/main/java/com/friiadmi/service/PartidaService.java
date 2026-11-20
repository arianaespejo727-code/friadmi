package com.friiadmi.service;

import com.friiadmi.dto.PartidaDTO;
import com.friiadmi.dto.ResultadoDTO;
import com.friiadmi.exception.FriiadmiException;
import com.friiadmi.model.Equipo;
import com.friiadmi.model.Partida;
import com.friiadmi.model.Resultado;
import com.friiadmi.repository.EquipoRepository;
import com.friiadmi.repository.PartidaRepository;
import com.friiadmi.repository.ResultadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class PartidaService {

    /** Puntos por posición (1.º a 10.º). Cada kill suma 1 punto adicional. */
    private static final int[] PUNTOS_POR_POSICION = {12, 9, 8, 7, 6, 5, 4, 3, 2, 1};

    private final PartidaRepository partidaRepository;
    private final ResultadoRepository resultadoRepository;
    private final EquipoRepository equipoRepository;

    public PartidaService(PartidaRepository partidaRepository,
                          ResultadoRepository resultadoRepository,
                          EquipoRepository equipoRepository) {
        this.partidaRepository = partidaRepository;
        this.resultadoRepository = resultadoRepository;
        this.equipoRepository = equipoRepository;
    }

    public static int calcularPuntos(int posicion, int kills) {
        int base = (posicion >= 1 && posicion <= PUNTOS_POR_POSICION.length)
                ? PUNTOS_POR_POSICION[posicion - 1]
                : 0;
        return base + kills;
    }

    @Transactional(readOnly = true)
    public List<Partida> listar() {
        return partidaRepository.findAllByOrderByFechaDescIdDesc();
    }

    @Transactional
    public Partida crear(PartidaDTO dto) {
        Partida partida = new Partida();
        partida.setNombre(dto.getNombre().trim());
        partida.setFecha(dto.getFecha());
        String mapa = dto.getMapa() == null ? "" : dto.getMapa().trim();
        partida.setMapa(mapa.isEmpty() ? null : mapa);
        return partidaRepository.save(partida);
    }

    @Transactional
    public void eliminar(Long id) {
        Partida partida = partidaRepository.findById(id)
                .orElseThrow(() -> new FriiadmiException("La partida no existe."));
        Set<Equipo> afectados = new LinkedHashSet<>();
        for (Resultado resultado : partida.getResultados()) {
            afectados.add(resultado.getEquipo());
        }
        partidaRepository.delete(partida);
        partidaRepository.flush();
        afectados.forEach(this::recalcular);
    }

    /** Registra los puntos de un equipo en una partida y actualiza su total. */
    @Transactional
    public Resultado registrarResultado(ResultadoDTO dto) {
        Partida partida = partidaRepository.findById(dto.getPartidaId())
                .orElseThrow(() -> new FriiadmiException("La partida no existe."));
        Equipo equipo = equipoRepository.findById(dto.getEquipoId())
                .orElseThrow(() -> new FriiadmiException("El equipo no existe."));

        if (resultadoRepository.existsByPartidaIdAndEquipoId(partida.getId(), equipo.getId())) {
            throw new FriiadmiException("El equipo " + equipo.getNombre() + " ya tiene resultado en " + partida.getNombre() + ".");
        }
        if (resultadoRepository.existsByPartidaIdAndPosicion(partida.getId(), dto.getPosicion())) {
            throw new FriiadmiException("La posición " + dto.getPosicion() + " ya está asignada en " + partida.getNombre() + ".");
        }

        Resultado resultado = new Resultado();
        resultado.setPartida(partida);
        resultado.setEquipo(equipo);
        resultado.setPosicion(dto.getPosicion());
        resultado.setKills(dto.getKills());
        resultado.setPuntos(calcularPuntos(dto.getPosicion(), dto.getKills()));
        resultado = resultadoRepository.save(resultado);
        resultadoRepository.flush();

        recalcular(equipo);
        return resultado;
    }

    @Transactional
    public void eliminarResultado(Long id) {
        Resultado resultado = resultadoRepository.findById(id)
                .orElseThrow(() -> new FriiadmiException("El resultado no existe."));
        Equipo equipo = resultado.getEquipo();
        resultadoRepository.delete(resultado);
        resultadoRepository.flush();
        recalcular(equipo);
    }

    /** Recalcula puntos y kills totales del equipo a partir de todos sus resultados. */
    private void recalcular(Equipo equipo) {
        List<Resultado> resultados = resultadoRepository.findByEquipoId(equipo.getId());
        equipo.setPuntosTotales(resultados.stream().mapToInt(Resultado::getPuntos).sum());
        equipo.setTotalKills(resultados.stream().mapToInt(Resultado::getKills).sum());
        equipoRepository.save(equipo);
    }
}
