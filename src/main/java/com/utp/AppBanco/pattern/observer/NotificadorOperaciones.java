package com.utp.AppBanco.pattern.observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class NotificadorOperaciones {

    @Autowired
    private List<NotificacionObserver> observadores;

    public void notificarTodos(String evento, String detalle) {
        System.out.println("[OBSERVER] Difundiendo evento a " + observadores.size() + " observador(es)");
        for (NotificacionObserver observador : observadores) {
            observador.actualizar(evento, detalle);
        }
    }
}
