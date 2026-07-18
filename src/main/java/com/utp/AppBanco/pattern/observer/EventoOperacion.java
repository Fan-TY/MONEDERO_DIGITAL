package com.utp.AppBanco.pattern.observer;

import com.utp.AppBanco.model.Usuario;

import java.time.LocalDateTime;

public class EventoOperacion {

    private Usuario usuario;
    private String tipo;
    private String detalle;
    private Double monto;
    private LocalDateTime fecha;

    public EventoOperacion() {
        this.fecha = LocalDateTime.now();
    }

    public EventoOperacion(Usuario usuario,
                           String tipo,
                           String detalle,
                           Double monto) {
        this.usuario = usuario;
        this.tipo = tipo;
        this.detalle = detalle;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}