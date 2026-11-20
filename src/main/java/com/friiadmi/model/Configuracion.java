package com.friiadmi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/** Configuración única del torneo (siempre id = 1). */
@Entity
@Table(name = "configuracion")
public class Configuracion {

    public static final Long ID_UNICO = 1L;

    @Id
    private Long id = ID_UNICO;

    @Column(name = "nombre_torneo", nullable = false, length = 120)
    private String nombreTorneo = "Torneo FRIIADMI";

    @Column(name = "comision_porcentaje", nullable = false, precision = 5, scale = 2)
    private BigDecimal comisionPorcentaje = new BigDecimal("20.00");

    @Column(name = "cuota_inscripcion", nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaInscripcion = new BigDecimal("10.00");

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
    public BigDecimal getComisionPorcentaje() { return comisionPorcentaje; }
    public void setComisionPorcentaje(BigDecimal comisionPorcentaje) { this.comisionPorcentaje = comisionPorcentaje; }
    public BigDecimal getCuotaInscripcion() { return cuotaInscripcion; }
    public void setCuotaInscripcion(BigDecimal cuotaInscripcion) { this.cuotaInscripcion = cuotaInscripcion; }
}
