package com.friiadmi.dto;

import java.math.BigDecimal;

/** Resultado del cálculo: Pozo total - Comisión = Premio ganador. */
public class PremioDTO {

    private BigDecimal pozoTotal = BigDecimal.ZERO;
    private BigDecimal comisionPorcentaje = BigDecimal.ZERO;
    private BigDecimal comisionMonto = BigDecimal.ZERO;
    private BigDecimal premioGanador = BigDecimal.ZERO;
    private BigDecimal montoPendiente = BigDecimal.ZERO;
    private BigDecimal premioProyectado = BigDecimal.ZERO;
    private long jugadoresPagados;
    private long jugadoresPendientes;
    private PosicionDTO lider;

    public BigDecimal getPozoTotal() { return pozoTotal; }
    public void setPozoTotal(BigDecimal pozoTotal) { this.pozoTotal = pozoTotal; }
    public BigDecimal getComisionPorcentaje() { return comisionPorcentaje; }
    public void setComisionPorcentaje(BigDecimal comisionPorcentaje) { this.comisionPorcentaje = comisionPorcentaje; }
    public BigDecimal getComisionMonto() { return comisionMonto; }
    public void setComisionMonto(BigDecimal comisionMonto) { this.comisionMonto = comisionMonto; }
    public BigDecimal getPremioGanador() { return premioGanador; }
    public void setPremioGanador(BigDecimal premioGanador) { this.premioGanador = premioGanador; }
    public BigDecimal getMontoPendiente() { return montoPendiente; }
    public void setMontoPendiente(BigDecimal montoPendiente) { this.montoPendiente = montoPendiente; }
    public BigDecimal getPremioProyectado() { return premioProyectado; }
    public void setPremioProyectado(BigDecimal premioProyectado) { this.premioProyectado = premioProyectado; }
    public long getJugadoresPagados() { return jugadoresPagados; }
    public void setJugadoresPagados(long jugadoresPagados) { this.jugadoresPagados = jugadoresPagados; }
    public long getJugadoresPendientes() { return jugadoresPendientes; }
    public void setJugadoresPendientes(long jugadoresPendientes) { this.jugadoresPendientes = jugadoresPendientes; }
    public PosicionDTO getLider() { return lider; }
    public void setLider(PosicionDTO lider) { this.lider = lider; }
}
