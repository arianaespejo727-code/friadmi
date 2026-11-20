package com.friiadmi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.friiadmi.model.EstadoPago;
import com.friiadmi.model.MetodoPago;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos del formulario de jugadores de FRIIADMI. */
public class JugadorDTO {

    private Long id;

    @NotBlank(message = "El nombre del jugador es obligatorio.")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres.")
    private String nombre;

    @NotBlank(message = "El ID de Free Fire es obligatorio.")
    @Pattern(regexp = "\\d{6,15}", message = "El ID de Free Fire debe tener entre 6 y 15 dígitos.")
    private String idFreeFire;

    @NotBlank(message = "El celular es obligatorio.")
    @Pattern(regexp = "9\\d{8}", message = "El celular debe tener 9 dígitos y empezar con 9.")
    private String celular;

    private Long equipoId;

    private boolean capitan;

    @NotNull(message = "El monto es obligatorio.")
    @DecimalMin(value = "0.00", message = "El monto no puede ser negativo.")
    private BigDecimal monto;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaPago;

    @NotNull(message = "Selecciona el estado de pago.")
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    private MetodoPago metodoPago;

    private MultipartFile comprobanteArchivo;

        private String fotoPerfil;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIdFreeFire() { return idFreeFire; }
    public void setIdFreeFire(String idFreeFire) { this.idFreeFire = idFreeFire; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public Long getEquipoId() { return equipoId; }
    public void setEquipoId(Long equipoId) { this.equipoId = equipoId; }
    public boolean isCapitan() { return capitan; }
    public void setCapitan(boolean capitan) { this.capitan = capitan; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public EstadoPago getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPago estadoPago) { this.estadoPago = estadoPago; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public MultipartFile getComprobanteArchivo() { return comprobanteArchivo; }
    public void setComprobanteArchivo(MultipartFile comprobanteArchivo) { this.comprobanteArchivo = comprobanteArchivo; }

        public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }
}
