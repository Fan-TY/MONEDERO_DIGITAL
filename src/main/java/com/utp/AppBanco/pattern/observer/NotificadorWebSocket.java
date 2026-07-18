package com.utp.AppBanco.pattern.observer;

import com.utp.AppBanco.model.Notificacion;
import com.utp.AppBanco.model.NotificacionDTO;
import com.utp.AppBanco.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificadorWebSocket implements NotificacionObserver {

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void actualizar(EventoOperacion evento) {

        // ============================
        // Guardar en MySQL
        // ============================

        Notificacion notificacion = new Notificacion();

        notificacion.setUsuario(evento.getUsuario());
        notificacion.setTipo(evento.getTipo());
        notificacion.setMensaje(evento.getDetalle());

        notificacionService.guardar(notificacion);

        // ============================
        // Enviar al navegador
        // ============================

        NotificacionDTO dto =
                new NotificacionDTO(
                        evento.getTipo(),
                        evento.getDetalle());

        messagingTemplate.convertAndSend(
                "/topic/notificaciones/" + evento.getUsuario().getId(),
                dto
        );

        System.out.println("======================================");
        System.out.println("[WEBSOCKET]");
        System.out.println("Enviada al usuario " + evento.getUsuario().getId());
        System.out.println("======================================");
    }

}