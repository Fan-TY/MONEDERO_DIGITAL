package com.utp.AppBanco.pattern.observer;

import org.springframework.stereotype.Component;

@Component
public class NotificadorEmail implements NotificacionObserver {

    @Override
    public void actualizar(EventoOperacion evento) {

        System.out.println("========================================");
        System.out.println("[OBSERVER][EMAIL]");
        System.out.println("Tipo: " + evento.getTipo());
        System.out.println("Usuario: " + evento.getUsuario().getNombre());
        System.out.println("Monto: S/. " + evento.getMonto());
        System.out.println("Detalle: " + evento.getDetalle());
        System.out.println("========================================");

    }
}