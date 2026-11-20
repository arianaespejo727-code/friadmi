package com.friiadmi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/** Datos del formulario de equipos: nombre + capitán + hasta 4 integrantes. */
public class EquipoDTO {

    private Long id;

    @NotBlank(message = "El nombre del equipo es obligatorio.")
    @Size(max = 80, message = "El nombre del equipo no puede superar 80 caracteres.")
    private String nombre;

    private Long capitanId;

    private List<Long> integrantesIds = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getCapitanId() { return capitanId; }
    public void setCapitanId(Long capitanId) { this.capitanId = capitanId; }
    public List<Long> getIntegrantesIds() { return integrantesIds; }
    public void setIntegrantesIds(List<Long> integrantesIds) { this.integrantesIds = integrantesIds; }
}
