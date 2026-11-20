package com.friiadmi.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.friiadmi.dto.JugadorDTO;
import com.friiadmi.exception.FriiadmiException;
import com.friiadmi.model.Equipo;
import com.friiadmi.model.EstadoPago;
import com.friiadmi.model.Jugador;
import com.friiadmi.repository.EquipoRepository;
import com.friiadmi.repository.JugadorRepository;

@Service
public class JugadorService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    private final JugadorRepository jugadorRepository;
    private final EquipoRepository equipoRepository;
    private final FileStorageService fileStorageService;

    public JugadorService(JugadorRepository jugadorRepository,
                          EquipoRepository equipoRepository,
                          FileStorageService fileStorageService) {
        this.jugadorRepository = jugadorRepository;
        this.equipoRepository = equipoRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional(readOnly = true)
    public List<Jugador> listar() {
        return jugadorRepository.findAllByOrderByNombreAsc();
    }

    @Transactional
    public Jugador guardar(JugadorDTO dto) {
        Long id = dto.getId();
        Long idComparacion = id == null ? -1L : id;
        Jugador jugador = id == null
                ? new Jugador()
                : jugadorRepository.findById(id).orElseThrow(() -> new FriiadmiException("El jugador no existe."));

        String idFreeFire = dto.getIdFreeFire().trim();
        boolean repetido = id == null
                ? jugadorRepository.existsByIdFreeFire(idFreeFire)
                : jugadorRepository.existsByIdFreeFireAndIdNot(idFreeFire, id);
        if (repetido) {
            throw new FriiadmiException("Ya existe un jugador con el ID de Free Fire " + idFreeFire + ".");
        }

        Equipo equipo = null;
        if (dto.getEquipoId() != null) {
            equipo = equipoRepository.findById(dto.getEquipoId())
                    .orElseThrow(() -> new FriiadmiException("El equipo seleccionado no existe."));
            long miembros = jugadorRepository.countByEquipoIdAndIdNot(equipo.getId(), idComparacion);
            if (miembros >= Equipo.MAX_JUGADORES) {
                throw new FriiadmiException("El equipo " + equipo.getNombre() + " ya tiene 5 jugadores (capitán + 4).");
            }
            if (dto.isCapitan()
                    && jugadorRepository.existsByEquipoIdAndCapitanTrueAndIdNot(equipo.getId(), idComparacion)) {
                throw new FriiadmiException("El equipo " + equipo.getNombre() + " ya tiene capitán.");
            }
        }

        jugador.setNombre(dto.getNombre().trim());
        jugador.setIdFreeFire(idFreeFire);
        jugador.setCelular(dto.getCelular().trim());
        jugador.setEquipo(equipo);
        jugador.setCapitan(equipo != null && dto.isCapitan());
        jugador.setMonto(dto.getMonto());
        jugador.setEstadoPago(dto.getEstadoPago());
        jugador.setMetodoPago(dto.getMetodoPago());
        jugador.setFotoPerfil(dto.getFotoPerfil());

        if (dto.getEstadoPago() == EstadoPago.PAGADO) {
            jugador.setFechaPago(dto.getFechaPago() != null ? dto.getFechaPago() : LocalDate.now(ZONA_LIMA));
        } else {
            jugador.setFechaPago(null);
        }

        String nuevoComprobante = fileStorageService.guardar(dto.getComprobanteArchivo());
        if (nuevoComprobante != null) {
            fileStorageService.eliminar(jugador.getComprobante());
            jugador.setComprobante(nuevoComprobante);
        }

        return jugadorRepository.save(jugador);
    }

    /** Marca al jugador como pagado con la fecha de hoy (hora de Lima). */
    @Transactional
    public Jugador marcarPagado(Long id) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new FriiadmiException("El jugador no existe."));
        jugador.setEstadoPago(EstadoPago.PAGADO);
        if (jugador.getFechaPago() == null) {
            jugador.setFechaPago(LocalDate.now(ZONA_LIMA));
        }
        return jugadorRepository.save(jugador);
    }

    @Transactional
    public void eliminar(Long id) {
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> new FriiadmiException("El jugador no existe."));
        fileStorageService.eliminar(jugador.getComprobante());
        jugadorRepository.delete(jugador);
    }
}
