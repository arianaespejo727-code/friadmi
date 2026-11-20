package com.friiadmi.service;

import com.friiadmi.dto.PosicionDTO;
import com.friiadmi.dto.PremioDTO;
import com.friiadmi.model.Configuracion;
import com.friiadmi.model.EstadoPago;
import com.friiadmi.model.Jugador;
import com.friiadmi.repository.JugadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/** Cálculo de FRIIADMI: Pozo total - Comisión (configurable) = Premio ganador. */
@Service
public class PremioService {

    private static final BigDecimal CIEN = new BigDecimal("100");

    private final JugadorRepository jugadorRepository;
    private final ConfiguracionService configuracionService;
    private final EquipoService equipoService;

    public PremioService(JugadorRepository jugadorRepository,
                         ConfiguracionService configuracionService,
                         EquipoService equipoService) {
        this.jugadorRepository = jugadorRepository;
        this.configuracionService = configuracionService;
        this.equipoService = equipoService;
    }

    @Transactional
    public PremioDTO calcular() {
        Configuracion config = configuracionService.obtener();
        BigDecimal porcentaje = config.getComisionPorcentaje();

        List<Jugador> pagados = jugadorRepository.findByEstadoPago(EstadoPago.PAGADO);
        List<Jugador> pendientes = jugadorRepository.findByEstadoPago(EstadoPago.PENDIENTE);

        BigDecimal pozo = sumar(pagados);
        BigDecimal pendiente = sumar(pendientes);
        BigDecimal comision = comision(pozo, porcentaje);
        BigDecimal proyectado = pozo.add(pendiente);

        PremioDTO premio = new PremioDTO();
        premio.setPozoTotal(pozo);
        premio.setComisionPorcentaje(porcentaje);
        premio.setComisionMonto(comision);
        premio.setPremioGanador(pozo.subtract(comision));
        premio.setMontoPendiente(pendiente);
        premio.setPremioProyectado(proyectado.subtract(comision(proyectado, porcentaje)));
        premio.setJugadoresPagados(pagados.size());
        premio.setJugadoresPendientes(pendientes.size());

        List<PosicionDTO> tabla = equipoService.tablaPosiciones();
        if (!tabla.isEmpty() && tabla.get(0).getPuntos() > 0) {
            premio.setLider(tabla.get(0));
        }
        return premio;
    }

    private BigDecimal sumar(List<Jugador> jugadores) {
        return jugadores.stream()
                .map(Jugador::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal comision(BigDecimal base, BigDecimal porcentaje) {
        return base.multiply(porcentaje).divide(CIEN, 2, RoundingMode.HALF_UP);
    }
}
