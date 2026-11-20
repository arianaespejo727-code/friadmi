package com.friiadmi.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Datos del panel principal de FRIIADMI. */
public class DashboardDTO {

    private BigDecimal totalRecaudado = BigDecimal.ZERO;
    private BigDecimal premioAPagar = BigDecimal.ZERO;
    private BigDecimal comisionMonto = BigDecimal.ZERO;
    private BigDecimal comisionPorcentaje = BigDecimal.ZERO;
    private BigDecimal montoPendiente = BigDecimal.ZERO;
    private long totalJugadores;
    private long totalEquipos;
    private long totalPartidas;
    private long jugadoresPendientes;
    private List<PosicionDTO> top = new ArrayList<>();

    public BigDecimal getTotalRecaudado() { return totalRecaudado; }
    public void setTotalRecaudado(BigDecimal totalRecaudado) { this.totalRecaudado = totalRecaudado; }
    public BigDecimal getPremioAPagar() { return premioAPagar; }
    public void setPremioAPagar(BigDecimal premioAPagar) { this.premioAPagar = premioAPagar; }
    public BigDecimal getComisionMonto() { return comisionMonto; }
    public void setComisionMonto(BigDecimal comisionMonto) { this.comisionMonto = comisionMonto; }
    public BigDecimal getComisionPorcentaje() { return comisionPorcentaje; }
    public void setComisionPorcentaje(BigDecimal comisionPorcentaje) { this.comisionPorcentaje = comisionPorcentaje; }
    public BigDecimal getMontoPendiente() { return montoPendiente; }
    public void setMontoPendiente(BigDecimal montoPendiente) { this.montoPendiente = montoPendiente; }
    public long getTotalJugadores() { return totalJugadores; }
    public void setTotalJugadores(long totalJugadores) { this.totalJugadores = totalJugadores; }
    public long getTotalEquipos() { return totalEquipos; }
    public void setTotalEquipos(long totalEquipos) { this.totalEquipos = totalEquipos; }
    public long getTotalPartidas() { return totalPartidas; }
    public void setTotalPartidas(long totalPartidas) { this.totalPartidas = totalPartidas; }
    public long getJugadoresPendientes() { return jugadoresPendientes; }
    public void setJugadoresPendientes(long jugadoresPendientes) { this.jugadoresPendientes = jugadoresPendientes; }
    public List<PosicionDTO> getTop() { return top; }
    public void setTop(List<PosicionDTO> top) { this.top = top; }
}
