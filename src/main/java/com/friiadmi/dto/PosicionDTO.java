package com.friiadmi.dto;

/** Fila de la tabla de posiciones de FRIIADMI. */
public class PosicionDTO {

    private final int posicion;
    private final Long equipoId;
    private final String equipo;
    private final String capitan;
    private final int partidasJugadas;
    private final int kills;
    private final int puntos;

    public PosicionDTO(int posicion, Long equipoId, String equipo, String capitan,
                       int partidasJugadas, int kills, int puntos) {
        this.posicion = posicion;
        this.equipoId = equipoId;
        this.equipo = equipo;
        this.capitan = capitan;
        this.partidasJugadas = partidasJugadas;
        this.kills = kills;
        this.puntos = puntos;
    }

    public int getPosicion() { return posicion; }
    public Long getEquipoId() { return equipoId; }
    public String getEquipo() { return equipo; }
    public String getCapitan() { return capitan; }
    public int getPartidasJugadas() { return partidasJugadas; }
    public int getKills() { return kills; }
    public int getPuntos() { return puntos; }
}
