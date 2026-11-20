package com.friiadmi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ResultadoDTO {

    @NotNull(message = "Selecciona la partida.")
    private Long partidaId;

    @NotNull(message = "Selecciona el equipo.")
    private Long equipoId;

    @NotNull(message = "La posición es obligatoria.")
    @Min(value = 1, message = "La posición mínima es 1.")
    @Max(value = 12, message = "La posición máxima es 12.")
    private Integer posicion;

    @NotNull(message = "Las kills son obligatorias.")
    @Min(value = 0, message = "Las kills no pueden ser negativas.")
    @Max(value = 99, message = "Las kills no pueden superar 99.")
    private Integer kills = 0;

    public Long getPartidaId() { return partidaId; }
    public void setPartidaId(Long partidaId) { this.partidaId = partidaId; }
    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }
    public Integer getPosicion() { return posicion; }
    public void setPosicion(Integer posicion) { this.posicion = posicion; }
    public Integer getKills() { return kills; }
    public void setKills(Integer kills) { this.kills = kills; }
}
