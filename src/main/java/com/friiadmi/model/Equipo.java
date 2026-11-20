package com.friiadmi.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/** Equipo de FRIIADMI: 1 capitán + hasta 4 jugadores. */
@Entity
@Table(name = "equipos")
public class Equipo {

    public static final int MAX_JUGADORES = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Column(name = "puntos_totales", nullable = false)
    private int puntosTotales = 0;

    @Column(name = "total_kills", nullable = false)
    private int totalKills = 0;

    @OneToMany(mappedBy = "equipo")
    @OrderBy("capitan DESC, nombre ASC")
    private List<Jugador> jugadores = new ArrayList<>();

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.REMOVE)
    private List<Resultado> resultados = new ArrayList<>();

    /** Capitán del equipo o null si todavía no tiene. */
    public Jugador getCapitan() {
        return jugadores.stream().filter(Jugador::isCapitan).findFirst().orElse(null);
    }

    /** Jugadores del equipo sin contar al capitán. */
    public List<Jugador> getIntegrantes() {
        return jugadores.stream().filter(j -> !j.isCapitan()).toList();
    }

    public int getNumeroJugadores() {
        return jugadores.size();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getPuntosTotales() { return puntosTotales; }
    public void setPuntosTotales(int puntosTotales) { this.puntosTotales = puntosTotales; }
    public int getTotalKills() { return totalKills; }
    public void setTotalKills(int totalKills) { this.totalKills = totalKills; }
    public List<Jugador> getJugadores() { return jugadores; }
    public void setJugadores(List<Jugador> jugadores) { this.jugadores = jugadores; }
    public List<Resultado> getResultados() { return resultados; }
    public void setResultados(List<Resultado> resultados) { this.resultados = resultados; }
}
