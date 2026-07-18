package com.utp.AppBanco.repository;

import com.utp.AppBanco.model.Notificacion;
import com.utp.AppBanco.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Historial de un usuario (más recientes primero)
    List<Notificacion> findByUsuarioOrderByFechaDesc(Usuario usuario);

    // Solo las no leídas
    List<Notificacion> findByUsuarioAndLeidoFalseOrderByFechaDesc(Usuario usuario);

    // Últimas 10
    List<Notificacion> findTop10ByUsuarioOrderByFechaDesc(Usuario usuario);

}