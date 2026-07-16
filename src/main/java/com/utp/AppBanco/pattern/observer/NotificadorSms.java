package com.utp.AppBanco.pattern.observer;

import org.springframework.stereotype.Component;

@Component
public class NotificadorSms implements NotificacionObserver {

    @Override
    public void actualizar(String evento, String detalle) {
        System.out.println("[OBSERVER][SMS] " + evento + " → " + detalle);
    }
}
