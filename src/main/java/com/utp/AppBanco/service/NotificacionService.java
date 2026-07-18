package com.utp.AppBanco.service;

import com.utp.AppBanco.model.Notificacion;
import com.utp.AppBanco.model.Usuario;
import com.utp.AppBanco.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    /**
     * Guarda una nueva notificación.
     */
    public Notificacion guardar(Notificacion notificacion) {
        return notificacionRepository.save(notificacion);
    }

    /**
     * Historial completo de un usuario.
     */
    public List<Notificacion> obtenerHistorial(Usuario usuario) {
        return notificacionRepository.findByUsuarioOrderByFechaDesc(usuario);
    }

    /**
     * Solo las notificaciones no leídas.
     */
    public List<Notificacion> obtenerNoLeidas(Usuario usuario) {
        return notificacionRepository.findByUsuarioAndLeidoFalseOrderByFechaDesc(usuario);
    }

    /**
     * Últimas 10 notificaciones.
     */
    public List<Notificacion> obtenerUltimas10(Usuario usuario) {
        return notificacionRepository.findTop10ByUsuarioOrderByFechaDesc(usuario);
    }

    /**
     * Marca una notificación como leída.
     */
    public void marcarComoLeida(Notificacion notificacion) {
        notificacion.setLeido(true);
        notificacionRepository.save(notificacion);
    }

}