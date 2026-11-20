package com.friiadmi.service;

import com.friiadmi.dto.DashboardDTO;
import com.friiadmi.dto.PosicionDTO;
import com.friiadmi.dto.PremioDTO;
import com.friiadmi.model.EstadoPago;
import com.friiadmi.repository.EquipoRepository;
import com.friiadmi.repository.JugadorRepository;
import com.friiadmi.repository.PartidaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DashboardService {

    private final JugadorRepository jugadorRepository;
    private final EquipoRepository equipoRepository;
    private final PartidaRepository partidaRepository;
    private final PremioService premioService;
    private final EquipoService equipoService;

    public DashboardService(JugadorRepository jugadorRepository,
                            EquipoRepository equipoRepository,
                            PartidaRepository partidaRepository,
                            PremioService premioService,
                            EquipoService equipoService) {
        this.jugadorRepository = jugadorRepository;
        this.equipoRepository = equipoRepository;
        this.partidaRepository = partidaRepository;
        this.premioService = premioService;
        this.equipoService = equipoService;
    }

    @Transactional
    public DashboardDTO resumen() {
        PremioDTO premio = premioService.calcular();

        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setTotalRecaudado(premio.getPozoTotal());
        dashboard.setPremioAPagar(premio.getPremioGanador());
        dashboard.setComisionMonto(premio.getComisionMonto());
        dashboard.setComisionPorcentaje(premio.getComisionPorcentaje());
        dashboard.setMontoPendiente(premio.getMontoPendiente());
        dashboard.setTotalJugadores(jugadorRepository.count());
        dashboard.setTotalEquipos(equipoRepository.count());
        dashboard.setTotalPartidas(partidaRepository.count());
        dashboard.setJugadoresPendientes(jugadorRepository.countByEstadoPago(EstadoPago.PENDIENTE));

        List<PosicionDTO> tabla = equipoService.tablaPosiciones();
        dashboard.setTop(tabla.stream().limit(5).toList());
        return dashboard;
    }
}
