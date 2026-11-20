package com.friiadmi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PartidaDTO {

    @NotBlank(message = "El nombre de la partida es obligatorio.")
    @Size(max = 100, message = "El nombre de la partida no puede superar 100 caracteres.")
    private String nombre;

    @NotNull(message = "La fecha de la partida es obligatoria.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @Size(max = 40, message = "El mapa no puede superar 40 caracteres.")
    private String mapa;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getMapa() { return mapa; }
    public void setMapa(String mapa) { this.mapa = mapa; }
}
