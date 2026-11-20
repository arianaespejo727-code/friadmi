package com.friiadmi.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ConfiguracionDTO {

    @NotBlank(message = "El nombre del torneo es obligatorio.")
    @Size(max = 120, message = "El nombre del torneo no puede superar 120 caracteres.")
    private String nombreTorneo;

    @NotNull(message = "La comisión es obligatoria.")
    @DecimalMin(value = "0.00", message = "La comisión no puede ser menor a 0%.")
    @DecimalMax(value = "100.00", message = "La comisión no puede ser mayor a 100%.")
    private BigDecimal comisionPorcentaje;

    @NotNull(message = "La cuota de inscripción es obligatoria.")
    @DecimalMin(value = "0.00", message = "La cuota no puede ser negativa.")
    private BigDecimal cuotaInscripcion;

    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
    public BigDecimal getComisionPorcentaje() { return comisionPorcentaje; }
    public void setComisionPorcentaje(BigDecimal comisionPorcentaje) { this.comisionPorcentaje = comisionPorcentaje; }
    public BigDecimal getCuotaInscripcion() { return cuotaInscripcion; }
    public void setCuotaInscripcion(BigDecimal cuotaInscripcion) { this.cuotaInscripcion = cuotaInscripcion; }
}
